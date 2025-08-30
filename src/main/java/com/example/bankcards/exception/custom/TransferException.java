package com.example.bankcards.exception.custom;

import com.example.bankcards.exception.base.ServiceException;
import org.springframework.http.HttpStatus;

/**
 * Исключение бизнес-логики перевода средств.
 */
public class TransferException extends ServiceException {
    public TransferException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
