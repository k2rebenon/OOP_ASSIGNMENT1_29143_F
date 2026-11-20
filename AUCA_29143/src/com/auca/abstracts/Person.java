package com.auca.abstracts;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;

import java.time.LocalDate;

public abstract class Person implements Identifiable, Registrable {
    private String personId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;

    protected Person(String personId, String firstName, String lastName, String email,
                     String phoneNumber, LocalDate dateOfBirth) throws ValidationException {
        this.personId = Registrable.requireText(personId, "Person ID",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        this.firstName = Registrable.requireText(firstName, "First name",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
        this.lastName = Registrable.requireText(lastName, "Last name",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
        this.email = Registrable.requireEmail(email, "Email");
        this.phoneNumber = Registrable.requirePhone(phoneNumber, "Phone number");
        this.dateOfBirth = Registrable.requireDateInPast(dateOfBirth, "Date of birth");
    }

    @Override
    public void validate() throws ValidationException {
        Registrable.requireText(personId, "Person ID",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        Registrable.requireText(firstName, "First name",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
        Registrable.requireText(lastName, "Last name",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
        Registrable.requireEmail(email, "Email");
        Registrable.requirePhone(phoneNumber, "Phone number");
        Registrable.requireDateInPast(dateOfBirth, "Date of birth");
    }

    @Override
    public abstract boolean isActive();

    @Override
    public abstract String getRoleDescription();

    @Override
    public String getId() {
        return personId;
    }

    @Override
    public String getDisplayName() {
        return firstName + " " + lastName;
    }

    @Override
    public String getRegistrationSummary() {
        return Identifiable.ID_PREFIX + Identifiable.ID_SEPARATOR + getId()
                + " | " + getDisplayName()
                + " | " + getRoleDescription()
                + " | " + (isActive() ? "ACTIVE" : "INACTIVE");
    }

    @Override
    public void printDetails() {
        System.out.println("  Role               : " + getRoleDescription());
        System.out.println("  Person ID          : " + personId);
        System.out.println("  First name         : " + firstName);
        System.out.println("  Last name          : " + lastName);
        System.out.println("  Email              : " + email);
        System.out.println("  Phone              : " + phoneNumber);
        System.out.println("  Date of birth      : " + dateOfBirth);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{personId='" + personId + "', name='" + getDisplayName()
                + "', email='" + email + "', phone='" + phoneNumber + "', dateOfBirth=" + dateOfBirth + "}";
    }

    public String getPersonId() {
        return personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) throws ValidationException {
        this.firstName = Registrable.requireText(firstName, "First name",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) throws ValidationException {
        this.lastName = Registrable.requireText(lastName, "Last name",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws ValidationException {
        this.email = Registrable.requireEmail(email, "Email");
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) throws ValidationException {
        this.phoneNumber = Registrable.requirePhone(phoneNumber, "Phone number");
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
}
