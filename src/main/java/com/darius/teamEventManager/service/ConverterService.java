package com.darius.teamEventManager.service;

import com.darius.teamEventManager.entity.TeamEvent;
import com.darius.teamEventManager.payload.response.EventDetailResponse;
import com.darius.teamEventManager.payload.response.EventListResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConverterService {
    public List<EventListResponse> teamEventListToEventListResponse(List<TeamEvent> eventList) {
        List<EventListResponse> response = new ArrayList<>();
        for (TeamEvent event : eventList) {
            response.add(new EventListResponse(event.getId(), event.getName()));
        }
        return response;
    }

    public EventDetailResponse eventToEventDetail(TeamEvent event) {
        return new EventDetailResponse(
                event.getName(), event.getDescription(), event.getLocation(), event.isPublic(), event.getCreatorId());
    }

}
