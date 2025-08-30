package com.example.bankcards.service.impl;

import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.enums.CardStatusEnum;
import com.example.bankcards.exception.custom.CardNotFoundException;
import com.example.bankcards.exception.custom.TransferException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Реализация {@link com.example.bankcards.service.TransferService}.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TransferServiceImpl implements TransferService {
    
    private final CardRepository cardRepository;

    /** {@inheritDoc} */
    @Override
    public void transfer(TransferRequest request) {
        Card fromCard = cardRepository.findByNumber(request.numberFrom())
                .orElseThrow(() -> new CardNotFoundException("Source card not found: " + request.numberFrom()));
        
        Card toCard = cardRepository.findByNumber(request.numberTo())
                .orElseThrow(() -> new CardNotFoundException("Destination card not found: " + request.numberTo()));
        
        // owner check
        if (!fromCard.getOwner().getUserId().equals(toCard.getOwner().getUserId())) {
            throw new TransferException("Cards must belong to the same user");
        }
        
        // status check
        if (fromCard.getStatus() != CardStatusEnum.ACTIVE) {
            throw new TransferException("Source card is not active. Status: " + fromCard.getStatus());
        }
        if (toCard.getStatus() != CardStatusEnum.ACTIVE) {
            throw new TransferException("Destination card is not active. Status: " + toCard.getStatus());
        }

        // amount check
        BigDecimal amount = request.amount();
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransferException("Transfer amount must be positive");
        }

        // balance check
        if (fromCard.getBalance().compareTo(amount) < 0) {
            throw new TransferException("Insufficient funds on source card");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(amount));
        toCard.setBalance(toCard.getBalance().add(amount));
        
        cardRepository.save(fromCard);
        cardRepository.save(toCard);
    }
} 