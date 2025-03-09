package com.order.processing.system.order.service.exceptions;

public class RecordNotFoundException extends RuntimeException {
    private String message;

    public RecordNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
