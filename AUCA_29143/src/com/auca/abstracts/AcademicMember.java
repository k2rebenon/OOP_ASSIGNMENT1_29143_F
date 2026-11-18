package.com.auca.abstract;

import java.time.LocalDate;

import com.auca.department.Department;

import com.auca.abstracts.Person;




public abstract class AcademicMember extends Person {
    
// Six attributes    
private String memberNumber;
    private Department department;
    private LocalDate joinDate;
    private String academicStatus;
    private String institutionalEmail;
    private String advisorName;

// Constructor
    public AcademicMember(
            String personId,
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            LocalDate dateOfBirth,
            String memberNumber,
            Department department,
            LocalDate joinDate,
            String academicStatus,
            String institutionalEmail,
            String advisorName) {

        // Send personal information to Person
        super(personId, firstName, lastName, email,
                phoneNumber, dateOfBirth);

        // Save academic information
        this.memberNumber = memberNumber;
        this.department = department;
        this.joinDate = joinDate;
        this.academicStatus = academicStatus;
        this.institutionalEmail = institutionalEmail;
        this.advisorName = advisorName;
    }

    // Getters
    public String getMemberNumber() {
        return memberNumber;
    }

    public Department getDepartment() {
        return department;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public String getAcademicStatus() {
        return academicStatus;
    }

    public String getInstitutionalEmail() {
        return institutionalEmail;
    }

    public String getAdvisorName() {
        return advisorName;
    }

    // Setters
    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public void setAcademicStatus(String academicStatus) {
        this.academicStatus = academicStatus;
    }

    public void setInstitutionalEmail(String institutionalEmail) {
        this.institutionalEmail = institutionalEmail;
    }

    public void setAdvisorName(String advisorName) {
        this.advisorName = advisorName;
    }

// Abstract method
    public abstract String getAcademicCategory();

    // Display academic information
    @Override
    public String toString() {
        return super.toString()
                + "\nMember Number: " + memberNumber
                + "\nDepartment: " + department
                + "\nJoin Date: " + joinDate
                + "\nAcademic Status: " + academicStatus
                + "\nInstitutional Email: " + institutionalEmail
                + "\nAdvisor Name: " + advisorName;
    }

}
