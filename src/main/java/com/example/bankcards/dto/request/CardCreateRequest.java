package com.example.bankcards.dto.request;

import java.time.YearMonth;
import java.util.UUID;

public record CardCreateRequest(
        String number,
        YearMonth expirationDate,
        UUID ownerId
) { // todo валидация
}
