package com.auca.main;

import com.auca.abstracts.Person;
import com.auca.administrator.Administrator;
import com.auca.assessment.Assessment;
import com.auca.bookloan.BookLoan;
import com.auca.classroom.Classroom;
import com.auca.course.Course;
import com.auca.department.Department;
import com.auca.enrollment.Enrollment;
import com.auca.exceptions.BookUnavailableException;
import com.auca.exceptions.DuplicateRecordException;
import com.auca.exceptions.PrerequisiteMissingException;
import com.auca.exceptions.ValidationException;
import com.auca.feepayment.FeePayment;
import com.auca.interfaces.Identifiable;
import com.auca.librarybook.LibraryBook;
import com.auca.student.Student;
import com.auca.teacher.Teacher;

import java.util.ArrayList;
import java.util.List;

public final class SchoolDatabase {
    private static final List<Department> departments = new ArrayList<>();
    private static final List<Student> students = new ArrayList<>();
    private static final List<Teacher> teachers = new ArrayList<>();
    private static final List<Administrator> administrators = new ArrayList<>();
    private static final List<Course> courses = new ArrayList<>();
    private static final List<Classroom> classrooms = new ArrayList<>();
    private static final List<Enrollment> enrollments = new ArrayList<>();
    private static final List<Assessment> assessments = new ArrayList<>();
    private static final List<FeePayment> feePayments = new ArrayList<>();
    private static final List<LibraryBook> libraryBooks = new ArrayList<>();
    private static final List<BookLoan> bookLoans = new ArrayList<>();

    private SchoolDatabase() {
    }

    public static List<Department> getDepartments() {
        return departments;
    }

    public static List<Student> getStudents() {
        return students;
    }

    public static List<Teacher> getTeachers() {
        return teachers;
    }

    public static List<Administrator> getAdministrators() {
        return administrators;
    }

    public static List<Course> getCourses() {
        return courses;
    }

    public static List<Classroom> getClassrooms() {
        return classrooms;
    }

    public static List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public static List<Assessment> getAssessments() {
        return assessments;
    }

    public static List<FeePayment> getFeePayments() {
        return feePayments;
    }

    public static List<LibraryBook> getLibraryBooks() {
        return libraryBooks;
    }

    public static List<BookLoan> getBookLoans() {
        return bookLoans;
    }

    public static List<Person> getPersons() {
        List<Person> persons = new ArrayList<>();
        persons.addAll(students);
        persons.addAll(teachers);
        persons.addAll(administrators);
        return persons;
    }

    public static List<Identifiable> getNonPersonRecords() {
        List<Identifiable> records = new ArrayList<>();
        records.addAll(departments);
        records.addAll(courses);
        records.addAll(classrooms);
        records.addAll(enrollments);
        records.addAll(assessments);
        records.addAll(feePayments);
        records.addAll(libraryBooks);
        records.addAll(bookLoans);
        return records;
    }

    public static int totalRecords() {
        return departments.size() + students.size() + teachers.size() + administrators.size()
                + courses.size() + classrooms.size() + enrollments.size() + assessments.size()
                + feePayments.size() + libraryBooks.size() + bookLoans.size();
    }

    public static Department findDepartment(String id) {
        for (Department d : departments) {
            if (d.getId().equalsIgnoreCase(id) || d.getDepartmentCode().equalsIgnoreCase(id)) {
                return d;
            }
        }
        return null;
    }

    public static Student findStudent(String id) {
        for (Student s : students) {
            if (s.getPersonId().equalsIgnoreCase(id) || s.getStudentNumber().equalsIgnoreCase(id)
                    || s.getMemberNumber().equalsIgnoreCase(id)) {
                return s;
            }
        }
        return null;
    }

    public static Teacher findTeacher(String id) {
        for (Teacher t : teachers) {
            if (t.getPersonId().equalsIgnoreCase(id) || t.getEmployeeNumber().equalsIgnoreCase(id)
                    || t.getMemberNumber().equalsIgnoreCase(id)) {
                return t;
            }
        }
        return null;
    }

    public static Administrator findAdministrator(String id) {
        for (Administrator a : administrators) {
            if (a.getPersonId().equalsIgnoreCase(id) || a.getStaffNumber().equalsIgnoreCase(id)) {
                return a;
            }
        }
        return null;
    }

    public static Course findCourse(String id) {
        for (Course c : courses) {
            if (c.getId().equalsIgnoreCase(id) || c.getCourseCode().equalsIgnoreCase(id)) {
                return c;
            }
        }
        return null;
    }

    public static Enrollment findEnrollment(String id) {
        for (Enrollment e : enrollments) {
            if (e.getId().equalsIgnoreCase(id)) {
                return e;
            }
        }
        return null;
    }

    public static LibraryBook findLibraryBook(String id) {
        for (LibraryBook b : libraryBooks) {
            if (b.getId().equalsIgnoreCase(id) || b.getIsbn().equalsIgnoreCase(id)) {
                return b;
            }
        }
        return null;
    }

    public static BookLoan findBookLoan(String id) {
        for (BookLoan l : bookLoans) {
            if (l.getId().equalsIgnoreCase(id)) {
                return l;
            }
        }
        return null;
    }

    public static boolean personIdExists(String personId) {
        for (Person p : getPersons()) {
            if (p.getPersonId().equalsIgnoreCase(personId)) {
                return true;
            }
        }
        return false;
    }

    public static boolean personEmailExists(String email) {
        for (Person p : getPersons()) {
            if (p.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    public static boolean memberNumberExists(String memberNumber) {
        for (Student s : students) {
            if (s.getMemberNumber().equalsIgnoreCase(memberNumber)) {
                return true;
            }
        }
        for (Teacher t : teachers) {
            if (t.getMemberNumber().equalsIgnoreCase(memberNumber)) {
                return true;
            }
        }
        return false;
    }

    public static boolean institutionalEmailExists(String email) {
        for (Student s : students) {
            if (s.getInstitutionalEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        for (Teacher t : teachers) {
            if (t.getInstitutionalEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    public static void addDepartment(Department department)
            throws ValidationException, DuplicateRecordException {
        department.validate();
        if (findDepartment(department.getId()) != null) {
            throw new DuplicateRecordException("Department ID " + department.getId() + " already exists.");
        }
        for (Department d : departments) {
            if (d.getDepartmentCode().equalsIgnoreCase(department.getDepartmentCode())) {
                throw new DuplicateRecordException("Department code " + department.getDepartmentCode()
                        + " already exists.");
            }
        }
        departments.add(department);
    }

    public static void addStudent(Student student)
            throws ValidationException, PrerequisiteMissingException, DuplicateRecordException {
        student.validate();
        if (personIdExists(student.getPersonId())) {
            throw new DuplicateRecordException("Person ID " + student.getPersonId() + " already exists.");
        }
        if (personEmailExists(student.getEmail())) {
            throw new DuplicateRecordException("Email " + student.getEmail() + " already exists.");
        }
        if (memberNumberExists(student.getMemberNumber())) {
            throw new DuplicateRecordException("Member number " + student.getMemberNumber()
                    + " already exists.");
        }
        if (institutionalEmailExists(student.getInstitutionalEmail())) {
            throw new DuplicateRecordException("Institutional email " + student.getInstitutionalEmail()
                    + " already exists.");
        }
        for (Student s : students) {
            if (s.getStudentNumber().equalsIgnoreCase(student.getStudentNumber())) {
                throw new DuplicateRecordException("Student number " + student.getStudentNumber()
                        + " already exists.");
            }
        }
        if (!departments.contains(student.getDepartment())) {
            throw new PrerequisiteMissingException("Department "
                    + student.getDepartment().getDisplayName() + " is not registered.");
        }
        students.add(student);
    }

    public static void addTeacher(Teacher teacher)
            throws ValidationException, PrerequisiteMissingException, DuplicateRecordException {
        teacher.validate();
        if (personIdExists(teacher.getPersonId())) {
            throw new DuplicateRecordException("Person ID " + teacher.getPersonId() + " already exists.");
        }
        if (personEmailExists(teacher.getEmail())) {
            throw new DuplicateRecordException("Email " + teacher.getEmail() + " already exists.");
        }
        if (memberNumberExists(teacher.getMemberNumber())) {
            throw new DuplicateRecordException("Member number " + teacher.getMemberNumber()
                    + " already exists.");
        }
        if (institutionalEmailExists(teacher.getInstitutionalEmail())) {
            throw new DuplicateRecordException("Institutional email " + teacher.getInstitutionalEmail()
                    + " already exists.");
        }
        for (Teacher t : teachers) {
            if (t.getEmployeeNumber().equalsIgnoreCase(teacher.getEmployeeNumber())) {
                throw new DuplicateRecordException("Employee number " + teacher.getEmployeeNumber()
                        + " already exists.");
            }
        }
        if (!departments.contains(teacher.getDepartment())) {
            throw new PrerequisiteMissingException("Department "
                    + teacher.getDepartment().getDisplayName() + " is not registered.");
        }
        teachers.add(teacher);
    }

    public static void addAdministrator(Administrator administrator)
            throws ValidationException, DuplicateRecordException {
        administrator.validate();
        if (personIdExists(administrator.getPersonId())) {
            throw new DuplicateRecordException("Person ID " + administrator.getPersonId()
                    + " already exists.");
        }
        if (personEmailExists(administrator.getEmail())) {
            throw new DuplicateRecordException("Email " + administrator.getEmail() + " already exists.");
        }
        for (Administrator a : administrators) {
            if (a.getStaffNumber().equalsIgnoreCase(administrator.getStaffNumber())) {
                throw new DuplicateRecordException("Staff number " + administrator.getStaffNumber()
                        + " already exists.");
            }
        }
        administrators.add(administrator);
    }

    public static void addCourse(Course course)
            throws ValidationException, PrerequisiteMissingException, DuplicateRecordException {
        course.validate();
        if (findCourse(course.getId()) != null) {
            throw new DuplicateRecordException("Course ID " + course.getId() + " already exists.");
        }
        for (Course c : courses) {
            if (c.getCourseCode().equalsIgnoreCase(course.getCourseCode())) {
                throw new DuplicateRecordException("Course code " + course.getCourseCode()
                        + " already exists.");
            }
        }
        if (!departments.contains(course.getDepartment())) {
            throw new PrerequisiteMissingException("The selected department is not registered.");
        }
        if (!teachers.contains(course.getAssignedTeacher())) {
            throw new PrerequisiteMissingException("The selected teacher is not registered.");
        }
        courses.add(course);
    }

    public static void addClassroom(Classroom classroom)
            throws ValidationException, DuplicateRecordException {
        classroom.validate();
        for (Classroom c : classrooms) {
            if (c.getId().equalsIgnoreCase(classroom.getId())) {
                throw new DuplicateRecordException("Classroom ID " + classroom.getId()
                        + " already exists.");
            }
            if (c.getBuildingName().equalsIgnoreCase(classroom.getBuildingName())
                    && c.getRoomNumber().equalsIgnoreCase(classroom.getRoomNumber())) {
                throw new DuplicateRecordException("Room " + classroom.getRoomNumber()
                        + " already exists in " + classroom.getBuildingName() + ".");
            }
        }
        classrooms.add(classroom);
    }

    public static void addEnrollment(Enrollment enrollment)
            throws ValidationException, PrerequisiteMissingException, DuplicateRecordException {
        enrollment.validate();
        if (findEnrollment(enrollment.getId()) != null) {
            throw new DuplicateRecordException("Enrollment ID " + enrollment.getId()
                    + " already exists.");
        }
        if (!students.contains(enrollment.getStudent())) {
            throw new PrerequisiteMissingException("The selected student is not registered.");
        }
        if (!courses.contains(enrollment.getCourse())) {
            throw new PrerequisiteMissingException("The selected course is not registered.");
        }
        for (Enrollment e : enrollments) {
            boolean duplicate = e.isActive()
                    && e.getStudent() == enrollment.getStudent()
                    && e.getCourse() == enrollment.getCourse()
                    && e.getSemester().equalsIgnoreCase(enrollment.getSemester());
            if (duplicate) {
                throw new DuplicateRecordException(enrollment.getStudent().getDisplayName()
                        + " already has an active enrollment in " + enrollment.getCourse().getCourseCode()
                        + " for " + enrollment.getSemester() + ".");
            }
        }
        enrollments.add(enrollment);
    }

    public static void addAssessment(Assessment assessment)
            throws ValidationException, PrerequisiteMissingException, DuplicateRecordException {
        assessment.validate();
        for (Assessment a : assessments) {
            if (a.getId().equalsIgnoreCase(assessment.getId())) {
                throw new DuplicateRecordException("Assessment ID " + assessment.getId()
                        + " already exists.");
            }
        }
        if (!enrollments.contains(assessment.getEnrollment())) {
            throw new PrerequisiteMissingException("The selected enrollment is not registered.");
        }
        if (!assessment.getEnrollment().isActive()) {
            throw new PrerequisiteMissingException("Assessments can only be recorded for active enrollments.");
        }
        assessments.add(assessment);
    }

    public static void addFeePayment(FeePayment payment)
            throws ValidationException, PrerequisiteMissingException, DuplicateRecordException {
        payment.validate();
        if (!students.contains(payment.getStudent())) {
            throw new PrerequisiteMissingException("The selected student is not registered.");
        }
        for (FeePayment p : feePayments) {
            if (p.getId().equalsIgnoreCase(payment.getId())) {
                throw new DuplicateRecordException("Payment ID " + payment.getId() + " already exists.");
            }
            if (p.getReceiptNumber().equalsIgnoreCase(payment.getReceiptNumber())) {
                throw new DuplicateRecordException("Receipt number " + payment.getReceiptNumber()
                        + " already exists.");
            }
        }
        feePayments.add(payment);
    }

    public static void addLibraryBook(LibraryBook book)
            throws ValidationException, DuplicateRecordException {
        book.validate();
        for (LibraryBook b : libraryBooks) {
            if (b.getId().equalsIgnoreCase(book.getId())) {
                throw new DuplicateRecordException("Book ID " + book.getId() + " already exists.");
            }
            if (b.getIsbn().equalsIgnoreCase(book.getIsbn())) {
                throw new DuplicateRecordException("ISBN " + book.getIsbn() + " already exists.");
            }
        }
        libraryBooks.add(book);
    }

    public static void addBookLoan(BookLoan loan)
            throws ValidationException, PrerequisiteMissingException, DuplicateRecordException,
            BookUnavailableException {
        loan.validate();
        if (findBookLoan(loan.getId()) != null) {
            throw new DuplicateRecordException("Loan ID " + loan.getId() + " already exists.");
        }
        if (!students.contains(loan.getStudent())) {
            throw new PrerequisiteMissingException("The selected student is not registered.");
        }
        if (!libraryBooks.contains(loan.getBook())) {
            throw new PrerequisiteMissingException("The selected book is not registered.");
        }
        if (loan.getBook().getAvailableCopies() <= 0) {
            throw new BookUnavailableException("No available copy of '"
                    + loan.getBook().getTitle() + "' remains.");
        }
        loan.getBook().borrowCopy();
        bookLoans.add(loan);
    }

    public static List<Object> search(String query) {
        List<Object> matches = new ArrayList<>();
        String q = query.trim();
        for (Department d : departments) {
            if (equalsAny(q, d.getId(), d.getDepartmentCode())) {
                matches.add(d);
            }
        }
        for (Student s : students) {
            if (equalsAny(q, s.getPersonId(), s.getStudentNumber(), s.getMemberNumber())) {
                matches.add(s);
            }
        }
        for (Teacher t : teachers) {
            if (equalsAny(q, t.getPersonId(), t.getEmployeeNumber(), t.getMemberNumber())) {
                matches.add(t);
            }
        }
        for (Administrator a : administrators) {
            if (equalsAny(q, a.getPersonId(), a.getStaffNumber())) {
                matches.add(a);
            }
        }
        for (Course c : courses) {
            if (equalsAny(q, c.getId(), c.getCourseCode())) {
                matches.add(c);
            }
        }
        for (Classroom c : classrooms) {
            if (equalsAny(q, c.getId())) {
                matches.add(c);
            }
        }
        for (Enrollment e : enrollments) {
            if (equalsAny(q, e.getId())) {
                matches.add(e);
            }
        }
        for (Assessment a : assessments) {
            if (equalsAny(q, a.getId())) {
                matches.add(a);
            }
        }
        for (FeePayment p : feePayments) {
            if (equalsAny(q, p.getId(), p.getReceiptNumber())) {
                matches.add(p);
            }
        }
        for (LibraryBook b : libraryBooks) {
            if (equalsAny(q, b.getId(), b.getIsbn())) {
                matches.add(b);
            }
        }
        for (BookLoan l : bookLoans) {
            if (equalsAny(q, l.getId())) {
                matches.add(l);
            }
        }
        return matches;
    }

    public static boolean equalsAny(String query, String... values) {
        for (String value : values) {
            if (value != null && value.equalsIgnoreCase(query)) {
                return true;
            }
        }
        return false;
    }
}
