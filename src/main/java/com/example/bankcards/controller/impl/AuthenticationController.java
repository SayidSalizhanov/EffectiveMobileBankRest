package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.AuthApi;
import com.example.bankcards.dto.request.AuthenticationRequest;
import com.example.bankcards.dto.response.AuthenticationResponse;
import com.example.bankcards.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Реализация {@link com.example.bankcards.controller.AuthApi}.
 * Делегирует операции аутентификации сервису {@link com.example.bankcards.security.AuthenticationService}.
 */
@RestController
@RequiredArgsConstructor
public class AuthenticationController implements AuthApi {

    private final AuthenticationService authenticationService;

    /** {@inheritDoc} */
    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }
}