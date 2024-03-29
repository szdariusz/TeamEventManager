package com.darius.teamEventManager.service;

import com.darius.teamEventManager.entity.AwaitingQueueElement;
import com.darius.teamEventManager.entity.TEMUser;
import com.darius.teamEventManager.entity.TeamEvent;
import com.darius.teamEventManager.exceptions.*;
import com.darius.teamEventManager.payload.request.events.CreateEventRequest;
import com.darius.teamEventManager.payload.request.events.EventIdRequest;
import com.darius.teamEventManager.payload.request.events.ManageEventRequest;
import com.darius.teamEventManager.payload.request.members.ManageMemberByIdRequest;
import com.darius.teamEventManager.payload.request.members.ManageMemberByNameRequest;
import com.darius.teamEventManager.payload.response.EventDetailResponse;
import com.darius.teamEventManager.payload.response.EventListResponse;
import com.darius.teamEventManager.repository.AwaitingQueueRepository;
import com.darius.teamEventManager.repository.TEMUserRepository;
import com.darius.teamEventManager.repository.TeamEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log4j2
@RequiredArgsConstructor
@Service
public class TeamEventService {
    private final ConverterService converter;
    private final AwaitingQueueRepository awaitingRepository;
    private final TeamEventRepository eventRepository;
    private final TEMUserRepository userRepository;

    public ResponseEntity<?> addUserToAwaitQueue(ManageEventRequest request) {

        TEMUser user = userRepository.findById(request.getUserId()).orElseThrow(NotFoundTEMUserException::new);
        TeamEvent event = eventRepository.findById(request.getEventId()).orElseThrow(NotFoundEventException::new);
        boolean elementExists = awaitingRepository.existsByUserIdAndEventId(user.getId(), event.getId());

        if (elementExists) {
            throw new RequestAlreadyExistsException();
        }
        awaitingRepository.save(new AwaitingQueueElement(request.getUserId(), request.getEventId()));

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> addUserToEvent(ManageMemberByIdRequest request) {

        TEMUser joiner = userRepository.findById(request.getMemberToManageId()).orElseThrow(NotFoundTEMUserException::new);
        TeamEvent event = eventRepository.findById(request.getEventId()).orElseThrow(NotFoundEventException::new);

        if (isMemberOf(event, request.getUserId())) {
            event.addTemUser(joiner);
            eventRepository.save(event);
            this.declineAwaitingUser(request);
            return ResponseEntity.ok().build();
        }

        throw new NotMemberException();
    }

    public ResponseEntity<?> declineAwaitingUser(ManageMemberByIdRequest request) {
        AwaitingQueueElement element = awaitingRepository.findByUserIdAndEventId(request.getMemberToManageId(), request.getEventId())
                .orElseThrow(NotFoundQueueElementException::new);
        TeamEvent event = eventRepository.findById(request.getEventId()).orElseThrow(NotFoundEventException::new);

        if (isMemberOf(event, request.getUserId())) {
            awaitingRepository.delete(element);
            return ResponseEntity.ok().build();
        }

        throw new NotMemberException();
    }

    public ResponseEntity<?> createEvent(CreateEventRequest request) {

        TeamEvent created = new TeamEvent(request.getName(), request.getDescription(), request.getDate(), request.getLocation(), request.getIsPublic());

        TEMUser creator = userRepository.findById(request.getUserId()).orElseThrow(NotFoundTEMUserException::new);

        created.setCreatorId(created.getCreatorId());
        created.addTemUser(creator);
        eventRepository.save(created);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<EventListResponse>> getAllEventsForUser(Integer userId) {
        TEMUser user = userRepository.findById(userId).orElseThrow(NotFoundTEMUserException::new);

        List<EventListResponse> response = converter.teamEventListToEventListResponse(eventRepository.findAllByTemUsers(user));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<List<EventListResponse>> getAllPublicEvents(Integer userId) {
        TEMUser user = userRepository.findById(userId).orElseThrow(NotFoundTEMUserException::new);

        List<EventListResponse> response = this.converter.teamEventListToEventListResponse(eventRepository.findAllByIsPublicTrueAndTemUsersNotContaining(user));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> getAwaitingEventMembers(ManageEventRequest request) {
        TeamEvent event = eventRepository.findById(request.getEventId()).orElseThrow(NotFoundTEMUserException::new);
        if (isMemberOf(event, request.getUserId())) {
            Set<AwaitingQueueElement> members = awaitingRepository.findAllByEventId(request.getEventId());
            Set<TEMUser> response = new HashSet<>();
            members.forEach(member -> response.add(userRepository.findById(member.getUserId()).get()));

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new NotMemberException();
    }

    public ResponseEntity<?> getEventDetails(EventIdRequest request) {
        TeamEvent event = eventRepository.findById(request.getEventId()).orElseThrow(NotFoundEventException::new);

        EventDetailResponse response = converter.eventToEventDetail(event);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> getEventMembers(ManageEventRequest request) {
        TeamEvent event = eventRepository.findById(request.getEventId()).orElseThrow(NotFoundEventException::new);

        if (isMemberOf(event, request.getUserId())) {
            Set<TEMUser> members = event.getTemUsers();
            return new ResponseEntity<>(members, HttpStatus.OK);
        }
        throw new NotMemberException();
    }

    public ResponseEntity<?> addUserToEventByName(ManageMemberByNameRequest request) {

        TEMUser joiner = userRepository.findByUsername(request.getMemberToManage()).orElseThrow(NotFoundTEMUserException::new);
        TeamEvent event = eventRepository.findById(request.getEventId()).orElseThrow(NotFoundEventException::new);

        if (isMemberOf(event, request.getUserId())) {
            event.addTemUser(joiner);
            eventRepository.save(event);
            this.declineAwaitingUser(
                    ManageMemberByIdRequest.builder()
                            .userId(request.getUserId())
                            .eventId(request.getEventId())
                            .memberToManageId(joiner.getId())
                            .build());

            return ResponseEntity.ok().build();
        }

        throw new NotMemberException();
    }

    public ResponseEntity<?> removeEventMember(ManageMemberByIdRequest request) {
        TeamEvent event = eventRepository.findById(request.getEventId()).orElseThrow(NotFoundEventException::new);
        TEMUser member = userRepository.findById(request.getMemberToManageId()).orElseThrow(NotFoundTEMUserException::new);

        if (isMemberOf(event, member.getId()) && isMemberOf(event, request.getMemberToManageId())) {
            event.removeTemUser(member);
            eventRepository.save(event);
            return ResponseEntity.ok().build();
        }
        throw new NotMemberException();
    }

    private boolean isMemberOf(TeamEvent event, Integer userId) {
        return event.getTemUsers().stream().anyMatch(member -> member.getId() == userId);
    }


}
