package com.example.bankcards.exception.custom;

import com.example.bankcards.exception.base.ServiceException;
import org.springframework.http.HttpStatus;

/**
 * Исключение: банковская карта не найдена.
 */
public class CardNotFoundException extends ServiceException {
    public CardNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
