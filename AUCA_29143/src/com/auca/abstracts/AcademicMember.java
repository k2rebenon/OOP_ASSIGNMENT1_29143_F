package com.auca.abstracts;

import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;

import java.time.LocalDate;

public abstract class AcademicMember extends Person {
    private String memberNumber;
    private Department department;
    private LocalDate joinDate;
    private String academicStatus;
    private String institutionalEmail;
    private String advisorName;

    protected AcademicMember(String personId, String firstName, String lastName, String email,
                             String phoneNumber, LocalDate dateOfBirth,
                             String memberNumber, Department department, LocalDate joinDate,
                             String academicStatus, String institutionalEmail, String advisorName)
            throws ValidationException {
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth);
        this.memberNumber = Registrable.requireText(memberNumber, "Member number",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        Registrable.requireReference(department, "Department");
        this.department = department;
        this.joinDate = Registrable.requireDateNotFuture(joinDate, "Join date");
        this.academicStatus = Registrable.requireChoice(academicStatus, "Academic status", "ACTIVE", "INACTIVE");
        this.institutionalEmail = Registrable.requireEmail(institutionalEmail, "Institutional email");
        this.advisorName = Registrable.requireText(advisorName, "Advisor name",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
    }

    @Override
    public void validate() throws ValidationException {
        super.validate();
        Registrable.requireText(memberNumber, "Member number",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        Registrable.requireReference(department, "Department");
        Registrable.requireDateNotFuture(joinDate, "Join date");
        Registrable.requireChoice(academicStatus, "Academic status", "ACTIVE", "INACTIVE");
        Registrable.requireEmail(institutionalEmail, "Institutional email");
        Registrable.requireText(advisorName, "Advisor name",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
    }

    @Override
    public boolean isActive() {
        return "ACTIVE".equals(academicStatus);
    }

    @Override
    public abstract String getAcademicCategory();

    @Override
    public void printDetails() {
        super.printDetails();
        System.out.println("  Member number      : " + memberNumber);
        System.out.println("  Department         : " + department.getDisplayName());
        System.out.println("  Join date          : " + joinDate);
        System.out.println("  Academic status    : " + academicStatus);
        System.out.println("  Institutional email: " + institutionalEmail);
        System.out.println("  Advisor name       : " + advisorName);
    }

    @Override
    public String toString() {
        return super.toString() + " AcademicMember{memberNumber='" + memberNumber
                + "', department='" + department.getDisplayName() + "', joinDate=" + joinDate
                + ", academicStatus='" + academicStatus + "', institutionalEmail='" + institutionalEmail
                + "', advisorName='" + advisorName + "'}";
    }

    public String getMemberNumber() {
        return memberNumber;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) throws ValidationException {
        Registrable.requireReference(department, "Department");
        this.department = department;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public String getAcademicStatus() {
        return academicStatus;
    }

    public void setAcademicStatus(String academicStatus) throws ValidationException {
        this.academicStatus = Registrable.requireChoice(academicStatus, "Academic status", "ACTIVE", "INACTIVE");
    }

    public String getInstitutionalEmail() {
        return institutionalEmail;
    }

    public void setInstitutionalEmail(String institutionalEmail) throws ValidationException {
        this.institutionalEmail = Registrable.requireEmail(institutionalEmail, "Institutional email");
    }

    public String getAdvisorName() {
        return advisorName;
    }

    public void setAdvisorName(String advisorName) throws ValidationException {
        this.advisorName = Registrable.requireText(advisorName, "Advisor name",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
    }
}
