package com.DariusApp.TeamEventManager.Controller;

import com.DariusApp.TeamEventManager.Payload.Request.*;
import com.DariusApp.TeamEventManager.Payload.Response.EventListResponse;
import com.DariusApp.TeamEventManager.Service.TeamEventService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/events")
public class TeamEventController {
    @Autowired
    TeamEventService eventService;

    @PostMapping("/awaiting")
    public ResponseEntity<?> getAwaitingQueueForEvent(@RequestBody ManageEventRequest request) {
        log.info("/awaiting  -->  " + request.toString());
        return eventService.getAwaitingEventMembers(request);
    }

    @PostMapping("/awaiting/accept")
    public ResponseEntity<?> acceptAwaitingUser(@RequestBody ManageMemberByIdRequest request) {
        log.info("/awaiting/accept  -->  " + request.toString());
        return eventService.addUserToEvent(request);
    }

    @PostMapping("/awaiting/decline")
    public ResponseEntity<?> declineAwaitingUser(@RequestBody ManageMemberByIdRequest request) {
        log.info("/awaiting/decline  -->  " + request.toString());
        return eventService.declineAwaitingUser(request);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewEvent(@RequestBody CreateEventRequest request) {
        log.info("/create  -->  " + request.toString());
        return eventService.createEvent(request);
    }

    @PostMapping("/details")
    public ResponseEntity<?> getEventDetails(@RequestBody EventIdRequest request) {
        log.info("/details  -->  " + request.toString());
        return eventService.getEventDetails(request);
    }

    @PostMapping("/invite")
    public ResponseEntity<?> inviteUser(@RequestBody ManageMemberByIdRequest request) {
        log.info("/invite  -->  " + request.toString());
        return eventService.addUserToEvent(request);
    }

    @PutMapping("/join")
    public ResponseEntity<?> joinToEvent(@RequestBody ManageEventRequest request) {
        log.info("/join  -->  " + request.toString());
        return eventService.addUserToAwaitQueue(request);
    }

    @PostMapping("/members")
    public ResponseEntity<?> getEventMembers(@RequestBody ManageEventRequest request) {
        log.info("/members  -->  " + request.toString());
        return eventService.getEventMembers(request);
    }

    @PostMapping("/members/remove")
    public ResponseEntity<?> removeEventMember(@RequestBody ManageMemberByIdRequest request) {
        log.info("/members/remove  -->  " + request.toString());
        return eventService.removeEventMember(request);
    }

    @PostMapping("/personal")
    public ResponseEntity<List<EventListResponse>> getAllPersonalEvents(
            @RequestBody UserIdRequest request) {
        log.info("/personal  -->  " + request.toString());
        return eventService.getAllEventsForUser(request.getUserId());
    }

    @PostMapping("/public")
    public ResponseEntity<List<EventListResponse>> getAllPublicEvents(
            @RequestBody UserIdRequest request) {
        log.info("/public  -->  " + request.toString());
        return eventService.getAllPublicEvents(request.getUserId());
    }


}
