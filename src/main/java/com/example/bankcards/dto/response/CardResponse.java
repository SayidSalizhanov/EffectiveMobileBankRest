package com.example.bankcards.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

@Builder
public record CardResponse(
        String number,
        YearMonth expirationDate,
        String status,
        BigDecimal balance,
        UUID ownerId
) {
}
