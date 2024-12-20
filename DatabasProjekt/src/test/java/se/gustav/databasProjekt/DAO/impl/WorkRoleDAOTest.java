package se.gustav.databasProjekt.DAO.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.gustav.databasProjekt.DAO.impl.WorkRoleDAOImpl;
import se.gustav.databasProjekt.JDBCUtil;
import se.gustav.databasProjekt.models.WorkRole;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// skapar tabellerna work_role och employee i testklassen eftersom testerna
// körs mot en  testdatabas, som vanligtvis är tom från början

public class WorkRoleDAOTest {
    private WorkRoleDAOImpl workRoleDAO;

    @BeforeEach
    void setUp() {
        workRoleDAO = new WorkRoleDAOImpl();

        // Skapa tabellerna för test
        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement()) {

            // Skapa work_role tabell
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS work_role (
                    role_id INT PRIMARY KEY AUTO_INCREMENT,
                    title VARCHAR(50) NOT NULL,
                    description VARCHAR(50) NOT NULL,
                    creation_date DATE NOT NULL
                )
            """);

            // Skapa employee tabell eftersom den används i JOIN
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS employee (
                    employee_id INT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(50) NOT NULL,
                    email VARCHAR(70) NOT NULL,
                    password VARCHAR(20) NOT NULL,
                    role_id INT NOT NULL,
                    salary DOUBLE NOT NULL,
                    FOREIGN KEY (role_id) REFERENCES work_role(role_id)
                )
            """);

            conn.commit();
        } catch (Exception e) {
            throw new RuntimeException("Could not set up test database", e);
        }
    }

    @AfterEach
    void tearDown() {
        // Rensa alla tabeller efter varje test
        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement()) {

            // Radera i rätt ordning pga foreign key constraints
            stmt.execute("DROP TABLE IF EXISTS employee");
            stmt.execute("DROP TABLE IF EXISTS work_role");

            conn.commit();
        } catch (Exception e) {
            System.err.println("Error cleaning up test database: " + e.getMessage());
        }
    }

    @Test
    void testCreateAndGetWorkRole() {
        // Skapa ny arbetsroll
        WorkRole newRole = new WorkRole(
                "Testroll",
                "Testbeskrivning",
                new Date(System.currentTimeMillis())
        );

        workRoleDAO.createWorkRole(newRole);

        // Hämta alla arbetsroller
        List<WorkRole> roles = workRoleDAO.getAllWorkRoles();

        // Verifiera att arbetsrollen finns
        assertFalse(roles.isEmpty(), "Listan ska inte vara tom");
        assertTrue(roles.stream()
                        .anyMatch(role -> role.getTitle().equals("Testroll")),
                "Den skapade rollen ska finnas i listan");
    }

    @Test
    void testCreateDuplicateWorkRole() {
        WorkRole role1 = new WorkRole(
                "Testroll",
                "Beskrivning 1",
                new Date(System.currentTimeMillis())
        );

        workRoleDAO.createWorkRole(role1);

        // Försök skapa en roll med samma titel
        WorkRole role2 = new WorkRole(
                "Testroll",
                "Beskrivning 2",
                new Date(System.currentTimeMillis())
        );

        assertThrows(RuntimeException.class, () -> {
            workRoleDAO.createWorkRole(role2);
        });
    }


}