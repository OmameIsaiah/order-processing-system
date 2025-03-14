package com.order.processing.system.account.service.exceptions;

public class UnauthorizedException extends RuntimeException {
    private String message;

    public UnauthorizedException(String message) {
        super(message);
        this.message = message;
    }
}
