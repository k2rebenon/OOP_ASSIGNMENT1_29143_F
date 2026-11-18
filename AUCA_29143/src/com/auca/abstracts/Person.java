package com.auca.abstracts;

import java.time.LocalDate;

import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;


public abstract  class Person implements Identifiable, Registrable {

    private String personId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private  LocalDate dateOfBirth;
    
    
    public Person(String personId, String firstName, String lastName, String email, String phoneNumber,
            LocalDate dateOfBirth) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }
// Getters

    public String getPersonId() {
        return personId;
    }


    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public String getEmail() {
        return email;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }


    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

// Setters
    public void setPersonId(String personId) {
        this.personId = personId;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

 // Identifiable methods
    @Override
    public String getId() {
        return personId;
    }

    @Override
    public String getDisplayName() {
        return firstName + " " + lastName;
    }

    // Methods to be completed by child classes
    @Override
    public abstract boolean isActive();

    public abstract String getRoleDescription();

    // Display person information
    @Override
    public String toString() {
        return "Person ID: " + personId
                + "\nName: " + getDisplayName()
                + "\nEmail: " + email
                + "\nPhone: " + phoneNumber
                + "\nDate of Birth: " + dateOfBirth;
    }


    
    
}
