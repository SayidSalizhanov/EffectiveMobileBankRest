package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.enums.CardStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для доступа к сущностям Card.
 */
@Repository
public interface CardRepository extends JpaRepository<Card, String> {
    
    /**
     * Находит карту по номеру.
     * @param number номер карты
     * @return optional карты
     */
    Optional<Card> findByNumber(String number);

    /**
     * Возвращает список карт с истекшим сроком действия на указанную дату.
     * Включает fetch владельца.
     * @param yearMonth указанный срок действия
     * @return список карт
     */
    @Query("SELECT c FROM Card c LEFT JOIN FETCH c.owner WHERE c.expirationDate < :yearMonth")
    List<Card> findExpiredCards(@Param("yearMonth") YearMonth yearMonth);

    /**
     * Проверяет существование карты по номеру.
     * @param number номер карты
     * @return логический тип (true/false)
     */
    boolean existsByNumber(String number);
}