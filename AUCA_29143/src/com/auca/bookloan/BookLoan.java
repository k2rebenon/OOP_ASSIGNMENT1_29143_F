package com.auca.bookloan;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.librarybook.LibraryBook;
import com.auca.student.Student;

import java.time.LocalDate;

public class BookLoan implements Identifiable, Registrable {
    private String loanId;
    private Student student;
    private LibraryBook book;
    private LocalDate borrowedDate;
    private LocalDate dueDate;
    private LocalDate returnedDate;

    public BookLoan(String loanId, Student student, LibraryBook book, LocalDate borrowedDate,
                    LocalDate dueDate, LocalDate returnedDate) throws ValidationException {
        this.loanId = Registrable.requireText(loanId, "Loan ID",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        Registrable.requireReference(student, "Student");
        Registrable.requireReference(book, "Book");
        this.student = student;
        this.book = book;
        if (borrowedDate == null) {
            throw new ValidationException("Borrowed date is required.");
        }
        this.borrowedDate = borrowedDate;
        validateDueDate(dueDate, borrowedDate);
        this.dueDate = dueDate;
        validateReturnedDate(returnedDate, borrowedDate);
        this.returnedDate = returnedDate;
    }

    private static void validateDueDate(LocalDate dueDate, LocalDate borrowedDate)
            throws ValidationException {
        if (dueDate == null) {
            throw new ValidationException("Due date is required.");
        }
        if (!dueDate.isAfter(borrowedDate)) {
            throw new ValidationException("Due date must be after the borrowed date.");
        }
    }

    private static void validateReturnedDate(LocalDate returnedDate, LocalDate borrowedDate)
            throws ValidationException {
        if (returnedDate != null && returnedDate.isBefore(borrowedDate)) {
            throw new ValidationException("Returned date cannot be before the borrowed date.");
        }
    }

    @Override
    public void validate() throws ValidationException {
        Registrable.requireText(loanId, "Loan ID",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        Registrable.requireReference(student, "Student");
        Registrable.requireReference(book, "Book");
        if (borrowedDate == null) {
            throw new ValidationException("Borrowed date is required.");
        }
        validateDueDate(dueDate, borrowedDate);
        validateReturnedDate(returnedDate, borrowedDate);
    }

    @Override
    public String getId() {
        return loanId;
    }

    @Override
    public String getDisplayName() {
        return book.getTitle() + " borrowed by " + student.getDisplayName();
    }

    @Override
    public boolean isActive() {
        return returnedDate == null;
    }

    @Override
    public String getRoleDescription() {
        return "Book Loan";
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + loanId + "] " + book.getTitle() + " -> " + student.getDisplayName()
                + " | borrowed " + borrowedDate + " | due " + dueDate
                + " | " + (isActive() ? "ACTIVE" : "RETURNED " + returnedDate);
    }

    @Override
    public void printDetails() {
        System.out.println("  Loan ID            : " + loanId);
        System.out.println("  Student            : " + student.getDisplayName()
                + " (" + student.getStudentNumber() + ")");
        System.out.println("  Book               : " + book.getDisplayName());
        System.out.println("  Borrowed date      : " + borrowedDate);
        System.out.println("  Due date           : " + dueDate);
        System.out.println("  Returned date      : " + (returnedDate == null ? "Not returned yet" : returnedDate));
    }

    @Override
    public String toString() {
        return "BookLoan{loanId='" + loanId + "', student='" + student.getDisplayName()
                + "', book='" + book.getTitle() + "', borrowedDate=" + borrowedDate
                + ", dueDate=" + dueDate + ", returnedDate=" + returnedDate + "}";
    }

    @Override
    public void returnBook(LocalDate returnDate) throws ValidationException {
        if (returnedDate != null) {
            throw new ValidationException("This loan has already been returned on " + returnedDate + ".");
        }
        if (returnDate == null) {
            throw new ValidationException("Returned date is required.");
        }
        if (returnDate.isBefore(borrowedDate)) {
            throw new ValidationException("Returned date cannot be before the borrowed date.");
        }
        book.returnCopy();
        returnedDate = returnDate;
    }

    public String getLoanId() {
        return loanId;
    }

    public Student getStudent() {
        return student;
    }

    public LibraryBook getBook() {
        return book;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) throws ValidationException {
        validateDueDate(dueDate, borrowedDate);
        this.dueDate = dueDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }
}
