package com.auca.assessment;

import com.auca.enrollment.Enrollment;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;

import java.time.LocalDate;

public class Assessment implements Identifiable, Registrable {
    private String assessmentId;
    private Enrollment enrollment;
    private String assessmentType;
    private double maximumScore;
    private double obtainedScore;
    private LocalDate assessmentDate;

    public Assessment(String assessmentId, Enrollment enrollment, String assessmentType,
                      double maximumScore, double obtainedScore, LocalDate assessmentDate)
            throws ValidationException {
        this.assessmentId = Registrable.requireText(assessmentId, "Assessment ID",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        Registrable.requireReference(enrollment, "Enrollment");
        this.enrollment = enrollment;
        this.assessmentType = Registrable.requireChoice(assessmentType, "Assessment type",
                "CAT", "QUIZ", "ASSIGNMENT", "EXAM", "PRACTICAL");
        validateMaximumScore(maximumScore);
        validateObtainedScore(obtainedScore, maximumScore);
        if (assessmentDate == null) {
            throw new ValidationException("Assessment date is required.");
        }
        this.maximumScore = maximumScore;
        this.obtainedScore = obtainedScore;
        this.assessmentDate = assessmentDate;
    }

    private static void validateMaximumScore(double maximumScore) throws ValidationException {
        if (Double.isNaN(maximumScore) || Double.isInfinite(maximumScore) || maximumScore <= 0) {
            throw new ValidationException("Maximum score must be greater than zero.");
        }
    }

    private static void validateObtainedScore(double obtainedScore, double maximumScore)
            throws ValidationException {
        if (Double.isNaN(obtainedScore) || Double.isInfinite(obtainedScore)
                || obtainedScore < 0 || obtainedScore > maximumScore) {
            throw new ValidationException("Obtained score must be between 0 and the maximum score ("
                    + maximumScore + ").");
        }
    }

    @Override
    public void validate() throws ValidationException {
        Registrable.requireText(assessmentId, "Assessment ID",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        Registrable.requireReference(enrollment, "Enrollment");
        Registrable.requireChoice(assessmentType, "Assessment type",
                "CAT", "QUIZ", "ASSIGNMENT", "EXAM", "PRACTICAL");
        validateMaximumScore(maximumScore);
        validateObtainedScore(obtainedScore, maximumScore);
        if (assessmentDate == null) {
            throw new ValidationException("Assessment date is required.");
        }
    }

    @Override
    public String getId() {
        return assessmentId;
    }

    @Override
    public String getDisplayName() {
        return assessmentType + " for " + enrollment.getDisplayName();
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public String getRoleDescription() {
        return "Assessment";
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + assessmentId + "] " + assessmentType + " | "
                + enrollment.getStudent().getDisplayName() + " / " + enrollment.getCourse().getCourseCode()
                + " | " + obtainedScore + "/" + maximumScore;
    }

    @Override
    public void printDetails() {
        System.out.println("  Assessment ID      : " + assessmentId);
        System.out.println("  Enrollment         : " + enrollment.getDisplayName());
        System.out.println("  Assessment type    : " + assessmentType);
        System.out.println("  Maximum score      : " + maximumScore);
        System.out.println("  Obtained score     : " + obtainedScore);
        System.out.println("  Assessment date    : " + assessmentDate);
    }

    @Override
    public String toString() {
        return "Assessment{assessmentId='" + assessmentId + "', enrollment='"
                + enrollment.getEnrollmentId() + "', assessmentType='" + assessmentType
                + "', maximumScore=" + maximumScore + ", obtainedScore=" + obtainedScore
                + ", assessmentDate=" + assessmentDate + "}";
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public double getMaximumScore() {
        return maximumScore;
    }

    public double getObtainedScore() {
        return obtainedScore;
    }

    public void setObtainedScore(double obtainedScore) throws ValidationException {
        validateObtainedScore(obtainedScore, maximumScore);
        this.obtainedScore = obtainedScore;
    }

    public LocalDate getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(LocalDate assessmentDate) throws ValidationException {
        if (assessmentDate == null) {
            throw new ValidationException("Assessment date is required.");
        }
        this.assessmentDate = assessmentDate;
    }
}
