package com.realworld.backend.exception;

import org.springframework.http.HttpStatus;

public class RealWorldException extends RuntimeException {
    private final RealWorldError error;

    public RealWorldException(RealWorldError error) {
        super(error.message());
        this.error = error;
    }

    public HttpStatus status() {
        return error.status();
    }
}
