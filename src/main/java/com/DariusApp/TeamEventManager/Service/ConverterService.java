package com.DariusApp.TeamEventManager.Service;

import com.DariusApp.TeamEventManager.Entity.TeamEvent;
import com.DariusApp.TeamEventManager.Payload.Response.EventDetailResponse;
import com.DariusApp.TeamEventManager.Payload.Response.EventListResponse;
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
