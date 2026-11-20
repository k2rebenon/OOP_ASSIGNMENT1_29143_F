package com.auca.exceptions;

public class DuplicateRecordException extends ValidationException {
    private static final long serialVersionUID = 1L;

    public DuplicateRecordException(String message) {
        super(message);
    }
}
