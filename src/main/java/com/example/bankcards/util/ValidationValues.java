package com.example.bankcards.util;

import lombok.experimental.UtilityClass;

/**
 * Константы для валидации и значений по умолчанию (например, пагинация).
 */
@UtilityClass
public class ValidationValues {
    /** Значения по умолчанию для параметров пагинации. */
    public final static class Page {
        public final static String LIMIT_DEFAULT_VALUE = "10";
        public final static String OFFSET_DEFAULT_VALUE = "0";
    }
}
