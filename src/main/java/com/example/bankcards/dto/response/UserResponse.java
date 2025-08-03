package com.example.bankcards.dto.response;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record UserResponse(
        String login,
        List<String> cardsNumbers,
        List<String> rolesNames,
        List<String> blockRequestStatuses
) {
}
