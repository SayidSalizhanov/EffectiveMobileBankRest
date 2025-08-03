package com.example.bankcards.service.impl;

import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.response.BalanceResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.BlockRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.BlockRequestStatus;
import com.example.bankcards.enums.CardStatusEnum;
import com.example.bankcards.exception.custom.CardNotFoundException;
import com.example.bankcards.exception.custom.UserNotFoundException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.BlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.CardUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CardServiceImpl implements CardService {
    
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final BlockRequestRepository blockRequestRepository;
    private final CardMapper cardMapper;
    
    @Override
    @Transactional(readOnly = true)
    public List<CardResponse> getAll(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Card> cards = cardRepository.findAll(pageRequest);
        return cards.getContent().stream()
                .map(cardMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public CardResponse getByNumber(String number) {
        Card card = requireByNumber(number);
        return cardMapper.toResponse(card);
    }
    
    @Override
    public void create(CardCreateRequest request) {
        User owner = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.ownerId()));
        
        Card card = cardMapper.toEntity(request, owner);
        cardRepository.save(card);
    }
    
    @Override
    public void block(String number) {
        Card card = requireByNumber(number);
        
        card.setStatus(CardStatusEnum.BLOCKED);
        cardRepository.save(card);
    }
    
    @Override
    public void activate(String number) {
        Card card = requireByNumber(number);
        
        if (CardUtil.isExpired(card.getExpirationDate())) {
            card.setStatus(CardStatusEnum.EXPIRED);
        } else {
            card.setStatus(CardStatusEnum.ACTIVE);
        }
        
        cardRepository.save(card);
    }
    
    @Override
    public void delete(String number) {
        if (!cardRepository.existsByNumber(number)) {
            throw new CardNotFoundException("Card not found with number: " + number);
        }
        cardRepository.deleteById(number);
    }
    
    @Override
    public void blockRequest(String number) {
        Card card = requireByNumber(number);
        
        BlockRequest blockRequest = BlockRequest.builder()
                .cardNumber(number)
                .requester(card.getOwner())
                .status(BlockRequestStatus.PENDING)
                .requestDate(LocalDateTime.now())
                .reason("User requested card block")
                .build();
        
        blockRequestRepository.save(blockRequest);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BalanceResponse getBalance(String number) {
        Card card = requireByNumber(number);
        return cardMapper.toBalanceResponse(card);
    }

    @Override
    public void updateExpiredCards() {
        List<Card> expiredCards = cardRepository.findExpiredCards(YearMonth.now());
        expiredCards.forEach(card -> {
            card.setStatus(CardStatusEnum.EXPIRED);
            cardRepository.save(card);
        });
    }

    private Card requireByNumber(String number) {
        return cardRepository.findByNumber(number)
                .orElseThrow(() -> new CardNotFoundException("Card not found with number: " + number));
    }
} 