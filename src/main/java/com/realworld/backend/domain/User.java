package com.realworld.backend.domain;

import com.realworld.backend.controller.dto.UserDto;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String name;
    private String email;
    private String password;

    protected User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }


    public static User of(UserDto dto) {
        return new User(dto.getUsername(), dto.getEmail(), dto.getPassword());
    }
}
