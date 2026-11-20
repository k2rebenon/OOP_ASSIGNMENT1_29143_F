package com.auca.course;

import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.teacher.Teacher;

public class Course implements Identifiable, Registrable {
    private String courseId;
    private String courseCode;
    private String courseTitle;
    private int creditHours;
    private Department department;
    private Teacher assignedTeacher;

    public Course(String courseId, String courseCode, String courseTitle, int creditHours,
                  Department department, Teacher assignedTeacher) throws ValidationException {
        this.courseId = Registrable.requireText(courseId, "Course ID",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        this.courseCode = Registrable.requireText(courseCode, "Course code", 2, 12);
        this.courseTitle = Registrable.requireText(courseTitle, "Course title",
                Registrable.MIN_NAME_LENGTH, 120);
        this.creditHours = validateCreditHours(creditHours);
        Registrable.requireReference(department, "Department");
        Registrable.requireReference(assignedTeacher, "Assigned teacher");
        this.department = department;
        this.assignedTeacher = assignedTeacher;
    }

    private static int validateCreditHours(int creditHours) throws ValidationException {
        if (creditHours < 1 || creditHours > 6) {
            throw new ValidationException("Credit hours must be between 1 and 6.");
        }
        return creditHours;
    }

    @Override
    public void validate() throws ValidationException {
        Registrable.requireText(courseId, "Course ID",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        Registrable.requireText(courseCode, "Course code", 2, 12);
        Registrable.requireText(courseTitle, "Course title", Registrable.MIN_NAME_LENGTH, 120);
        validateCreditHours(creditHours);
        Registrable.requireReference(department, "Department");
        Registrable.requireReference(assignedTeacher, "Assigned teacher");
    }

    @Override
    public String getId() {
        return courseId;
    }

    @Override
    public String getDisplayName() {
        return courseCode + " - " + courseTitle;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public String getRoleDescription() {
        return "Course";
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + courseId + "] " + courseCode + " - " + courseTitle
                + " | " + creditHours + " credits | " + department.getDisplayName()
                + " | " + assignedTeacher.getDisplayName();
    }

    @Override
    public void printDetails() {
        System.out.println("  Course ID          : " + courseId);
        System.out.println("  Course code        : " + courseCode);
        System.out.println("  Course title       : " + courseTitle);
        System.out.println("  Credit hours       : " + creditHours);
        System.out.println("  Department         : " + department.getDisplayName());
        System.out.println("  Assigned teacher   : " + assignedTeacher.getDisplayName());
    }

    @Override
    public String toString() {
        return "Course{courseId='" + courseId + "', courseCode='" + courseCode + "', courseTitle='"
                + courseTitle + "', creditHours=" + creditHours + ", department='"
                + department.getDisplayName() + "', assignedTeacher='"
                + assignedTeacher.getDisplayName() + "'}";
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) throws ValidationException {
        this.courseTitle = Registrable.requireText(courseTitle, "Course title",
                Registrable.MIN_NAME_LENGTH, 120);
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) throws ValidationException {
        this.creditHours = validateCreditHours(creditHours);
    }

    public Department getDepartment() {
        return department;
    }

    public Teacher getAssignedTeacher() {
        return assignedTeacher;
    }

    public void setAssignedTeacher(Teacher assignedTeacher) throws ValidationException {
        Registrable.requireReference(assignedTeacher, "Assigned teacher");
        this.assignedTeacher = assignedTeacher;
    }
}
