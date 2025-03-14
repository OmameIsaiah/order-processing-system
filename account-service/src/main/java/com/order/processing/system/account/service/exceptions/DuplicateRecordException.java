package com.order.processing.system.account.service.exceptions;

public class DuplicateRecordException extends RuntimeException {
    private String message;

    public DuplicateRecordException(String message) {
        super(message);
        this.message = message;
    }
}
