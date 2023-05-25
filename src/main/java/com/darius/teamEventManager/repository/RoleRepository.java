package com.darius.teamEventManager.repository;

import com.darius.teamEventManager.entity.Role;
import com.darius.teamEventManager.entity.UserRoleTypes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(UserRoleTypes name);

    Boolean existsByName(UserRoleTypes name);
}
