package com.example.bankcards.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Запрос на аутентификацию: содержит логин и пароль пользователя.
 * @param login логин
 * @param password пароль
 */
@Schema(description = "Запрос на аутентификацию")
public record AuthenticationRequest(
        @Schema(description = "Логин пользователя", example = "user123", minLength = 3, maxLength = 50)
        @NotBlank(message = "Login is required")
        @Size(min = 3, max = 50, message = "Login must be between 3 and 50 characters")
        String login,

        @Schema(description = "Пароль пользователя", example = "password123", minLength = 6, maxLength = 255)
        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
        String password
) {
}