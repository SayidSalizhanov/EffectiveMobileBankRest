package com.example.bankcards.service;

import com.example.bankcards.dto.response.BlockRequestResponse;

import java.util.List;
import java.util.UUID;

public interface BlockRequestService {
    List<BlockRequestResponse> getAll(Integer page, Integer size);
    List<BlockRequestResponse> getByStatus(String status, Integer page, Integer size);
    void approve(UUID requestId);
    void reject(UUID requestId);
} 