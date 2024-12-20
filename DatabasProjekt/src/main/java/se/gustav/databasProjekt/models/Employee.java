package se.gustav.databasProjekt.models;

public class Employee {
    private int employee_id;
    private String name;
    private String email;
    private String password;
    private int role_id; // Kopplar den anställda till en specifik arbetsroll i databasen.
    private double salary;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    // Regex-mönster för att validera att e-postadresser har korrekt format.

    // Default konstruktor utan employee_id. Skapa en ny anställd utan att inkludera ett ID
    // ID:t genereras sen av databasen.
    public Employee (String name, String email, String password, int role_id, double salary) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role_id = role_id;
        this.salary = salary;
    }

    // Orginal konstruktor med employee_id.
    // Representera en befintlig anställd med ett unikt ID.
    // Används för hämtar eller uppdaterar en anställd
    public Employee(int employee_id, String name, String email, String password, int role_id, double salary) {
        this.employee_id = employee_id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role_id = role_id;
        this.salary = salary;
    }

    /*
    fler konstruktorer Ger flexibilitet för olika scenarier
    ny anställd, befintlig anställd osv
     */


    // Validerar om en given e-postadress matchar ett korrekt format.
    public static boolean isValidEmail(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    public int getEmployee_id() {
        return employee_id;
    }
    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getRole_id() {
        return role_id;
    }
    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }
    public double getSalary() {
        return salary;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employee_id=" + employee_id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role_id=" + role_id +
                '}';
    }


}
