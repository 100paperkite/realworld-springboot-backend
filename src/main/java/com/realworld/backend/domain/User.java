package com.realworld.backend.domain;

import com.realworld.backend.controller.dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String bio;
    private String image;


    public static User from(UserDto dto) {
        return User.builder()
                .name(dto.getUsername())
                .email(dto.getEmail())
                .bio(dto.getBio())
                .image(dto.getImage())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
