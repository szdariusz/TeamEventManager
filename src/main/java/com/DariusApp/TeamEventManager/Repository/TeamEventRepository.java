package com.DariusApp.TeamEventManager.Repository;

import com.DariusApp.TeamEventManager.Entity.TEMUser;
import com.DariusApp.TeamEventManager.Entity.TeamEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TeamEventRepository extends JpaRepository<TeamEvent, Integer> {
    List<TeamEvent> findAllByTemUsers (TEMUser user);
    List<TeamEvent> findAllByIsPublicTrueAndTemUsersNotContaining(TEMUser user);

}
