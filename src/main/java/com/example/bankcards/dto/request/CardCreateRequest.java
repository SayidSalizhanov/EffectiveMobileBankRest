package com.example.bankcards.dto.request;

import java.time.LocalDate;
import java.util.UUID;

public record CardCreateRequest(
        String number,
        LocalDate expirationDate,
        UUID ownerId
) { // todo валидация
}
