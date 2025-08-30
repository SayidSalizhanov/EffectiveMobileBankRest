package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

/**
 * Ответ с информацией о балансе банковской карты.
 * @param cardNumber номер карты
 * @param balance баланс
 */
@Builder
@Schema(description = "Ответ с балансом карты")
public record BalanceResponse(
        @Schema(description = "Номер карты", example = "1234567812345678")
        String cardNumber,

        @Schema(description = "Текущий баланс", example = "1500.75")
        BigDecimal balance
) {
}