package com.example.bankcards.controller;

import com.example.bankcards.dto.request.AuthenticationRequest;
import com.example.bankcards.dto.response.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * REST API для операций аутентификации пользователей.
 * <p>
 * Содержит эндпоинты для входа пользователя и получения JWT токена.
 */
@Tag(name = "Аутентификация", description = "Управление аутентификацией пользователей")
@RequestMapping("/api/auth")
public interface AuthApi {

    /**
     * Выполняет аутентификацию пользователя по переданным учетным данным.
     * При успешной аутентификации возвращает JWT токен для доступа к защищенным ресурсам.
     * @param request запрос с логином и паролем
     * @return ответ с JWT токеном и служебной информацией
     */
    @Operation(
            summary = "Аутентификация пользователя",
            description = "Выполняет вход пользователя в систему и возвращает JWT токен",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешная аутентификация",
                            content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))
                    )
            }
    )
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    AuthenticationResponse login(
            @Valid @RequestBody AuthenticationRequest request
    );
}
