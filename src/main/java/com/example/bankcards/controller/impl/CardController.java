package com.example.bankcards.controller.impl;

import com.example.bankcards.controller.CardApi;
import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.response.BalanceResponse;
import com.example.bankcards.dto.response.BlockRequestResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.service.BlockRequestService;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CardController implements CardApi {

    private final CardService cardService;
    private final BlockRequestService blockRequestService;

    @Override
    public List<CardResponse> getCards(Integer page, Integer size) {
        return cardService.getAll(page, size);
    }

    @Override
    public CardResponse getCardByNumber(String number) {
        return cardService.getByNumber(number);
    }

    @Override
    public void createCard(CardCreateRequest request) {
        cardService.create(request);
    }

    @Override
    public void blockCard(String number) {
        cardService.block(number);
    }

    @Override
    public void activateCard(String number) {
        cardService.activate(number);
    }

    @Override
    public void deleteCard(String number) {
        cardService.delete(number);
    }

    @Override
    public BalanceResponse getCardBalance(String number) {
        return cardService.getBalance(number);
    }

    // block-requests

    @Override
    public void blockCardRequest(String number) {
        cardService.blockRequest(number);
    }

    @Override
    public List<BlockRequestResponse> getAllBlockRequests(Integer page, Integer size) {
        return blockRequestService.getAll(page, size);
    }

    @Override
    public List<BlockRequestResponse> getBlockRequestsByStatus(String status, Integer page, Integer size) {
        return blockRequestService.getByStatus(status, page, size);
    }

    @Override
    public void approveBlockRequest(UUID requestId) {
        blockRequestService.approve(requestId);
    }

    @Override
    public void rejectBlockRequest(UUID requestId) {
        blockRequestService.reject(requestId);
    }
}
