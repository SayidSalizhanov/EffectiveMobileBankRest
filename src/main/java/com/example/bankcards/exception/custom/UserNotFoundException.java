package com.example.bankcards.exception.custom;

import com.example.bankcards.exception.base.ServiceException;
import org.springframework.http.HttpStatus;

/**
 * Исключение: пользователь не найден.
 */
public class UserNotFoundException extends ServiceException {
    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
