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
        log.info("/awaiting: {}", request);
        return eventService.getAwaitingEventMembers(request);
    }

    @PostMapping("/awaiting/accept")
    public ResponseEntity<?> acceptAwaitingUser(@RequestBody ManageMemberByIdRequest request) {
        log.info("/awaiting/accept: {}", request);
        return eventService.addUserToEvent(request);
    }

    @PostMapping("/awaiting/decline")
    public ResponseEntity<?> declineAwaitingUser(@RequestBody ManageMemberByIdRequest request) {
        log.info("/awaiting/decline: {}", request);
        return eventService.declineAwaitingUser(request);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewEvent(@RequestBody CreateEventRequest request) {
        log.info("/create: {}", request);
        return eventService.createEvent(request);
    }

    @PostMapping("/details")
    public ResponseEntity<?> getEventDetails(@RequestBody EventIdRequest request) {
        log.info("/details: {}", request);
        return eventService.getEventDetails(request);
    }

    @PostMapping("/invite")
    public ResponseEntity<?> inviteUser(@RequestBody ManageMemberByNameRequest request) {
        log.info("/invite: {}", request);
        return eventService.addUserToEventByName(request);
    }

    @PutMapping("/join")
    public ResponseEntity<?> joinToEvent(@RequestBody ManageEventRequest request) {
        log.info("/join: {}", request);
        return eventService.addUserToAwaitQueue(request);
    }

    @PostMapping("/members")
    public ResponseEntity<?> getEventMembers(@RequestBody ManageEventRequest request) {
        log.info("/members: {}", request);
        return eventService.getEventMembers(request);
    }

    @PostMapping("/members/remove")
    public ResponseEntity<?> removeEventMember(@RequestBody ManageMemberByIdRequest request) {
        log.info("/members/remove: {}", request);
        return eventService.removeEventMember(request);
    }

    @PostMapping("/personal")
    public ResponseEntity<List<EventListResponse>> getAllPersonalEvents(
            @RequestBody UserIdRequest request) {
        log.info("/personal: {}", request);
        return eventService.getAllEventsForUser(request.getUserId());
    }

    @PostMapping("/public")
    public ResponseEntity<List<EventListResponse>> getAllPublicEvents(
            @RequestBody UserIdRequest request) {
        log.info("/public: {}", request);
        return eventService.getAllPublicEvents(request.getUserId());
    }


}
