package com.example.bankcards.dto.request;

public record UserUpdateRequest(
        String oldPassword,
        String newPassword
) { // todo валидация
}
