package com.darius.teamEventManager.repository;

import com.darius.teamEventManager.entity.TEMUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TEMUserRepository extends JpaRepository<TEMUser, Integer> {
    Optional<TEMUser> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmailAddress(String emailAddress);
}
