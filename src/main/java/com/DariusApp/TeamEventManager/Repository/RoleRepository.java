package com.DariusApp.TeamEventManager.Repository;

import com.DariusApp.TeamEventManager.Entity.Role;
import com.DariusApp.TeamEventManager.Entity.UserRoleTypes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer>
{
    Optional<Role> findByName(UserRoleTypes name);
    Boolean existsByName(UserRoleTypes name);
}
