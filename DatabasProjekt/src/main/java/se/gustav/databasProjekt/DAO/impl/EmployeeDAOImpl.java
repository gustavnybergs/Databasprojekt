package se.gustav.databasProjekt.DAO.impl;

import se.gustav.databasProjekt.DAO.interfaces.EmployeeDAO;
import se.gustav.databasProjekt.JDBCUtil;
import se.gustav.databasProjekt.models.Employee;

import java.sql.*;
import java.util.*;

public class EmployeeDAOImpl implements EmployeeDAO {

    @Override
    public void createEmployee(Employee employee) {
        String insertQuery = """
            INSERT INTO employee 
            (name, email, password, role_id, salary) 
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getEmail());
            pstmt.setString(3, employee.getPassword());
            pstmt.setInt(4, employee.getRole_id());
            pstmt.setDouble(5, employee.getSalary());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    employee.setEmployee_id(generatedKeys.getInt(1));
                }
            }

            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Could not create employee", e);
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        String query = """
            SELECT e.*
            FROM employee e
            ORDER BY e.role_id, e.employee_id
            """;
        List<Employee> employees = new ArrayList<>();

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        null,  // skyddar password
                        rs.getInt("role_id"),
                        rs.getDouble("salary")
                );
                employees.add(employee);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not get employees", e);
        }

        return employees;
    }

    @Override
    public Employee getEmployee(int employee_id) {
        String query = """
            SELECT e.*
            FROM employee e
            WHERE employee_id = ?
            """;

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, employee_id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                            rs.getInt("employee_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            null, // skyddar password
                            rs.getInt("role_id"),
                            rs.getDouble("salary")
                    );
                }
                throw new RuntimeException("Employee not found with id: " + employee_id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not get employee with id: " + employee_id, e);
        }
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        String query = """
            SELECT e.*
            FROM employee e 
            WHERE email = ?
            """;

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                            rs.getInt("employee_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            null, // skyddar password
                            rs.getInt("role_id"),
                            rs.getDouble("salary")
                    );
                }
                throw new RuntimeException("Employee not found with email: " + email);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not get employee with email: " + email, e);
        }
    }

    @Override
    public Employee getEmployeeWithRole(int employee_id) {
        String query = """ 
            SELECT e.*, w.title, w.description
            FROM employee e
            JOIN work_role w ON e.role_id = w.role_id
            WHERE e.employee_id = ?
            """;

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, employee_id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                            rs.getInt("employee_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            null,  // skyddar password
                            rs.getInt("role_id"),
                            rs.getDouble("salary")
                    );
                }
                throw new RuntimeException("Employee not found with id: " + employee_id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not get employee with id: " + employee_id, e);
        }
    }

    @Override
    public void updateEmployee(Employee employee) {
        String query = """
            UPDATE employee
            SET name = ?, 
                email = ?, 
                salary = ?
            WHERE employee_id = ?
            """;

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getEmail());
            pstmt.setDouble(3, employee.getSalary());
            pstmt.setInt(4, employee.getEmployee_id());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("No employee found with id: " + employee.getEmployee_id());
            }
            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException("Could not update employee", e);
        }
    }

    @Override
    public void deleteEmployee(int employeeId) {
        String deleteQuery = "DELETE FROM employee WHERE employee_id = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

            pstmt.setInt(1, employeeId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Employee not found");
            }

            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete employee", e);
        }
    }

    @Override
    public List<Employee> getEmployeesByRoleId(int roleId) {
        String query = """
            SELECT e.*, w.title, w.description 
            FROM employee e
            JOIN work_role w ON e.role_id = w.role_id 
            WHERE e.role_id = ?
            ORDER BY e.employee_id
            """;

        List<Employee> employees = new ArrayList<>();

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, roleId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Employee emp = new Employee(
                            rs.getInt("employee_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            null,
                            rs.getInt("role_id"),
                            rs.getDouble("salary")
                    );
                    employees.add(emp);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not get employees for role: " + roleId, e);
        }

        return employees;
    }

    @Override
    public boolean loginEmployee(String email, String password) {
        String query = """
            SELECT employee_id, role_id
            FROM employee 
            WHERE email = ? AND password = ?
            """;

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not verify login credentials", e);
        }
    }

    @Override
    public void changePassword(int employee_id, String oldPassword, String newPassword) {
        String verifyQuery = """
            SELECT COUNT(*) as count 
            FROM employee
            WHERE employee_id = ? AND password = ?
            """;

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(verifyQuery)) {

            pstmt.setInt(1, employee_id);
            pstmt.setString(2, oldPassword);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt("count") > 0) {
                    updatePassword(employee_id, newPassword);
                } else {
                    throw new RuntimeException("Invalid old password");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not change password", e);
        }
    }

    private void updatePassword(int employee_id, String newPassword) {
        String query = """
            UPDATE employee
            SET password = ?
            WHERE employee_id = ?
            """;

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, newPassword);
            pstmt.setInt(2, employee_id);
            pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException("Could not update password", e);
        }
    }
}