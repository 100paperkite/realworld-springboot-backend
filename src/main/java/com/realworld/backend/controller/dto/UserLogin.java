package com.realworld.backend.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin implements UserRequestResponse {
    private String email;
    private String password;

}
