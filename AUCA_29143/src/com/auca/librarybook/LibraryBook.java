package com.auca.librarybook;

import com.auca.exceptions.BookUnavailableException;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;

public class LibraryBook implements Identifiable, Registrable {
    private String bookId;
    private String isbn;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;

    public LibraryBook(String bookId, String isbn, String title, String author,
                       int totalCopies, int availableCopies) throws ValidationException {
        this.bookId = Registrable.requireText(bookId, "Book ID",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        this.isbn = validateIsbn(isbn);
        this.title = Registrable.requireText(title, "Book title", 1, 120);
        this.author = Registrable.requireText(author, "Book author",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
        validateCopies(totalCopies, availableCopies);
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    private static String validateIsbn(String isbn) throws ValidationException {
        String value = Registrable.requireText(isbn, "ISBN", 10, 17);
        if (!value.matches("^[0-9Xx-]+$")) {
            throw new ValidationException("ISBN may contain only digits, hyphens and the letter X.");
        }
        return value;
    }

    private static void validateCopies(int totalCopies, int availableCopies) throws ValidationException {
        if (totalCopies < 0) {
            throw new ValidationException("Total copies must be zero or greater.");
        }
        if (availableCopies < 0) {
            throw new ValidationException("Available copies must be zero or greater.");
        }
        if (availableCopies > totalCopies) {
            throw new ValidationException("Available copies cannot exceed total copies.");
        }
    }

    @Override
    public void validate() throws ValidationException {
        Registrable.requireText(bookId, "Book ID",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        validateIsbn(isbn);
        Registrable.requireText(title, "Book title", 1, 120);
        Registrable.requireText(author, "Book author",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
        validateCopies(totalCopies, availableCopies);
    }

    @Override
    public String getId() {
        return bookId;
    }

    @Override
    public String getDisplayName() {
        return title + " (" + isbn + ")";
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public String getRoleDescription() {
        return "Library Book";
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + bookId + "] " + title + " | " + author + " | ISBN " + isbn
                + " | available " + availableCopies + "/" + totalCopies;
    }

    @Override
    public void printDetails() {
        System.out.println("  Book ID            : " + bookId);
        System.out.println("  ISBN               : " + isbn);
        System.out.println("  Title              : " + title);
        System.out.println("  Author             : " + author);
        System.out.println("  Total copies       : " + totalCopies);
        System.out.println("  Available copies   : " + availableCopies);
    }

    @Override
    public String toString() {
        return "LibraryBook{bookId='" + bookId + "', isbn='" + isbn + "', title='" + title
                + "', author='" + author + "', totalCopies=" + totalCopies
                + ", availableCopies=" + availableCopies + "}";
    }

    @Override
    public void borrowCopy() throws BookUnavailableException {
        if (availableCopies <= 0) {
            throw new BookUnavailableException("No available copy of '" + title + "' remains.");
        }
        availableCopies--;
    }

    @Override
    public void returnCopy() throws ValidationException {
        if (availableCopies >= totalCopies) {
            throw new ValidationException("All copies of '" + title + "' are already available.");
        }
        availableCopies++;
    }

    public String getBookId() {
        return bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws ValidationException {
        this.title = Registrable.requireText(title, "Book title", 1, 120);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) throws ValidationException {
        this.author = Registrable.requireText(author, "Book author",
                Registrable.MIN_NAME_LENGTH, Registrable.MAX_NAME_LENGTH);
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) throws ValidationException {
        validateCopies(totalCopies, availableCopies);
        this.totalCopies = totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }
}
