package com.example.bankcards.exception.custom;

/**
 * Исключение: неверные учетные данные при аутентификации.
 */
public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException(String message) {
        super(message);
    }
}