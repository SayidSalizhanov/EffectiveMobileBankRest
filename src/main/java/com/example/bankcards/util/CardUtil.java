package com.example.bankcards.util;

import lombok.experimental.UtilityClass;

import java.time.YearMonth;

/**
 * Утилиты для работы с банковскими картами: маскировка номера и проверка срока действия.
 */
@UtilityClass
public class CardUtil {
    /**
     * Маскирует номер карты, оставляя последние 4 цифры.
     * @param cardNumber номер карты
     * @return замаскированный номер карты
     */
    public static String maskCardNumber(String cardNumber) {
        if (cardNumber.length() != 16) {
            throw new RuntimeException("Card number must be equals 16");
        }
        return "************" + cardNumber.substring(cardNumber.length() - 4);
    }

    /**
     * Возвращает true, если срок действия карты истек на текущий момент.
     * @param expirationDate месяц и год просрочки карты
     * @return логический тип (true/false)
     */
    public static boolean isExpired(YearMonth expirationDate) {
        return expirationDate.isBefore(YearMonth.now());
    }
}
