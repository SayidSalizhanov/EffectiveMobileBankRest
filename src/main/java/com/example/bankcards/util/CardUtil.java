package com.example.bankcards.util;

import lombok.experimental.UtilityClass;

import java.time.YearMonth;

@UtilityClass
public class CardUtil {
    public static String maskCardNumber(String cardNumber) {
        if (cardNumber.length() != 16) {
            throw new RuntimeException("Card number must be equals 16");
        }
        return "************" + cardNumber.substring(cardNumber.length() - 4);
    }

    public static boolean isExpired(YearMonth expirationDate) {
        return expirationDate.isBefore(YearMonth.now());
    }

    public static YearMonth getCurrentYearMonth() {
        return YearMonth.now();
    }

    public static boolean isValidExpirationDate(YearMonth yearMonth) {
        return yearMonth != null && !yearMonth.isBefore(YearMonth.now());
    }
}
