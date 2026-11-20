package com.auca.department;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.teacher.Teacher;

public class Department implements Identifiable, Registrable {
    private String departmentId;
    private String departmentName;
    private String departmentCode;
    private String officeLocation;
    private String contactEmail;
    private Teacher headTeacher;

    public Department(String departmentId, String departmentName, String departmentCode,
                      String officeLocation, String contactEmail, Teacher headTeacher)
            throws ValidationException {
        this.departmentId = Registrable.requireText(departmentId, "Department ID",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        this.departmentName = Registrable.requireText(departmentName, "Department name",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
        this.departmentCode = Registrable.requireText(departmentCode, "Department code", 2, 10);
        this.officeLocation = Registrable.requireText(officeLocation, "Office location", 1, 60);
        this.contactEmail = Registrable.requireEmail(contactEmail, "Contact email");
        this.headTeacher = headTeacher;
    }

    @Override
    public void validate() throws ValidationException {
        Registrable.requireText(departmentId, "Department ID",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        Registrable.requireText(departmentName, "Department name",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
        Registrable.requireText(departmentCode, "Department code", 2, 10);
        Registrable.requireText(officeLocation, "Office location", 1, 60);
        Registrable.requireEmail(contactEmail, "Contact email");
    }

    @Override
    public String getId() {
        return departmentId;
    }

    @Override
    public String getDisplayName() {
        return departmentName;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public String getRoleDescription() {
        return "Department";
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + departmentId + "] " + departmentName + " (" + departmentCode + ")";
    }

    @Override
    public void printDetails() {
        System.out.println("  Department ID      : " + departmentId);
        System.out.println("  Department name    : " + departmentName);
        System.out.println("  Department code    : " + departmentCode);
        System.out.println("  Office location    : " + officeLocation);
        System.out.println("  Contact email      : " + contactEmail);
        System.out.println("  Head teacher       : "
                + (headTeacher == null ? "Not assigned" : headTeacher.getDisplayName()));
    }

    @Override
    public String toString() {
        return "Department{departmentId='" + departmentId + "', departmentName='" + departmentName
                + "', departmentCode='" + departmentCode + "', officeLocation='" + officeLocation
                + "', contactEmail='" + contactEmail + "', headTeacher='"
                + (headTeacher == null ? "None" : headTeacher.getDisplayName()) + "'}";
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) throws ValidationException {
        this.departmentName = Registrable.requireText(departmentName, "Department name",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) throws ValidationException {
        this.officeLocation = Registrable.requireText(officeLocation, "Office location", 1, 60);
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) throws ValidationException {
        this.contactEmail = Registrable.requireEmail(contactEmail, "Contact email");
    }

    public Teacher getHeadTeacher() {
        return headTeacher;
    }

    public void setHeadTeacher(Teacher headTeacher) {
        this.headTeacher = headTeacher;
    }
}
