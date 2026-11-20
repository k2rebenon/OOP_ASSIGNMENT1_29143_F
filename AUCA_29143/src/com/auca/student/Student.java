package com.auca.student;

import com.auca.abstracts.AcademicMember;
import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;

import java.time.LocalDate;

public class Student extends AcademicMember {
    private String studentNumber;
    private String programName;
    private LocalDate admissionDate;
    private int studyYear;
    private String guardianPhone;
    private boolean scholarshipStatus;

    public Student(String personId, String firstName, String lastName, String email,
                   String phoneNumber, LocalDate dateOfBirth,
                   String memberNumber, Department department, LocalDate joinDate,
                   String academicStatus, String institutionalEmail, String advisorName,
                   String studentNumber, String programName, LocalDate admissionDate,
                   int studyYear, String guardianPhone, boolean scholarshipStatus)
            throws ValidationException {
        super(personId, firstName, lastName, email, phoneNumber, dateOfBirth,
                memberNumber, department, joinDate, academicStatus, institutionalEmail, advisorName);
        this.studentNumber = Registrable.requireText(studentNumber, "Student number",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        this.programName = Registrable.requireText(programName, "Program name",
                Registrable.MIN_NAME_LENGTH, 120);
        this.admissionDate = Registrable.requireDateNotFuture(admissionDate, "Admission date");
        this.studyYear = validateStudyYear(studyYear);
        this.guardianPhone = Registrable.requirePhone(guardianPhone, "Guardian phone");
        this.scholarshipStatus = scholarshipStatus;
    }

    private static int validateStudyYear(int studyYear) throws ValidationException {
        if (studyYear < 1 || studyYear > 6) {
            throw new ValidationException("Study year must be between 1 and 6.");
        }
        return studyYear;
    }

    @Override
    public void validate() throws ValidationException {
        super.validate();
        Registrable.requireText(studentNumber, "Student number",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        Registrable.requireText(programName, "Program name", Registrable.MIN_NAME_LENGTH, 120);
        Registrable.requireDateNotFuture(admissionDate, "Admission date");
        validateStudyYear(studyYear);
        Registrable.requirePhone(guardianPhone, "Guardian phone");
    }

    @Override
    public String getRoleDescription() {
        return "Student";
    }

    @Override
    public String getAcademicCategory() {
        return "Student";
    }

    @Override
    public void printDetails() {
        super.printDetails();
        System.out.println("  Student number     : " + studentNumber);
        System.out.println("  Program name       : " + programName);
        System.out.println("  Admission date     : " + admissionDate);
        System.out.println("  Study year         : " + studyYear);
        System.out.println("  Guardian phone     : " + guardianPhone);
        System.out.println("  Scholarship        : " + (scholarshipStatus ? "YES" : "NO"));
    }

    @Override
    public String toString() {
        return super.toString() + " Student{studentNumber='" + studentNumber + "', programName='"
                + programName + "', admissionDate=" + admissionDate + ", studyYear=" + studyYear
                + ", guardianPhone='" + guardianPhone + "', scholarshipStatus=" + scholarshipStatus + "}";
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) throws ValidationException {
        this.programName = Registrable.requireText(programName, "Program name",
                Registrable.MIN_NAME_LENGTH, 120);
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public int getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(int studyYear) throws ValidationException {
        this.studyYear = validateStudyYear(studyYear);
    }

    public String getGuardianPhone() {
        return guardianPhone;
    }

    public void setGuardianPhone(String guardianPhone) throws ValidationException {
        this.guardianPhone = Registrable.requirePhone(guardianPhone, "Guardian phone");
    }

    public boolean isScholarshipStatus() {
        return scholarshipStatus;
    }

    public void setScholarshipStatus(boolean scholarshipStatus) {
        this.scholarshipStatus = scholarshipStatus;
    }
}
