package com.realworld.backend.service;

import com.realworld.backend.domain.User;
import com.realworld.backend.exception.RealWorldException;
import com.realworld.backend.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.realworld.backend.exception.RealWorldError.AUTHENTICATION_FAILED;
import static com.realworld.backend.exception.RealWorldError.DUPLICATE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = new UserService(userRepository);


    @Test
    @DisplayName("로그인에 성공한다")
    public void assertLoginSucceed() throws Exception {
        //given
        User user = new User("test", "test@test.test", "test");

        given(userRepository.findByEmail(user.getEmail()))
                .willReturn(Optional.of(user));

        //when
        User expected = userService.login(user.getEmail(), user.getPassword());

        //then
        assertThat(expected).isEqualTo(user);
    }

    @Test
    @DisplayName("로그인할 유저가 존재하지 않으면 예외가 난다")
    public void assertExceptionWhenLoginUserNotExist() throws Exception {
        //given
        String email = "test@test.test";
        given(userRepository.findByEmail(email))
                .willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> userService.login(email, "invalid"))
                .isInstanceOf(RealWorldException.class)
                .hasMessage(AUTHENTICATION_FAILED.message());
    }

    @Test
    @DisplayName("로그인 비밀번호가 맞지 않으면 예외가 난다")
    public void assertExceptionWhenLoginFailed() throws Exception {
        //given
        User user = new User("test", "test@test.test", "test");
        given(userRepository.findByEmail(user.getEmail()))
                .willReturn(Optional.of(user));

        //when, then
        assertThatThrownBy(() -> userService.login(user.getEmail(), "invalid"))
                .isInstanceOf(RealWorldException.class)
                .hasMessage(AUTHENTICATION_FAILED.message());
    }

    @Test
    @DisplayName("중복 유저라면 예외 발생")
    public void assertDuplicateRegistration() throws Exception {
        User user = new User("test", "test@test.test", "test");
        given(userRepository.findByEmail("test@test.test")).willReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.register(user))
                .isInstanceOf(RealWorldException.class)
                .hasMessage(DUPLICATE_USER.message());
    }

    @Test
    @DisplayName("등록한 유저의 ID를 반환한다")
    public void assertReturnsIdWhenRegister() throws Exception {
        //given
        Long id = 1L;
        User user = new MockUser(id, "test", "test@test.test", "test");
        given(userRepository.save(user)).willReturn(user);

        //when
        Long resultId = userService.register(user);

        //then
        assertThat(resultId).isEqualTo(id);
    }

    private static class MockUser extends User {
        private final Long id;

        public MockUser(Long id, String name, String email, String password) {
            super(name, email, password);
            this.id = id;
        }

        @Override
        public Long getId() {
            return id;
        }
    }
}