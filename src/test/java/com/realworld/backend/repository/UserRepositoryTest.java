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
    @DisplayName("이름으로 찾을 수 있다")
    @Transactional
    void assertFindByName() {
        User user = new User("test", "test@test.test", "testtesttest");
        userRepository.save(user);

        Optional<User> found = userRepository.findByName("test");
        Optional<User> notFound = userRepository.findByName("notFound");

        assertThat(found).isPresent();
        assertThat(notFound).isNotPresent();

        assertThat(found.get()).isEqualTo(user);
    }
}