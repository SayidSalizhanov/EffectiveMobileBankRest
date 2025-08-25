package com.example.bankcards.entity;

import com.example.bankcards.enums.CardStatusEnum;
import com.example.bankcards.util.YearMonthConverter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Objects;

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
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Card card = (Card) object;
        return Objects.equals(number, card.number)
                && Objects.equals(expirationDate, card.expirationDate)
                && status == card.status
                && Objects.equals(balance, card.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, expirationDate, status, balance);
    }
}
