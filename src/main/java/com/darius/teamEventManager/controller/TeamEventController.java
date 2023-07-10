package com.darius.teamEventManager.controller;

import com.darius.teamEventManager.payload.request.auth.UserIdRequest;
import com.darius.teamEventManager.payload.request.events.CreateEventRequest;
import com.darius.teamEventManager.payload.request.events.EventIdRequest;
import com.darius.teamEventManager.payload.request.events.ManageEventRequest;
import com.darius.teamEventManager.payload.request.members.ManageMemberByIdRequest;
import com.darius.teamEventManager.payload.request.members.ManageMemberByNameRequest;
import com.darius.teamEventManager.payload.response.EventListResponse;
import com.darius.teamEventManager.service.TeamEventService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@Api
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/events")
public class TeamEventController {
    private final TeamEventService eventService;

    @PostMapping("/awaiting")
    public ResponseEntity<?> getAwaitingQueueForEvent(@RequestBody ManageEventRequest request) {
        log.debug("/awaiting: {}", request);
        return eventService.getAwaitingEventMembers(request);
    }

    @PostMapping("/awaiting/accept")
    public ResponseEntity<?> acceptAwaitingUser(@RequestBody ManageMemberByIdRequest request) {
        log.debug("/awaiting/accept: {}", request);
        return eventService.addUserToEvent(request);
    }

    @PostMapping("/awaiting/decline")
    public ResponseEntity<?> declineAwaitingUser(@RequestBody ManageMemberByIdRequest request) {
        log.debug("/awaiting/decline: {}", request);
        return eventService.declineAwaitingUser(request);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewEvent(@RequestBody CreateEventRequest request) {
        log.debug("/create: {}", request);
        return eventService.createEvent(request);
    }

    @PostMapping("/details")
    public ResponseEntity<?> getEventDetails(@RequestBody EventIdRequest request) {
        log.debug("/details: {}", request);
        return eventService.getEventDetails(request);
    }

    @PostMapping("/invite")
    public ResponseEntity<?> inviteUser(@RequestBody ManageMemberByNameRequest request) {
        log.debug("/invite: {}", request);
        return eventService.addUserToEventByName(request);
    }

    @PutMapping("/join")
    public ResponseEntity<?> joinToEvent(@RequestBody ManageEventRequest request) {
        log.debug("/join: {}", request);
        return eventService.addUserToAwaitQueue(request);
    }

    @PostMapping("/members")
    public ResponseEntity<?> getEventMembers(@RequestBody ManageEventRequest request) {
        log.debug("/members: {}", request);
        return eventService.getEventMembers(request);
    }

    @PostMapping("/members/remove")
    public ResponseEntity<?> removeEventMember(@RequestBody ManageMemberByIdRequest request) {
        log.debug("/members/remove: {}", request);
        return eventService.removeEventMember(request);
    }

    @PostMapping("/personal")
    public ResponseEntity<List<EventListResponse>> getAllPersonalEvents(
            @RequestBody UserIdRequest request) {
        log.debug("/personal: {}", request);
        return eventService.getAllEventsForUser(request.getUserId());
    }

    @PostMapping("/public")
    public ResponseEntity<List<EventListResponse>> getAllPublicEvents(
            @RequestBody UserIdRequest request) {
        log.debug("/public: {}", request);
        return eventService.getAllPublicEvents(request.getUserId());
    }


}
