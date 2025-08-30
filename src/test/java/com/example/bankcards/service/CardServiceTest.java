package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.response.BalanceResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.BlockRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.BlockRequestStatusEnum;
import com.example.bankcards.enums.CardStatusEnum;
import com.example.bankcards.exception.custom.CardNotFoundException;
import com.example.bankcards.exception.custom.UserNotFoundException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.BlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.impl.CardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BlockRequestRepository blockRequestRepository;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardServiceImpl cardService;

    private Card card;
    private CardResponse cardResponse;
    private User user;
    private String cardNumber;
    private UUID userId;
    private YearMonth expirationDate;

    @BeforeEach
    void setUp() {
        cardNumber = "1234567890123456";
        userId = UUID.randomUUID();
        expirationDate = YearMonth.now().plusYears(1);
        user = User.builder()
                .userId(userId)
                .login("testuser")
                .passwordHash("password123")
                .build();
        card = Card.builder()
                .number(cardNumber)
                .expirationDate(expirationDate)
                .status(CardStatusEnum.ACTIVE)
                .balance(BigDecimal.valueOf(100.00))
                .owner(user)
                .build();
        cardResponse = CardResponse.builder()
                .number(cardNumber)
                .expirationDate(expirationDate)
                .status(CardStatusEnum.ACTIVE.name())
                .balance(BigDecimal.valueOf(100.00))
                .ownerId(userId)
                .build();
    }

    @Test
    void getAll_ShouldReturnCardList() {
        // given
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Card> cardPage = new PageImpl<>(Collections.singletonList(card));
        when(cardRepository.findAll(pageRequest)).thenReturn(cardPage);
        when(cardMapper.toResponse(card)).thenReturn(cardResponse);

        // when
        List<CardResponse> result = cardService.getAll(page, size);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(cardResponse);
        verify(cardRepository).findAll(pageRequest);
        verify(cardMapper).toResponse(card);
    }

    @Test
    void getByNumber_ShouldReturnCard_WhenCardExists() {
        // given
        when(cardRepository.findByNumber(cardNumber)).thenReturn(Optional.of(card));
        when(cardMapper.toResponse(card)).thenReturn(cardResponse);

        // when
        CardResponse result = cardService.getByNumber(cardNumber);

        // then
        assertThat(result).isEqualTo(cardResponse);
        verify(cardRepository).findByNumber(cardNumber);
        verify(cardMapper).toResponse(card);
    }

    @Test
    void getByNumber_ShouldThrowException_WhenCardDoesNotExist() {
        // given
        when(cardRepository.findByNumber(cardNumber)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> cardService.getByNumber(cardNumber))
                .isInstanceOf(CardNotFoundException.class)
                .hasMessage("Card not found with number: " + cardNumber);
        verify(cardRepository).findByNumber(cardNumber);
        verify(cardMapper, never()).toResponse(any());
    }

    @Test
    void create_ShouldCreateCard_WhenUserExists() {
        // given
        CardCreateRequest request = new CardCreateRequest(cardNumber, expirationDate, userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cardMapper.toEntity(request, user)).thenReturn(card);
        when(cardRepository.save(card)).thenReturn(card);

        // when
        cardService.create(request);

        // then
        verify(userRepository).findById(userId);
        verify(cardMapper).toEntity(request, user);
        verify(cardRepository).save(card);
    }

    @Test
    void create_ShouldThrowException_WhenUserDoesNotExist() {
        // given
        CardCreateRequest request = new CardCreateRequest(cardNumber, expirationDate, userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> cardService.create(request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found with id: " + userId);
        verify(userRepository).findById(userId);
        verify(cardMapper, never()).toEntity(any(), any());
        verify(cardRepository, never()).save(any());
    }

    @Test
    void block_ShouldBlockCard_WhenCardExists() {
        // given
        when(cardRepository.findByNumber(cardNumber)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);

        // when
        cardService.block(cardNumber);

        // then
        assertThat(card.getStatus()).isEqualTo(CardStatusEnum.BLOCKED);
        verify(cardRepository).findByNumber(cardNumber);
        verify(cardRepository).save(card);
    }

    @Test
    void block_ShouldThrowException_WhenCardDoesNotExist() {
        // given
        when(cardRepository.findByNumber(cardNumber)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> cardService.block(cardNumber))
                .isInstanceOf(CardNotFoundException.class)
                .hasMessage("Card not found with number: " + cardNumber);
        verify(cardRepository).findByNumber(cardNumber);
        verify(cardRepository, never()).save(any());
    }

    @Test
    void activate_ShouldActivateCard_WhenCardExistsAndNotExpired() {
        // given
        when(cardRepository.findByNumber(cardNumber)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);

        // when
        cardService.activate(cardNumber);

        // then
        assertThat(card.getStatus()).isEqualTo(CardStatusEnum.ACTIVE);
        verify(cardRepository).findByNumber(cardNumber);
        verify(cardRepository).save(card);
    }

    @Test
    void activate_ShouldSetExpired_WhenCardExistsAndExpired() {
        // given
        YearMonth expiredDate = YearMonth.now().minusMonths(1);
        card.setExpirationDate(expiredDate);
        when(cardRepository.findByNumber(cardNumber)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);

        // when
        cardService.activate(cardNumber);

        // then
        assertThat(card.getStatus()).isEqualTo(CardStatusEnum.EXPIRED);
        verify(cardRepository).findByNumber(cardNumber);
        verify(cardRepository).save(card);
    }

    @Test
    void activate_ShouldThrowException_WhenCardDoesNotExist() {
        // given
        when(cardRepository.findByNumber(cardNumber)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> cardService.activate(cardNumber))
                .isInstanceOf(CardNotFoundException.class)
                .hasMessage("Card not found with number: " + cardNumber);
        verify(cardRepository).findByNumber(cardNumber);
        verify(cardRepository, never()).save(any());
    }

    @Test
    void delete_ShouldDeleteCard_WhenCardExists() {
        // given
        when(cardRepository.existsByNumber(cardNumber)).thenReturn(true);
        doNothing().when(cardRepository).deleteById(cardNumber);

        // when
        cardService.delete(cardNumber);

        // then
        verify(cardRepository).existsByNumber(cardNumber);
        verify(cardRepository).deleteById(cardNumber);
    }

    @Test
    void delete_ShouldThrowException_WhenCardDoesNotExist() {
        // given
        when(cardRepository.existsByNumber(cardNumber)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> cardService.delete(cardNumber))
                .isInstanceOf(CardNotFoundException.class)
                .hasMessage("Card not found with number: " + cardNumber);
        verify(cardRepository).existsByNumber(cardNumber);
        verify(cardRepository, never()).deleteById(any());
    }

    @Test
    void blockRequest_ShouldCreateBlockRequest_WhenCardExists() {
        // given
        when(cardRepository.findByNumber(cardNumber)).thenReturn(Optional.of(card));
        BlockRequest blockRequest = BlockRequest.builder()
                .cardNumber(cardNumber)
                .requester(user)
                .status(BlockRequestStatusEnum.PENDING)
                .requestDate(LocalDateTime.now())
                .reason("User requested card block")
                .build();
        when(blockRequestRepository.save(any(BlockRequest.class))).thenReturn(blockRequest);

        // when
        cardService.blockRequest(cardNumber);

        // then
        verify(cardRepository).findByNumber(cardNumber);
        verify(blockRequestRepository).save(any(BlockRequest.class));
    }

    @Test
    void blockRequest_ShouldThrowException_WhenCardDoesNotExist() {
        // given
        when(cardRepository.findByNumber(cardNumber)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> cardService.blockRequest(cardNumber))
                .isInstanceOf(CardNotFoundException.class)
                .hasMessage("Card not found with number: " + cardNumber);
        verify(cardRepository).findByNumber(cardNumber);
        verify(blockRequestRepository, never()).save(any());
    }

    @Test
    void getBalance_ShouldReturnBalance_WhenCardExists() {
        // given
        BalanceResponse balanceResponse = BalanceResponse.builder()
                .cardNumber(cardNumber)
                .balance(card.getBalance())
                .build();
        when(cardRepository.findByNumber(cardNumber)).thenReturn(Optional.of(card));
        when(cardMapper.toBalanceResponse(card)).thenReturn(balanceResponse);

        // when
        BalanceResponse result = cardService.getBalance(cardNumber);

        // then
        assertThat(result).isEqualTo(balanceResponse);
        verify(cardRepository).findByNumber(cardNumber);
        verify(cardMapper).toBalanceResponse(card);
    }

    @Test
    void getBalance_ShouldThrowException_WhenCardDoesNotExist() {
        // given
        when(cardRepository.findByNumber(cardNumber)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> cardService.getBalance(cardNumber))
                .isInstanceOf(CardNotFoundException.class)
                .hasMessage("Card not found with number: " + cardNumber);
        verify(cardRepository).findByNumber(cardNumber);
        verify(cardMapper, never()).toBalanceResponse(any());
    }

    @Test
    void updateExpiredCards_ShouldUpdateExpiredCards() {
        // given
        YearMonth now = YearMonth.now();
        List<Card> expiredCards = Collections.singletonList(card);
        when(cardRepository.findExpiredCards(now)).thenReturn(expiredCards);
        when(cardRepository.save(card)).thenReturn(card);

        // when
        cardService.updateExpiredCards();

        // then
        assertThat(card.getStatus()).isEqualTo(CardStatusEnum.EXPIRED);
        verify(cardRepository).findExpiredCards(now);
        verify(cardRepository).save(card);
    }
}