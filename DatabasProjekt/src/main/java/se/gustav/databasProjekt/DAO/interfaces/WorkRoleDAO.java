package se.gustav.databasProjekt.DAO.interfaces;

import se.gustav.databasProjekt.models.WorkRole;

import java.util.List;

public interface WorkRoleDAO {
    void createWorkRole(WorkRole workRole);
    List<WorkRole> getAllWorkRoles();
    WorkRole getWorkRole(int role_id);
    void updateWorkRole(WorkRole workRole);
    void deleteWorkRole(int role_id);
    boolean existsWithTitleAndDescription(String title, String description);

}
