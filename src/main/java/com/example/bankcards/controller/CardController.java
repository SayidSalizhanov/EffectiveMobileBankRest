package com.example.bankcards.controller;

import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.response.BalanceResponse;
import com.example.bankcards.dto.response.BlockRequestResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.service.BlockRequestService;
import com.example.bankcards.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.example.bankcards.util.ValidationValues.Page.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final BlockRequestService blockRequestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<CardResponse> getCards(
            @RequestParam(defaultValue = OFFSET_DEFAULT_VALUE) Integer page,
            @RequestParam(defaultValue = LIMIT_DEFAULT_VALUE) Integer size
    ) {
        return cardService.getAll(page, size);
    }

    @GetMapping("/{number}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public CardResponse getCardByNumber(@PathVariable("number") String number) {
        return cardService.getByNumber(number);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public void createCard(@Valid @RequestBody CardCreateRequest request) {
        cardService.create(request);
    }

    @PostMapping("/{number}/block")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public void blockCard(@PathVariable("number") String number) {
        cardService.block(number);
    }

    @PostMapping("/{number}/activate")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public void activateCard(@PathVariable("number") String number) {
        cardService.activate(number);
    }

    @DeleteMapping("/{number}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCard(@PathVariable("number") String number) {
        cardService.delete(number);
    }

    @GetMapping("/{number}/balance")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public BalanceResponse getCardBalance(@PathVariable("number") String number) {
        return cardService.getBalance(number);
    }

    // block-requests

    @PostMapping("/{number}/block-request")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    public void blockCardRequest(@PathVariable("number") String number) {
        cardService.blockRequest(number);
    }

    @GetMapping("/block-requests")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<BlockRequestResponse> getAllBlockRequests(
            @RequestParam(defaultValue = OFFSET_DEFAULT_VALUE) Integer page,
            @RequestParam(defaultValue = LIMIT_DEFAULT_VALUE) Integer size) {
        return blockRequestService.getAll(page, size);
    }

    @GetMapping("/block-requests/status/{status}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<BlockRequestResponse> getBlockRequestsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = OFFSET_DEFAULT_VALUE) Integer page,
            @RequestParam(defaultValue = LIMIT_DEFAULT_VALUE) Integer size) {
        return blockRequestService.getByStatus(status, page, size);
    }

    @PostMapping("/block-requests/{requestId}/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void approveBlockRequest(@PathVariable UUID requestId) {
        blockRequestService.approve(requestId);
    }

    @PostMapping("/block-requests/{requestId}/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void rejectBlockRequest(@PathVariable UUID requestId) {
        blockRequestService.reject(requestId);
    }
}
