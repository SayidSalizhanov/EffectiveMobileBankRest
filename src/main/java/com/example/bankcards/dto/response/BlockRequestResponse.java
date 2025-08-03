package com.example.bankcards.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record BlockRequestResponse(
        UUID requestId,
        String cardNumber,
        String requesterLogin,
        String status,
        LocalDateTime requestDate,
        LocalDateTime processedDate,
        String reason
) {
} 