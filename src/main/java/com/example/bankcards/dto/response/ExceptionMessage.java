package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
@Schema(description = "Сообщение об ошибке")
public record ExceptionMessage(
        @Schema(description = "Текст ошибки", example = "Invalid input data")
        String message,

        @Schema(description = "Детали ошибок по полям (если есть)",
                example = "{\"fieldName\": [\"Error message 1\", \"Error message 2\"]}")
        Map<String, List<String>> errors,

        @Schema(description = "Имя класса исключения", example = "MethodArgumentNotValidException")
        String exceptionName
) {
}