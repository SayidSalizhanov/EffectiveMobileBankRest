package com.example.bankcards.dto.request;

import java.math.BigDecimal;

public record TransferRequest(
        String numberFrom,
        String numberTo,
        BigDecimal amount
) { // todo валидация
}
