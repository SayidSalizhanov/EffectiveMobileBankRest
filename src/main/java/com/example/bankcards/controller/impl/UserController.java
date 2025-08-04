package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.UserApi;
import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.request.UserUpdateRequest;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public List<UserResponse> getUsers(Integer page, Integer size) {
        return userService.getAll(page, size);
    }

    @Override
    public UserResponse getUserById(UUID userId) {
        return userService.getById(userId);
    }

    @Override
    public UUID create(UserCreateRequest request) {
        return userService.create(request);
    }

    @Override
    public void update(UUID userId, UserUpdateRequest request) {
        userService.update(userId, request);
    }

    @Override
    public void delete(UUID userId) {
        userService.delete(userId);
    }
}
