package com.example.bankcards.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CardResponse(
        String number,
        LocalDate expirationDate,
        String status,
        BigDecimal balance,
        UUID ownerId
) {
}
