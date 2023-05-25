package com.darius.teamEventManager.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class EventDetailResponse {
    private String name;
    private String description;
    private String location;
    private Boolean isPublic;
    private Integer creator;


    public EventDetailResponse(String name, String description, String location, boolean isPublic, Integer creator) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.isPublic = isPublic;
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventDetailResponse response = (EventDetailResponse) o;
        return Objects.equals(name, response.name) && Objects.equals(description, response.description) && Objects.equals(location, response.location) && Objects.equals(isPublic, response.isPublic) && Objects.equals(creator, response.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, location, isPublic, creator);
    }

    @Override
    public String toString() {
        return "EventDetailResponse{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", isPublic=" + isPublic +
                ", creator=" + creator +
                '}';
    }
}
