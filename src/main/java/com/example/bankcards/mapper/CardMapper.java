package com.example.bankcards.mapper;

import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.response.BalanceResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.enums.CardStatusEnum;
import com.example.bankcards.util.CardUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Маппер для преобразования сущности Card в DTO и обратно.
 */
@Component
public class CardMapper {

	/**
	 * Преобразует сущность Card в DTO ответа.
	 * @param card карта
	 * @return dto карты (response)
	 */
	public CardResponse toResponse(Card card) {
		return CardResponse.builder()
				.number(CardUtil.maskCardNumber(card.getNumber()))
				.expirationDate(card.getExpirationDate())
				.status(card.getStatus().name())
				.balance(card.getBalance())
				.ownerId(card.getOwner().getUserId())
				.build();
	}
	
	/**
	 * Создает сущность Card на основе запроса и владельца.
	 * Новая карта по умолчанию активна и с нулевым балансом.
	 * @param request dto создания карты (request)
	 * @param owner владелец карты
	 * @return сущность карты
	 */
	public Card toEntity(CardCreateRequest request, User owner) {
		return Card.builder()
				.number(request.number())
				.expirationDate(request.expirationDate())
				.status(CardStatusEnum.ACTIVE)
				.balance(BigDecimal.ZERO)
				.owner(owner)
				.build();
	}
	
	/**
	 * Создает DTO баланса карты с маскировкой номера.
	 * @param card карта
	 * @return dto баланса (response)
	 */
	public BalanceResponse toBalanceResponse(Card card) {
		return BalanceResponse.builder()
				.cardNumber(CardUtil.maskCardNumber(card.getNumber()))
				.balance(card.getBalance())
				.build();
	}
} 