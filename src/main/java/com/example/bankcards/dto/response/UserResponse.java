package com.example.bankcards.dto.response;

import java.util.List;

public record UserResponse(
        String login,
        List<String> cardsNumbers,
        List<String> rolesNames
) {
}
