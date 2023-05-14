package com.realworld.backend.repository;

import com.realworld.backend.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("이메일로 찾을 수 있다")
    @Transactional
    void assertFindByName() {
        String email = "test@test.test";
        User user = new User("test", email, "testtesttest");
        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail(email);
        Optional<User> notFound = userRepository.findByEmail("invalid@invalid.invalid");

        assertThat(found).isPresent();
        assertThat(notFound).isNotPresent();

        assertThat(found.get()).isEqualTo(user);
    }
}