package com.example.bankcards.controller;

import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.response.BalanceResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.bankcards.util.ValidationValues.Page.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CardResponse> getCards(
            @RequestParam(defaultValue = OFFSET_DEFAULT_VALUE) Integer page,
            @RequestParam(defaultValue = LIMIT_DEFAULT_VALUE) Integer size
    ) {
        return cardService.getAll(page, size);
    }

    @GetMapping("/{number}")
    @ResponseStatus(HttpStatus.OK)
    public CardResponse getCardByNumber(@PathVariable("number") String number) {
        return cardService.getByNumber(number);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCard(@RequestBody CardCreateRequest request) {
        cardService.create(request);
    }

    @PostMapping("/{number}/block")
    @ResponseStatus(HttpStatus.OK)
    public void blockCard(@PathVariable("number") String number) {
        cardService.block(number);
    }

    @PostMapping("/{number}/activate")
    @ResponseStatus(HttpStatus.OK)
    public void activateCard(@PathVariable("number") String number) {
        cardService.activate(number);
    }

    @DeleteMapping("/{number}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCard(@PathVariable("number") String number) {
        cardService.delete(number);
    }

    @PostMapping("/{number}/block/request")
    @ResponseStatus(HttpStatus.OK)
    public void blockCardRequest(@PathVariable("number") String number) {
        cardService.blockRequest(number);
    }

    @GetMapping("/{number}/balance")
    @ResponseStatus(HttpStatus.OK)
    public BalanceResponse getCardBalance(@PathVariable("number") String number) {
        return cardService.getBalance(number);
    }
}
