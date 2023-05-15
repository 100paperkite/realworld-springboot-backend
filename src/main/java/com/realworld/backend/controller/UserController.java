package com.realworld.backend.controller;

import com.realworld.backend.controller.dto.UserDto;
import com.realworld.backend.controller.dto.UserLogin;
import com.realworld.backend.controller.dto.UserRegistration;
import com.realworld.backend.domain.User;
import com.realworld.backend.service.UserService;
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
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> register(@RequestBody UserRegistration registration) {
        User user = userService.register(registration);
        return ResponseEntity.ok(UserDto.valueOf(user, ""));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserLogin login) {
        User user = userService.login(login);
        return ResponseEntity.ok(UserDto.valueOf(user, ""));
    }
}
