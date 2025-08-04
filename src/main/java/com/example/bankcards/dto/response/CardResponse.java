package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

@Builder
@Schema(description = "Ответ с информацией о карте")
public record CardResponse(
        @Schema(description = "Номер карты", example = "1234567812345678")
        String number,

        @Schema(description = "Дата окончания действия", example = "2025-12")
        YearMonth expirationDate,

        @Schema(description = "Статус карты", example = "ACTIVE", allowableValues = {"ACTIVE", "BLOCKED"})
        String status,

        @Schema(description = "Текущий баланс", example = "1500.75")
        BigDecimal balance,

        @Schema(description = "ID владельца карты", example = "d3d94468-2d6a-4d2a-9f38-0a9d27f8c1b3")
        UUID ownerId
) {
}