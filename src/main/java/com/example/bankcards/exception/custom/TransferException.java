package com.example.bankcards.exception.custom;

import com.example.bankcards.exception.ServiceException;
import org.springframework.http.HttpStatus;

public class TransferException extends ServiceException {
    public TransferException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
