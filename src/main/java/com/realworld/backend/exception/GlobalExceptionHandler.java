package com.realworld.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RealWorldException.class)
    public ResponseEntity<ErrorMessage> handleRealWorldException(RealWorldException exception) {
        ErrorMessage message = new ErrorMessage(exception.getMessage());
        return new ResponseEntity<>(message, exception.status());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleAllException(Exception exception) {
        ErrorMessage message = new ErrorMessage("internal server error");
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
