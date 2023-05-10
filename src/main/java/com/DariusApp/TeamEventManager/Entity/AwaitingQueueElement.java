package com.DariusApp.TeamEventManager.Entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table(name = "awaiting")
public class AwaitingQueueElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "event_id")
    private Integer eventId;

    protected AwaitingQueueElement() {
    }

    public AwaitingQueueElement(Integer userId, Integer eventId) {
        this.userId = userId;
        this.eventId = eventId;
    }

    public AwaitingQueueElement(Integer id, Integer userId, Integer eventId) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
    }
}
