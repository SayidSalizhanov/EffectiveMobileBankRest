package com.example.bankcards.exception.handler;

import com.example.bankcards.dto.response.ExceptionMessage;
import com.example.bankcards.exception.base.ServiceException;
import com.example.bankcards.exception.custom.BadCredentialsException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // обработчик для исключений, когда не можем преобразовать значение аргумента метода контроллера в ожидаемый тип
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ExceptionMessage handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        return ExceptionMessage
                .builder()
                .exceptionName(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ExceptionMessage handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return ExceptionMessage
                .builder()
                .exceptionName(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .build();
    }

    // обработчик для исключений, когда происходит ошибка валидации методов или параметров контроллера
    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ExceptionMessage handleMethodValidationException(HandlerMethodValidationException exception) {
        Map<String, List<String>> errors = new HashMap<>();
        List<ParameterValidationResult> ex = exception.getParameterValidationResults();
        for (ParameterValidationResult result : ex) {
            String parameterName = result.getMethodParameter().getParameterName();
            errors.put(parameterName, result.getResolvableErrors().stream().map(MessageSourceResolvable::getDefaultMessage).toList());

        }
        return ExceptionMessage.builder()
                .message(exception.getMessage())
                .errors(errors)
                .exceptionName(exception.getClass().getSimpleName())
                .build();
    }

    // обработчик для исключений, когда не проходит валидация объекта DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ExceptionMessage handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, List<String>> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.computeIfAbsent(error.getField(), k -> new ArrayList<>())
                    .add(error.getDefaultMessage());
        });
        return ExceptionMessage.builder()
                .message(exception.getMessage())
                .errors(errors)
                .exceptionName(exception.getClass().getSimpleName())
                .build();
    }

    // обработчик для исключений, когда происходят на уровне сервиса
    @ExceptionHandler(ServiceException.class)
    public final ResponseEntity<ExceptionMessage> handleServiceException(ServiceException exception) {
        return ResponseEntity.status(exception.getStatus())
                .body(ExceptionMessage.builder()
                        .exceptionName(exception.getClass().getSimpleName())
                        .message(exception.getMessage())
                        .build()
                );
    }

    // обработчик для всех остальных исключений, которых не должно было быть
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ExceptionMessage onAllExceptions(Exception exception) {
        exception.printStackTrace();
        return ExceptionMessage.builder()
                .message(exception.getMessage())
                .exceptionName(exception.getClass().getSimpleName())
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public final ExceptionMessage handleBadCredentialsException(BadCredentialsException exception) {
        return ExceptionMessage.builder()
                .message(exception.getMessage())
                .exceptionName(exception.getClass().getSimpleName())
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public final ExceptionMessage handleAccessDeniedException(AccessDeniedException exception) {
        return ExceptionMessage.builder()
                .message("Access denied")
                .exceptionName(exception.getClass().getSimpleName())
                .build();
    }
} 