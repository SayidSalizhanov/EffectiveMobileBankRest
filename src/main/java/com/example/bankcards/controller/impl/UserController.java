package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.UserApi;
import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.request.UserPasswordUpdateRequest;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Реализация {@link com.example.bankcards.controller.UserApi}.
 * Делегирует операции над пользователями сервису {@link com.example.bankcards.service.UserService}.
 */
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    /** {@inheritDoc} */
    @Override
    public List<UserResponse> getUsers(Integer page, Integer size) {
        return userService.getAll(page, size);
    }

    /** {@inheritDoc} */
    @Override
    public UserResponse getUserById(UUID userId) {
        return userService.getById(userId);
    }

    /** {@inheritDoc} */
    @Override
    public UUID create(UserCreateRequest request) {
        return userService.create(request);
    }

    /** {@inheritDoc} */
    @Override
    public void update(UUID userId, UserPasswordUpdateRequest request) {
        userService.updatePassword(userId, request);
    }

    /** {@inheritDoc} */
    @Override
    public void delete(UUID userId) {
        userService.delete(userId);
    }
}
