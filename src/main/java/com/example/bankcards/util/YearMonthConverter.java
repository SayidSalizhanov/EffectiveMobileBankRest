package com.example.bankcards.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

/**
 * Конвертер JPA для хранения YearMonth как строки в БД (yyyy-MM) и обратно.
 */
@Converter(autoApply = true)
public class YearMonthConverter implements AttributeConverter<YearMonth, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    /**
     * Преобразует YearMonth в строку для базы данных.
     * @param yearMonth месяц и год
     * @return месяц и год в формате строки
     */
    @Override
    public String convertToDatabaseColumn(YearMonth yearMonth) {
        if (yearMonth == null) {
            return null;
        }
        return yearMonth.format(FORMATTER);
    }

    /**
     * Преобразует строку из базы данных в YearMonth.
     * @param dbData месяц и год в формате строки из бд
     * @return месяц и год в формате YearMonth
     */
    @Override
    public YearMonth convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return YearMonth.parse(dbData, FORMATTER);
    }
}