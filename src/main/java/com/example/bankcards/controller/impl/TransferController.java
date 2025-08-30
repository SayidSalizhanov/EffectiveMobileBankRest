package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.TransferApi;
import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Реализация {@link com.example.bankcards.controller.TransferApi}.
 * Делегирует операции перевода сервису {@link com.example.bankcards.service.TransferService}.
 */
@RestController
@RequiredArgsConstructor
public class TransferController implements TransferApi {

    private final TransferService transferService;

    /** {@inheritDoc} */
    @Override
    public void transfer(TransferRequest request) {
        transferService.transfer(request);
    }
}
