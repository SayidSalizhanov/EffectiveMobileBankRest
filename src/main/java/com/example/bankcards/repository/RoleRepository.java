package com.example.bankcards.repository;

import com.example.bankcards.entity.Role;
import com.example.bankcards.enums.RoleNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий ролей.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    /**
     * Поиск роли по названию.
     * @param name название
     * @return optional роли
     */
    Optional<Role> findByName(RoleNameEnum name);
}