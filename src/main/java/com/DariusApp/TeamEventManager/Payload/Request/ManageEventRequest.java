package com.DariusApp.TeamEventManager.Payload.Request;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
public class ManageEventRequest {
    private Integer userId;
    private Integer eventId;

    public ManageEventRequest(Integer userId, Integer eventId) {
        this.userId = userId;
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManageEventRequest that = (ManageEventRequest) o;
        return Objects.equals(userId, that.userId) && Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, eventId);
    }

    @Override
    public String toString() {
        return "ManageEventRequest{" +
                "userId=" + userId +
                ", eventId=" + eventId +
                '}';
    }
}
