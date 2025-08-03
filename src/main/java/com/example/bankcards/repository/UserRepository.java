package com.example.bankcards.repository;

import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    Optional<User> findByLogin(String login);
    
    boolean existsByLogin(String login);
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles LEFT JOIN FETCH u.cards LEFT JOIN FETCH u.blockRequests")
    Page<User> findAllWithRoles(Pageable pageable);
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles LEFT JOIN FETCH u.cards LEFT JOIN FETCH u.blockRequests WHERE u.userId = :userId")
    Optional<User> findByIdWithRoles(@Param("userId") UUID userId);
} 