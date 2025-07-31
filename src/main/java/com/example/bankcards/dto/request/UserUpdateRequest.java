package com.example.bankcards.dto.request;

public record UserUpdateRequest(
        String newPassword
) { // todo валидация
}
