package com.example.bankcards.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Запрос на обновление пароля пользователя.
 */
@Schema(description = "Запрос на обновление пароля пользователя")
public record UserUpdateRequest(
        @Schema(description = "Текущий пароль", example = "oldPassword123")
        @NotBlank(message = "Old password is required")
        String oldPassword,

        @Schema(description = "Новый пароль", example = "newSecurePassword456", minLength = 6, maxLength = 255)
        @NotBlank(message = "New password is required")
        @Size(min = 6, max = 255, message = "New password must be between 6 and 255 characters")
        String newPassword
) {
}