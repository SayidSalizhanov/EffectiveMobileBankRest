package com.example.bankcards.repository;

import com.example.bankcards.entity.BlockRequest;
import com.example.bankcards.enums.BlockRequestStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий запросов на блокировку карт.
 */
@Repository
public interface BlockRequestRepository extends JpaRepository<BlockRequest, UUID> {
    
    /**
     * Возвращает запросы с подгрузкой инициатора, по убыванию даты.
     * @param pageable атрибуты пагинации
     * @return страница запросов на блокировку
     */
    @Query("SELECT br FROM BlockRequest br JOIN FETCH br.requester ORDER BY br.requestDate DESC")
    Page<BlockRequest> findAllWithRequester(Pageable pageable);
    
    /**
     * Возвращает запросы по статусу с подгрузкой инициатора, по убыванию даты
     * @param status статус запроса на блокировку
     * @param pageable атрибуты пагинации
     * @return страница запросов на блокировку
     */
    @Query("SELECT br FROM BlockRequest br JOIN FETCH br.requester WHERE br.status = :status ORDER BY br.requestDate DESC")
    Page<BlockRequest> findByStatusWithRequester(@Param("status") BlockRequestStatusEnum status, Pageable pageable);
}