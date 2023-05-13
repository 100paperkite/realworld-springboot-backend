package com.realworld.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realworld.backend.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.Map;

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

    @Test
    @DisplayName("회원가입에 성공한다")
    public void assertUserRegistration() throws Exception {
        //given
        String name = "jane";
        String email = "jane@jane.jane";
        String password = "janejanejane";

        var content = Map.of(
                "user", Map.of(
                        "username", name,
                        "email", email,
                        "password", password
                )
        );

        //when
        ResultActions result = mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(content)));


        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("user.username").value(name))
                .andExpect(jsonPath("user.email").value(email))
                .andExpect(jsonPath("user.password").value(password));
    }
}