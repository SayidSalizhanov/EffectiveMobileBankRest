package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.response.BalanceResponse;
import com.example.bankcards.dto.response.CardResponse;

import java.util.List;

public interface CardService {
    List<CardResponse> getAll(Integer page, Integer size);
    CardResponse getByNumber(String number);
    void create(CardCreateRequest request);
    void block(String number);
    void activate(String number);
    void delete(String number);
    void blockRequest(String number);
    BalanceResponse getBalance(String number);
    void updateExpiredCards();
}
