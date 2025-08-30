package com.example.bankcards.mapper;

import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.util.CardUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Маппер для преобразования пользователей в DTO и обратно.
 */
@Component
@RequiredArgsConstructor
public class UserMapper {

	private final PasswordEncoder passwordEncoder;

	/**
	 * Преобразует сущность User в DTO ответа с маскировкой номеров карт.
	 * @param user пользователь
	 * @return dto пользователя (response)
	 */
	public UserResponse toResponse(User user) {
		return UserResponse.builder()
				.login(user.getLogin())
				.cardsNumbers(user.getCards().stream()
						.map(card -> CardUtil.maskCardNumber(card.getNumber()))
						.collect(Collectors.toList()))
				.rolesNames(user.getRoles().stream()
						.map(role -> role.getName().name())
						.collect(Collectors.toList()))
				.blockRequestStatuses(user.getBlockRequests().stream()
						.map(request -> request.getStatus().name())
						.collect(Collectors.toList()))
				.build();
	}
	
	/**
	 * Создает сущность User из запроса на создание, хэшируя пароль.
	 * @param request dto создания пользователя (request)
	 * @return сущность пользователя
	 */
	public User toEntity(UserCreateRequest request) {
		return User.builder()
				.login(request.login())
				.passwordHash(passwordEncoder.encode(
						request.password()
				))
				.build();
	}
} 