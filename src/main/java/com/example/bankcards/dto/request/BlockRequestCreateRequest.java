package com.example.bankcards.dto.request;

public record BlockRequestCreateRequest(
        String cardNumber,
        String reason
) { // todo валидация
} 