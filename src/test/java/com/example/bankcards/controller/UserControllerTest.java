package com.example.bankcards.controller;

import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.request.UserUpdateRequest;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.exception.custom.UserAlreadyExistsException;
import com.example.bankcards.exception.custom.UserNotFoundException;
import com.example.bankcards.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void getUsers_ShouldReturnUserList() throws Exception {
        // given
        UserResponse userResponse = UserResponse.builder()
                .login("testuser")
                .cardsNumbers(List.of("1234567890123456"))
                .rolesNames(List.of("ROLE_USER"))
                .blockRequestStatuses(List.of("PENDING"))
                .build();
        List<UserResponse> users = Collections.singletonList(userResponse);
        given(userService.getAll(anyInt(), anyInt())).willReturn(users);

        // when & then
        mockMvc.perform(get("/api/users")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].login").value("testuser"))
                .andExpect(jsonPath("$[0].cardsNumbers[0]").value("1234567890123456"))
                .andExpect(jsonPath("$[0].rolesNames[0]").value("ROLE_USER"));
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UserResponse userResponse = UserResponse.builder()
                .login("testuser")
                .cardsNumbers(List.of("1234567890123456"))
                .rolesNames(List.of("ROLE_USER"))
                .blockRequestStatuses(List.of("PENDING"))
                .build();
        given(userService.getById(userId)).willReturn(userResponse);

        // when & then
        mockMvc.perform(get("/api/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value("testuser"))
                .andExpect(jsonPath("$.cardsNumbers[0]").value("1234567890123456"));
    }

    @Test
    void getUserById_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        given(userService.getById(userId)).willThrow(new UserNotFoundException("User not found"));

        // when & then
        mockMvc.perform(get("/api/users/{userId}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_ShouldCreateUser() throws Exception {
        // given
        UserCreateRequest request = new UserCreateRequest("newuser", "password123");
        UUID userId = UUID.randomUUID();
        given(userService.create(request)).willReturn(userId);

        // when & then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(userId.toString()));
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenInvalidInput() throws Exception {
        // given
        UserCreateRequest invalidRequest = new UserCreateRequest("ab", "pass");

        // when & then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_ShouldReturnConflict_WhenUserAlreadyExists() throws Exception {
        // given
        UserCreateRequest request = new UserCreateRequest("existinguser", "password123");
        given(userService.create(request)).willThrow(new UserAlreadyExistsException("User already exists"));

        // when & then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateUser_ShouldUpdateUser() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UserUpdateRequest request = new UserUpdateRequest("oldpassword", "newpassword123");
        willDoNothing().given(userService).update(eq(userId), eq(request));

        // when & then
        mockMvc.perform(put("/api/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateUser_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UserUpdateRequest request = new UserUpdateRequest("oldpassword", "newpassword123");
        willThrow(new UserNotFoundException("User not found")).given(userService).update(eq(userId), eq(request));

        // when & then
        mockMvc.perform(put("/api/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_ShouldReturnBadRequest_WhenInvalidInput() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UserUpdateRequest invalidRequest = new UserUpdateRequest("", "pass");

        // when & then
        mockMvc.perform(put("/api/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_ShouldDeleteUser() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        willDoNothing().given(userService).delete(userId);

        // when & then
        mockMvc.perform(delete("/api/users/{userId}", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        willThrow(new UserNotFoundException("User not found")).given(userService).delete(userId);

        // when & then
        mockMvc.perform(delete("/api/users/{userId}", userId))
                .andExpect(status().isNotFound());
    }
}