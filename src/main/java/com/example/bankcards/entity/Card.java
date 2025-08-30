package com.example.bankcards.entity;

import com.example.bankcards.enums.CardStatusEnum;
import com.example.bankcards.util.YearMonthConverter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Objects;

/**
 * Сущность банковской карты с данными о владельце, балансе, статусе и сроке действия.
 * Первичный ключ — номер карты.
 */
@Entity
@Table(name = "cards")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Card {
    @Id
    @Column(length = 16, nullable = false)
    private String number;

    @Convert(converter = YearMonthConverter.class)
    @Column(name = "expiration_date", nullable = false)
    private YearMonth expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatusEnum status;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id",
            nullable = false
    )
    private User owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(number, card.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
