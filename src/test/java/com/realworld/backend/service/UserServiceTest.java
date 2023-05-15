package com.realworld.backend.service;

import com.realworld.backend.controller.dto.UserLogin;
import com.realworld.backend.controller.dto.UserRegistration;
import com.realworld.backend.domain.User;
import com.realworld.backend.exception.RealWorldException;
import com.realworld.backend.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.realworld.backend.exception.RealWorldError.AUTHENTICATION_FAILED;
import static com.realworld.backend.exception.RealWorldError.DUPLICATE_USER;
import static com.realworld.backend.fixture.UserFixture.USER;
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
        given(userRepository.findByEmail(USER.getEmail())).willReturn(Optional.of(USER));

        UserLogin dto = new UserLogin(USER.getEmail(), USER.getPassword());

        //when
        User expected = userService.login(dto);

        //then
        assertThat(expected).isEqualTo(USER);
    }

    @Test
    @DisplayName("로그인할 유저가 존재하지 않으면 예외가 난다")
    public void assertExceptionWhenLoginUserNotExist() throws Exception {
        //given
        given(userRepository.findByEmail(USER.getEmail())).willReturn(Optional.empty());
        UserLogin dto = new UserLogin(USER.getEmail(), "invalid");

        //when, then
        assertThatThrownBy(() -> userService.login(dto))
                .isInstanceOf(RealWorldException.class)
                .hasMessage(AUTHENTICATION_FAILED.message());
    }

    @Test
    @DisplayName("로그인 비밀번호가 맞지 않으면 예외가 난다")
    public void assertExceptionWhenLoginFailed() throws Exception {
        //given
        given(userRepository.findByEmail(USER.getEmail())).willReturn(Optional.of(USER));
        UserLogin dto = new UserLogin(USER.getEmail(), "invalid");

        //when, then
        assertThatThrownBy(() -> userService.login(dto))
                .isInstanceOf(RealWorldException.class)
                .hasMessage(AUTHENTICATION_FAILED.message());
    }

    @Test
    @DisplayName("회원가입 시 중복 유저라면 예외 발생")
    public void assertDuplicateRegistration() throws Exception {
        given(userRepository.findByEmail("test@test.test")).willReturn(Optional.of(USER));
        UserRegistration dto = new UserRegistration(USER.getEmail(), USER.getName(), USER.getPassword());


        assertThatThrownBy(() -> userService.register(dto))
                .isInstanceOf(RealWorldException.class)
                .hasMessage(DUPLICATE_USER.message());
    }

    @Test
    @DisplayName("회원가입 성공 시 등록한 유저를 반환한다")
    public void assertReturnsIdWhenRegister() throws Exception {
        //given
        given(userRepository.save(USER)).willReturn(USER);
        UserRegistration dto = new UserRegistration(USER.getEmail(), USER.getName(), USER.getPassword());


        //when
        User user = userService.register(dto);

        //then
        assertThat(user.getId()).isEqualTo(USER.getId());
        assertThat(user).isEqualTo(USER);
    }
}