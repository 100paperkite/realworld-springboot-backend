package com.realworld.backend.controller.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.realworld.backend.domain.User;
import lombok.Getter;

@Getter
@JsonTypeName("user")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class UserDto {
    private final String username;
    private final String email;
    private final String password;


    public UserDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public static UserDto of(User user) {
        return new UserDto(user.getName(), user.getEmail(), user.getPassword());
    }
}