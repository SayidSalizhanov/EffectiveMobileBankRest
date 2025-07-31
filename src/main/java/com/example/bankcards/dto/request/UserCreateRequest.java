package com.example.bankcards.dto.request;

public record UserCreateRequest(
        String login,
        String password
) { // todo валидация
}
