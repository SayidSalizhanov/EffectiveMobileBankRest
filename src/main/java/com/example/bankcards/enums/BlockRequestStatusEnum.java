package com.example.bankcards.enums;

/**
 * Статусы запроса на блокировку карты.
 */
public enum BlockRequestStatusEnum {
    /** Запрос создан и ожидает обработки. */
    PENDING,
    /** Запрос одобрен администратором. */
    APPROVED,
    /** Запрос отклонен администратором. */
    REJECTED,
    /** Запрос обработан. */
    PROCESSED
}