package com.example.bankcards.mapper;

import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.request.UserUpdateRequest;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.util.CardUtil;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {
    
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
    
    public User toEntity(UserCreateRequest request) {
        return User.builder()
                .login(request.login())
                .passwordHash(request.password()) // todo хеширование
                .build();
    }
    
    public void updateEntity(User user, UserUpdateRequest request) {
        if (request.newPassword() != null && request.oldPassword().equals(user.getPasswordHash())) { // todo хеширование
            user.setPasswordHash(request.newPassword()); // todo хеширование
        }
    }
} 