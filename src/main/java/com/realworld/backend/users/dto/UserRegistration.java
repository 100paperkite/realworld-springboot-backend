package com.realworld.backend.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistration implements UserDto {
    private String email;
    private String username;
    private String password;
}
