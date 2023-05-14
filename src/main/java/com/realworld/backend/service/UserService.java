package com.realworld.backend.service;

import com.realworld.backend.domain.User;
import com.realworld.backend.exception.RealWorldError;
import com.realworld.backend.exception.RealWorldException;
import com.realworld.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                    throw new RealWorldException(RealWorldError.DUPLICATE_USER);
                });
    }
}
