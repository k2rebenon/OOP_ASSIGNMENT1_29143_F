package com.auca.enrollment;

import com.auca.course.Course;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.student.Student;

import java.time.LocalDate;

public class Enrollment implements Identifiable, Registrable {
    private String enrollmentId;
    private Student student;
    private Course course;
    private LocalDate enrollmentDate;
    private String semester;
    private String enrollmentStatus;

    public Enrollment(String enrollmentId, Student student, Course course, LocalDate enrollmentDate,
                      String semester, String enrollmentStatus) throws ValidationException {
        this.enrollmentId = Registrable.requireText(enrollmentId, "Enrollment ID",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        Registrable.requireReference(student, "Student");
        Registrable.requireReference(course, "Course");
        this.student = student;
        this.course = course;
        this.enrollmentDate = Registrable.requireDateNotFuture(enrollmentDate, "Enrollment date");
        this.semester = Registrable.requireChoice(semester, "Semester", "Semester 1", "Semester 2");
        this.enrollmentStatus = Registrable.requireChoice(enrollmentStatus, "Enrollment status",
                "ACTIVE", "COMPLETED", "DROPPED");
    }

    @Override
    public void validate() throws ValidationException {
        Registrable.requireText(enrollmentId, "Enrollment ID",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        Registrable.requireReference(student, "Student");
        Registrable.requireReference(course, "Course");
        Registrable.requireDateNotFuture(enrollmentDate, "Enrollment date");
        Registrable.requireChoice(semester, "Semester", "Semester 1", "Semester 2");
        Registrable.requireChoice(enrollmentStatus, "Enrollment status",
                "ACTIVE", "COMPLETED", "DROPPED");
    }

    @Override
    public String getId() {
        return enrollmentId;
    }

    @Override
    public String getDisplayName() {
        return student.getDisplayName() + " in " + course.getCourseCode() + " (" + semester + ")";
    }

    @Override
    public boolean isActive() {
        return "ACTIVE".equals(enrollmentStatus);
    }

    @Override
    public String getRoleDescription() {
        return "Enrollment";
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + enrollmentId + "] " + getDisplayName() + " | " + enrollmentStatus;
    }

    @Override
    public void printDetails() {
        System.out.println("  Enrollment ID      : " + enrollmentId);
        System.out.println("  Student            : " + student.getDisplayName()
                + " (" + student.getStudentNumber() + ")");
        System.out.println("  Course             : " + course.getCourseCode() + " - " + course.getCourseTitle());
        System.out.println("  Enrollment date    : " + enrollmentDate);
        System.out.println("  Semester           : " + semester);
        System.out.println("  Enrollment status  : " + enrollmentStatus);
    }

    @Override
    public String toString() {
        return "Enrollment{enrollmentId='" + enrollmentId + "', student='"
                + student.getDisplayName() + "', course='" + course.getCourseCode()
                + "', enrollmentDate=" + enrollmentDate + ", semester='" + semester
                + "', enrollmentStatus='" + enrollmentStatus + "'}";
    }

    public String getEnrollmentId() {
        return enrollmentId;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public String getSemester() {
        return semester;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(String enrollmentStatus) throws ValidationException {
        this.enrollmentStatus = Registrable.requireChoice(enrollmentStatus, "Enrollment status",
                "ACTIVE", "COMPLETED", "DROPPED");
    }
}
