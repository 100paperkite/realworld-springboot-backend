package com.realworld.backend.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public enum RealWorldError {
    DUPLICATE_USER("duplicate user registration", CONFLICT),
    AUTHENTICATION_FAILED("unregistered user", UNAUTHORIZED);

    private final String message;
    private final HttpStatus status;

    RealWorldError(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String message() {
        return message;
    }

    public HttpStatus status() {
        return status;
    }
}
