package com.realworld.backend.users.dto;

import com.realworld.backend.users.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfo implements UserDto {
    private String email;
    private String token;
    private String username;
    private String bio;
    private String image;

    public static UserInfo valueOf(User user, String token) {
        UserInfo response = new UserInfo();

        response.email = user.getEmail();
        response.username = user.getName();
        response.bio = user.getBio();
        response.image = user.getImage();

        response.token = token;

        return response;
    }
}
