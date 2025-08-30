package com.example.bankcards.service;

import com.example.bankcards.dto.request.UserCreateRequest;
import com.example.bankcards.dto.request.UserPasswordUpdateRequest;
import com.example.bankcards.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

/**
 * Сервис управления пользователями.
 */
public interface UserService {
    /**
     * Возвращает список пользователей с пагинацией.
     * @param page номер страницы
     * @param size размер страницы
     * @return список dto пользователей (response)
     */
    List<UserResponse> getAll(Integer page, Integer size);

    /**
     * Возвращает пользователя по идентификатору.
     * @param userId id пользователя
     * @return dto пользователя (response)
     */
    UserResponse getById(UUID userId);

    /**
     * Создает нового пользователя и возвращает его идентификатор.
     * @param request dto создания пользователя (request)
     * @return id созданного пользователя
     */
    UUID create(UserCreateRequest request);

    /**
     * Обновляет пароль пользователя.
     * @param userId id пользователя
     * @param request dto изменения пароля пользователя (request)
     */
    void updatePassword(UUID userId, UserPasswordUpdateRequest request);

    /**
     * Удаляет пользователя.
     * @param userId id пользователя
     */
    void delete(UUID userId);
}
