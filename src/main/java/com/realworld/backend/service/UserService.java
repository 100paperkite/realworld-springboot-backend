package com.realworld.backend.service;

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
    public Long register(User user) {
        validateDuplicated(user);
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicated(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent((u) -> {
                    throw new RealWorldException(DUPLICATE_USER);
                });
    }

    @Transactional(readOnly = true)
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RealWorldException(AUTHENTICATION_FAILED));

        if (!user.getPassword().equals(password)) {
            throw new RealWorldException(AUTHENTICATION_FAILED);
        }
        return user;
    }
}
