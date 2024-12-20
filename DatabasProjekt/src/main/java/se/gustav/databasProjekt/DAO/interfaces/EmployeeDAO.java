package se.gustav.databasProjekt.DAO.interfaces;

import se.gustav.databasProjekt.models.Employee;

import java.util.List;

public interface EmployeeDAO {
    void createEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Employee getEmployee(int employee_id);
    Employee getEmployeeByEmail(String email); // För inloggning
    Employee getEmployeeWithRole(int employee_id); // För JOIN-operationen
    void updateEmployee(Employee employee);
    void deleteEmployee(int employee_id);
    boolean loginEmployee(String email, String password);
    void changePassword(int employee_id, String oldPassword, String newPassword);
    List<Employee> getEmployeesByRoleId(int roleId);

}
