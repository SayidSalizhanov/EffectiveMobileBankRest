package com.example.bankcards.service;

import com.example.bankcards.dto.request.TransferRequest;

public interface TransferService {
    void transfer(TransferRequest request);
}
