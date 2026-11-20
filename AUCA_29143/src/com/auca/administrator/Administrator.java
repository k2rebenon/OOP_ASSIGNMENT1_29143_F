package com.auca.administrator;

import com.auca.abstracts.Person;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;

import java.time.LocalDate;

public class Administrator extends Person {
    private String staffNumber;
    private String jobTitle;
    private String assignedOffice;
    private LocalDate employmentDate;
    private int accessLevel;
    private boolean active;

    public Administrator(String personId, String firstName, String lastName, String email,
                         String phoneNumber, LocalDate dateOfBirth,
                         String staffNumber, String jobTitle, String assignedOffice,
                         LocalDate employmentDate, int accessLevel, boolean active)
            throws ValidationException {
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth);
        this.staffNumber = Registrable.requireText(staffNumber, "Staff number",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        this.jobTitle = Registrable.requireText(jobTitle, "Job title",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
        this.assignedOffice = Registrable.requireText(assignedOffice, "Assigned office", 1, 40);
        this.employmentDate = Registrable.requireDateNotFuture(employmentDate, "Employment date");
        this.accessLevel = validateAccessLevel(accessLevel);
        this.active = active;
    }

    private static int validateAccessLevel(int accessLevel) throws ValidationException {
        if (accessLevel < 1 || accessLevel > 5) {
            throw new ValidationException("Access level must be between 1 and 5.");
        }
        return accessLevel;
    }

    @Override
    public void validate() throws ValidationException {
        super.validate();
        Registrable.requireText(staffNumber, "Staff number",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        Registrable.requireText(jobTitle, "Job title",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
        Registrable.requireText(assignedOffice, "Assigned office", 1, 40);
        Registrable.requireDateNotFuture(employmentDate, "Employment date");
        validateAccessLevel(accessLevel);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public String getRoleDescription() {
        return "Administrator";
    }

    @Override
    public void printDetails() {
        super.printDetails();
        System.out.println("  Staff number       : " + staffNumber);
        System.out.println("  Job title          : " + jobTitle);
        System.out.println("  Assigned office    : " + assignedOffice);
        System.out.println("  Employment date    : " + employmentDate);
        System.out.println("  Access level       : " + accessLevel);
        System.out.println("  Active             : " + (active ? "YES" : "NO"));
    }

    @Override
    public String toString() {
        return super.toString() + " Administrator{staffNumber='" + staffNumber + "', jobTitle='"
                + jobTitle + "', assignedOffice='" + assignedOffice + "', employmentDate=" + employmentDate
                + ", accessLevel=" + accessLevel + ", active=" + active + "}";
    }

    public String getStaffNumber() {
        return staffNumber;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) throws ValidationException {
        this.jobTitle = Registrable.requireText(jobTitle, "Job title",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
    }

    public String getAssignedOffice() {
        return assignedOffice;
    }

    public void setAssignedOffice(String assignedOffice) throws ValidationException {
        this.assignedOffice = Registrable.requireText(assignedOffice, "Assigned office", 1, 40);
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) throws ValidationException {
        this.accessLevel = validateAccessLevel(accessLevel);
    }

    public boolean isActiveFlag() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
