package com.order.processing.system.account.service.exceptions;

public class AccessDeniedException extends RuntimeException {
    private String message;

    public AccessDeniedException(String message) {
        super(message);
        this.message = message;
    }
}
