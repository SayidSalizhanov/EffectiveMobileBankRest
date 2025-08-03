package com.example.bankcards.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.YearMonth;
import java.util.UUID;

public record CardCreateRequest(
        @NotBlank(message = "Card number is required")
        @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
        String number,

        @NotNull(message = "Expiration date is required")
        @Future(message = "Expiration date must be in the future")
        YearMonth expirationDate,

        @NotNull(message = "Owner ID is required")
        UUID ownerId
) {
}
