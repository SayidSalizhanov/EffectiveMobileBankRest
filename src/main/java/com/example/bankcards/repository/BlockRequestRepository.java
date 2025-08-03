package com.example.bankcards.repository;

import com.example.bankcards.entity.BlockRequest;
import com.example.bankcards.enums.BlockRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BlockRequestRepository extends JpaRepository<BlockRequest, UUID> {
    
    @Query("SELECT br FROM BlockRequest br LEFT JOIN FETCH br.requester ORDER BY br.requestDate DESC")
    Page<BlockRequest> findAllWithRequester(Pageable pageable);
    
    @Query("SELECT br FROM BlockRequest br LEFT JOIN FETCH br.requester WHERE br.status = :status ORDER BY br.requestDate DESC")
    Page<BlockRequest> findByStatus(@Param("status") BlockRequestStatus status, Pageable pageable);
}