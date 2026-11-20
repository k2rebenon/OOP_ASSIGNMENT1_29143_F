package com.auca.teacher;

import com.auca.abstracts.AcademicMember;
import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Teacher extends AcademicMember {
    private String employeeNumber;
    private String specialization;
    private LocalDate hireDate;
    private BigDecimal salary;
    private String officeNumber;
    private String employmentType;

    public Teacher(String personId, String firstName, String lastName, String email,
                   String phoneNumber, LocalDate dateOfBirth,
                   String memberNumber, Department department, LocalDate joinDate,
                   String academicStatus, String institutionalEmail, String advisorName,
                   String employeeNumber, String specialization, LocalDate hireDate,
                   BigDecimal salary, String officeNumber, String employmentType)
            throws ValidationException {
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth,
                memberNumber, department, joinDate, academicStatus, institutionalEmail, advisorName);
        this.employeeNumber = Registrable.requireText(employeeNumber, "Employee number",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        this.specialization = Registrable.requireText(specialization, "Specialization",
                Registrable.MIN_NAME_LENGTH, 120);
        this.hireDate = Registrable.requireDateNotFuture(hireDate, "Hire date");
        this.salary = validateSalary(salary);
        this.officeNumber = Registrable.requireText(officeNumber, "Office number", 1, 20);
        this.employmentType = Registrable.requireChoice(employmentType, "Employment type",
                "FULL_TIME", "PART_TIME", "CONTRACT");
    }

    private static BigDecimal validateSalary(BigDecimal salary) throws ValidationException {
        if (salary == null) {
            throw new ValidationException("Salary is required.");
        }
        if (salary.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Salary must be strictly positive.");
        }
        return salary;
    }

    @Override
    public void validate() throws ValidationException {
        super.validate();
        Registrable.requireText(employeeNumber, "Employee number",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        Registrable.requireText(specialization, "Specialization", Registrable.MIN_NAME_LENGTH, 120);
        Registrable.requireDateNotFuture(hireDate, "Hire date");
        validateSalary(salary);
        Registrable.requireText(officeNumber, "Office number", 1, 20);
        Registrable.requireChoice(employmentType, "Employment type",
                "FULL_TIME", "PART_TIME", "CONTRACT");
    }

    @Override
    public String getRoleDescription() {
        return "Teacher";
    }

    @Override
    public String getAcademicCategory() {
        return "Teacher";
    }

    @Override
    public void printDetails() {
        super.printDetails();
        System.out.println("  Employee number    : " + employeeNumber);
        System.out.println("  Specialization     : " + specialization);
        System.out.println("  Hire date          : " + hireDate);
        System.out.println("  Salary             : " + salary.toPlainString() + " RWF");
        System.out.println("  Office number      : " + officeNumber);
        System.out.println("  Employment type    : " + employmentType);
    }

    @Override
    public String toString() {
        return super.toString() + " Teacher{employeeNumber='" + employeeNumber + "', specialization='"
                + specialization + "', hireDate=" + hireDate + ", salary=" + salary.toPlainString()
                + ", officeNumber='" + officeNumber + "', employmentType='" + employmentType + "'}";
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) throws ValidationException {
        this.specialization = Registrable.requireText(specialization, "Specialization",
                Registrable.MIN_NAME_LENGTH, 120);
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) throws ValidationException {
        this.salary = validateSalary(salary);
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) throws ValidationException {
        this.officeNumber = Registrable.requireText(officeNumber, "Office number", 1, 20);
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) throws ValidationException {
        this.employmentType = Registrable.requireChoice(employmentType, "Employment type",
                "FULL_TIME", "PART_TIME", "CONTRACT");
    }
}
