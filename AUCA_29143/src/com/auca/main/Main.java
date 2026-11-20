package com.auca.main;

import com.auca.abstracts.Person;
import com.auca.administrator.Administrator;
import com.auca.assessment.Assessment;
import com.auca.bookloan.BookLoan;
import com.auca.classroom.Classroom;
import com.auca.course.Course;
import com.auca.department.Department;
import com.auca.enrollment.Enrollment;
import com.auca.exceptions.DuplicateRecordException;
import com.auca.exceptions.RecordNotFoundException;
import com.auca.exceptions.ValidationException;
import com.auca.feepayment.FeePayment;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.librarybook.LibraryBook;
import com.auca.student.Student;
import com.auca.teacher.Teacher;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

public class Main {
    private static final Scanner SC = new Scanner(System.in);
    private static final Object BACK = new Object();

    public static void main(String[] args) {
        System.out.println("Welcome to the AUCA School Management System.");
        try {
            boolean running = true;
            while (running) {
                printMainMenu();
                int choice = readMenuChoice(0, 19);
                if (choice == 0) {
                    running = false;
                    System.out.println("\n+ Goodbye.");
                } else {
                    try {
                        handle(choice);
                    } catch (EndOfInput e) {
                        throw e;
                    } catch (CancelForm | BackForm e) {
                        System.out.println("\n+ Operation cancelled. No changes were saved.");
                    } catch (Exception e) {
                        System.out.println("\n! " + messageOf(e));
                    }
                }
            }
        } catch (EndOfInput e) {
            System.out.println("\nInput stream closed. Exiting application.");
        }
    }

    private static void printMainMenu() {
        System.out.println();
        System.out.println("============== AUCA_ID SCHOOL MANAGEMENT ==============");
        System.out.println(" 1. Register Department       11. Borrow Library Book");
        System.out.println(" 2. Register Student          12. Return Library Book");
        System.out.println(" 3. Register Teacher          13. View All Records");
        System.out.println(" 4. Register Administrator    14. Search Record by ID");
        System.out.println(" 5. Register Course           15. Update Existing Record");
        System.out.println(" 6. Register Classroom        16. Student Academic Summary");
        System.out.println(" 7. Enroll Student            17. Student Payment History");
        System.out.println(" 8. Record Assessment         18. View Available Books");
        System.out.println(" 9. Record Fee Payment        19. Demonstrate Polymorphism");
        System.out.println("10. Register Library Book      0. Exit");
        System.out.print("Select an option: ");
    }

    private static void handle(int choice) throws Exception {
        switch (choice) {
            case 1 -> registerDepartment();
            case 2 -> registerStudent();
            case 3 -> registerTeacher();
            case 4 -> registerAdministrator();
            case 5 -> registerCourse();
            case 6 -> registerClassroom();
            case 7 -> registerEnrollment();
            case 8 -> recordAssessment();
            case 9 -> recordFeePayment();
            case 10 -> registerLibraryBook();
            case 11 -> borrowBook();
            case 12 -> returnBook();
            case 13 -> viewAllRecords();
            case 14 -> searchRecord();
            case 15 -> updateRecord();
            case 16 -> studentAcademicSummary();
            case 17 -> studentPaymentHistory();
            case 18 -> viewAvailableBooks();
            case 19 -> demonstratePolymorphism();
            default -> throw new ValidationException("Invalid menu selection.");
        }
    }

    private static void viewAllRecords() {
        if (SchoolDatabase.totalRecords() == 0) {
            System.out.println("\nNo records have been registered yet.");
            return;
        }
        System.out.println("\n===================== ALL RECORDS =====================");
        printGroup("DEPARTMENTS", SchoolDatabase.getDepartments());
        printGroup("STUDENTS", SchoolDatabase.getStudents());
        printGroup("TEACHERS", SchoolDatabase.getTeachers());
        printGroup("ADMINISTRATORS", SchoolDatabase.getAdministrators());
        printGroup("COURSES", SchoolDatabase.getCourses());
        printGroup("CLASSROOMS", SchoolDatabase.getClassrooms());
        printGroup("ENROLLMENTS", SchoolDatabase.getEnrollments());
        printGroup("ASSESSMENTS", SchoolDatabase.getAssessments());
        printGroup("FEE PAYMENTS", SchoolDatabase.getFeePayments());
        printGroup("LIBRARY BOOKS", SchoolDatabase.getLibraryBooks());
        printGroup("BOOK LOANS", SchoolDatabase.getBookLoans());
    }

    private static void printGroup(String title, List<? extends Registrable> records) {
        if (records.isEmpty()) {
            return;
        }
        System.out.println("\n-- " + title + " (" + records.size() + ") --");
        for (Registrable record : records) {
            record.printDetails();
            System.out.println("  --------------------------------------------------");
        }
    }

    private static void searchRecord() throws Exception {
        System.out.print("\nEnter record ID, code, receipt number or ISBN: ");
        String query = readLine().trim();
        if (query.isEmpty()) {
            throw new ValidationException("Search text cannot be blank.");
        }
        List<Object> matches = SchoolDatabase.search(query);
        if (matches.isEmpty()) {
            throw new RecordNotFoundException("No record matches \"" + query + "\".");
        }
        System.out.println("\n+ " + matches.size() + " record(s) found.");
        for (Object match : matches) {
            System.out.println("  --------------------------------------------------");
            ((Registrable) match).printDetails();
        }
    }

    private static void updateRecord() throws Exception {
        System.out.print("\nEnter the ID of the record to update: ");
        String query = readLine().trim();
        List<Object> matches = SchoolDatabase.search(query);
        if (matches.isEmpty()) {
            throw new RecordNotFoundException("No record matches \"" + query + "\".");
        }
        Object record;
        if (matches.size() == 1) {
            record = matches.get(0);
        } else {
            System.out.println("\nMultiple records match. Select one:");
            for (int i = 0; i < matches.size(); i++) {
                System.out.println((i + 1) + ". " + ((Registrable) matches.get(i)).getRegistrationSummary());
            }
            System.out.println("0. Cancel");
            System.out.print("Select: ");
            int choice = readMenuChoice(0, matches.size());
            if (choice == 0) {
                throw new CancelForm();
            }
            record = matches.get(choice - 1);
        }
        System.out.println("\nSelected record:");
        ((Registrable) record).printDetails();
        if (record instanceof Department d) {
            updateDepartment(d);
        } else if (record instanceof Student s) {
            updateStudent(s);
        } else if (record instanceof Teacher t) {
            updateTeacher(t);
        } else if (record instanceof Administrator a) {
            updateAdministrator(a);
        } else if (record instanceof Course c) {
            updateCourse(c);
        } else if (record instanceof Classroom c) {
            updateClassroom(c);
        } else if (record instanceof Enrollment e) {
            updateEnrollment(e);
        } else if (record instanceof Assessment a) {
            updateAssessment(a);
        } else if (record instanceof FeePayment p) {
            updateFeePayment(p);
        } else if (record instanceof LibraryBook b) {
            updateLibraryBook(b);
        } else if (record instanceof BookLoan l) {
            updateBookLoan(l);
        }
    }

    private static void updateDepartment(Department d) {
        while (true) {
            System.out.println("\n-- UPDATE DEPARTMENT --");
            System.out.println("1. Department name");
            System.out.println("2. Office location");
            System.out.println("3. Contact email");
            System.out.println("4. Head teacher");
            System.out.println("0. Done");
            System.out.print("Select field: ");
            int choice = readMenuChoice(0, 4);
            if (choice == 0) {
                return;
            }
            try {
                if (choice == 1) {
                    String value = Registrable.requireText(readLineAfter("New department name: "),
                            "Department name", Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
                    if (confirmChange("Department name", d.getDepartmentName(), value)) {
                        d.setDepartmentName(value);
                        System.out.println("+ Department name updated.");
                    }
                } else if (choice == 2) {
                    String value = Registrable.requireText(readLineAfter("New office location: "),
                            "Office location", 1, 60);
                    if (confirmChange("Office location", d.getOfficeLocation(), value)) {
                        d.setOfficeLocation(value);
                        System.out.println("+ Office location updated.");
                    }
                } else if (choice == 3) {
                    String value = Registrable.requireEmail(readLineAfter("New contact email: "),
                            "Contact email");
                    if (confirmChange("Contact email", d.getContactEmail(), value)) {
                        d.setContactEmail(value);
                        System.out.println("+ Contact email updated.");
                    }
                } else {
                    Teacher teacher = chooseTeacher(true);
                    String oldValue = d.getHeadTeacher() == null
                            ? "Not assigned" : d.getHeadTeacher().getDisplayName();
                    String newValue = teacher == null ? "Not assigned" : teacher.getDisplayName();
                    if (confirmChange("Head teacher", oldValue, newValue)) {
                        d.setHeadTeacher(teacher);
                        System.out.println("+ Head teacher updated.");
                    }
                }
            } catch (CancelForm | BackForm e) {
                System.out.println("+ Change cancelled.");
            } catch (Exception e) {
                System.out.println("! " + messageOf(e));
            }
        }
    }

    private static void updateStudent(Student s) {
        while (true) {
            System.out.println("\n-- UPDATE STUDENT --");
            System.out.println("1. First name");
            System.out.println("2. Last name");
            System.out.println("3. Email");
            System.out.println("4. Phone");
            System.out.println("5. Program name");
            System.out.println("6. Study year");
            System.out.println("7. Guardian phone");
            System.out.println("8. Scholarship status");
            System.out.println("9. Academic status");
            System.out.println("0. Done");
            System.out.print("Select field: ");
            int choice = readMenuChoice(0, 9);
            if (choice == 0) {
                return;
            }
            try {
                if (choice == 1) {
                    String value = Registrable.requireText(readLineAfter("New first name: "),
                            "First name", Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
                    if (confirmChange("First name", s.getFirstName(), value)) {
                        s.setFirstName(value);
                        System.out.println("+ First name updated.");
                    }
                } else if (choice == 2) {
                    String value = Registrable.requireText(readLineAfter("New last name: "),
                            "Last name", Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
                    if (confirmChange("Last name", s.getLastName(), value)) {
                        s.setLastName(value);
                        System.out.println("+ Last name updated.");
                    }
                } else if (choice == 3) {
                    String value = Registrable.requireEmail(readLineAfter("New email: "), "Email");
                    if (!value.equalsIgnoreCase(s.getEmail()) && SchoolDatabase.personEmailExists(value)) {
                        throw new DuplicateRecordException("Email " + value + " already exists.");
                    }
                    if (confirmChange("Email", s.getEmail(), value)) {
                        s.setEmail(value);
                        System.out.println("+ Email updated.");
                    }
                } else if (choice == 4) {
                    String value = Registrable.requirePhone(readLineAfter("New phone: "), "Phone number");
                    if (confirmChange("Phone", s.getPhoneNumber(), value)) {
                        s.setPhoneNumber(value);
                        System.out.println("+ Phone updated.");
                    }
                } else if (choice == 5) {
                    String value = Registrable.requireText(readLineAfter("New program name: "),
                            "Program name", Registrable.MIN_NAME_LENGTH, 120);
                    if (confirmChange("Program name", s.getProgramName(), value)) {
                        s.setProgramName(value);
                        System.out.println("+ Program name updated.");
                    }
                } else if (choice == 6) {
                    int value = Registrable.parseInt(readLineAfter("New study year (1-6): "),
                            "Study year", 1, 6);
                    if (confirmChange("Study year", String.valueOf(s.getStudyYear()),
                            String.valueOf(value))) {
                        s.setStudyYear(value);
                        System.out.println("+ Study year updated.");
                    }
                } else if (choice == 7) {
                    String value = Registrable.requirePhone(readLineAfter("New guardian phone: "),
                            "Guardian phone");
                    if (confirmChange("Guardian phone", s.getGuardianPhone(), value)) {
                        s.setGuardianPhone(value);
                        System.out.println("+ Guardian phone updated.");
                    }
                } else if (choice == 8) {
                    boolean value = parseYesNo(readLineAfter("Scholarship [1 YES / 2 NO]: "),
                            "Scholarship status");
                    if (confirmChange("Scholarship status",
                            s.isScholarshipStatus() ? "YES" : "NO", value ? "YES" : "NO")) {
                        s.setScholarshipStatus(value);
                        System.out.println("+ Scholarship status updated.");
                    }
                } else {
                    String value = parseOption(readLineAfter("Academic status [1 ACTIVE / 2 INACTIVE]: "),
                            "Academic status", "ACTIVE", "INACTIVE");
                    if (confirmChange("Academic status", s.getAcademicStatus(), value)) {
                        s.setAcademicStatus(value);
                        System.out.println("+ Academic status updated.");
                    }
                }
            } catch (CancelForm | BackForm e) {
                System.out.println("+ Change cancelled.");
            } catch (Exception e) {
                System.out.println("! " + messageOf(e));
            }
        }
    }

    private static void updateTeacher(Teacher t) {
        while (true) {
            System.out.println("\n-- UPDATE TEACHER --");
            System.out.println("1. Specialization");
            System.out.println("2. Salary");
            System.out.println("3. Office number");
            System.out.println("4. Employment type");
            System.out.println("5. Academic status");
            System.out.println("6. Advisor name");
            System.out.println("0. Done");
            System.out.print("Select field: ");
            int choice = readMenuChoice(0, 6);
            if (choice == 0) {
                return;
            }
            try {
                if (choice == 1) {
                    String value = Registrable.requireText(readLineAfter("New specialization: "),
                            "Specialization", Registrable.MIN_NAME_LENGTH, 120);
                    if (confirmChange("Specialization", t.getSpecialization(), value)) {
                        t.setSpecialization(value);
                        System.out.println("+ Specialization updated.");
                    }
                } else if (choice == 2) {
                    BigDecimal value = Registrable.parsePositiveMoney(readLineAfter("New salary: "),
                            "Salary");
                    if (confirmChange("Salary", t.getSalary().toPlainString(), value.toPlainString())) {
                        t.setSalary(value);
                        System.out.println("+ Salary updated.");
                    }
                } else if (choice == 3) {
                    String value = Registrable.requireText(readLineAfter("New office number: "),
                            "Office number", 1, 20);
                    if (confirmChange("Office number", t.getOfficeNumber(), value)) {
                        t.setOfficeNumber(value);
                        System.out.println("+ Office number updated.");
                    }
                } else if (choice == 4) {
                    String value = parseOption(readLineAfter(
                                    "Employment type [1 FULL_TIME / 2 PART_TIME / 3 CONTRACT]: "),
                            "Employment type", "FULL_TIME", "PART_TIME", "CONTRACT");
                    if (confirmChange("Employment type", t.getEmploymentType(), value)) {
                        t.setEmploymentType(value);
                        System.out.println("+ Employment type updated.");
                    }
                } else if (choice == 5) {
                    String value = parseOption(readLineAfter("Academic status [1 ACTIVE / 2 INACTIVE]: "),
                            "Academic status", "ACTIVE", "INACTIVE");
                    if (confirmChange("Academic status", t.getAcademicStatus(), value)) {
                        t.setAcademicStatus(value);
                        System.out.println("+ Academic status updated.");
                    }
                } else {
                    String value = Registrable.requireText(readLineAfter("New advisor name: "),
                            "Advisor name", Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
                    if (confirmChange("Advisor name", t.getAdvisorName(), value)) {
                        t.setAdvisorName(value);
                        System.out.println("+ Advisor name updated.");
                    }
                }
            } catch (CancelForm | BackForm e) {
                System.out.println("+ Change cancelled.");
            } catch (Exception e) {
                System.out.println("! " + messageOf(e));
            }
        }
    }

    private static void updateAdministrator(Administrator a) {
        while (true) {
            System.out.println("\n-- UPDATE ADMINISTRATOR --");
            System.out.println("1. Job title");
            System.out.println("2. Assigned office");
            System.out.println("3. Access level");
            System.out.println("4. Active status");
            System.out.println("0. Done");
            System.out.print("Select field: ");
            int choice = readMenuChoice(0, 4);
            if (choice == 0) {
                return;
            }
            try {
                if (choice == 1) {
                    String value = Registrable.requireText(readLineAfter("New job title: "),
                            "Job title", Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
                    if (confirmChange("Job title", a.getJobTitle(), value)) {
                        a.setJobTitle(value);
                        System.out.println("+ Job title updated.");
                    }
                } else if (choice == 2) {
                    String value = Registrable.requireText(readLineAfter("New assigned office: "),
                            "Assigned office", 1, 40);
                    if (confirmChange("Assigned office", a.getAssignedOffice(), value)) {
                        a.setAssignedOffice(value);
                        System.out.println("+ Assigned office updated.");
                    }
                } else if (choice == 3) {
                    int value = Registrable.parseInt(readLineAfter("New access level (1-5): "),
                            "Access level", 1, 5);
                    if (confirmChange("Access level", String.valueOf(a.getAccessLevel()),
                            String.valueOf(value))) {
                        a.setAccessLevel(value);
                        System.out.println("+ Access level updated.");
                    }
                } else {
                    boolean value = parseYesNo(readLineAfter("Active status [1 YES / 2 NO]: "),
                            "Active status");
                    if (confirmChange("Active status", a.isActive() ? "YES" : "NO",
                            value ? "YES" : "NO")) {
                        a.setActive(value);
                        System.out.println("+ Active status updated.");
                    }
                }
            } catch (CancelForm | BackForm e) {
                System.out.println("+ Change cancelled.");
            } catch (Exception e) {
                System.out.println("! " + messageOf(e));
            }
        }
    }

    private static void updateCourse(Course c) {
        while (true) {
            System.out.println("\n-- UPDATE COURSE --");
            System.out.println("1. Course title");
            System.out.println("2. Credit hours");
            System.out.println("3. Assigned teacher");
            System.out.println("0. Done");
            System.out.print("Select field: ");
            int choice = readMenuChoice(0, 3);
            if (choice == 0) {
                return;
            }
            try {
                if (choice == 1) {
                    String value = Registrable.requireText(readLineAfter("New course title: "),
                            "Course title", Registrable.MIN_NAME_LENGTH, 120);
                    if (confirmChange("Course title", c.getCourseTitle(), value)) {
                        c.setCourseTitle(value);
                        System.out.println("+ Course title updated.");
                    }
                } else if (choice == 2) {
                    int value = Registrable.parseInt(readLineAfter("New credit hours (1-6): "),
                            "Credit hours", 1, 6);
                    if (confirmChange("Credit hours", String.valueOf(c.getCreditHours()),
                            String.valueOf(value))) {
                        c.setCreditHours(value);
                        System.out.println("+ Credit hours updated.");
                    }
                } else {
                    Teacher teacher = chooseTeacher(false);
                    if (confirmChange("Assigned teacher",
                            c.getAssignedTeacher().getDisplayName(), teacher.getDisplayName())) {
                        c.setAssignedTeacher(teacher);
                        System.out.println("+ Assigned teacher updated.");
                    }
                }
            } catch (CancelForm | BackForm e) {
                System.out.println("+ Change cancelled.");
            } catch (Exception e) {
                System.out.println("! " + messageOf(e));
            }
        }
    }

    private static void updateClassroom(Classroom c) {
        while (true) {
            System.out.println("\n-- UPDATE CLASSROOM --");
            System.out.println("1. Building name");
            System.out.println("2. Room number");
            System.out.println("3. Capacity");
            System.out.println("4. Floor number");
            System.out.println("5. Availability");
            System.out.println("0. Done");
            System.out.print("Select field: ");
            int choice = readMenuChoice(0, 5);
            if (choice == 0) {
                return;
            }
            try {
                if (choice == 1) {
                    String value = Registrable.requireText(readLineAfter("New building name: "),
                            "Building name", Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
                    if (confirmChange("Building name", c.getBuildingName(), value)) {
                        c.setBuildingName(value);
                        System.out.println("+ Building name updated.");
                    }
                } else if (choice == 2) {
                    String value = Registrable.requireText(readLineAfter("New room number: "),
                            "Room number", 1, 10);
                    if (confirmChange("Room number", c.getRoomNumber(), value)) {
                        c.setRoomNumber(value);
                        System.out.println("+ Room number updated.");
                    }
                } else if (choice == 3) {
                    int value = Registrable.parseInt(readLineAfter("New capacity (> 0): "),
                            "Capacity", 1, 100000);
                    if (confirmChange("Capacity", String.valueOf(c.getCapacity()),
                            String.valueOf(value))) {
                        c.setCapacity(value);
                        System.out.println("+ Capacity updated.");
                    }
                } else if (choice == 4) {
                    int value = Registrable.parseInt(readLineAfter("New floor number (>= 0): "),
                            "Floor number", 0, 200);
                    if (confirmChange("Floor number", String.valueOf(c.getFloorNumber()),
                            String.valueOf(value))) {
                        c.setFloorNumber(value);
                        System.out.println("+ Floor number updated.");
                    }
                } else {
                    boolean value = parseYesNo(readLineAfter("Available [1 YES / 2 NO]: "),
                            "Availability");
                    if (confirmChange("Availability", c.isAvailable() ? "YES" : "NO",
                            value ? "YES" : "NO")) {
                        c.setAvailable(value);
                        System.out.println("+ Availability updated.");
                    }
                }
            } catch (CancelForm | BackForm e) {
                System.out.println("+ Change cancelled.");
            } catch (Exception e) {
                System.out.println("! " + messageOf(e));
            }
        }
    }

    private static void updateEnrollment(Enrollment e) {
        while (true) {
            System.out.println("\n-- UPDATE ENROLLMENT --");
            System.out.println("1. Enrollment status");
            System.out.println("0. Done");
            System.out.print("Select field: ");
            int choice = readMenuChoice(0, 1);
            if (choice == 0) {
                return;
            }
            try {
                String value = parseOption(readLineAfter(
                                "Enrollment status [1 ACTIVE / 2 COMPLETED / 3 DROPPED]: "),
                        "Enrollment status", "ACTIVE", "COMPLETED", "DROPPED");
                if (confirmChange("Enrollment status", e.getEnrollmentStatus(), value)) {
                    e.setEnrollmentStatus(value);
                    System.out.println("+ Enrollment status updated.");
                }
            } catch (Exception ex) {
                System.out.println("! " + messageOf(ex));
            }
        }
    }

    private static void updateAssessment(Assessment a) {
        while (true) {
            System.out.println("\n-- UPDATE ASSESSMENT --");
            System.out.println("1. Obtained score");
            System.out.println("2. Assessment date");
            System.out.println("0. Done");
            System.out.print("Select field: ");
            int choice = readMenuChoice(0, 2);
            if (choice == 0) {
                return;
            }
            try {
                if (choice == 1) {
                    double value = Registrable.parseDouble(readLineAfter(
                                    "New obtained score (0-" + a.getMaximumScore() + "): "),
                            "Obtained score", 0, a.getMaximumScore());
                    if (confirmChange("Obtained score", String.valueOf(a.getObtainedScore()),
                            String.valueOf(value))) {
                        a.setObtainedScore(value);
                        System.out.println("+ Obtained score updated.");
                    }
                } else {
                    LocalDate value = Registrable.parseDate(readLineAfter(
                            "New assessment date [YYYY-MM-DD]: "), "Assessment date");
                    if (confirmChange("Assessment date", String.valueOf(a.getAssessmentDate()),
                            String.valueOf(value))) {
                        a.setAssessmentDate(value);
                        System.out.println("+ Assessment date updated.");
                    }
                }
            } catch (Exception ex) {
                System.out.println("! " + messageOf(ex));
            }
        }
    }

    private static void updateFeePayment(FeePayment p) {
        while (true) {
            System.out.println("\n-- UPDATE FEE PAYMENT --");
            System.out.println("1. Payment method");
            System.out.println("0. Done");
            System.out.print("Select field: ");
            int choice = readMenuChoice(0, 1);
            if (choice == 0) {
                return;
            }
            try {
                String value = parseOption(readLineAfter(
                                "Payment method [1 CASH / 2 MOBILE_MONEY / 3 BANK_TRANSFER / 4 CHEQUE]: "),
                        "Payment method", "CASH", "MOBILE_MONEY", "BANK_TRANSFER", "CHEQUE");
                if (confirmChange("Payment method", p.getPaymentMethod(), value)) {
                    p.setPaymentMethod(value);
                    System.out.println("+ Payment method updated.");
                }
            } catch (Exception ex) {
                System.out.println("! " + messageOf(ex));
            }
        }
    }

    private static void updateLibraryBook(LibraryBook b) {
        while (true) {
            System.out.println("\n-- UPDATE LIBRARY BOOK --");
            System.out.println("1. Title");
            System.out.println("2. Author");
            System.out.println("3. Total copies");
            System.out.println("0. Done");
            System.out.print("Select field: ");
            int choice = readMenuChoice(0, 3);
            if (choice == 0) {
                return;
            }
            try {
                if (choice == 1) {
                    String value = Registrable.requireText(readLineAfter("New title: "),
                            "Book title", 1, 120);
                    if (confirmChange("Title", b.getTitle(), value)) {
                        b.setTitle(value);
                        System.out.println("+ Title updated.");
                    }
                } else if (choice == 2) {
                    String value = Registrable.requireText(readLineAfter("New author: "),
                            "Book author", Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
                    if (confirmChange("Author", b.getAuthor(), value)) {
                        b.setAuthor(value);
                        System.out.println("+ Author updated.");
                    }
                } else {
                    int value = Registrable.parseInt(readLineAfter(
                                    "New total copies (>= available " + b.getAvailableCopies() + "): "),
                            "Total copies", b.getAvailableCopies(), 100000);
                    if (confirmChange("Total copies", String.valueOf(b.getTotalCopies()),
                            String.valueOf(value))) {
                        b.setTotalCopies(value);
                        System.out.println("+ Total copies updated.");
                    }
                }
            } catch (Exception ex) {
                System.out.println("! " + messageOf(ex));
            }
        }
    }

    private static void updateBookLoan(BookLoan l) {
        while (true) {
            System.out.println("\n-- UPDATE BOOK LOAN --");
            System.out.println("1. Due date");
            System.out.println("0. Done");
            System.out.print("Select field: ");
            int choice = readMenuChoice(0, 1);
            if (choice == 0) {
                return;
            }
            try {
                LocalDate value = Registrable.parseDate(readLineAfter(
                        "New due date [YYYY-MM-DD, after " + l.getBorrowedDate() + "]: "), "Due date");
                if (value.isAfter(l.getBorrowedDate())
                        && confirmChange("Due date", String.valueOf(l.getDueDate()),
                        String.valueOf(value))) {
                    l.setDueDate(value);
                    System.out.println("+ Due date updated.");
                } else if (!value.isAfter(l.getBorrowedDate())) {
                    System.out.println("! Due date must be after the borrowed date.");
                }
            } catch (Exception ex) {
                System.out.println("! " + messageOf(ex));
            }
        }
    }

    private static boolean confirmChange(String label, String oldValue, String newValue) {
        if (oldValue.equals(newValue)) {
            System.out.println("! The new value is the same as the current value.");
            return false;
        }
        System.out.println("Change " + label + " from '" + oldValue + "' to '" + newValue + "'?");
        System.out.println("1. Confirm   0. Cancel");
        System.out.print("Select: ");
        return readMenuChoice(0, 1) == 1;
    }

    private static String readLineAfter(String prompt) {
        System.out.print(prompt);
        return readLine().trim();
    }

    private static void studentAcademicSummary() {
        Student student = chooseStudent();
        System.out.println("\n================ ACADEMIC SUMMARY ================");
        student.printDetails();

        List<Enrollment> studentEnrollments = new ArrayList<>();
        for (Enrollment e : SchoolDatabase.getEnrollments()) {
            if (e.getStudent() == student) {
                studentEnrollments.add(e);
            }
        }
        System.out.println("\n  Enrollments: " + studentEnrollments.size());
        if (studentEnrollments.isEmpty()) {
            System.out.println("  This student has no enrollments.");
        }
        double totalObtained = 0;
        double totalMaximum = 0;
        for (Enrollment e : studentEnrollments) {
            System.out.println("\n  Course: " + e.getCourse().getCourseCode() + " - "
                    + e.getCourse().getCourseTitle());
            System.out.println("  Semester: " + e.getSemester() + " | Status: " + e.getEnrollmentStatus());
            int count = 0;
            for (Assessment a : SchoolDatabase.getAssessments()) {
                if (a.getEnrollment() == e) {
                    System.out.println("    " + a.getAssessmentType() + " on " + a.getAssessmentDate()
                            + ": " + a.getObtainedScore() + "/" + a.getMaximumScore());
                    totalObtained += a.getObtainedScore();
                    totalMaximum += a.getMaximumScore();
                    count++;
                }
            }
            if (count == 0) {
                System.out.println("    No assessments recorded yet.");
            }
        }
        if (totalMaximum > 0) {
            double percentage = totalObtained * 100.0 / totalMaximum;
            System.out.printf("%n  Overall score: %.2f/%.2f (%.2f%%)%n",
                    totalObtained, totalMaximum, percentage);
        } else {
            System.out.println("\n  Overall score: no assessments recorded.");
        }

        BigDecimal paid = BigDecimal.ZERO;
        for (FeePayment p : SchoolDatabase.getFeePayments()) {
            if (p.getStudent() == student) {
                paid = paid.add(p.getAmount());
            }
        }
        System.out.println("  Total fees paid: " + paid.toPlainString() + " RWF");
    }

    private static void studentPaymentHistory() {
        Student student = chooseStudent();
        List<FeePayment> payments = new ArrayList<>();
        for (FeePayment p : SchoolDatabase.getFeePayments()) {
            if (p.getStudent() == student) {
                payments.add(p);
            }
        }
        System.out.println("\n================ PAYMENT HISTORY ================");
        System.out.println("  Student: " + student.getDisplayName()
                + " (" + student.getStudentNumber() + ")");
        if (payments.isEmpty()) {
            System.out.println("  No payments recorded for this student.");
            return;
        }
        payments.sort(Comparator.comparing(FeePayment::getPaymentDate));
        BigDecimal total = BigDecimal.ZERO;
        for (FeePayment p : payments) {
            System.out.println("  " + p.getPaymentDate() + " | " + p.getReceiptNumber()
                    + " | " + p.getAmount().toPlainString() + " RWF | " + p.getPaymentMethod());
            total = total.add(p.getAmount());
        }
        System.out.println("  --------------------------------------------------");
        System.out.println("  Payments: " + payments.size() + " | Total: " + total.toPlainString() + " RWF");
    }

    private static void viewAvailableBooks() {
        List<LibraryBook> available = new ArrayList<>();
        for (LibraryBook b : SchoolDatabase.getLibraryBooks()) {
            if (b.getAvailableCopies() > 0) {
                available.add(b);
            }
        }
        System.out.println("\n================ AVAILABLE BOOKS ================");
        if (available.isEmpty()) {
            System.out.println("  No book currently has an available copy.");
            return;
        }
        for (LibraryBook b : available) {
            System.out.println("  " + b.getRegistrationSummary());
        }
    }

    private static void demonstratePolymorphism() {
        System.out.println("\n============ POLYMORPHISM DEMONSTRATION ============");
        System.out.println("Person and AcademicMember are abstract and cannot be instantiated:");
        System.out.println("  Person -> AcademicMember -> Student / Teacher, and Person -> Administrator.");
        System.out.println("  A List<Person> holds every person subtype; overridden methods are called via Person.");
        List<Person> people = SchoolDatabase.getPersons();
        if (people.isEmpty()) {
            System.out.println("\n  No Student, Teacher or Administrator has been registered yet.");
        } else {
            for (Person p : people) {
                System.out.println("\n  Person reference -> " + p.getClass().getSimpleName());
                System.out.println("    getRoleDescription(): " + p.getRoleDescription());
                System.out.println("    getAcademicCategory(): " + p.getAcademicCategory());
                System.out.println("    getRegistrationSummary(): " + p.getRegistrationSummary());
                System.out.println("    isActive(): " + p.isActive());
                System.out.println("    printDetails()");
                p.printDetails();
            }
        }
        List<Identifiable> records = SchoolDatabase.getNonPersonRecords();
        if (records.isEmpty()) {
            System.out.println("\n  No non-person records exist for the Identifiable demonstration.");
        } else {
            System.out.println("\n  List<Identifiable> calls interface methods on unrelated types:");
            for (Identifiable record : records) {
                System.out.println("    " + record.getClass().getSimpleName()
                        + " -> getId=" + record.getId()
                        + ", getDisplayName=" + record.getDisplayName()
                        + ", isActive=" + record.isActive());
            }
        }
    }

    private static Department registerDepartment() {
        Form form = new Form("DEPARTMENT REGISTRATION");
        form.step("IDENTITY DETAILS")
                .ask("Department ID", "String | unique | 4-30 chars", raw -> {
                    String id = Registrable.requireText(raw, "Department ID", 4, 30);
                    if (SchoolDatabase.findDepartment(id) != null) {
                        throw new DuplicateRecordException("Department ID " + id + " already exists.");
                    }
                    return id;
                })
                .ask("Department name", "String | 2-60 chars", raw ->
                        Registrable.requireText(raw, "Department name", 2, 60))
                .ask("Department code", "String | unique | 2-10 chars", raw -> {
                    String code = Registrable.requireText(raw, "Department code", 2, 10);
                    for (Department d : SchoolDatabase.getDepartments()) {
                        if (d.getDepartmentCode().equalsIgnoreCase(code)) {
                            throw new DuplicateRecordException("Department code " + code
                                    + " already exists.");
                        }
                    }
                    return code;
                });
        form.step("CONTACT DETAILS")
                .ask("Office location", "String | 1-60 chars", raw ->
                        Registrable.requireText(raw, "Office location", 1, 60))
                .ask("Contact email", "Email", raw ->
                        Registrable.requireEmail(raw, "Contact email"));
        form.step("HEAD TEACHER")
                .select("Head teacher", "Teacher | optional | select", () -> chooseTeacher(true));

        Department[] created = new Department[1];
        boolean saved = runForm(form, values -> {
            Department department = new Department(str(values, 1), str(values, 2), str(values, 3),
                    str(values, 4), str(values, 5), (Teacher) values[6]);
            SchoolDatabase.addDepartment(department);
            created[0] = department;
            System.out.println("+ Department " + department.getDisplayName()
                    + " registered successfully with ID " + department.getId() + ".");
        });
        return saved ? created[0] : null;
    }

    private static Student registerStudent() {
        Form form = new Form("STUDENT REGISTRATION");
        form.step("PERSONAL DETAILS")
                .ask("Person ID", "String | unique | 4-30 chars", raw -> {
                    String id = Registrable.requireText(raw, "Person ID", 4, 30);
                    if (SchoolDatabase.personIdExists(id)) {
                        throw new DuplicateRecordException("Person ID " + id + " already exists.");
                    }
                    return id;
                })
                .ask("First name", "String | 2-60 chars", raw ->
                        Registrable.requireText(raw, "First name", 2, 60))
                .ask("Last name", "String | 2-60 chars", raw ->
                        Registrable.requireText(raw, "Last name", 2, 60))
                .ask("Email", "Email | unique", raw -> {
                    String email = Registrable.requireEmail(raw, "Email");
                    if (SchoolDatabase.personEmailExists(email)) {
                        throw new DuplicateRecordException("Email " + email + " already exists.");
                    }
                    return email;
                })
                .ask("Phone", "String | 10-15 digits", raw ->
                        Registrable.requirePhone(raw, "Phone number"))
                .ask("Date of birth", "LocalDate | YYYY-MM-DD", raw ->
                        Registrable.requireDateInPast(Registrable.parseDate(raw, "Date of birth"),
                                "Date of birth"));
        form.step("ACADEMIC DETAILS")
                .ask("Member number", "String | unique | 4-30 chars", raw -> {
                    String number = Registrable.requireText(raw, "Member number", 4, 30);
                    if (SchoolDatabase.memberNumberExists(number)) {
                        throw new DuplicateRecordException("Member number " + number
                                + " already exists.");
                    }
                    return number;
                })
                .select("Department", "Department | select", () -> chooseDepartment(false))
                .ask("Join date", "LocalDate | YYYY-MM-DD", raw ->
                        Registrable.requireDateNotFuture(Registrable.parseDate(raw, "Join date"),
                                "Join date"))
                .ask("Academic status", "1 ACTIVE / 2 INACTIVE", raw ->
                        parseOption(raw, "Academic status", "ACTIVE", "INACTIVE"))
                .ask("Institutional email", "Email | unique", raw -> {
                    String email = Registrable.requireEmail(raw, "Institutional email");
                    if (SchoolDatabase.institutionalEmailExists(email)) {
                        throw new DuplicateRecordException("Institutional email " + email
                                + " already exists.");
                    }
                    return email;
                })
                .ask("Advisor name", "String | 2-60 chars", raw ->
                        Registrable.requireText(raw, "Advisor name", 2, 60));
        form.step("STUDENT DETAILS")
                .ask("Student number", "String | unique | 4-30 chars", raw -> {
                    String number = Registrable.requireText(raw, "Student number", 4, 30);
                    for (Student s : SchoolDatabase.getStudents()) {
                        if (s.getStudentNumber().equalsIgnoreCase(number)) {
                            throw new DuplicateRecordException("Student number " + number
                                    + " already exists.");
                        }
                    }
                    return number;
                })
                .ask("Program name", "String | 2-120 chars", raw ->
                        Registrable.requireText(raw, "Program name", 2, 120))
                .ask("Admission date", "LocalDate | YYYY-MM-DD", raw ->
                        Registrable.requireDateNotFuture(Registrable.parseDate(raw, "Admission date"),
                                "Admission date"))
                .ask("Study year", "int | 1-6", raw ->
                        Registrable.parseInt(raw, "Study year", 1, 6))
                .ask("Guardian phone", "String | 10-15 digits", raw ->
                        Registrable.requirePhone(raw, "Guardian phone"))
                .ask("Scholarship", "1 YES / 2 NO", raw ->
                        parseYesNo(raw, "Scholarship status"));
        form.step("REVIEW & CONFIRM");

        Student[] created = new Student[1];
        boolean saved = runForm(form, values -> {
            Student student = new Student(
                    str(values, 1), str(values, 2), str(values, 3), str(values, 4),
                    str(values, 5), date(values, 6),
                    str(values, 7), (Department) values[8], date(values, 9),
                    str(values, 10), str(values, 11), str(values, 12),
                    str(values, 13), str(values, 14), date(values, 15),
                    intValue(values, 16), str(values, 17), boolValue(values, 18));
            SchoolDatabase.addStudent(student);
            created[0] = student;
            System.out.println("+ Student " + student.getStudentNumber() + " ("
                    + student.getDisplayName() + ") registered successfully.");
        });
        return saved ? created[0] : null;
    }

    private static Teacher registerTeacher() {
        Form form = new Form("TEACHER REGISTRATION");
        form.step("PERSONAL DETAILS")
                .ask("Person ID", "String | unique | 4-30 chars", raw -> {
                    String id = Registrable.requireText(raw, "Person ID", 4, 30);
                    if (SchoolDatabase.personIdExists(id)) {
                        throw new DuplicateRecordException("Person ID " + id + " already exists.");
                    }
                    return id;
                })
                .ask("First name", "String | 2-60 chars", raw ->
                        Registrable.requireText(raw, "First name", 2, 60))
                .ask("Last name", "String | 2-60 chars", raw ->
                        Registrable.requireText(raw, "Last name", 2, 60))
                .ask("Email", "Email | unique", raw -> {
                    String email = Registrable.requireEmail(raw, "Email");
                    if (SchoolDatabase.personEmailExists(email)) {
                        throw new DuplicateRecordException("Email " + email + " already exists.");
                    }
                    return email;
                })
                .ask("Phone", "String | 10-15 digits", raw ->
                        Registrable.requirePhone(raw, "Phone number"))
                .ask("Date of birth", "LocalDate | YYYY-MM-DD", raw ->
                        Registrable.requireDateInPast(Registrable.parseDate(raw, "Date of birth"),
                                "Date of birth"));
        form.step("ACADEMIC DETAILS")
                .ask("Member number", "String | unique | 4-30 chars", raw -> {
                    String number = Registrable.requireText(raw, "Member number", 4, 30);
                    if (SchoolDatabase.memberNumberExists(number)) {
                        throw new DuplicateRecordException("Member number " + number
                                + " already exists.");
                    }
                    return number;
                })
                .select("Department", "Department | select", () -> chooseDepartment(false))
                .ask("Join date", "LocalDate | YYYY-MM-DD", raw ->
                        Registrable.requireDateNotFuture(Registrable.parseDate(raw, "Join date"),
                                "Join date"))
                .ask("Academic status", "1 ACTIVE / 2 INACTIVE", raw ->
                        parseOption(raw, "Academic status", "ACTIVE", "INACTIVE"))
                .ask("Institutional email", "Email | unique", raw -> {
                    String email = Registrable.requireEmail(raw, "Institutional email");
                    if (SchoolDatabase.institutionalEmailExists(email)) {
                        throw new DuplicateRecordException("Institutional email " + email
                                + " already exists.");
                    }
                    return email;
                })
                .ask("Advisor name", "String | 2-60 chars", raw ->
                        Registrable.requireText(raw, "Advisor name", 2, 60));
        form.step("EMPLOYMENT DETAILS")
                .ask("Employee number", "String | unique | 4-30 chars", raw -> {
                    String number = Registrable.requireText(raw, "Employee number", 4, 30);
                    for (Teacher t : SchoolDatabase.getTeachers()) {
                        if (t.getEmployeeNumber().equalsIgnoreCase(number)) {
                            throw new DuplicateRecordException("Employee number " + number
                                    + " already exists.");
                        }
                    }
                    return number;
                })
                .ask("Specialization", "String | 2-120 chars", raw ->
                        Registrable.requireText(raw, "Specialization", 2, 120))
                .ask("Hire date", "LocalDate | YYYY-MM-DD", raw ->
                        Registrable.requireDateNotFuture(Registrable.parseDate(raw, "Hire date"),
                                "Hire date"))
                .ask("Salary", "Decimal | > 0 RWF", raw ->
                        Registrable.parsePositiveMoney(raw, "Salary"))
                .ask("Office number", "String | 1-20 chars", raw ->
                        Registrable.requireText(raw, "Office number", 1, 20))
                .ask("Employment type", "1 FULL_TIME / 2 PART_TIME / 3 CONTRACT", raw ->
                        parseOption(raw, "Employment type", "FULL_TIME", "PART_TIME", "CONTRACT"));
        form.step("REVIEW & CONFIRM");

        Teacher[] created = new Teacher[1];
        boolean saved = runForm(form, values -> {
            Teacher teacher = new Teacher(
                    str(values, 1), str(values, 2), str(values, 3), str(values, 4),
                    str(values, 5), date(values, 6),
                    str(values, 7), (Department) values[8], date(values, 9),
                    str(values, 10), str(values, 11), str(values, 12),
                    str(values, 13), str(values, 14), date(values, 15),
                    decimal(values, 16), str(values, 17), str(values, 18));
            SchoolDatabase.addTeacher(teacher);
            created[0] = teacher;
            System.out.println("+ Teacher " + teacher.getEmployeeNumber() + " ("
                    + teacher.getDisplayName() + ") registered successfully.");
        });
        return saved ? created[0] : null;
    }

    private static Administrator registerAdministrator() {
        Form form = new Form("ADMINISTRATOR REGISTRATION");
        form.step("PERSONAL DETAILS")
                .ask("Person ID", "String | unique | 4-30 chars", raw -> {
                    String id = Registrable.requireText(raw, "Person ID", 4, 30);
                    if (SchoolDatabase.personIdExists(id)) {
                        throw new DuplicateRecordException("Person ID " + id + " already exists.");
                    }
                    return id;
                })
                .ask("First name", "String | 2-60 chars", raw ->
                        Registrable.requireText(raw, "First name", 2, 60))
                .ask("Last name", "String | 2-60 chars", raw ->
                        Registrable.requireText(raw, "Last name", 2, 60))
                .ask("Email", "Email | unique", raw -> {
                    String email = Registrable.requireEmail(raw, "Email");
                    if (SchoolDatabase.personEmailExists(email)) {
                        throw new DuplicateRecordException("Email " + email + " already exists.");
                    }
                    return email;
                })
                .ask("Phone", "String | 10-15 digits", raw ->
                        Registrable.requirePhone(raw, "Phone number"))
                .ask("Date of birth", "LocalDate | YYYY-MM-DD", raw ->
                        Registrable.requireDateInPast(Registrable.parseDate(raw, "Date of birth"),
                                "Date of birth"));
        form.step("ADMINISTRATOR DETAILS")
                .ask("Staff number", "String | unique | 4-30 chars", raw -> {
                    String number = Registrable.requireText(raw, "Staff number", 4, 30);
                    for (Administrator a : SchoolDatabase.getAdministrators()) {
                        if (a.getStaffNumber().equalsIgnoreCase(number)) {
                            throw new DuplicateRecordException("Staff number " + number
                                    + " already exists.");
                        }
                    }
                    return number;
                })
                .ask("Job title", "String | 2-60 chars", raw ->
                        Registrable.requireText(raw, "Job title", 2, 60))
                .ask("Assigned office", "String | 1-40 chars", raw ->
                        Registrable.requireText(raw, "Assigned office", 1, 40))
                .ask("Employment date", "LocalDate | YYYY-MM-DD", raw ->
                        Registrable.requireDateNotFuture(Registrable.parseDate(raw, "Employment date"),
                                "Employment date"))
                .ask("Access level", "int | 1-5", raw ->
                        Registrable.parseInt(raw, "Access level", 1, 5))
                .ask("Active", "1 YES / 2 NO", raw -> parseYesNo(raw, "Active status"));
        form.step("REVIEW & CONFIRM");

        Administrator[] created = new Administrator[1];
        boolean saved = runForm(form, values -> {
            Administrator administrator = new Administrator(
                    str(values, 1), str(values, 2), str(values, 3), str(values, 4),
                    str(values, 5), date(values, 6),
                    str(values, 7), str(values, 8), str(values, 9), date(values, 10),
                    intValue(values, 11), boolValue(values, 12));
            SchoolDatabase.addAdministrator(administrator);
            created[0] = administrator;
            System.out.println("+ Administrator " + administrator.getStaffNumber() + " ("
                    + administrator.getDisplayName() + ") registered successfully.");
        });
        return saved ? created[0] : null;
    }

    private static Course registerCourse() {
        Form form = new Form("COURSE REGISTRATION");
        form.step("COURSE DETAILS")
                .ask("Course ID", "String | unique | 4-30 chars", raw -> {
                    String id = Registrable.requireText(raw, "Course ID", 4, 30);
                    if (SchoolDatabase.findCourse(id) != null) {
                        throw new DuplicateRecordException("Course ID " + id + " already exists.");
                    }
                    return id;
                })
                .ask("Course code", "String | unique | 2-12 chars", raw -> {
                    String code = Registrable.requireText(raw, "Course code", 2, 12);
                    for (Course c : SchoolDatabase.getCourses()) {
                        if (c.getCourseCode().equalsIgnoreCase(code)) {
                            throw new DuplicateRecordException("Course code " + code
                                    + " already exists.");
                        }
                    }
                    return code;
                })
                .ask("Course title", "String | 2-120 chars", raw ->
                        Registrable.requireText(raw, "Course title", 2, 120))
                .ask("Credit hours", "int | 1-6", raw ->
                        Registrable.parseInt(raw, "Credit hours", 1, 6));
        form.step("RELATIONS")
                .select("Department", "Department | select", () -> chooseDepartment(false))
                .select("Assigned teacher", "Teacher | select", () -> chooseTeacher(false));
        form.step("REVIEW & CONFIRM");

        Course[] created = new Course[1];
        boolean saved = runForm(form, values -> {
            Course course = new Course(str(values, 1), str(values, 2), str(values, 3),
                    intValue(values, 4), (Department) values[5], (Teacher) values[6]);
            SchoolDatabase.addCourse(course);
            created[0] = course;
            System.out.println("+ Course " + course.getCourseCode() + " ("
                    + course.getCourseTitle() + ") registered successfully.");
        });
        return saved ? created[0] : null;
    }

    private static Classroom registerClassroom() {
        Form form = new Form("CLASSROOM REGISTRATION");
        form.step("IDENTITY DETAILS")
                .ask("Classroom ID", "String | unique | 4-30 chars", raw -> {
                    String id = Registrable.requireText(raw, "Classroom ID", 4, 30);
                    for (Classroom c : SchoolDatabase.getClassrooms()) {
                        if (c.getId().equalsIgnoreCase(id)) {
                            throw new DuplicateRecordException("Classroom ID " + id
                                    + " already exists.");
                        }
                    }
                    return id;
                })
                .ask("Building name", "String | 2-60 chars", raw ->
                        Registrable.requireText(raw, "Building name", 2, 60))
                .ask("Room number", "String | 1-10 chars", raw ->
                        Registrable.requireText(raw, "Room number", 1, 10));
        form.step("LOGISTICS")
                .ask("Capacity", "int | > 0", raw ->
                        Registrable.parseInt(raw, "Capacity", 1, 100000))
                .ask("Floor number", "int | >= 0", raw ->
                        Registrable.parseInt(raw, "Floor number", 0, 200))
                .ask("Available", "1 YES / 2 NO", raw -> parseYesNo(raw, "Availability"));
        form.step("REVIEW & CONFIRM");

        Classroom[] created = new Classroom[1];
        boolean saved = runForm(form, values -> {
            Classroom classroom = new Classroom(str(values, 1), str(values, 2), str(values, 3),
                    intValue(values, 4), intValue(values, 5), boolValue(values, 6));
            SchoolDatabase.addClassroom(classroom);
            created[0] = classroom;
            System.out.println("+ Classroom " + classroom.getId() + " in "
                    + classroom.getDisplayName() + " registered successfully.");
        });
        return saved ? created[0] : null;
    }

    private static Enrollment registerEnrollment() {
        Form form = new Form("ENROLLMENT REGISTRATION");
        form.step("IDENTITY")
                .ask("Enrollment ID", "String | unique | 4-30 chars", raw -> {
                    String id = Registrable.requireText(raw, "Enrollment ID", 4, 30);
                    if (SchoolDatabase.findEnrollment(id) != null) {
                        throw new DuplicateRecordException("Enrollment ID " + id
                                + " already exists.");
                    }
                    return id;
                });
        form.step("PARTICIPANTS")
                .select("Student", "Student | select", Main::chooseStudent)
                .select("Course", "Course | select", Main::chooseCourse);
        form.step("ENROLLMENT DETAILS")
                .ask("Enrollment date", "LocalDate | YYYY-MM-DD", raw ->
                        Registrable.requireDateNotFuture(Registrable.parseDate(raw, "Enrollment date"),
                                "Enrollment date"))
                .ask("Semester", "1 Semester 1 / 2 Semester 2", raw ->
                        parseOption(raw, "Semester", "Semester 1", "Semester 2"))
                .ask("Enrollment status", "1 ACTIVE / 2 COMPLETED / 3 DROPPED", raw ->
                        parseOption(raw, "Enrollment status", "ACTIVE", "COMPLETED", "DROPPED"));
        form.step("REVIEW & CONFIRM");

        Enrollment[] created = new Enrollment[1];
        boolean saved = runForm(form, values -> {
            Enrollment enrollment = new Enrollment(str(values, 1), (Student) values[2],
                    (Course) values[3], date(values, 4), str(values, 5), str(values, 6));
            SchoolDatabase.addEnrollment(enrollment);
            created[0] = enrollment;
            System.out.println("+ Student " + enrollment.getStudent().getDisplayName()
                    + " enrolled in " + enrollment.getCourse().getCourseCode()
                    + " for " + enrollment.getSemester() + ".");
        });
        return saved ? created[0] : null;
    }

    private static Assessment recordAssessment() {
        final double[] maximumHolder = {0};
        Form form = new Form("ASSESSMENT REGISTRATION");
        form.step("ASSESSMENT DETAILS")
                .ask("Assessment ID", "String | unique | 4-30 chars", raw -> {
                    String id = Registrable.requireText(raw, "Assessment ID", 4, 30);
                    for (Assessment a : SchoolDatabase.getAssessments()) {
                        if (a.getId().equalsIgnoreCase(id)) {
                            throw new DuplicateRecordException("Assessment ID " + id
                                    + " already exists.");
                        }
                    }
                    return id;
                })
                .select("Enrollment", "Enrollment | active | select", () -> chooseEnrollment(true))
                .ask("Assessment type", "1 CAT / 2 QUIZ / 3 ASSIGNMENT / 4 EXAM / 5 PRACTICAL", raw ->
                        parseOption(raw, "Assessment type",
                                "CAT", "QUIZ", "ASSIGNMENT", "EXAM", "PRACTICAL"));
        form.step("SCORES")
                .ask("Maximum score", "Decimal | > 0", raw -> {
                    double value = Registrable.parseDouble(raw, "Maximum score", 0.01, 1000);
                    maximumHolder[0] = value;
                    return value;
                })
                .ask("Obtained score", "Decimal | 0 to maximum", raw ->
                        Registrable.parseDouble(raw, "Obtained score", 0, maximumHolder[0]));
        form.step("ASSESSMENT DATE")
                .ask("Assessment date", "LocalDate | YYYY-MM-DD", raw ->
                        Registrable.parseDate(raw, "Assessment date"));
        form.step("REVIEW & CONFIRM");

        Assessment[] created = new Assessment[1];
        boolean saved = runForm(form, values -> {
            Assessment assessment = new Assessment(str(values, 1), (Enrollment) values[2],
                    str(values, 3), doubleValue(values, 4), doubleValue(values, 5),
                    date(values, 6));
            SchoolDatabase.addAssessment(assessment);
            created[0] = assessment;
            System.out.println("+ Assessment " + assessment.getId() + " ("
                    + assessment.getAssessmentType() + ") recorded successfully.");
        });
        return saved ? created[0] : null;
    }

    private static FeePayment recordFeePayment() {
        Form form = new Form("FEE PAYMENT REGISTRATION");
        form.step("PAYMENT IDENTIFICATION")
                .ask("Payment ID", "String | unique | 4-30 chars", raw -> {
                    String id = Registrable.requireText(raw, "Payment ID", 4, 30);
                    for (FeePayment p : SchoolDatabase.getFeePayments()) {
                        if (p.getId().equalsIgnoreCase(id)) {
                            throw new DuplicateRecordException("Payment ID " + id
                                    + " already exists.");
                        }
                    }
                    return id;
                })
                .select("Student", "Student | select", Main::chooseStudent);
        form.step("PAYMENT DETAILS")
                .ask("Amount", "Decimal | > 0 RWF", raw ->
                        Registrable.parsePositiveMoney(raw, "Payment amount"))
                .ask("Payment date", "LocalDate | YYYY-MM-DD", raw ->
                        Registrable.parseDate(raw, "Payment date"))
                .ask("Payment method", "1 CASH / 2 MOBILE_MONEY / 3 BANK_TRANSFER / 4 CHEQUE", raw ->
                        parseOption(raw, "Payment method",
                                "CASH", "MOBILE_MONEY", "BANK_TRANSFER", "CHEQUE"))
                .ask("Receipt number", "String | unique | 3-30 chars", raw -> {
                    String receipt = Registrable.requireText(raw, "Receipt number", 3, 30);
                    for (FeePayment p : SchoolDatabase.getFeePayments()) {
                        if (p.getReceiptNumber().equalsIgnoreCase(receipt)) {
                            throw new DuplicateRecordException("Receipt number " + receipt
                                    + " already exists.");
                        }
                    }
                    return receipt;
                });
        form.step("REVIEW & CONFIRM");

        FeePayment[] created = new FeePayment[1];
        boolean saved = runForm(form, values -> {
            FeePayment payment = new FeePayment(str(values, 1), (Student) values[2],
                    decimal(values, 3), date(values, 4), str(values, 5), str(values, 6));
            SchoolDatabase.addFeePayment(payment);
            created[0] = payment;
            System.out.println("+ Payment " + payment.getReceiptNumber() + " of "
                    + payment.getAmount().toPlainString() + " RWF recorded for "
                    + payment.getStudent().getDisplayName() + ".");
        });
        return saved ? created[0] : null;
    }

    private static LibraryBook registerLibraryBook() {
        final int[] totalHolder = {0};
        Form form = new Form("LIBRARY BOOK REGISTRATION");
        form.step("BOOK IDENTIFICATION")
                .ask("Book ID", "String | unique | 4-30 chars", raw -> {
                    String id = Registrable.requireText(raw, "Book ID", 4, 30);
                    if (SchoolDatabase.findLibraryBook(id) != null) {
                        throw new DuplicateRecordException("Book ID " + id + " already exists.");
                    }
                    return id;
                })
                .ask("ISBN", "String | unique | 10-17 characters", raw -> {
                    String isbn = Registrable.requireText(raw, "ISBN", 10, 17);
                    if (!isbn.matches("^[0-9Xx-]+$")) {
                        throw new ValidationException(
                                "ISBN may contain only digits, hyphens and the letter X.");
                    }
                    for (LibraryBook b : SchoolDatabase.getLibraryBooks()) {
                        if (b.getIsbn().equalsIgnoreCase(isbn)) {
                            throw new DuplicateRecordException("ISBN " + isbn + " already exists.");
                        }
                    }
                    return isbn;
                })
                .ask("Title", "String | 1-120 chars", raw ->
                        Registrable.requireText(raw, "Book title", 1, 120))
                .ask("Author", "String | 2-60 chars", raw ->
                        Registrable.requireText(raw, "Book author", 2, 60));
        form.step("STOCK")
                .ask("Total copies", "int | >= 0", raw -> {
                    int value = Registrable.parseInt(raw, "Total copies", 0, 100000);
                    totalHolder[0] = value;
                    return value;
                })
                .ask("Available copies", "int | 0 to total copies", raw ->
                        Registrable.parseInt(raw, "Available copies", 0, totalHolder[0]));
        form.step("REVIEW & CONFIRM");

        LibraryBook[] created = new LibraryBook[1];
        boolean saved = runForm(form, values -> {
            LibraryBook book = new LibraryBook(str(values, 1), str(values, 2), str(values, 3),
                    str(values, 4), intValue(values, 5), intValue(values, 6));
            SchoolDatabase.addLibraryBook(book);
            created[0] = book;
            System.out.println("+ Book " + book.getTitle() + " (ID " + book.getId()
                    + ") registered successfully.");
        });
        return saved ? created[0] : null;
    }

    private static BookLoan borrowBook() {
        final LocalDate[] borrowedHolder = {null};
        Form form = new Form("BOOK LOAN REGISTRATION");
        form.step("LOAN IDENTIFICATION")
                .ask("Loan ID", "String | unique | 4-30 chars", raw -> {
                    String id = Registrable.requireText(raw, "Loan ID", 4, 30);
                    if (SchoolDatabase.findBookLoan(id) != null) {
                        throw new DuplicateRecordException("Loan ID " + id + " already exists.");
                    }
                    return id;
                })
                .select("Student", "Student | select", Main::chooseStudent);
        form.step("BOOK")
                .select("Book", "LibraryBook | available only | select",
                        () -> chooseBook(true));
        form.step("LOAN DATES")
                .ask("Borrowed date", "LocalDate | YYYY-MM-DD", raw -> {
                    LocalDate value = Registrable.requireDateNotFuture(
                            Registrable.parseDate(raw, "Borrowed date"), "Borrowed date");
                    borrowedHolder[0] = value;
                    return value;
                })
                .ask("Due date", "LocalDate | after borrowed date", raw -> {
                    LocalDate value = Registrable.parseDate(raw, "Due date");
                    if (borrowedHolder[0] != null && !value.isAfter(borrowedHolder[0])) {
                        throw new ValidationException("Due date must be after the borrowed date.");
                    }
                    return value;
                });
        form.step("RETURN STATUS")
                .derived("Returned date", "Not returned yet");
        form.step("REVIEW & CONFIRM");

        BookLoan[] created = new BookLoan[1];
        boolean saved = runForm(form, values -> {
            BookLoan loan = new BookLoan(str(values, 1), (Student) values[2],
                    (LibraryBook) values[3], date(values, 4), date(values, 5), null);
            SchoolDatabase.addBookLoan(loan);
            created[0] = loan;
            System.out.println("+ Book '" + loan.getBook().getTitle() + "' borrowed by "
                    + loan.getStudent().getDisplayName() + ". Due on " + loan.getDueDate()
                    + ". Available copies left: " + loan.getBook().getAvailableCopies() + ".");
        });
        return saved ? created[0] : null;
    }

    private static void returnBook() throws Exception {
        List<BookLoan> active = new ArrayList<>();
        for (BookLoan loan : SchoolDatabase.getBookLoans()) {
            if (loan.isActive()) {
                active.add(loan);
            }
        }
        System.out.println("\n================ RETURN LIBRARY BOOK ================");
        if (active.isEmpty()) {
            System.out.println("  No active book loan exists.");
            return;
        }
        for (int i = 0; i < active.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + active.get(i).getRegistrationSummary());
        }
        System.out.println("  0. Cancel");
        System.out.print("Select loan: ");
        int choice = readMenuChoice(0, active.size());
        if (choice == 0) {
            System.out.println("+ Return cancelled.");
            return;
        }
        BookLoan loan = active.get(choice - 1);
        System.out.print("Returned date [YYYY-MM-DD] (press ENTER for today): ");
        String raw = readLine().trim();
        LocalDate returnedDate = raw.isEmpty() ? LocalDate.now() : Registrable.parseDate(raw, "Returned date");
        System.out.println("Confirm return of '" + loan.getBook().getTitle() + "' by "
                + loan.getStudent().getDisplayName() + "?");
        System.out.println("1. Confirm   0. Cancel");
        System.out.print("Select: ");
        if (readMenuChoice(0, 1) == 0) {
            System.out.println("+ Return cancelled.");
            return;
        }
        loan.returnBook(returnedDate);
        System.out.println("+ Book returned on " + returnedDate + ". Available copies now: "
                + loan.getBook().getAvailableCopies() + ".");
    }

    private static Department chooseDepartment(boolean allowNone) {
        while (true) {
            List<Department> list = SchoolDatabase.getDepartments();
            if (list.isEmpty()) {
                if (allowNone) {
                    System.out.println("No Department is registered yet; this relation will remain unassigned.");
                    return null;
                }
                System.out.println("\nNo Department is registered.");
                System.out.println("1. Register Department now");
                System.out.println("0. Cancel");
                System.out.print("Select: ");
                if (readSelectorChoice(0, 1) == 0) {
                    throw new CancelForm();
                }
                registerDepartment();
                System.out.println("+ Returning to the interrupted registration...");
                continue;
            }
            System.out.println("\n+----------------- CHOOSE DEPARTMENT ------------------------+");
            for (int i = 0; i < list.size(); i++) {
                Department d = list.get(i);
                System.out.println("| " + (i + 1) + "  [" + d.getDepartmentId() + "] "
                        + d.getDepartmentName());
            }
            System.out.println("| " + (list.size() + 1) + "  [+] Register a new Department");
            System.out.println(allowNone ? "| 0  Skip (no head teacher)" : "| 0  Cancel");
            System.out.println("+------------------------------------------------------------+");
            System.out.print("Select option: ");
            int choice = readSelectorChoice(0, list.size() + 1);
            if (choice == 0) {
                if (allowNone) {
                    return null;
                }
                throw new CancelForm();
            }
            if (choice == list.size() + 1) {
                registerDepartment();
                System.out.println("+ Returning to the interrupted registration...");
                continue;
            }
            return list.get(choice - 1);
        }
    }

    private static Teacher chooseTeacher(boolean allowNone) {
        while (true) {
            List<Teacher> list = SchoolDatabase.getTeachers();
            if (list.isEmpty()) {
                if (allowNone) {
                    System.out.println("No Teacher is registered yet; this relation will remain unassigned.");
                    return null;
                }
                System.out.println("\nNo Teacher is registered.");
                System.out.println("1. Register Teacher now");
                System.out.println("0. Cancel");
                System.out.print("Select: ");
                if (readSelectorChoice(0, 1) == 0) {
                    throw new CancelForm();
                }
                registerTeacher();
                System.out.println("+ Returning to the interrupted registration...");
                continue;
            }
            System.out.println("\n+------------------ CHOOSE TEACHER --------------------------+");
            for (int i = 0; i < list.size(); i++) {
                Teacher t = list.get(i);
                System.out.println("| " + (i + 1) + "  [" + t.getEmployeeNumber() + "] "
                        + t.getDisplayName() + " - " + t.getDepartment().getDisplayName());
            }
            System.out.println("| " + (list.size() + 1) + "  [+] Register a new Teacher");
            System.out.println(allowNone ? "| 0  Skip (no head teacher)" : "| 0  Cancel");
            System.out.println("+------------------------------------------------------------+");
            System.out.print("Select option: ");
            int choice = readSelectorChoice(0, list.size() + 1);
            if (choice == 0) {
                if (allowNone) {
                    return null;
                }
                throw new CancelForm();
            }
            if (choice == list.size() + 1) {
                registerTeacher();
                System.out.println("+ Returning to the interrupted registration...");
                continue;
            }
            return list.get(choice - 1);
        }
    }

    private static Student chooseStudent() {
        while (true) {
            List<Student> list = SchoolDatabase.getStudents();
            if (list.isEmpty()) {
                System.out.println("\nNo Student is registered.");
                System.out.println("1. Register Student now");
                System.out.println("0. Cancel");
                System.out.print("Select: ");
                if (readSelectorChoice(0, 1) == 0) {
                    throw new CancelForm();
                }
                registerStudent();
                System.out.println("+ Returning to the interrupted registration...");
                continue;
            }
            System.out.println("\n+------------------ CHOOSE STUDENT --------------------------+");
            for (int i = 0; i < list.size(); i++) {
                Student s = list.get(i);
                System.out.println("| " + (i + 1) + "  [" + s.getStudentNumber() + "] "
                        + s.getDisplayName() + " - " + s.getProgramName());
            }
            System.out.println("| " + (list.size() + 1) + "  [+] Register a new Student");
            System.out.println("| 0  Cancel");
            System.out.println("+------------------------------------------------------------+");
            System.out.print("Select option: ");
            int choice = readSelectorChoice(0, list.size() + 1);
            if (choice == 0) {
                throw new CancelForm();
            }
            if (choice == list.size() + 1) {
                registerStudent();
                System.out.println("+ Returning to the interrupted registration...");
                continue;
            }
            return list.get(choice - 1);
        }
    }

    private static Course chooseCourse() {
        while (true) {
            List<Course> list = SchoolDatabase.getCourses();
            if (list.isEmpty()) {
                System.out.println("\nNo Course is registered.");
                System.out.println("1. Register Course now");
                System.out.println("0. Cancel");
                System.out.print("Select: ");
                if (readSelectorChoice(0, 1) == 0) {
                    throw new CancelForm();
                }
                registerCourse();
                System.out.println("+ Returning to the interrupted registration...");
                continue;
            }
            System.out.println("\n+------------------- CHOOSE COURSE --------------------------+");
            for (int i = 0; i < list.size(); i++) {
                Course c = list.get(i);
                System.out.println("| " + (i + 1) + "  [" + c.getCourseCode() + "] "
                        + c.getCourseTitle() + " - " + c.getDepartment().getDisplayName());
            }
            System.out.println("| " + (list.size() + 1) + "  [+] Register a new Course");
            System.out.println("| 0  Cancel");
            System.out.println("+------------------------------------------------------------+");
            System.out.print("Select option: ");
            int choice = readSelectorChoice(0, list.size() + 1);
            if (choice == 0) {
                throw new CancelForm();
            }
            if (choice == list.size() + 1) {
                registerCourse();
                System.out.println("+ Returning to the interrupted registration...");
                continue;
            }
            return list.get(choice - 1);
        }
    }

    private static Enrollment chooseEnrollment(boolean activeOnly) {
        while (true) {
            List<Enrollment> list = new ArrayList<>();
            for (Enrollment e : SchoolDatabase.getEnrollments()) {
                if (!activeOnly || e.isActive()) {
                    list.add(e);
                }
            }
            if (list.isEmpty()) {
                System.out.println(activeOnly
                        ? "\nNo active Enrollment is registered."
                        : "\nNo Enrollment is registered.");
                System.out.println("1. Register Enrollment now");
                System.out.println("0. Cancel");
                System.out.print("Select: ");
                if (readSelectorChoice(0, 1) == 0) {
                    throw new CancelForm();
                }
                registerEnrollment();
                System.out.println("+ Returning to the interrupted registration...");
                continue;
            }
            System.out.println("\n+----------------- CHOOSE ENROLLMENT ------------------------+");
            for (int i = 0; i < list.size(); i++) {
                Enrollment e = list.get(i);
                System.out.println("| " + (i + 1) + "  [" + e.getId() + "] "
                        + e.getStudent().getDisplayName() + " -> "
                        + e.getCourse().getCourseCode() + " (" + e.getSemester() + ")");
            }
            System.out.println("| " + (list.size() + 1) + "  [+] Register a new Enrollment");
            System.out.println("| 0  Cancel");
            System.out.println("+------------------------------------------------------------+");
            System.out.print("Select option: ");
            int choice = readSelectorChoice(0, list.size() + 1);
            if (choice == 0) {
                throw new CancelForm();
            }
            if (choice == list.size() + 1) {
                registerEnrollment();
                System.out.println("+ Returning to the interrupted registration...");
                continue;
            }
            return list.get(choice - 1);
        }
    }

    private static LibraryBook chooseBook(boolean availableOnly) {
        while (true) {
            List<LibraryBook> list = new ArrayList<>();
            for (LibraryBook b : SchoolDatabase.getLibraryBooks()) {
                if (!availableOnly || b.getAvailableCopies() > 0) {
                    list.add(b);
                }
            }
            if (list.isEmpty()) {
                System.out.println(availableOnly
                        ? "\nNo Library Book with an available copy exists."
                        : "\nNo Library Book is registered.");
                System.out.println("1. Register Library Book now");
                System.out.println("0. Cancel");
                System.out.print("Select: ");
                if (readSelectorChoice(0, 1) == 0) {
                    throw new CancelForm();
                }
                registerLibraryBook();
                System.out.println("+ Returning to the interrupted registration...");
                continue;
            }
            System.out.println("\n+----------------- CHOOSE LIBRARY BOOK ----------------------+");
            for (int i = 0; i < list.size(); i++) {
                LibraryBook b = list.get(i);
                System.out.println("| " + (i + 1) + "  [" + b.getId() + "] " + b.getTitle()
                        + " - " + b.getAuthor() + " | available " + b.getAvailableCopies()
                        + "/" + b.getTotalCopies());
            }
            System.out.println("| " + (list.size() + 1) + "  [+] Register a new Library Book");
            System.out.println("| 0  Cancel");
            System.out.println("+------------------------------------------------------------+");
            System.out.print("Select option: ");
            int choice = readSelectorChoice(0, list.size() + 1);
            if (choice == 0) {
                throw new CancelForm();
            }
            if (choice == list.size() + 1) {
                registerLibraryBook();
                System.out.println("+ Returning to the interrupted registration...");
                continue;
            }
            return list.get(choice - 1);
        }
    }

    private static boolean runForm(Form form, FormSaver saver) {
        try {
            int stepIndex = 0;
            while (stepIndex < form.steps.size()) {
                FormStep step = form.steps.get(stepIndex);
                printFormHeader(form, stepIndex + 1);
                System.out.println("[STEP " + (stepIndex + 1) + "/" + form.steps.size() + "] "
                        + step.title);
                if (!step.fields.isEmpty()) {
                    System.out.println("Type BACK to return, CANCEL to discard the unsaved record.");
                }
                boolean goBack = false;
                for (FormField field : step.fields) {
                    if (field.derived) {
                        System.out.printf("%02d  %-20s [%s] : %s%n", field.number, field.label,
                                field.format, renderValue(field.value));
                        continue;
                    }
                    Object result;
                    try {
                        result = promptField(field);
                    } catch (BackForm e) {
                        goBack = true;
                        break;
                    }
                    if (result == BACK) {
                        goBack = true;
                        break;
                    }
                    field.value = result;
                }
                if (goBack) {
                    if (stepIndex == 0) {
                        System.out.println("! You are already at the first step.");
                    } else {
                        stepIndex--;
                    }
                } else {
                    stepIndex++;
                }
            }
            return reviewForm(form, saver);
        } catch (CancelForm e) {
            System.out.println("\n+ Cancelled. No record was saved.");
            return false;
        }
    }

    private static Object promptField(FormField field) {
        while (true) {
            if (field.selector != null) {
                System.out.printf("%02d  %-20s [%s]%n", field.number, field.label, field.format);
                if (field.value != null) {
                    System.out.println("     current: " + renderValue(field.value));
                }
                return field.selector.get();
            }
            String current = field.value == null ? "" : " [current: " + renderValue(field.value) + "]";
            System.out.printf("%02d  %-20s [%s]%s : ", field.number, field.label, field.format, current);
            String line = readLine().trim();
            if (line.equalsIgnoreCase("BACK")) {
                return BACK;
            }
            if (line.equalsIgnoreCase("CANCEL")) {
                throw new CancelForm();
            }
            if (line.isEmpty() && field.value != null) {
                return field.value;
            }
            try {
                return field.parser.parse(line);
            } catch (CancelForm e) {
                throw e;
            } catch (BackForm e) {
                throw e;
            } catch (EndOfInput e) {
                throw e;
            } catch (Exception e) {
                System.out.println("! " + messageOf(e));
                System.out.println("  Please re-enter field " + String.format("%02d", field.number) + ".");
            }
        }
    }

    private static boolean reviewForm(Form form, FormSaver saver) {
        while (true) {
            System.out.println("\n+---------------- REVIEW " + form.title + " ----------------------+");
            for (FormStep step : form.steps) {
                if (step.fields.isEmpty()) {
                    continue;
                }
                System.out.println("| " + step.title);
                for (FormField field : step.fields) {
                    System.out.printf("| %02d %-20s : %s%n", field.number, field.label,
                            renderValue(field.value));
                }
            }
            System.out.println("+------------------------------------------------------------+");
            System.out.println("| [1] Save record   [2] Edit field   [0] Cancel");
            System.out.println("+------------------------------------------------------------+");
            System.out.print("Select option: ");
            int choice = readMenuChoice(0, 2);
            if (choice == 0) {
                System.out.println("\n+ Cancelled. No record was saved.");
                return false;
            }
            if (choice == 2) {
                editField(form);
                continue;
            }
            try {
                saver.save(collectValues(form));
                return true;
            } catch (CancelForm e) {
                System.out.println("\n+ Cancelled. No record was saved.");
                return false;
            } catch (Exception e) {
                System.out.println("\n! " + messageOf(e));
                System.out.println("  Nothing was saved. Edit the record or cancel.");
            }
        }
    }

    private static void editField(Form form) {
        System.out.print("Enter field number to edit (01-" + String.format("%02d", form.fieldCount())
                + ") or 0 to return: ");
        int number = readMenuChoice(0, form.fieldCount());
        if (number == 0) {
            return;
        }
        FormField field = form.findField(number);
        if (field == null) {
            System.out.println("! No such field.");
            return;
        }
        if (field.derived) {
            System.out.println("! Field " + String.format("%02d", number)
                    + " is derived and cannot be edited.");
            return;
        }
        System.out.println("Current value: " + renderValue(field.value));
        try {
            Object value;
            if (field.selector != null) {
                value = field.selector.get();
            } else {
                System.out.print("New value for " + field.label + " [" + field.format + "]: ");
                String line = readLine().trim();
                if (line.isEmpty() && field.value != null) {
                    return;
                }
                value = field.parser.parse(line);
            }
            field.value = value;
            System.out.println("+ Field updated. Returning to review.");
        } catch (CancelForm | BackForm e) {
            System.out.println("+ Edit cancelled.");
        } catch (EndOfInput e) {
            throw e;
        } catch (Exception e) {
            System.out.println("! " + messageOf(e));
        }
    }

    private static void printFormHeader(Form form, int stepNumber) {
        System.out.println("\n+------------------------------------------------------------+");
        System.out.println("| AUCA_ID  /  SCHOOL MANAGEMENT                              |");
        System.out.printf("| %-46s Step %d of %d |%n", form.title, stepNumber, form.steps.size());
        System.out.println("+------------------------------------------------------------+");
        printStepTabs(form);
        System.out.println("+------------------------------------------------------------+");
    }

    private static void printStepTabs(Form form) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < form.steps.size(); i++) {
            String tab = (line.isEmpty() ? "" : " ") + "[" + (i + 1) + "] "
                    + form.steps.get(i).title;
            if (line.length() + tab.length() > 58) {
                System.out.printf("| %-58s |%n", line);
                line.setLength(0);
                tab = tab.substring(1);
            }
            line.append(tab);
        }
        if (!line.isEmpty()) {
            System.out.printf("| %-58s |%n", line);
        }
    }

    private static Object[] collectValues(Form form) {
        Object[] values = new Object[form.fieldCount() + 1];
        for (FormStep step : form.steps) {
            for (FormField field : step.fields) {
                values[field.number] = field.value;
            }
        }
        return values;
    }

    private static String renderValue(Object value) {
        if (value == null) {
            return "Not assigned";
        }
        if (value instanceof Identifiable identifiable) {
            return identifiable.getDisplayName();
        }
        if (value instanceof Boolean flag) {
            return flag ? "YES" : "NO";
        }
        if (value instanceof BigDecimal amount) {
            return amount.toPlainString() + " RWF";
        }
        return value.toString();
    }

    private static String str(Object[] values, int index) {
        return (String) values[index];
    }

    private static LocalDate date(Object[] values, int index) {
        return (LocalDate) values[index];
    }

    private static int intValue(Object[] values, int index) {
        return (Integer) values[index];
    }

    private static double doubleValue(Object[] values, int index) {
        return (Double) values[index];
    }

    private static BigDecimal decimal(Object[] values, int index) {
        return (BigDecimal) values[index];
    }

    private static boolean boolValue(Object[] values, int index) {
        return (Boolean) values[index];
    }

    private static String parseOption(String raw, String label, String... allowed)
            throws ValidationException {
        String value = raw.trim();
        if (value.matches("\\d{1,2}")) {
            int number = Integer.parseInt(value);
            if (number >= 1 && number <= allowed.length) {
                return allowed[number - 1];
            }
        }
        return Registrable.requireChoice(value, label, allowed);
    }

    private static boolean parseYesNo(String raw, String label) throws ValidationException {
        return "YES".equals(parseOption(raw, label, "YES", "NO"));
    }

    private static String readLine() {
        if (!SC.hasNextLine()) {
            throw new EndOfInput();
        }
        return SC.nextLine();
    }

    private static int readMenuChoice(int min, int max) {
        while (true) {
            String line = readLine().trim();
            int value;
            try {
                value = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("! Enter a whole number from " + min + " to " + max + ": ");
                continue;
            }
            if (value < min || value > max) {
                System.out.print("! Selection out of range. Enter " + min + " to " + max + ": ");
                continue;
            }
            return value;
        }
    }

    private static int readSelectorChoice(int min, int max) {
        while (true) {
            String line = readLine().trim();
            if (line.equalsIgnoreCase("CANCEL")) {
                throw new CancelForm();
            }
            if (line.equalsIgnoreCase("BACK")) {
                throw new BackForm();
            }
            int value;
            try {
                value = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("! Enter a whole number from " + min + " to " + max
                        + ", BACK or CANCEL: ");
                continue;
            }
            if (value < min || value > max) {
                System.out.print("! Selection out of range. Enter " + min + " to " + max
                        + ", BACK or CANCEL: ");
                continue;
            }
            return value;
        }
    }

    private static String messageOf(Exception e) {
        String message = e.getMessage();
        return message == null || message.isBlank() ? e.getClass().getSimpleName() : message;
    }

    private interface InputParser {
        Object parse(String raw) throws Exception;
    }

    private interface FormSaver {
        void save(Object[] values) throws Exception;
    }

    private static final class FormField {
        private final int number;
        private final String label;
        private final String format;
        private final InputParser parser;
        private final Supplier<Object> selector;
        private final boolean derived;
        private Object value;

        private FormField(int number, String label, String format, InputParser parser,
                          Supplier<Object> selector, boolean derived, Object value) {
            this.number = number;
            this.label = label;
            this.format = format;
            this.parser = parser;
            this.selector = selector;
            this.derived = derived;
            this.value = value;
        }
    }

    private static final class FormStep {
        private final String title;
        private final List<FormField> fields = new ArrayList<>();

        private FormStep(String title) {
            this.title = title;
        }
    }

    private static final class Form {
        private final String title;
        private final List<FormStep> steps = new ArrayList<>();
        private FormStep currentStep;
        private int nextNumber = 1;

        private Form(String title) {
            this.title = title;
        }

        private Form step(String title) {
            currentStep = new FormStep(title);
            steps.add(currentStep);
            return this;
        }

        private Form ask(String label, String format, InputParser parser) {
            currentStep.fields.add(new FormField(nextNumber++, label, format, parser, null, false, null));
            return this;
        }

        private Form select(String label, String format, Supplier<Object> selector) {
            currentStep.fields.add(new FormField(nextNumber++, label, format, null, selector, false, null));
            return this;
        }

        private Form derived(String label, Object value) {
            currentStep.fields.add(new FormField(nextNumber++, label, "derived", null, null, true, value));
            return this;
        }

        private int fieldCount() {
            return nextNumber - 1;
        }

        private FormField findField(int number) {
            for (FormStep step : steps) {
                for (FormField field : step.fields) {
                    if (field.number == number) {
                        return field;
                    }
                }
            }
            return null;
        }
    }

    private static final class EndOfInput extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    private static final class BackForm extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    private static final class CancelForm extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }
}
