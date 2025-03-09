package com.order.processing.system.inventory.service.exceptions;

public class DuplicateRecordException extends RuntimeException {
    private String message;

    public DuplicateRecordException(String message) {
        super(message);
        this.message = message;
    }
}
