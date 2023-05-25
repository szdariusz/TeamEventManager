package com.darius.teamEventManager.payload.request.events;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Builder
public class CreateEventRequest {
    private String name;
    private String description;
    private LocalDate date;
    private String location;
    private Boolean isPublic;
    private Integer userId;

    @Override
    public String toString() {
        return "CreateEventRequest{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", location='" + location + '\'' +
                ", isPublic=" + isPublic +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateEventRequest that = (CreateEventRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(date, that.date) && Objects.equals(location, that.location) && Objects.equals(isPublic, that.isPublic) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, date, location, isPublic, userId);
    }
}
