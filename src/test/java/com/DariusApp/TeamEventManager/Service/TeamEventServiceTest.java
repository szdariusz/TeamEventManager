package com.DariusApp.TeamEventManager.Service;

import com.DariusApp.TeamEventManager.Entity.AwaitingQueueElement;
import com.DariusApp.TeamEventManager.Entity.TEMUser;
import com.DariusApp.TeamEventManager.Entity.TeamEvent;
import com.DariusApp.TeamEventManager.Payload.Request.events.EventIdRequest;
import com.DariusApp.TeamEventManager.Payload.Response.EventDetailResponse;
import com.DariusApp.TeamEventManager.Payload.Response.EventListResponse;
import com.DariusApp.TeamEventManager.Payload.Response.MessageResponse;
import com.DariusApp.TeamEventManager.Repository.AwaitingQueueRepository;
import com.DariusApp.TeamEventManager.Repository.TEMUserRepository;
import com.DariusApp.TeamEventManager.Repository.TeamEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.DariusApp.TeamEventManager.Payload.Response.ResponseMessages.*;
import static com.DariusApp.TeamEventManager.TestData.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamEventServiceTest {
    @Mock
    ConverterService converterService;
    @Mock
    AwaitingQueueRepository awaitingQueueRepository;
    @Mock
    TeamEventRepository eventRepository;
    @Mock
    TEMUserRepository userRepository;

    TeamEventService teamEventService;

    @BeforeEach
    void setup() {
        teamEventService = new TeamEventService(converterService, awaitingQueueRepository, eventRepository, userRepository);
    }

    @Test
    void addUserToAwaitQueueRequestAlreadyExists() {
        // given
        when(awaitingQueueRepository.existsByUserIdAndEventId(any(), any())).thenReturn(true);

        ResponseEntity<?> expected = ResponseEntity.internalServerError().body(new MessageResponse(ALREADY_REQUESTED));

        // when
        ResponseEntity<?> actual = teamEventService.addUserToAwaitQueue(TEST_MANAGE_EVENT_REQUEST);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void addUserToAwaitQueueWithUserWhoIsMemberAlready() {
        // given
        TeamEvent event = TEST_TEAM_EVENT;
        TEMUser user = TEST_USER;
        event.addTemUser(user);

        when(awaitingQueueRepository.existsByUserIdAndEventId(any(), any())).thenReturn(false);
        when(userRepository.existsById(any())).thenReturn(true);
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        ResponseEntity<?> expected = ResponseEntity.internalServerError().body(new MessageResponse(ALREADY_MEMBER));

        // when
        ResponseEntity<?> actual = teamEventService.addUserToAwaitQueue(TEST_MANAGE_EVENT_REQUEST);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void addUserToAwaitQueueWithGoodParameters() {
        // given
        TeamEvent event = TEST_TEAM_EVENT_2;
        when(awaitingQueueRepository.existsByUserIdAndEventId(any(), any())).thenReturn(false);
        when(userRepository.existsById(any())).thenReturn(true);
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        ResponseEntity<?> expected = ResponseEntity.ok().build();

        // when
        ResponseEntity<?> actual = teamEventService.addUserToAwaitQueue(TEST_MANAGE_EVENT_REQUEST);

        // then
        Mockito.verify(awaitingQueueRepository, times(1)).save(any(AwaitingQueueElement.class));
        assertEquals(expected, actual);
    }

    @Test
    void addUserToAwaitQueueWithInvalidUserIdOrEventId() {
        // given
        when(awaitingQueueRepository.existsByUserIdAndEventId(any(), any())).thenReturn(false);
        when(userRepository.existsById(any())).thenReturn(false);
        ResponseEntity<?> expected = ResponseEntity.internalServerError().body(new MessageResponse(NOT_FOUND_USER_OR_EVENT));

        // when
        ResponseEntity<?> actual = teamEventService.addUserToAwaitQueue(TEST_MANAGE_EVENT_REQUEST);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void addUserToEventValidRequest() {
        // given
        TEMUser user = TEST_USER;
        TeamEvent event = TEST_TEAM_EVENT;
        event.addTemUser(user);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        ResponseEntity<?> expected = ResponseEntity.ok().build();

        // when
        ResponseEntity<?> actual = teamEventService.addUserToEvent(TEST_MANAGE_MEMBER_REQUEST);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void addUserToEventNonExistingUser() {
        // given
        TeamEvent event = TEST_TEAM_EVENT;

        when(userRepository.findById(any())).thenReturn(Optional.empty());
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        ResponseEntity<?> expected = ResponseEntity.internalServerError().body(new MessageResponse(NOT_FOUND_USER_OR_EVENT));

        // when
        ResponseEntity<?> actual = teamEventService.addUserToEvent(TEST_MANAGE_MEMBER_REQUEST);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void addUserToEventNonExistingEvent() {
        // given
        TEMUser user = TEST_USER;

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(eventRepository.findById(any())).thenReturn(Optional.empty());
        ResponseEntity<?> expected = ResponseEntity.internalServerError().body(new MessageResponse(NOT_FOUND_USER_OR_EVENT));

        // when
        ResponseEntity<?> actual = teamEventService.addUserToEvent(TEST_MANAGE_MEMBER_REQUEST);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void declineAwaitingUserValidRequest() {
        // given
        TeamEvent event = TEST_TEAM_EVENT;
        when(awaitingQueueRepository.findByUserIdAndEventId(any(), any())).thenReturn(Optional.ofNullable(TEST_AWAITING_QUEUE_ELEMENT));
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        ResponseEntity<?> expected = ResponseEntity.ok().build();

        // when
        ResponseEntity<?> actual = teamEventService.declineAwaitingUser(TEST_MANAGE_MEMBER_REQUEST);

        // then
        verify(awaitingQueueRepository, times(1)).delete(TEST_AWAITING_QUEUE_ELEMENT);
        assertEquals(expected, actual);
    }

    @Test
    void declineAwaitingUserNonExistingEvent() {
        // given
        when(awaitingQueueRepository.findByUserIdAndEventId(any(), any())).thenReturn(Optional.ofNullable(TEST_AWAITING_QUEUE_ELEMENT));
        when(eventRepository.findById(any())).thenReturn(Optional.empty());
        ResponseEntity<?> expected = ResponseEntity.internalServerError().body(new MessageResponse(NOT_FOUND_USER_OR_EVENT));

        // when
        ResponseEntity<?> actual = teamEventService.declineAwaitingUser(TEST_MANAGE_MEMBER_REQUEST);

        // then
        verify(awaitingQueueRepository, times(0)).delete(TEST_AWAITING_QUEUE_ELEMENT);
        assertEquals(expected, actual);
    }

    @Test
    void createEventSuccessful() {
        // given
        when(userRepository.findById(any())).thenReturn(Optional.of(TEST_USER));
        ResponseEntity<?> expected = ResponseEntity.ok().build();

        // when
        ResponseEntity<?> actual = teamEventService.createEvent(TEST_CREATE_EVENT_REQUEST);

        // then
        verify(eventRepository, times(1)).save(any());
        assertEquals(expected, actual);
    }

    @Test
    void createEventNonExistingUser() {
        // given
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        ResponseEntity<?> expected = ResponseEntity.internalServerError().body(new MessageResponse(NOT_FOUND_USER_OR_EVENT));

        // when
        ResponseEntity<?> actual = teamEventService.createEvent(TEST_CREATE_EVENT_REQUEST);

        // then
        verify(eventRepository, times(0)).save(any());
        assertEquals(expected, actual);
    }

    @Test
    void getAllPublicEventsValidId() {
        // given
        TeamEvent event = TEST_TEAM_EVENT;
        event.addTemUser(TEST_USER);
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(TEST_USER));
        when(eventRepository.findAllByIsPublicTrueAndTemUsersNotContaining(any())).thenReturn(List.of(event));
        List<EventListResponse> response = converterService.teamEventListToEventListResponse(List.of(event));
        ResponseEntity<?> expected = new ResponseEntity<>(response, HttpStatus.OK);

        // when
        ResponseEntity<?> actual = teamEventService.getAllPublicEvents(TEST_USER_ID);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void getAllPublicEventsInvalidId() {
        // given
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.empty());
        ResponseEntity<?> expected = ResponseEntity.internalServerError().build();

        // when
        ResponseEntity<?> actual = teamEventService.getAllPublicEvents(TEST_USER_ID);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void getAllEventsForUserValidId() {
        // given
        TeamEvent event = TEST_TEAM_EVENT;
        event.addTemUser(TEST_USER);
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(TEST_USER));
        when(eventRepository.findAllByTemUsers(any())).thenReturn(List.of(event));
        List<EventListResponse> response = converterService.teamEventListToEventListResponse(List.of(event));
        ResponseEntity<?> expected = new ResponseEntity<>(response, HttpStatus.OK);

        // when
        ResponseEntity<?> actual = teamEventService.getAllEventsForUser(TEST_USER_ID);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void getAllEventsForUserInvalidId() {
        // given
        TeamEvent event = TEST_TEAM_EVENT;
        event.addTemUser(TEST_USER);
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.empty());
        ResponseEntity<?> expected = ResponseEntity.internalServerError().build();

        // when
        ResponseEntity<?> actual = teamEventService.getAllEventsForUser(TEST_USER_ID);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void getAwaitingEventMembersValidRequest() {
        // given
        TeamEvent event = TEST_TEAM_EVENT;
        TEMUser user = TEST_USER;
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        when(awaitingQueueRepository.findAllByEventId(any())).thenReturn(new HashSet<>(List.of(TEST_AWAITING_QUEUE_ELEMENT)));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        Set<TEMUser> response = new HashSet<>(List.of(user));
        ResponseEntity<?> expected = new ResponseEntity<>(response, HttpStatus.OK);

        // when
        ResponseEntity<?> actual = teamEventService.getAwaitingEventMembers(TEST_MANAGE_EVENT_REQUEST);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void getAwaitingEventMembersInvalidRequest() {
        // given
        TeamEvent event = TEST_TEAM_EVENT_2;
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        ResponseEntity<?> expected = ResponseEntity.internalServerError().body(new MessageResponse(NOT_MEMBER));

        // when
        ResponseEntity<?> actual = teamEventService.getAwaitingEventMembers(TEST_MANAGE_EVENT_REQUEST);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void getEventDetailsValidEventId() {
        // given
        TeamEvent event = TEST_TEAM_EVENT;
        when(eventRepository.findById(TEST_EVENT_ID)).thenReturn(Optional.of(event));
        EventDetailResponse response = converterService.eventToEventDetail(event);
        ResponseEntity<?> expected = new ResponseEntity<>(response, HttpStatus.OK);

        // when
        ResponseEntity<?> actual = teamEventService.getEventDetails(new EventIdRequest(TEST_EVENT_ID));

        // then
        verify(converterService, times(2)).eventToEventDetail(any());
        assertEquals(expected, actual);
    }

    @Test
    void getEventDetailsInvalidEventId() {
        // given
        when(eventRepository.findById(TEST_EVENT_ID)).thenReturn(Optional.empty());
        ResponseEntity<?> expected = ResponseEntity.internalServerError().body(NOT_FOUND_EVENT);

        // when
        ResponseEntity<?> actual = teamEventService.getEventDetails(new EventIdRequest(TEST_EVENT_ID));

        // then
        verify(converterService, times(0)).eventToEventDetail(any());
        assertEquals(expected, actual);
    }

    @Test
    void getEventMembersValidRequest() {
        // given
        TeamEvent event = TEST_TEAM_EVENT;
        TEMUser user = TEST_USER;
        event.addTemUser(user);

        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        Set<TEMUser> members = event.getTemUsers();

        ResponseEntity<?> expected = new ResponseEntity<>(members, HttpStatus.OK);

        // when
        ResponseEntity<?> actual = teamEventService.getEventMembers(TEST_MANAGE_EVENT_REQUEST);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void getEventMembersNotAMember() {
        // given
        TeamEvent event = TEST_TEAM_EVENT;
        event.setTemUsers(new HashSet<>());
        TEMUser user = TEST_USER_2;
        event.addTemUser(user);

        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        Set<TEMUser> members = event.getTemUsers();

        ResponseEntity<?> expected = ResponseEntity.internalServerError().body(new MessageResponse(NOT_MEMBER));

        // when
        ResponseEntity<?> actual = teamEventService.getEventMembers(TEST_MANAGE_EVENT_REQUEST);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void removeEventMemberValidRequest() {
        // given
        TeamEvent event = TEST_TEAM_EVENT;
        event.setTemUsers(new HashSet<>());
        TEMUser user = TEST_USER;
        TEMUser user2 = TEST_USER_2;
        event.addTemUser(user);
        event.addTemUser(user2);

        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        ResponseEntity<?> expected = ResponseEntity.ok().build();

        // when
        ResponseEntity<?> actual = teamEventService.removeEventMember(TEST_MANAGE_MEMBER_REQUEST);

        // then
        verify(eventRepository, times(1)).save(any());
        assertEquals(expected, actual);
    }

    @Test
    void removeEventMemberNonExistingMember() {
        // given
        TeamEvent event = TEST_TEAM_EVENT;
        event.setTemUsers(new HashSet<>());
        System.out.println(event.getTemUsers());
        TEMUser user = TEST_USER;
        event.addTemUser(user);

        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        ResponseEntity<?> expected = ResponseEntity.internalServerError().body(new MessageResponse(NOT_MEMBER));

        // when
        ResponseEntity<?> actual = teamEventService.removeEventMember(TEST_MANAGE_MEMBER_REQUEST);

        // then
        verify(eventRepository, times(0)).save(any());
        assertEquals(expected, actual);
    }
}