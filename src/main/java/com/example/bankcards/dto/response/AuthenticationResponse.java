package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Ответ с данными аутентификации (JWT токен).
 * @param token jwt-токен
 */
@Builder
@Schema(description = "Ответ с JWT токеном аутентификации")
public record AuthenticationResponse(
        @Schema(description = "JWT токен доступа", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token
){
}