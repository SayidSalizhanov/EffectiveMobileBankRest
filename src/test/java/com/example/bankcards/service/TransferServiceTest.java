package com.example.bankcards.service;

import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.CardStatusEnum;
import com.example.bankcards.exception.custom.CardNotFoundException;
import com.example.bankcards.exception.custom.TransferException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.impl.TransferServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private TransferServiceImpl transferService;

    @Test
    void transfer_ShouldTransferAmount_WhenConditionsAreMet() {
        // given
        User user = User.builder().userId(UUID.randomUUID()).build();
        Card fromCard = Card.builder()
                .number("1234567890123456")
                .status(CardStatusEnum.ACTIVE)
                .balance(BigDecimal.valueOf(200.00))
                .owner(user)
                .build();
        Card toCard = Card.builder()
                .number("6543210987654321")
                .status(CardStatusEnum.ACTIVE)
                .balance(BigDecimal.valueOf(100.00))
                .owner(user)
                .build();
        TransferRequest request = new TransferRequest("1234567890123456", "6543210987654321", BigDecimal.valueOf(50.00));

        when(cardRepository.findByNumber("1234567890123456")).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByNumber("6543210987654321")).thenReturn(Optional.of(toCard));

        // when
        transferService.transfer(request);

        // then
        assertThat(fromCard.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(150.00));
        assertThat(toCard.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(150.00));
        verify(cardRepository).save(fromCard);
        verify(cardRepository).save(toCard);
    }

    @Test
    void transfer_ShouldThrowException_WhenCardsHaveDifferentOwners() {
        // given
        User user1 = User.builder().userId(UUID.randomUUID()).build();
        User user2 = User.builder().userId(UUID.randomUUID()).build();
        Card fromCard = Card.builder().number("1234567890123456").owner(user1).status(CardStatusEnum.ACTIVE).balance(BigDecimal.valueOf(200.00)).build();
        Card toCard = Card.builder().number("6543210987654321").owner(user2).status(CardStatusEnum.ACTIVE).build();
        TransferRequest request = new TransferRequest("1234567890123456", "6543210987654321", BigDecimal.valueOf(50.00));

        when(cardRepository.findByNumber("1234567890123456")).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByNumber("6543210987654321")).thenReturn(Optional.of(toCard));

        // when & then
        assertThatThrownBy(() -> transferService.transfer(request))
                .isInstanceOf(TransferException.class)
                .hasMessage("Cards must belong to the same user");
        verify(cardRepository, never()).save(any());
    }

    @Test
    void transfer_ShouldThrowException_WhenSourceCardIsNotActive() {
        // given
        User user = User.builder().userId(UUID.randomUUID()).build();
        Card fromCard = Card.builder().number("1234567890123456").status(CardStatusEnum.BLOCKED).owner(user).build();
        Card toCard = Card.builder().number("6543210987654321").status(CardStatusEnum.ACTIVE).owner(user).build();
        TransferRequest request = new TransferRequest("1234567890123456", "6543210987654321", BigDecimal.valueOf(50.00));

        when(cardRepository.findByNumber("1234567890123456")).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByNumber("6543210987654321")).thenReturn(Optional.of(toCard));

        // when & then
        assertThatThrownBy(() -> transferService.transfer(request))
                .isInstanceOf(TransferException.class)
                .hasMessage("Source card is not active. Status: BLOCKED");
        verify(cardRepository, never()).save(any());
    }

    @Test
    void transfer_ShouldThrowException_WhenDestinationCardIsNotActive() {
        // given
        User user = User.builder().userId(UUID.randomUUID()).build();
        Card fromCard = Card.builder().number("1234567890123456").status(CardStatusEnum.ACTIVE).owner(user).balance(BigDecimal.valueOf(200.00)).build();
        Card toCard = Card.builder().number("6543210987654321").status(CardStatusEnum.BLOCKED).owner(user).build();
        TransferRequest request = new TransferRequest("1234567890123456", "6543210987654321", BigDecimal.valueOf(50.00));

        when(cardRepository.findByNumber("1234567890123456")).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByNumber("6543210987654321")).thenReturn(Optional.of(toCard));

        // when & then
        assertThatThrownBy(() -> transferService.transfer(request))
                .isInstanceOf(TransferException.class)
                .hasMessage("Destination card is not active. Status: BLOCKED");
        verify(cardRepository, never()).save(any());
    }

    @Test
    void transfer_ShouldThrowException_WhenInsufficientFunds() {
        // given
        User user = User.builder().userId(UUID.randomUUID()).build();
        Card fromCard = Card.builder().number("1234567890123456").status(CardStatusEnum.ACTIVE).balance(BigDecimal.valueOf(50.00)).owner(user).build();
        Card toCard = Card.builder().number("6543210987654321").status(CardStatusEnum.ACTIVE).balance(BigDecimal.valueOf(100.00)).owner(user).build();
        TransferRequest request = new TransferRequest("1234567890123456", "6543210987654321", BigDecimal.valueOf(100.00));

        when(cardRepository.findByNumber("1234567890123456")).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByNumber("6543210987654321")).thenReturn(Optional.of(toCard));

        // when & then
        assertThatThrownBy(() -> transferService.transfer(request))
                .isInstanceOf(TransferException.class)
                .hasMessage("Insufficient funds on source card");
        verify(cardRepository, never()).save(any());
    }

    @Test
    void transfer_ShouldThrowException_WhenSourceCardNotFound() {
        // given
        TransferRequest request = new TransferRequest("1234567890123456", "6543210987654321", BigDecimal.valueOf(50.00));
        when(cardRepository.findByNumber("1234567890123456")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> transferService.transfer(request))
                .isInstanceOf(CardNotFoundException.class)
                .hasMessage("Source card not found: 1234567890123456");
        verify(cardRepository, never()).save(any());
    }

    @Test
    void transfer_ShouldThrowException_WhenDestinationCardNotFound() {
        // given
        User user = User.builder().userId(UUID.randomUUID()).build();
        Card fromCard = Card.builder().number("1234567890123456").status(CardStatusEnum.ACTIVE).balance(BigDecimal.valueOf(200.00)).owner(user).build();
        TransferRequest request = new TransferRequest("1234567890123456", "6543210987654321", BigDecimal.valueOf(50.00));

        when(cardRepository.findByNumber("1234567890123456")).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByNumber("6543210987654321")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> transferService.transfer(request))
                .isInstanceOf(CardNotFoundException.class)
                .hasMessage("Destination card not found: 6543210987654321");
        verify(cardRepository, never()).save(any());
    }

    @Test
    void transfer_ShouldThrowException_WhenAmountIsZero() {
        // given
        User user = User.builder().userId(UUID.randomUUID()).build();
        Card fromCard = Card.builder()
                .number("1234567890123456")
                .status(CardStatusEnum.ACTIVE)
                .balance(BigDecimal.valueOf(200.00))
                .owner(user)
                .build();
        Card toCard = Card.builder()
                .number("6543210987654321")
                .status(CardStatusEnum.ACTIVE)
                .balance(BigDecimal.valueOf(100.00))
                .owner(user)
                .build();
        TransferRequest request = new TransferRequest("1234567890123456", "6543210987654321", BigDecimal.ZERO);

        when(cardRepository.findByNumber("1234567890123456")).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByNumber("6543210987654321")).thenReturn(Optional.of(toCard));

        // when & then
        assertThatThrownBy(() -> transferService.transfer(request))
                .isInstanceOf(TransferException.class)
                .hasMessage("Transfer amount must be positive");
        verify(cardRepository, never()).save(any());
    }

    @Test
    void transfer_ShouldThrowException_WhenAmountIsNegative() {
        // given
        User user = User.builder().userId(UUID.randomUUID()).build();
        Card fromCard = Card.builder()
                .number("1234567890123456")
                .status(CardStatusEnum.ACTIVE)
                .balance(BigDecimal.valueOf(200.00))
                .owner(user)
                .build();
        Card toCard = Card.builder()
                .number("6543210987654321")
                .status(CardStatusEnum.ACTIVE)
                .balance(BigDecimal.valueOf(100.00))
                .owner(user)
                .build();
        TransferRequest request = new TransferRequest("1234567890123456", "6543210987654321", BigDecimal.valueOf(-50.00));

        when(cardRepository.findByNumber("1234567890123456")).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByNumber("6543210987654321")).thenReturn(Optional.of(toCard));

        // when & then
        assertThatThrownBy(() -> transferService.transfer(request))
                .isInstanceOf(TransferException.class)
                .hasMessage("Transfer amount must be positive");
        verify(cardRepository, never()).save(any());
    }
}