package com.example.bankcards.service;

import com.example.bankcards.dto.request.TransferRequest;

/**
 * Сервис переводов средств между картами.
 */
public interface TransferService {
    /**
     * Выполняет перевод между картами.
     * @param request dto перевода (request)
     */
    void transfer(TransferRequest request);
}
