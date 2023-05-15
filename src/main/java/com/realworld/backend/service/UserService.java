package com.realworld.backend.service;

import com.realworld.backend.controller.dto.UserLogin;
import com.realworld.backend.controller.dto.UserRegistration;
import com.realworld.backend.domain.User;
import com.realworld.backend.exception.RealWorldException;
import com.realworld.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.realworld.backend.exception.RealWorldError.AUTHENTICATION_FAILED;
import static com.realworld.backend.exception.RealWorldError.DUPLICATE_USER;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User register(UserRegistration registration) {

        User user = User.builder()
                .email(registration.getEmail())
                .name(registration.getUsername())
                .password(registration.getPassword())
                .build();

        validateDuplicated(user.getEmail());


        return userRepository.save(user);
    }

    private void validateDuplicated(String email) {
        userRepository.findByEmail(email).ifPresent((u) -> {
            throw new RealWorldException(DUPLICATE_USER);
        });
    }

    @Transactional(readOnly = true)
    public User login(UserLogin login) {
        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> new RealWorldException(AUTHENTICATION_FAILED));

        if (!user.getPassword().equals(login.getPassword())) {
            throw new RealWorldException(AUTHENTICATION_FAILED);
        }
        return user;
    }
}
