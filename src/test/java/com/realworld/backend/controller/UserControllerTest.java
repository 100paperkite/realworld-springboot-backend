package com.realworld.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realworld.backend.controller.dto.UserDto;
import com.realworld.backend.domain.User;
import com.realworld.backend.exception.RealWorldException;
import com.realworld.backend.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;

import static com.realworld.backend.exception.RealWorldError.DUPLICATE_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockHttpServletRequestBuilder registerRequest(UserDto dto) throws Exception {
        return post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(dto));
    }

    @Test
    @DisplayName("회원가입에 성공하면 200과 User 반환")
    public void assertUserRegistration() throws Exception {
        //given
        String name = "jane";
        String email = "jane@jane.jane";
        String password = "janejanejane";

        UserDto dto = new UserDto(name, email, password);

        //when
        ResultActions result = mockMvc.perform(registerRequest(dto));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("user.username").value(name))
                .andExpect(jsonPath("user.email").value(email))
                .andExpect(jsonPath("user.password").value(password));
    }

    @Test
    @DisplayName("회원가입 시 중복 유저라면 409 에러와 적절한 에러메시지를 반환")
    public void assertDuplicatedUserRegistration() throws Exception {
        //given
        String name = "jane";
        String email = "jane@jane.jane";
        String password = "janejanejane";

        UserDto dto = new UserDto(name, email, password);

        // when
        doThrow(new RealWorldException(DUPLICATE_USER))
                .when(userService).register(any(User.class));


        //then
        mockMvc.perform(registerRequest(dto))
                .andExpect(status().is(DUPLICATE_USER.status().value()))
                .andExpect(jsonPath("errors.body").value(DUPLICATE_USER.message()));
    }
}