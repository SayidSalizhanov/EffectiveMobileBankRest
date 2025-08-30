package com.example.bankcards.exception.custom;

import com.example.bankcards.exception.base.ServiceException;
import org.springframework.http.HttpStatus;

/**
 * Исключение: запрос на блокировку не найден.
 */
public class BlockRequestNotFoundException extends ServiceException {
    public BlockRequestNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
