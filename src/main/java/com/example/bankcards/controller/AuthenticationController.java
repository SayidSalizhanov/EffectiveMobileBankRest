package com.example.bankcards.controller;

import com.example.bankcards.dto.request.AuthenticationRequest;
import com.example.bankcards.dto.response.AuthenticationResponse;
import com.example.bankcards.security.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "Управление аутентификацией пользователей")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
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
    public AuthenticationResponse login(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return authenticationService.authenticate(request);
    }
}