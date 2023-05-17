package com.realworld.backend.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realworld.backend.exception.RealWorldException;
import com.realworld.backend.security.TokenProvider;
import com.realworld.backend.users.dto.UserLogin;
import com.realworld.backend.users.dto.UserRegistration;
import com.realworld.backend.users.fixture.UserFixture;
import com.realworld.backend.users.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;

import static com.realworld.backend.exception.RealWorldError.AUTHENTICATION_FAILED;
import static com.realworld.backend.exception.RealWorldError.DUPLICATE_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TokenProvider provider;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    private static MockHttpServletRequest jsonPostProcessor(MockHttpServletRequest request) {
        request.setContentType(String.valueOf(MediaType.APPLICATION_JSON));
        request.setCharacterEncoding(String.valueOf(StandardCharsets.UTF_8));
        return request;
    }

    private MockHttpServletRequestBuilder registerRequest(UserRegistration dto) throws Exception {
        return post("/api/users")
                .content(objectMapper.writeValueAsString(dto))
                .with(UserControllerTest::jsonPostProcessor)
                .with(csrf());

    }

    private MockHttpServletRequestBuilder loginRequest(UserLogin dto) throws Exception {
        return post("/api/users/login")
                .content(objectMapper.writeValueAsString(dto))
                .with(UserControllerTest::jsonPostProcessor)
                .with(csrf());
    }

    @Test
    @WithMockUser
    @DisplayName("회원가입에 성공하면 200과 User 반환")
    public void assertUserRegistration() throws Exception {
        //given
        given(userService.register(any(UserRegistration.class))).willReturn(UserFixture.USER);
        given(provider.createToken(any())).willReturn(UserFixture.JWT);

        //when
        ResultActions result = mockMvc.perform(registerRequest(UserFixture.REGISTER));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("user.username").value(UserFixture.USER.getName()))
                .andExpect(jsonPath("user.email").value(UserFixture.USER.getEmail()))
                .andExpect(jsonPath("user.token").value(UserFixture.JWT))
                .andExpect(jsonPath("user.bio").hasJsonPath())
                .andExpect(jsonPath("user.image").hasJsonPath());
    }

    @Test
    @WithMockUser
    @DisplayName("회원가입 시 중복 유저라면 409 에러와 적절한 에러메시지를 반환")
    public void assertDuplicatedUserRegistration() throws Exception {
        //given
        doThrow(new RealWorldException(DUPLICATE_USER))
                .when(userService).register(any(UserRegistration.class));

        //when, then
        mockMvc.perform(registerRequest(UserFixture.REGISTER))
                .andExpect(status().is(DUPLICATE_USER.status().value()))
                .andExpect(jsonPath("errors.body").value(DUPLICATE_USER.message()));
    }

    @Test
    @WithMockUser
    @DisplayName("로그인이 실패하면 401을 반환")
    public void assertLoginFailed() throws Exception {
        //given
        doThrow(new RealWorldException(AUTHENTICATION_FAILED))
                .when(userService).login(any(UserLogin.class));

        //when, then
        mockMvc.perform(loginRequest(UserFixture.LOGIN))
                .andExpect(status().is(AUTHENTICATION_FAILED.status().value()))
                .andExpect(jsonPath("errors.body").value(AUTHENTICATION_FAILED.message()));
    }

    @Test
    @WithMockUser
    @DisplayName("로그인이 성공하면 200과 User를 반환")
    public void assertLoginSuccess() throws Exception {
        //given
        given(userService.login(any(UserLogin.class))).willReturn(UserFixture.USER);
        given(provider.createToken(any())).willReturn(UserFixture.JWT);

        // when
        var result = mockMvc.perform(loginRequest(UserFixture.LOGIN));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("user.username").value(UserFixture.USER.getName()))
                .andExpect(jsonPath("user.email").value(UserFixture.USER.getEmail()))
                .andExpect(jsonPath("user.token").value(UserFixture.JWT))
                .andExpect(jsonPath("user.bio").hasJsonPath())
                .andExpect(jsonPath("user.image").hasJsonPath());

    }
}