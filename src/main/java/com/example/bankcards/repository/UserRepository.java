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

/**
 * Репозиторий пользователей.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Проверяет существование пользователя по логину.
     * @param login логин
     * @return логический тип (true/false)
     */
    boolean existsByLogin(String login);

    /**
     * Находит пользователя по логину.
     * @param login логин
     * @return optional пользователя
     */
    Optional<User> findByLogin(String login);
} 