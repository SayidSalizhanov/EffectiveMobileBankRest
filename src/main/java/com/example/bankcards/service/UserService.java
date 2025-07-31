package com.example.bankcards.service;

import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.request.UserUpdateRequest;
import com.example.bankcards.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponse> getAll(Integer page, Integer size);
    UserResponse getById(UUID userId);
    UUID create(UserCreateRequest request);
    void update(UUID userId, UserUpdateRequest request);
    void delete(UUID userId);
}
