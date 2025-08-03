package com.example.bankcards.entity;

import com.example.bankcards.enums.CardStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;

@Entity
@Table(name = "cards")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Card {
    @Id
    @Column(length = 16, nullable = false)
    private String number;

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
}
