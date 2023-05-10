package com.DariusApp.TeamEventManager.Repository;

import com.DariusApp.TeamEventManager.Entity.AwaitingQueueElement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface AwaitingQueueRepository extends JpaRepository<AwaitingQueueElement,Integer> {
    public Set<AwaitingQueueElement> findAllByEventId(Integer eventId);
    public Optional<AwaitingQueueElement> findByUserIdAndEventId(Integer userId, Integer eventId);
    public Boolean existsByUserIdAndEventId(Integer userId, Integer eventId);
}
