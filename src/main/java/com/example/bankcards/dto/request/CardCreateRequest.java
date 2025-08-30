package com.example.bankcards.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.YearMonth;
import java.util.UUID;

/**
 * Запрос на создание новой банковской карты.
 * @param number номер карты
 * @param expirationDate дата недействительности
 * @param ownerId id владельца
 */
@Schema(description = "Запрос на создание банковской карты")
public record CardCreateRequest(
        @Schema(description = "Номер карты (16 цифр)", example = "1234567812345678", pattern = "\\d{16}")
        @NotBlank(message = "Card number is required")
        @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
        String number,

        @Schema(description = "Дата окончания действия (MM-yyyy)", example = "2025-12")
        @NotNull(message = "Expiration date is required")
        @Future(message = "Expiration date must be in the future")
        YearMonth expirationDate,

        @Schema(description = "ID владельца карты", example = "d3d94468-2d6a-4d2a-9f38-0a9d27f8c1b3")
        @NotNull(message = "Owner ID is required")
        UUID ownerId
) {
}