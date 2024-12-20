package se.gustav.databasProjekt.ui;

import se.gustav.databasProjekt.DAO.interfaces.EmployeeDAO;
import se.gustav.databasProjekt.DAO.interfaces.WorkRoleDAO;
import se.gustav.databasProjekt.models.Employee;
import se.gustav.databasProjekt.models.WorkRole;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuChoiceHandler {

    private final InputHandler inputHandler;
    private final WorkRoleDAO WORK_ROLE_DAO;
    private final EmployeeDAO EMPLOYEE_DAO;

    public MenuChoiceHandler(InputHandler inputHandler, WorkRoleDAO workRoleDAO, EmployeeDAO employeeDAO) {
        this.inputHandler = inputHandler;
        this.WORK_ROLE_DAO = workRoleDAO;
        this.EMPLOYEE_DAO = employeeDAO;
    }

    void handleCreateWorkRole() {
        System.out.println("\n=== Skapa ny arbetsroll ===");

        try {
            System.out.print("Ange titel: ");
            String title = inputHandler.getUserInputForCreating();

            System.out.print("Ange beskrivning: ");
            String description = inputHandler.getUserInputForCreating();

            WorkRole newRole = new WorkRole(title, description, new Date(System.currentTimeMillis()));
            //En ny arbetsroll skapas som ett WorkRole-objekt.
            try {
                WORK_ROLE_DAO.createWorkRole(newRole); //DAO försöker spara arbetsrollen i databasen
                System.out.println("Arbetsroll skapad!");
                handleCreateEmployeeForRole(newRole.getRole_id());

                // createworkrole metoden ovan checkar om denna workrole redan finns
                // och om det skriver ut ett meddelande "finns inte"

            } catch (RuntimeException e) {
                if (e.getMessage().contains("finns redan")) {
                    System.out.println("\nDet finns redan en arbetsroll med titeln: " + title);
                    System.out.print("Vill du skapa en variant av denna roll med ny beskrivning? (ja/nej): ");
                    String answer = inputHandler.getUserInputForCreating().toLowerCase();

                    if (answer.equals("ja")) {
                        System.out.print("Ange ny beskrivning: ");
                        String newDescription = inputHandler.getUserInputForCreating();

                        if (WORK_ROLE_DAO.existsWithTitleAndDescription(title, newDescription)) {
                            System.out.println("Det finns redan en arbetsroll med denna titel och beskrivning.");
                            return;
                        }

                        WorkRole variantRole = new WorkRole(title, newDescription,
                                new Date(System.currentTimeMillis()), true);

                        WORK_ROLE_DAO.createWorkRole(variantRole);
                        System.out.println("Ny variant av arbetsrollen skapad!");
                        handleCreateEmployeeForRole(variantRole.getRole_id());
                    }
                } else {
                    throw e;
                }
            }
        } catch (RuntimeException e) {
            System.out.println("Kunde inte skapa arbetsroll: " + e.getMessage());
        }
    }

    void handleDeleteWorkRole() {
        System.out.println("\n=== Ta bort arbetsroll ===");

        try {
            System.out.print("Ange roll-ID att ta bort: ");
            int roleId = Integer.parseInt(inputHandler.getUserInputForMenuChoice());

            WORK_ROLE_DAO.deleteWorkRole(roleId);
            System.out.println("Arbetsroll borttagen!");

        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt ID. Ange ett nummer.");
        } catch (RuntimeException e) {
            System.out.println("Kunde inte ta bort arbetsroll: " + e.getMessage());
        }
    }

    void handleShowAllWorkRoles() {
        System.out.println("\n=== Alla arbetsroller ===");
        try {
            List<WorkRole> roles = WORK_ROLE_DAO.getAllWorkRoles();
            // säkerställer att listan inte innehåller något annat än WR-objekt
            if (roles.isEmpty()) {
                System.out.println("Inga arbetsroller hittades.");
            } else {
                for (WorkRole role : roles) {
                    System.out.println("Roll-ID: " + role.getRole_id());
                    System.out.println("Titel: " + role.getTitle());
                    System.out.println("Beskrivning: " + role.getDescription());
                    if (role.getEmployeeName() != null) {
                        System.out.println("Anställda: " + role.getEmployeeName());
                        // eftersom alla workroles kanske inte har en anställd
                    }
                    System.out.println("Skapad: " + role.getCreation_date());
                    System.out.println("-----------------");
                }
            }
        } catch (RuntimeException e) {
            System.out.println("Kunde inte hämta arbetsroller: " + e.getMessage());
        }
    }

    void handleShowWorkRole() {
        System.out.println("\n=== Visa arbetsroll ===");
        try {
            System.out.print("Ange roll-ID: ");
            int roleId = Integer.parseInt(inputHandler.getUserInputForMenuChoice());

            WorkRole role = WORK_ROLE_DAO.getWorkRole(roleId);

            System.out.println("Roll-ID: " + role.getRole_id());
            System.out.println("Titel: " + role.getTitle());
            System.out.println("Beskrivning: " + role.getDescription());
            if (role.getEmployeeName() != null) {
                System.out.println("Anställda: " + role.getEmployeeName());
            }
            System.out.println("Skapad: " + role.getCreation_date());
        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt ID. Ange ett nummer.");
        } catch (RuntimeException e) {
            System.out.println("Kunde inte hitta arbetsroll: " + e.getMessage());
        }
    }

    void handleUpdateWorkRole() {
        System.out.println("\n=== Uppdatera arbetsroll ===");

        try {
            System.out.print("Ange roll-ID att uppdatera: ");
            int roleId = Integer.parseInt(inputHandler.getUserInputForCreating());

            WorkRole currentRole = WORK_ROLE_DAO.getWorkRole(roleId);
            System.out.println("Nuvarande information:");
            System.out.println("Titel: " + currentRole.getTitle());
            System.out.println("Beskrivning: " + currentRole.getDescription());

            System.out.println("\nAnge ny information (eller lämna tomt för att behålla nuvarande):");

            System.out.print("Ny titel: ");
            String title = inputHandler.getUserInputForCreating();
            title = title.isEmpty() ? currentRole.getTitle() : title;
            //ternära operatorer för att bestämma värden baserat på
            // om en inmatning är tom eller inte

            System.out.print("Ny beskrivning: ");
            String description = inputHandler.getUserInputForCreating();
            description = description.isEmpty() ? currentRole.getDescription() : description;
            // Behåll den befintliga titeln från currentRole om inmatning är tom
            WorkRole updatedRole = new WorkRole(
                    roleId,
                    title,
                    description,
                    currentRole.getCreation_date()
            );

            WORK_ROLE_DAO.updateWorkRole(updatedRole);
            System.out.println("Arbetsroll uppdaterad!");

        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt nummer angivet.");
        } catch (RuntimeException e) {
            System.out.println("Kunde inte uppdatera arbetsroll: " + e.getMessage());
        }
    }

    void handleCreateEmployeeForRole(int roleId) {
        try {
            System.out.print("\nVill du skapa en anställd för denna roll? (ja/nej): ");
            String answer = inputHandler.getUserInputForCreating().toLowerCase();

            while (answer.equals("ja")) {
                System.out.print("Ange namn på den anställda: ");
                String name = inputHandler.getUserInputForCreating();

                String email;
                do {
                    System.out.print("Ange email: ");
                    email = inputHandler.getUserInputForCreating();
                    if (!Employee.isValidEmail(email)) {
                        System.out.println("Ogiltig email-adress. Försök igen.");
                    }
                } while (!Employee.isValidEmail(email));
                // så länge det är falskt så kör loopen på nytt

                System.out.print("Ange lösenord: ");
                String password = inputHandler.getUserInputForCreating();

                System.out.print("Ange lön: ");
                double salary = Double.parseDouble(inputHandler.getUserInputForCreating());

                Employee newEmployee = new Employee(name, email, password, roleId, salary);
                EMPLOYEE_DAO.createEmployee(newEmployee);
                System.out.println("Anställd skapad och kopplad till arbetsrollen!");

                System.out.print("\nVill du skapa ytterligare en anställd för denna roll? (ja/nej): ");
                answer = inputHandler.getUserInputForCreating().toLowerCase();
            }
        } catch (RuntimeException e) {
            System.out.println("Kunde inte skapa anställd: " + e.getMessage());
        }
    }

    void handleCreateEmployee() {
        System.out.println("\n=== Skapa ny anställd ===");
        try {
            System.out.println("Tillgängliga arbetsroller:");
            List<WorkRole> roles = WORK_ROLE_DAO.getAllWorkRoles();
            Map<Integer, WorkRole> uniqueRoles = new HashMap<>();
            //Vi skapar en Map med Integer som nyckel och WorkRole som värde
            // int för unika id kopplat till WR och WR för att kunna skapa ny anställd
            for (WorkRole role : roles) {
                if (!uniqueRoles.containsKey(role.getRole_id())) {
                    uniqueRoles.put(role.getRole_id(), role);
                    System.out.println(role.getRole_id() + ": " + role.getTitle() +
                            " (" + role.getDescription() + ")");
                }
            }
            // för att filtrera ut unika arbetsroller innan vi låter användaren
            // välja en roll att koppla den nya anställda till

            System.out.print("Välj roll-ID: ");
            int roleId = Integer.parseInt(inputHandler.getUserInputForCreating());

            System.out.print("Ange namn på den anställda: ");
            String name = inputHandler.getUserInputForCreating();

            String email;
            do {
                System.out.print("Ange email: ");
                email = inputHandler.getUserInputForCreating();
                if (!Employee.isValidEmail(email)) {
                    System.out.println("Ogiltig email-adress. Försök igen.");
                }
            } while (!Employee.isValidEmail(email));

            System.out.print("Ange lösenord: ");
            String password = inputHandler.getUserInputForCreating();

            System.out.print("Ange lön: ");
            double salary = Double.parseDouble(inputHandler.getUserInputForCreating());

            Employee newEmployee = new Employee(name, email, password, roleId, salary);
            EMPLOYEE_DAO.createEmployee(newEmployee);
            System.out.println("Anställd skapad!");

        } catch (RuntimeException e) {
            System.out.println("Kunde inte skapa anställd: " + e.getMessage());
        }
    }

    void handleShowAllEmployees() {
        System.out.println("\n=== Alla anställda ===");
        try {
            List<Employee> employees = EMPLOYEE_DAO.getAllEmployees();
            if (employees.isEmpty()) {
                System.out.println("Inga anställda hittades.");
            } else {
                for (Employee employee : employees) {
                    System.out.println("Anställnings ID: " + employee.getRole_id() + "." +
                            employee.getEmployee_id());
                    System.out.println("Namn: " + employee.getName());
                    System.out.println("Email: " + employee.getEmail());
                    System.out.println("Lön: " + employee.getSalary());

                    // hämtar tillhörande information från WR objekt som är kopplad
                    // till ett specifikt employee
                    WorkRole role = WORK_ROLE_DAO.getWorkRole(employee.getRole_id());
                    System.out.println("Roll: " + role.getTitle());
                    System.out.println("Beskrivning: " + role.getDescription());
                    System.out.println("-----------------");
                }
            }
        } catch (RuntimeException e) {
            System.out.println("Kunde inte hämta anställda: " + e.getMessage());
        }
    }

    void handleShowEmployee() {
        System.out.println("\n=== Visa anställd ===");
        try {
            System.out.print("Ange anställnings-ID: ");
            int employeeId = Integer.parseInt(inputHandler.getUserInputForMenuChoice());

            Employee employee = EMPLOYEE_DAO.getEmployee(employeeId);

            System.out.println("Anställnings-ID: " + employee.getRole_id() + "." +
                    employee.getEmployee_id());
            System.out.println("Namn: " + employee.getName());
            System.out.println("Email: " + employee.getEmail());
            System.out.println("Lön: " + employee.getSalary());

            WorkRole role = WORK_ROLE_DAO.getWorkRole(employee.getRole_id());
            System.out.println("Roll: " + role.getTitle());
            System.out.println("Beskrivning: " + role.getDescription());

        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt ID. Ange ett nummer.");
        } catch (RuntimeException e) {
            System.out.println("Kunde inte hitta anställd: " + e.getMessage());
        }
    }

    void handleLoginEmployee() {
        System.out.println("\n=== Logga in ===");
        try {
            System.out.print("Ange email: ");
            String email = inputHandler.getUserInputForCreating();

            System.out.print("Ange lösenord: ");
            String password = inputHandler.getUserInputForCreating();

            // skickas till vår sql kod och kollar matchning för email o password
            // finns en match retuneras employeeid och roleid
            // värderna används för att skapa nedan objekt å få ut info
            if (EMPLOYEE_DAO.loginEmployee(email, password)) {
                Employee employee = EMPLOYEE_DAO.getEmployeeByEmail(email);
                WorkRole workRole = WORK_ROLE_DAO.getWorkRole(employee.getRole_id());

                System.out.println("Inloggad som: " + employee.getName());
                System.out.println("Roll: " + workRole.getTitle());
                System.out.println("Beskrivning: " + workRole.getDescription());
                System.out.println("Lön: " + employee.getSalary());
            } else {
                System.out.println("Felaktigt email eller lösenord.");
            }
        } catch (RuntimeException e) {
            System.out.println("Inloggning misslyckades: " + e.getMessage());
        }
    }

    void handleDeleteEmployee() {
        System.out.println("\n=== Radera anställd ===");
        try {
            System.out.println("Tillgängliga anställda:");
            List<Employee> employees = EMPLOYEE_DAO.getAllEmployees();
            for (Employee emp : employees) {
                WorkRole role = WORK_ROLE_DAO.getWorkRole(emp.getRole_id());
                System.out.println(emp.getRole_id() + "." + emp.getEmployee_id() +
                        ": " + emp.getName() + " (" + role.getTitle() + ")");
            }

            System.out.print("\nAnge anställnings-ID att radera: ");
            int employeeId = Integer.parseInt(inputHandler.getUserInputForCreating());

            System.out.print("Är du säker på att du vill radera denna anställd? (ja/nej): ");
            String confirm = inputHandler.getUserInputForCreating().toLowerCase();

            if (confirm.equals("ja")) {
                EMPLOYEE_DAO.deleteEmployee(employeeId);
                System.out.println("Anställd raderad!");
            } else {
                System.out.println("Radering avbruten.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ogiltigt ID. Ange ett nummer.");
        } catch (RuntimeException e) {
            System.out.println("Kunde inte radera anställd: " + e.getMessage());
        }
    }


}
