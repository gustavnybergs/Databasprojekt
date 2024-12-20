package se.gustav.databasProjekt.DAO.impl;

import se.gustav.databasProjekt.DAO.interfaces.WorkRoleDAO;
import se.gustav.databasProjekt.JDBCUtil;
import se.gustav.databasProjekt.models.WorkRole;

import java.sql.*;
import java.sql.Date;
import java.util.*;


public class WorkRoleDAOImpl implements WorkRoleDAO {


    @Override
    public void createWorkRole(WorkRole workRole) {
        try (Connection conn = JDBCUtil.getConnection()) {

            // Kontrollera bara efter dubletter om det INTE är en variant
            if (!workRole.isVariant()) {
                String checkQuery = "SELECT role_id FROM work_role WHERE LOWER(title) = LOWER(?)";
                // jämförelsen kommer jämföra en titel i databasen mot den inmatade titeln i "?"

                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, workRole.getTitle());
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        throw new RuntimeException("finns redan");
                    }
                }
            }

            String insertQuery = "INSERT INTO work_role (title, description, creation_date) VALUES (?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, workRole.getTitle());
                pstmt.setString(2, workRole.getDescription());
                pstmt.setDate(3, workRole.getCreation_date());

                int affectedRows = pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        workRole.setRole_id(generatedKeys.getInt(1));
                    }
                }

                conn.commit();

            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not create work role", e);
        }
    }

    public boolean existsWithTitleAndDescription(String title, String description) {
        String checkQuery = "SELECT COUNT(*) FROM work_role WHERE LOWER(title) = LOWER(?) AND LOWER(description) = LOWER(?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkQuery)) {

            pstmt.setString(1, title);
            pstmt.setString(2, description);
            ResultSet rs = pstmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Could not check work role existence", e);
        }
    }

    @Override
    public List<WorkRole> getAllWorkRoles() {
        String query = """
           SELECT wr.role_id, wr.title, wr.description, wr.creation_date,
                  GROUP_CONCAT(e.name) as employee_names, 
                  COUNT(e.employee_id) as employee_count
           FROM work_role wr
           LEFT JOIN employee e ON wr.role_id = e.role_id
           GROUP BY wr.role_id, wr.title, wr.description, wr.creation_date
           ORDER BY wr.role_id
           """;

        List<WorkRole> workRoles = new ArrayList<>();

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int roleId = rs.getInt("role_id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                Date creationDate = rs.getDate("creation_date");
                String employeeNames = rs.getString("employee_names");
                int employeeCount = rs.getInt("employee_count");

                WorkRole workRole = new WorkRole(roleId, title, description, creationDate);
                if (employeeNames != null) {
                    workRole.setEmployeeName(employeeNames);
                }
                workRoles.add(workRole);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not get work roles", e);
        }

        return workRoles;
    }

    @Override
    public WorkRole getWorkRole(int role_id) {
        String query = "SELECT * FROM work_role WHERE role_id = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, role_id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new WorkRole(
                            rs.getInt("role_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getDate("creation_date")
                    );
                }
                throw new RuntimeException("Work role not found with id: " + role_id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not get work role with id: " + role_id, e);
        }
    }

    @Override
    public void updateWorkRole(WorkRole workRole) {
        String query = """
                   UPDATE work_role
                   SET title = ?, description = ?, creation_date = ?
                   WHERE role_id = ?
                        """;

        try (Connection conn = JDBCUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)){

            pstmt.setString(1, workRole.getTitle());
            pstmt.setString(2, workRole.getDescription());
            pstmt.setDate(3, workRole.getCreation_date());
            pstmt.setInt(4, workRole.getRole_id());
            pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException("Could not update work role with id: " + workRole.getRole_id(), e);
        }

    }

    @Override
    public void deleteWorkRole(int role_id) {
        String query = "DELETE FROM work_role WHERE role_id = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, role_id);

            pstmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException("Could not delete work role with id: " + role_id, e);
        }

    }
}
