package com.example.bankcards.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Запрос на перевод средств")
public record TransferRequest(
        @Schema(description = "Номер карты отправителя", example = "1234567812345678", pattern = "\\d{16}")
        @NotBlank(message = "Source card number is required")
        @Pattern(regexp = "\\d{16}", message = "Source card number must be 16 digits")
        String numberFrom,

        @Schema(description = "Номер карты получателя", example = "8765432187654321", pattern = "\\d{16}")
        @NotBlank(message = "Destination card number is required")
        @Pattern(regexp = "\\d{16}", message = "Destination card number must be 16 digits")
        String numberTo,

        @Schema(description = "Сумма перевода", example = "100.50", minimum = "0.01")
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
        BigDecimal amount
) {
}