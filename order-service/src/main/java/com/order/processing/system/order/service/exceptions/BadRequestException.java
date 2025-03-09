package com.order.processing.system.order.service.exceptions;

public class BadRequestException extends RuntimeException {
    private String message;

    public BadRequestException(String message) {
        super(message);
        this.message = message;
    }
}
