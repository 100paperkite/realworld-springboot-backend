package com.realworld.backend.controller;

import com.realworld.backend.controller.dto.UserDto;
import com.realworld.backend.domain.User;
import com.realworld.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("register")
    @ResponseBody
    public ResponseEntity<UserDto> register(@RequestBody UserDto user) {
        User newUser = User.of(user);

        userService.register(newUser);

        return ResponseEntity.ok(UserDto.of(newUser));
    }
}
