package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.TransferApi;
import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TransferController implements TransferApi {

    private final TransferService transferService;

    @Override
    public void transfer(TransferRequest request) {
        transferService.transfer(request);
    }
}
