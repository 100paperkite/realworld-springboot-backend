package com.realworld.backend.controller.dto;

import com.realworld.backend.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDto implements UserRequestResponse {
    private String email;
    private String token;
    private String username;
    private String bio;
    private String image;

    public static UserDto valueOf(User user, String token) {
        UserDto response = new UserDto();

        response.email = user.getEmail();
        response.username = user.getName();
        response.bio = user.getBio();
        response.image = user.getImage();

        response.token = token;

        return response;
    }
}
