package com.realworld.backend.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

public enum RealWorldError {
    DUPLICATE_USER("duplicate user registration", CONFLICT);

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
