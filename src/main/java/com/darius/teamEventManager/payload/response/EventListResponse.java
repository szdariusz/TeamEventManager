package com.darius.teamEventManager.payload.response;

import lombok.Getter;

import java.util.Objects;

@Getter
public class EventListResponse {

    private Integer id;
    private String name;

    public EventListResponse(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventListResponse that = (EventListResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "EventListResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
