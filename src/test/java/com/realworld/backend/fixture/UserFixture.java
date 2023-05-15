package com.realworld.backend.fixture;

import com.realworld.backend.controller.dto.UserLogin;
import com.realworld.backend.controller.dto.UserRegistration;
import com.realworld.backend.domain.User;

public final class UserFixture {
    public static User USER = User.builder()
            .id(1L)
            .name("test")
            .email("test@test.test")
            .password("testtesttest")
            .build();
    public static UserRegistration REGISTER = new UserRegistration(USER.getEmail(), USER.getName(), USER.getPassword());
    public static UserLogin LOGIN = new UserLogin(USER.getEmail(), USER.getPassword());

    private UserFixture() {
    }
}
