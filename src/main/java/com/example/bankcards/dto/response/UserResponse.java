package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

/**
 * Ответ с агрегированной информацией о пользователе: логин, карты, роли и статусы запросов.
 * @param login логин
 * @param cardsNumbers карты пользователя
 * @param rolesNames роли пользователя
 * @param blockRequestStatuses статусы запросов на блокировку карт
 */
@Builder
@Schema(description = "Ответ с информацией о пользователе")
public record UserResponse(
        @Schema(description = "Логин пользователя", example = "user123")
        String login,

        @ArraySchema(
                arraySchema = @Schema(description = "Номера карт, привязанных к пользователю"),
                schema = @Schema(description = "Номер карты", example = "1234567812345678")
        )
        List<String> cardsNumbers,

        @ArraySchema(
                arraySchema = @Schema(description = "Роли пользователя"),
                schema = @Schema(description = "Название роли", example = "ROLE_USER")
        )
        List<String> rolesNames,

        @ArraySchema(
                arraySchema = @Schema(description = "Статусы запросов на блокировку, инициированных пользователем"),
                schema = @Schema(description = "Статус запроса", example = "PENDING")
        )
        List<String> blockRequestStatuses
) {
}