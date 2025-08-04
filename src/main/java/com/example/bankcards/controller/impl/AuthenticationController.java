package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.AuthApi;
import com.example.bankcards.dto.request.AuthenticationRequest;
import com.example.bankcards.dto.response.AuthenticationResponse;
import com.example.bankcards.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthenticationController implements AuthApi {

    private final AuthenticationService authenticationService;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }
}