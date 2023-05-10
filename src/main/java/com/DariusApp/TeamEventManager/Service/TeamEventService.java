package com.DariusApp.TeamEventManager.Service;

import com.DariusApp.TeamEventManager.Entity.AwaitingQueueElement;
import com.DariusApp.TeamEventManager.Entity.TEMUser;
import com.DariusApp.TeamEventManager.Entity.TeamEvent;
import com.DariusApp.TeamEventManager.Payload.Request.*;
import com.DariusApp.TeamEventManager.Payload.Response.EventDetailResponse;
import com.DariusApp.TeamEventManager.Payload.Response.EventListResponse;
import com.DariusApp.TeamEventManager.Payload.Response.MessageResponse;
import com.DariusApp.TeamEventManager.Repository.AwaitingQueueRepository;
import com.DariusApp.TeamEventManager.Repository.TEMUserRepository;
import com.DariusApp.TeamEventManager.Repository.TeamEventRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.DariusApp.TeamEventManager.Payload.Response.ResponseMessages.*;

@Log4j2
@Service
public class TeamEventService {
    @Autowired
    ConverterService converter;
    @Autowired
    AwaitingQueueRepository awaitingRepository;
    @Autowired
    TeamEventRepository eventRepository;
    @Autowired
    TEMUserRepository userRepository;

    public TeamEventService(ConverterService converter, AwaitingQueueRepository awaitingRepository, TeamEventRepository eventRepository, TEMUserRepository userRepository) {
        this.converter = converter;
        this.awaitingRepository = awaitingRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> addUserToAwaitQueue(ManageEventRequest request) {

        boolean userExists = userRepository.existsById(request.getUserId());
        Optional<TeamEvent> event = eventRepository.findById(request.getEventId());
        boolean elementExists =
                awaitingRepository.existsByUserIdAndEventId(request.getUserId(), request.getEventId());

        if (elementExists) {
            return ResponseEntity.internalServerError().body(new MessageResponse(ALREADY_REQUESTED));
        }
        if (event.isPresent() && userExists) {
            if (isMemberOf(event.get(), request.getUserId())) {
                return ResponseEntity.internalServerError().body(new MessageResponse(ALREADY_MEMBER));
            }
            awaitingRepository.save(new AwaitingQueueElement(request.getUserId(), request.getEventId()));
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().body(new MessageResponse(NOT_FOUND_USER_OR_EVENT));
    }

    public ResponseEntity<?> addUserToEvent(ManageMemberByIdRequest request) {

        Optional<TEMUser> joiner = userRepository.findById(request.getMemberToManageId());
        Optional<TeamEvent> event = eventRepository.findById(request.getEventId());

        if (joiner.isPresent() && event.isPresent() && isMemberOf(event.get(), request.getUserId())) {

            TeamEvent existingEvent = event.get();
            existingEvent.addTemUser(joiner.get());
            eventRepository.save(existingEvent);
            this.declineAwaitingUser(request);

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().body(new MessageResponse(NOT_FOUND_USER_OR_EVENT));
    }

    public ResponseEntity<?> declineAwaitingUser(ManageMemberByIdRequest request) {
        Optional<AwaitingQueueElement> element =
                awaitingRepository.findByUserIdAndEventId(request.getMemberToManageId(), request.getEventId());
        Optional<TeamEvent> event = eventRepository.findById(request.getEventId());
        if (event.isPresent()) {
            if (element.isPresent() && isMemberOf(event.get(), request.getUserId())) {
                awaitingRepository.delete(element.get());
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.internalServerError().body(new MessageResponse(NOT_FOUND_USER_OR_EVENT));
    }

    public ResponseEntity<?> createEvent(CreateEventRequest request) {

        TeamEvent created = new TeamEvent(request.getName(), request.getDescription(), request.getDate(),
                request.getLocation(), request.getIsPublic());

        Optional<TEMUser> creator = userRepository.findById(request.getUserId());

        if (creator.isPresent()) {
            created.setCreatorId(created.getCreatorId());
            created.addTemUser(creator.get());
            eventRepository.save(created);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().body(new MessageResponse(NOT_FOUND_USER_OR_EVENT));
    }

    public ResponseEntity<List<EventListResponse>> getAllEventsForUser(Integer userId) {
        Optional<TEMUser> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<EventListResponse> response =
                    converter.teamEventListToEventListResponse(eventRepository.findAllByTemUsers(user.get()));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return ResponseEntity.internalServerError().build();
    }

    public ResponseEntity<List<EventListResponse>> getAllPublicEvents(Integer userId) {
        Optional<TEMUser> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<EventListResponse> response =
                    this.converter.teamEventListToEventListResponse(
                            eventRepository.findAllByIsPublicTrueAndTemUsersNotContaining(user.get()));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return ResponseEntity.internalServerError().build();

    }

    public ResponseEntity<?> getAwaitingEventMembers(ManageEventRequest request) {
        Optional<TeamEvent> event = eventRepository.findById(request.getEventId());
        if (isMemberOf(event.get(), request.getUserId())) {
            Set<AwaitingQueueElement> members = awaitingRepository.findAllByEventId(request.getEventId());
            Set<TEMUser> response = new HashSet<>();
            members.forEach(member -> response.add(userRepository.findById(member.getUserId()).get()));

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return ResponseEntity.internalServerError().body(new MessageResponse(NOT_MEMBER));
    }

    public ResponseEntity<?> getEventDetails(EventIdRequest request) {
        Optional<TeamEvent> event = eventRepository.findById(request.getEventId());
        if (event.isPresent()) {
            EventDetailResponse response = converter.eventToEventDetail(event.get());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return ResponseEntity.internalServerError().body(NOT_FOUND_EVENT);
    }

    public ResponseEntity<?> getEventMembers(ManageEventRequest request) {
        Optional<TeamEvent> event = eventRepository.findById(request.getEventId());
        if (event.isPresent()) {
            if (isMemberOf(event.get(), request.getUserId())) {
                Set<TEMUser> members = event.get().getTemUsers();
                return new ResponseEntity<>(members, HttpStatus.OK);
            }
            return ResponseEntity.internalServerError().body(new MessageResponse(NOT_MEMBER));
        }
        return ResponseEntity.internalServerError().body(new MessageResponse(NOT_FOUND_EVENT));
    }

    public ResponseEntity<?> addUserToEventByName(ManageMemberByNameRequest request) {

        Optional<TEMUser> joiner = userRepository.findByUsername(request.getMemberToManage());
        Optional<TeamEvent> event = eventRepository.findById(request.getEventId());

        if (joiner.isPresent() && event.isPresent() && isMemberOf(event.get(), request.getUserId())) {

            TeamEvent existingEvent = event.get();
            existingEvent.addTemUser(joiner.get());
            eventRepository.save(existingEvent);
            this.declineAwaitingUser(ManageMemberByIdRequest.builder()
                    .userId(request.getUserId())
                    .eventId(request.getEventId())
                    .memberToManageId(joiner.get().getId()).build());

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().body(new MessageResponse(NOT_FOUND_USER_OR_EVENT));
    }

    public ResponseEntity<?> removeEventMember(ManageMemberByIdRequest request) {
        Optional<TeamEvent> event = eventRepository.findById(request.getEventId());
        Optional<TEMUser> member = userRepository.findById(request.getMemberToManageId());

        if (isMemberOf(event.get(), request.getUserId()) && member.isPresent()) {
            if (isMemberOf(event.get(), request.getMemberToManageId())) {
                TeamEvent existingEvent = event.get();
                existingEvent.removeTemUser(member.get());
                eventRepository.save(existingEvent);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.internalServerError().body(new MessageResponse(NOT_MEMBER));
    }

    private boolean isMemberOf(TeamEvent event, Integer userId) {
        return event.getTemUsers().stream().anyMatch(member -> member.getId() == userId);
    }


}
