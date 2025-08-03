package com.example.bankcards.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank(message = "Source card number is required")
        @Pattern(regexp = "\\d{16}", message = "Source card number must be 16 digits")
        String numberFrom,

        @NotBlank(message = "Destination card number is required")
        @Pattern(regexp = "\\d{16}", message = "Destination card number must be 16 digits")
        String numberTo,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
        BigDecimal amount
) {
}
