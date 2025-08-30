package com.example.bankcards.service;

import com.example.bankcards.dto.response.BlockRequestResponse;

import java.util.List;
import java.util.UUID;

/**
 * Сервис управления запросами на блокировку карт.
 */
public interface BlockRequestService {
    /**
     * Возвращает все запросы на блокировку с пагинацией.
     * @param page номер страницы
     * @param size размер страницы
     * @return список dto запросов на блокировку (response)
     */
    List<BlockRequestResponse> getAll(Integer page, Integer size);

    /**
     * Возвращает запросы по статусу с пагинацией.
     * @param status статус запроса на блокировку
     * @param page номер страницы
     * @param size размер страницы
     * @return список dto запросов на блокировку (response)
     */
    List<BlockRequestResponse> getByStatus(String status, Integer page, Integer size);

    /**
     * Одобряет запрос на блокировку.
     * @param requestId id запроса на блокировку
     */
    void approve(UUID requestId);

    /**
     * Отклоняет запрос на блокировку.
     * @param requestId id запроса на блокировку
     */
    void reject(UUID requestId);
} 