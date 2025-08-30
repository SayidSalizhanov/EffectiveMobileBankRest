package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardCreateRequest;
import com.example.bankcards.dto.response.BalanceResponse;
import com.example.bankcards.dto.response.CardResponse;

import java.util.List;

/**
 * Сервис работы с банковскими картами.
 */
public interface CardService {
    /**
     * Возвращает список карт с пагинацией.
     * @param page номер страницы
     * @param size размер страницы
     * @return список dto карт (response)
     */
    List<CardResponse> getAll(Integer page, Integer size);

    /**
     * Возвращает карту по номеру.
     * @param number номер карты
     * @return dto карты (response)
     */
    CardResponse getByNumber(String number);

    /**
     * Создает новую карту.
     * @param request dto карты (request)
     */
    void create(CardCreateRequest request);

    /**
     * Блокирует карту.
     * @param number номер карты
     */
    void block(String number);

    /**
     * Активирует карту.
     * @param number номер карты
     */
    void activate(String number);

    /**
     * Удаляет карту.
     * @param number номер карты
     */
    void delete(String number);

    /**
     * Создает запрос на блокировку карты.
     * @param number номер карты
     */
    void blockRequest(String number);

    /**
     * Возвращает текущий баланс карты.
     * @param number номер карты
     * @return dto баланса карты (response)
     */
    BalanceResponse getBalance(String number);

    /**
     * Обновляет статусы просроченных карт на EXPIRED.
     */
    void updateExpiredCards();
}
