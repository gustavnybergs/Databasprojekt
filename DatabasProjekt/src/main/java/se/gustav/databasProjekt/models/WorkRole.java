package se.gustav.databasProjekt.models;

import java.sql.Date;

public class WorkRole {
    private int role_id;
    private String title;
    private String description;
    private Date creation_date;
    private String employeeName;
    private boolean isVariant;
    private int employeeId;

    // Konstruktor för att skapa ny arbetsroll
    public WorkRole(String title, String description, Date creation_date) {
        this.title = title;
        this.description = description;
        this.creation_date = creation_date;
        this.isVariant = false;
    }

    // Konstruktor för att skapa variant av en arbetsroll
    // tex med annan description eller lön
    public WorkRole(String title, String description, Date creation_date, boolean isVariant) {
        this(title, description, creation_date);  // Anropar första konstruktorn
        this.isVariant = isVariant;
    }

    // Konstruktor för att uppdatera existerande arbetsroll
    public WorkRole(int role_id, String title, String description, Date creation_date) {
        this(title, description, creation_date);  // Anropar första konstruktorn
        this.role_id = role_id;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public boolean isVariant() {
        return isVariant;
    }

    public void setVariant(boolean variant) {
        isVariant = variant;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public String toString() {
        return "WorkRole{" +
                "role_id=" + role_id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", creation_date=" + creation_date +
                ", employeeName='" + employeeName + '\'' +
                '}';
    }

}