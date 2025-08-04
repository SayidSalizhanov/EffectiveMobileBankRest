package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Schema(description = "Ответ с информацией о запросе на блокировку")
public record BlockRequestResponse(
        @Schema(description = "ID запроса", example = "d3d94468-2d6a-4d2a-9f38-0a9d27f8c1b3")
        UUID requestId,

        @Schema(description = "Номер карты", example = "1234567812345678")
        String cardNumber,

        @Schema(description = "Логин инициатора запроса", example = "user123")
        String requesterLogin,

        @Schema(description = "Статус запроса", example = "PENDING", allowableValues = {"PENDING", "APPROVED", "REJECTED"})
        String status,

        @Schema(description = "Дата создания запроса", example = "2024-01-01T12:00:00")
        LocalDateTime requestDate,

        @Schema(description = "Дата обработки запроса", example = "2024-01-02T10:30:00")
        LocalDateTime processedDate,

        @Schema(description = "Причина блокировки (если отклонено)", example = "Недостаточно оснований")
        String reason
) {
}