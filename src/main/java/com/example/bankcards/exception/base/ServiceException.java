package com.example.bankcards.exception.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Базовое исключение уровня сервиса с возможностью указать HTTP статус ответа.
 */
@Getter
public class ServiceException extends RuntimeException {
    private final HttpStatus status;

    public ServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
