package com.realworld.backend.users.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin implements UserDto {
    private String email;
    private String password;

}
