package com.auca.feepayment;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.student.Student;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FeePayment implements Identifiable, Registrable {
    private String paymentId;
    private Student student;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String receiptNumber;

    public FeePayment(String paymentId, Student student, BigDecimal amount, LocalDate paymentDate,
                      String paymentMethod, String receiptNumber) throws ValidationException {
        this.paymentId = Registrable.requireText(paymentId, "Payment ID",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        Registrable.requireReference(student, "Student");
        this.student = student;
        this.amount = validateAmount(amount);
        if (paymentDate == null) {
            throw new ValidationException("Payment date is required.");
        }
        this.paymentDate = paymentDate;
        this.paymentMethod = Registrable.requireChoice(paymentMethod, "Payment method",
                "CASH", "MOBILE_MONEY", "BANK_TRANSFER", "CHEQUE");
        this.receiptNumber = Registrable.requireText(receiptNumber, "Receipt number", 3, 30);
    }

    private static BigDecimal validateAmount(BigDecimal amount) throws ValidationException {
        if (amount == null) {
            throw new ValidationException("Payment amount is required.");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Payment amount must be strictly positive.");
        }
        return amount;
    }

    @Override
    public void validate() throws ValidationException {
        Registrable.requireText(paymentId, "Payment ID",
                Identifiable.MIN_ID_LENGTH, Identifiable.MAX_ID_LENGTH);
        Registrable.requireReference(student, "Student");
        validateAmount(amount);
        if (paymentDate == null) {
            throw new ValidationException("Payment date is required.");
        }
        Registrable.requireChoice(paymentMethod, "Payment method",
                "CASH", "MOBILE_MONEY", "BANK_TRANSFER", "CHEQUE");
        Registrable.requireText(receiptNumber, "Receipt number", 3, 30);
    }

    @Override
    public String getId() {
        return paymentId;
    }

    @Override
    public String getDisplayName() {
        return "Payment " + receiptNumber + " - " + student.getDisplayName();
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public String getRoleDescription() {
        return "Fee Payment";
    }

    @Override
    public String getRegistrationSummary() {
        return "[" + paymentId + "] " + receiptNumber + " | " + student.getDisplayName()
                + " | " + amount.toPlainString() + " RWF | " + paymentMethod + " | " + paymentDate;
    }

    @Override
    public void printDetails() {
        System.out.println("  Payment ID         : " + paymentId);
        System.out.println("  Student            : " + student.getDisplayName()
                + " (" + student.getStudentNumber() + ")");
        System.out.println("  Amount             : " + amount.toPlainString() + " RWF");
        System.out.println("  Payment date       : " + paymentDate);
        System.out.println("  Payment method     : " + paymentMethod);
        System.out.println("  Receipt number     : " + receiptNumber);
    }

    @Override
    public String toString() {
        return "FeePayment{paymentId='" + paymentId + "', student='" + student.getDisplayName()
                + "', amount=" + amount.toPlainString() + ", paymentDate=" + paymentDate
                + ", paymentMethod='" + paymentMethod + "', receiptNumber='" + receiptNumber + "'}";
    }

    public String getPaymentId() {
        return paymentId;
    }

    public Student getStudent() {
        return student;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) throws ValidationException {
        this.paymentMethod = Registrable.requireChoice(paymentMethod, "Payment method",
                "CASH", "MOBILE_MONEY", "BANK_TRANSFER", "CHEQUE");
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }
}
