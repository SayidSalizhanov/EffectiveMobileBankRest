package com.example.bankcards.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Логирует вызовы методов сервисов.
 * Показывает название метода, параметры, результат и время выполнения.
 */
@Slf4j
@Aspect
@Component
@ConditionalOnProperty(name = "aspect.service.logging", havingValue = "true")
public class ServiceLoggingAspect {

    /**
     * Логирует вызовы методов сервисов.
     * @param joinPoint информация о вызываемом методе
     * @return результат выполнения метода
     * @throws Throwable если произошла ошибка
     */
    @Around("execution(* com.example.bankcards.service..*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.info("Вызов метода: {}.{}() с параметрами: {}", className, methodName, joinPoint.getArgs());

        long startTime = System.currentTimeMillis();
        Object result;

        try {
            result = joinPoint.proceed();

            long elapsedTime = System.currentTimeMillis() - startTime;
            log.info("Метод: {}.{}() выполнен за {} мс. Результат: {}",
                    className, methodName, elapsedTime, result);

            return result;
        } catch (Exception e) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.error("Ошибка в методе: {}.{}() за {} мс. Причина: {}",
                    className, methodName, elapsedTime, e.getMessage());
            throw e;
        }
    }
}