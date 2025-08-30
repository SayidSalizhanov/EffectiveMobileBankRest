package com.example.bankcards.exception.custom;

import com.example.bankcards.exception.base.ServiceException;
import org.springframework.http.HttpStatus;

/**
 * Исключение: роль не найдена.
 */
public class RoleNotFoundException extends ServiceException {
    public RoleNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
