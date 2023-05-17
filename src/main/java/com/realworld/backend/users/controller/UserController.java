package com.realworld.backend.users.controller;

import com.realworld.backend.security.TokenProvider;
import com.realworld.backend.users.dto.UserInfo;
import com.realworld.backend.users.dto.UserLogin;
import com.realworld.backend.users.dto.UserRegistration;
import com.realworld.backend.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final TokenProvider tokenProvider;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserInfo> register(@RequestBody UserRegistration registration) {
        com.realworld.backend.users.entity.User user = userService.register(registration);
        String accessToken = tokenProvider.createToken(user.getEmail());
        return ResponseEntity.ok(UserInfo.valueOf(user, accessToken));
    }

    @PostMapping("/login")
    public ResponseEntity<UserInfo> login(@RequestBody UserLogin login) {
        com.realworld.backend.users.entity.User user = userService.login(login);
        String accessToken = tokenProvider.createToken(user.getEmail());
        return ResponseEntity.ok(UserInfo.valueOf(user, accessToken));
    }
}
