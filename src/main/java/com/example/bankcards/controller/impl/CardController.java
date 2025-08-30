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

/**
 * Реализация {@link com.example.bankcards.controller.CardApi}.
 * Делегирует операции по сервисам {@link com.example.bankcards.service.CardService}
 * и {@link com.example.bankcards.service.BlockRequestService}.
 */
@RestController
@RequiredArgsConstructor
public class CardController implements CardApi {

    private final CardService cardService;
    private final BlockRequestService blockRequestService;

    /** {@inheritDoc} */
    @Override
    public List<CardResponse> getCards(Integer page, Integer size) {
        return cardService.getAll(page, size);
    }

    /** {@inheritDoc} */
    @Override
    public CardResponse getCardByNumber(String number) {
        return cardService.getByNumber(number);
    }

    /** {@inheritDoc} */
    @Override
    public void createCard(CardCreateRequest request) {
        cardService.create(request);
    }

    /** {@inheritDoc} */
    @Override
    public void blockCard(String number) {
        cardService.block(number);
    }

    /** {@inheritDoc} */
    @Override
    public void activateCard(String number) {
        cardService.activate(number);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteCard(String number) {
        cardService.delete(number);
    }

    /** {@inheritDoc} */
    @Override
    public BalanceResponse getCardBalance(String number) {
        return cardService.getBalance(number);
    }

    // block-requests

    /** {@inheritDoc} */
    @Override
    public void blockCardRequest(String number) {
        cardService.blockRequest(number);
    }

    /** {@inheritDoc} */
    @Override
    public List<BlockRequestResponse> getAllBlockRequests(Integer page, Integer size) {
        return blockRequestService.getAll(page, size);
    }

    /** {@inheritDoc} */
    @Override
    public List<BlockRequestResponse> getBlockRequestsByStatus(String status, Integer page, Integer size) {
        return blockRequestService.getByStatus(status, page, size);
    }

    /** {@inheritDoc} */
    @Override
    public void approveBlockRequest(UUID requestId) {
        blockRequestService.approve(requestId);
    }

    /** {@inheritDoc} */
    @Override
    public void rejectBlockRequest(UUID requestId) {
        blockRequestService.reject(requestId);
    }
}
