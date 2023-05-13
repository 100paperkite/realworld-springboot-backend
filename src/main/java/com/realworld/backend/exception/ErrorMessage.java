package com.realworld.backend.exception;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;

import java.util.List;

@Getter
@JsonTypeName("errors")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class ErrorMessage {
    private final List<String> body;

    public ErrorMessage() {
        this.body = List.of();
    }

    public ErrorMessage(String message) {
        this.body = List.of(message);
    }
}
