package com.darius.teamEventManager.payload.request.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventIdRequest {
    private Integer eventId;

    public EventIdRequest() {
    }

    public EventIdRequest(Integer eventId) {
        this.eventId = eventId;
    }

    @Override
    public String toString() {
        return "EventIdRequest{" +
                "eventId=" + eventId +
                '}';
    }
}
