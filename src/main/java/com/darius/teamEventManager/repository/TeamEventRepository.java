package com.darius.teamEventManager.repository;

import com.darius.teamEventManager.entity.TEMUser;
import com.darius.teamEventManager.entity.TeamEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamEventRepository extends JpaRepository<TeamEvent, Integer> {
    List<TeamEvent> findAllByTemUsers(TEMUser user);

    List<TeamEvent> findAllByIsPublicTrueAndTemUsersNotContaining(TEMUser user);

}
