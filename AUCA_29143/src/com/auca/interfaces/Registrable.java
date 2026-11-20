package com.auca.interfaces;

import com.auca.exceptions.BookUnavailableException;
import com.auca.exceptions.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public interface Registrable {
    int MIN_NAME_LENGTH = 2;
    int MAX_NAME_LENGTH = 60;
    int MAX_EMAIL_LENGTH = 100;
    int MIN_PHONE_LENGTH = 10;
    int MAX_PHONE_LENGTH = 15;
    String DATE_PATTERN = "yyyy-MM-dd";

    void validate() throws ValidationException;

    void printDetails();

    String getRegistrationSummary();

    default String getRoleDescription() {
        return "Registered Entity";
    }

    default String getAcademicCategory() {
        return "Not applicable";
    }

    default void borrowCopy() throws BookUnavailableException {
        throw new BookUnavailableException("This record type does not support borrowing copies.");
    }

    default void returnCopy() throws ValidationException {
        throw new ValidationException("This record type does not support returning copies.");
    }

    default void returnBook(LocalDate returnDate) throws ValidationException {
        throw new ValidationException("This record type does not support book returns.");
    }

    static String requireText(String raw, String label, int min, int max) throws ValidationException {
        if (raw == null) {
            throw new ValidationException(label + " is required.");
        }
        String value = raw.trim().replaceAll("\\s+", " ");
        if (value.isEmpty()) {
            throw new ValidationException(label + " is required.");
        }
        if (value.length() < min || value.length() > max) {
            throw new ValidationException(label + " must be " + min + "-" + max + " characters long.");
        }
        return value;
    }

    static String requireEmail(String raw, String label) throws ValidationException {
        String value = requireText(raw, label, 5, MAX_EMAIL_LENGTH);
        if (!value.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new ValidationException(label + " is not a valid email address.");
        }
        return value;
    }

    static String requirePhone(String raw, String label) throws ValidationException {
        if (raw == null) {
            throw new ValidationException(label + " is required.");
        }
        String value = raw.trim().replaceAll("[\\s-]", "");
        if (!value.matches("^\\+?\\d{" + MIN_PHONE_LENGTH + "," + MAX_PHONE_LENGTH + "}$")) {
            throw new ValidationException(label + " must contain " + MIN_PHONE_LENGTH + "-"
                    + MAX_PHONE_LENGTH + " digits, with an optional leading +.");
        }
        return value;
    }

    static LocalDate parseDate(String raw, String label) throws ValidationException {
        if (raw == null || raw.trim().isEmpty()) {
            throw new ValidationException(label + " is required. Use " + DATE_PATTERN + ".");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN.replace("yyyy", "uuuu"))
                .withResolverStyle(ResolverStyle.STRICT);
        try {
            return LocalDate.parse(raw.trim(), formatter);
        } catch (DateTimeParseException e) {
            throw new ValidationException("Invalid date for " + label + ": use " + DATE_PATTERN
                    + " (example: 2004-05-16).");
        }
    }

    static int parseInt(String raw, String label, int min, int max) throws ValidationException {
        if (raw == null || raw.trim().isEmpty()) {
            throw new ValidationException(label + " is required.");
        }
        int value;
        try {
            value = Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException(label + " must be a whole number.");
        }
        if (value < min || value > max) {
            throw new ValidationException(label + " must be between " + min + " and " + max + ".");
        }
        return value;
    }

    static double parseDouble(String raw, String label, double min, double max) throws ValidationException {
        if (raw == null || raw.trim().isEmpty()) {
            throw new ValidationException(label + " is required.");
        }
        double value;
        try {
            value = Double.parseDouble(raw.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException(label + " must be a number.");
        }
        if (Double.isNaN(value) || Double.isInfinite(value) || value < min || value > max) {
            throw new ValidationException(label + " must be between " + min + " and " + max + ".");
        }
        return value;
    }

    static BigDecimal parsePositiveMoney(String raw, String label) throws ValidationException {
        if (raw == null || raw.trim().isEmpty()) {
            throw new ValidationException(label + " is required.");
        }
        BigDecimal value;
        try {
            value = new BigDecimal(raw.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException(label + " must be a valid amount.");
        }
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(label + " must be strictly positive.");
        }
        return value;
    }

    static String requireChoice(String raw, String label, String... allowed) throws ValidationException {
        if (raw == null) {
            throw new ValidationException(label + " is required.");
        }
        String value = raw.trim();
        for (String option : allowed) {
            if (option.equalsIgnoreCase(value)) {
                return option;
            }
        }
        throw new ValidationException(label + " must be one of: " + String.join(", ", allowed) + ".");
    }

    static LocalDate requireDateNotFuture(LocalDate date, String label) throws ValidationException {
        if (date == null) {
            throw new ValidationException(label + " is required.");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new ValidationException(label + " cannot be in the future.");
        }
        return date;
    }

    static LocalDate requireDateInPast(LocalDate date, String label) throws ValidationException {
        if (date == null) {
            throw new ValidationException(label + " is required.");
        }
        if (!date.isBefore(LocalDate.now())) {
            throw new ValidationException(label + " must be in the past.");
        }
        return date;
    }

    static void requireReference(Object reference, String label) throws ValidationException {
        if (reference == null) {
            throw new ValidationException(label + " must reference an existing record.");
        }
    }
}
