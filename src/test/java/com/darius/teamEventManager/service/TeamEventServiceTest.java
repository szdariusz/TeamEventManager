package com.darius.teamEventManager.service;

import com.darius.teamEventManager.TestData.TestData;
import com.darius.teamEventManager.entity.AwaitingQueueElement;
import com.darius.teamEventManager.entity.TEMUser;
import com.darius.teamEventManager.entity.TeamEvent;
import com.darius.teamEventManager.payload.request.events.EventIdRequest;
import com.darius.teamEventManager.payload.response.EventDetailResponse;
import com.darius.teamEventManager.payload.response.EventListResponse;
import com.darius.teamEventManager.payload.response.MessageResponse;
import com.darius.teamEventManager.repository.AwaitingQueueRepository;
import com.darius.teamEventManager.repository.TEMUserRepository;
import com.darius.teamEventManager.repository.TeamEventRepository;
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

import static com.darius.teamEventManager.payload.response.ResponseMessages.*;
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
        ResponseEntity<?> actual = teamEventService.addUserToAwaitQueue(TestData.TEST_MANAGE_EVENT_REQUEST);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void addUserToAwaitQueueWithUserWhoIsMemberAlready() {
        // given
        TeamEvent event = TestData.TEST_TEAM_EVENT;
        TEMUser user = TestData.TEST_USER;
        event.addTemUser(user);

        when(awaitingQueueRepository.existsByUserIdAndEventId(any(), any())).thenReturn(false);
        when(userRepository.existsById(any())).thenReturn(true);
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        ResponseEntity<?> expected = ResponseEntity.internalServerError().body(new MessageResponse(ALREADY_MEMBER));

        // when
        ResponseEntity<?> actual = teamEventService.addUserToAwaitQueue(TestData.TEST_MANAGE_EVENT_REQUEST);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void addUserToAwaitQueueWithGoodParameters() {
        // given
        TeamEvent event = TestData.TEST_TEAM_EVENT_2;
        when(awaitingQueueRepository.existsByUserIdAndEventId(any(), any())).thenReturn(false);
        when(userRepository.existsById(any())).thenReturn(true);
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        ResponseEntity<?> expected = ResponseEntity.ok().build();

        // when
        ResponseEntity<?> actual = teamEventService.addUserToAwaitQueue(TestData.TEST_MANAGE_EVENT_REQUEST);

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
        ResponseEntity<?> actual = teamEventService.addUserToAwaitQueue(TestData.TEST_MANAGE_EVENT_REQUEST);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void addUserToEventValidRequest() {
        // given
        TEMUser user = TestData.TEST_USER;
        TeamEvent event = TestData.TEST_TEAM_EVENT;
        event.addTemUser(user);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        ResponseEntity<?> expected = ResponseEntity.ok().build();

        // when
        ResponseEntity<?> actual = teamEventService.addUserToEvent(TestData.TEST_MANAGE_MEMBER_REQUEST);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void addUserToEventNonExistingUser() {
        // given
        TeamEvent event = TestData.TEST_TEAM_EVENT;

        when(userRepository.findById(any())).thenReturn(Optional.empty());
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        ResponseEntity<?> expected = ResponseEntity.internalServerError().body(new MessageResponse(NOT_FOUND_USER_OR_EVENT));

        // when
        ResponseEntity<?> actual = teamEventService.addUserToEvent(TestData.TEST_MANAGE_MEMBER_REQUEST);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void addUserToEventNonExistingEvent() {
        // given
        TEMUser user = TestData.TEST_USER;

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(eventRepository.findById(any())).thenReturn(Optional.empty());
        ResponseEntity<?> expected = ResponseEntity.internalServerError().body(new MessageResponse(NOT_FOUND_USER_OR_EVENT));

        // when
        ResponseEntity<?> actual = teamEventService.addUserToEvent(TestData.TEST_MANAGE_MEMBER_REQUEST);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void declineAwaitingUserValidRequest() {
        // given
        TeamEvent event = TestData.TEST_TEAM_EVENT;
        when(awaitingQueueRepository.findByUserIdAndEventId(any(), any())).thenReturn(Optional.ofNullable(TestData.TEST_AWAITING_QUEUE_ELEMENT));
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        ResponseEntity<?> expected = ResponseEntity.ok().build();

        // when
        ResponseEntity<?> actual = teamEventService.declineAwaitingUser(TestData.TEST_MANAGE_MEMBER_REQUEST);

        // then
        verify(awaitingQueueRepository, times(1)).delete(TestData.TEST_AWAITING_QUEUE_ELEMENT);
        assertEquals(expected, actual);
    }

    @Test
    void declineAwaitingUserNonExistingEvent() {
        // given
        when(awaitingQueueRepository.findByUserIdAndEventId(any(), any())).thenReturn(Optional.ofNullable(TestData.TEST_AWAITING_QUEUE_ELEMENT));
        when(eventRepository.findById(any())).thenReturn(Optional.empty());
        ResponseEntity<?> expected = ResponseEntity.internalServerError().body(new MessageResponse(NOT_FOUND_USER_OR_EVENT));

        // when
        ResponseEntity<?> actual = teamEventService.declineAwaitingUser(TestData.TEST_MANAGE_MEMBER_REQUEST);

        // then
        verify(awaitingQueueRepository, times(0)).delete(TestData.TEST_AWAITING_QUEUE_ELEMENT);
        assertEquals(expected, actual);
    }

    @Test
    void createEventSuccessful() {
        // given
        when(userRepository.findById(any())).thenReturn(Optional.of(TestData.TEST_USER));
        ResponseEntity<?> expected = ResponseEntity.ok().build();

        // when
        ResponseEntity<?> actual = teamEventService.createEvent(TestData.TEST_CREATE_EVENT_REQUEST);

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
        ResponseEntity<?> actual = teamEventService.createEvent(TestData.TEST_CREATE_EVENT_REQUEST);

        // then
        verify(eventRepository, times(0)).save(any());
        assertEquals(expected, actual);
    }

    @Test
    void getAllPublicEventsValidId() {
        // given
        TeamEvent event = TestData.TEST_TEAM_EVENT;
        event.addTemUser(TestData.TEST_USER);
        when(userRepository.findById(TestData.TEST_USER_ID)).thenReturn(Optional.of(TestData.TEST_USER));
        when(eventRepository.findAllByIsPublicTrueAndTemUsersNotContaining(any())).thenReturn(List.of(event));
        List<EventListResponse> response = converterService.teamEventListToEventListResponse(List.of(event));
        ResponseEntity<?> expected = new ResponseEntity<>(response, HttpStatus.OK);

        // when
        ResponseEntity<?> actual = teamEventService.getAllPublicEvents(TestData.TEST_USER_ID);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void getAllPublicEventsInvalidId() {
        // given
        when(userRepository.findById(TestData.TEST_USER_ID)).thenReturn(Optional.empty());
        ResponseEntity<?> expected = ResponseEntity.internalServerError().build();

        // when
        ResponseEntity<?> actual = teamEventService.getAllPublicEvents(TestData.TEST_USER_ID);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void getAllEventsForUserValidId() {
        // given
        TeamEvent event = TestData.TEST_TEAM_EVENT;
        event.addTemUser(TestData.TEST_USER);
        when(userRepository.findById(TestData.TEST_USER_ID)).thenReturn(Optional.of(TestData.TEST_USER));
        when(eventRepository.findAllByTemUsers(any())).thenReturn(List.of(event));
        List<EventListResponse> response = converterService.teamEventListToEventListResponse(List.of(event));
        ResponseEntity<?> expected = new ResponseEntity<>(response, HttpStatus.OK);

        // when
        ResponseEntity<?> actual = teamEventService.getAllEventsForUser(TestData.TEST_USER_ID);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void getAllEventsForUserInvalidId() {
        // given
        TeamEvent event = TestData.TEST_TEAM_EVENT;
        event.addTemUser(TestData.TEST_USER);
        when(userRepository.findById(TestData.TEST_USER_ID)).thenReturn(Optional.empty());
        ResponseEntity<?> expected = ResponseEntity.internalServerError().build();

        // when
        ResponseEntity<?> actual = teamEventService.getAllEventsForUser(TestData.TEST_USER_ID);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void getAwaitingEventMembersValidRequest() {
        // given
        TeamEvent event = TestData.TEST_TEAM_EVENT;
        TEMUser user = TestData.TEST_USER;
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        when(awaitingQueueRepository.findAllByEventId(any())).thenReturn(new HashSet<>(List.of(TestData.TEST_AWAITING_QUEUE_ELEMENT)));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        Set<TEMUser> response = new HashSet<>(List.of(user));
        ResponseEntity<?> expected = new ResponseEntity<>(response, HttpStatus.OK);

        // when
        ResponseEntity<?> actual = teamEventService.getAwaitingEventMembers(TestData.TEST_MANAGE_EVENT_REQUEST);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void getAwaitingEventMembersInvalidRequest() {
        // given
        TeamEvent event = TestData.TEST_TEAM_EVENT_2;
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        ResponseEntity<?> expected = ResponseEntity.internalServerError().body(new MessageResponse(NOT_MEMBER));

        // when
        ResponseEntity<?> actual = teamEventService.getAwaitingEventMembers(TestData.TEST_MANAGE_EVENT_REQUEST);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void getEventDetailsValidEventId() {
        // given
        TeamEvent event = TestData.TEST_TEAM_EVENT;
        when(eventRepository.findById(TestData.TEST_EVENT_ID)).thenReturn(Optional.of(event));
        EventDetailResponse response = converterService.eventToEventDetail(event);
        ResponseEntity<?> expected = new ResponseEntity<>(response, HttpStatus.OK);

        // when
        ResponseEntity<?> actual = teamEventService.getEventDetails(new EventIdRequest(TestData.TEST_EVENT_ID));

        // then
        verify(converterService, times(2)).eventToEventDetail(any());
        assertEquals(expected, actual);
    }

    @Test
    void getEventDetailsInvalidEventId() {
        // given
        when(eventRepository.findById(TestData.TEST_EVENT_ID)).thenReturn(Optional.empty());
        ResponseEntity<?> expected = ResponseEntity.internalServerError().body(NOT_FOUND_EVENT);

        // when
        ResponseEntity<?> actual = teamEventService.getEventDetails(new EventIdRequest(TestData.TEST_EVENT_ID));

        // then
        verify(converterService, times(0)).eventToEventDetail(any());
        assertEquals(expected, actual);
    }

    @Test
    void getEventMembersValidRequest() {
        // given
        TeamEvent event = TestData.TEST_TEAM_EVENT;
        TEMUser user = TestData.TEST_USER;
        event.addTemUser(user);

        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        Set<TEMUser> members = event.getTemUsers();

        ResponseEntity<?> expected = new ResponseEntity<>(members, HttpStatus.OK);

        // when
        ResponseEntity<?> actual = teamEventService.getEventMembers(TestData.TEST_MANAGE_EVENT_REQUEST);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void getEventMembersNotAMember() {
        // given
        TeamEvent event = TestData.TEST_TEAM_EVENT;
        event.setTemUsers(new HashSet<>());
        TEMUser user = TestData.TEST_USER_2;
        event.addTemUser(user);

        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        Set<TEMUser> members = event.getTemUsers();

        ResponseEntity<?> expected = ResponseEntity.internalServerError().body(new MessageResponse(NOT_MEMBER));

        // when
        ResponseEntity<?> actual = teamEventService.getEventMembers(TestData.TEST_MANAGE_EVENT_REQUEST);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void removeEventMemberValidRequest() {
        // given
        TeamEvent event = TestData.TEST_TEAM_EVENT;
        event.setTemUsers(new HashSet<>());
        TEMUser user = TestData.TEST_USER;
        TEMUser user2 = TestData.TEST_USER_2;
        event.addTemUser(user);
        event.addTemUser(user2);

        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        ResponseEntity<?> expected = ResponseEntity.ok().build();

        // when
        ResponseEntity<?> actual = teamEventService.removeEventMember(TestData.TEST_MANAGE_MEMBER_REQUEST);

        // then
        verify(eventRepository, times(1)).save(any());
        assertEquals(expected, actual);
    }

    @Test
    void removeEventMemberNonExistingMember() {
        // given
        TeamEvent event = TestData.TEST_TEAM_EVENT;
        event.setTemUsers(new HashSet<>());
        System.out.println(event.getTemUsers());
        TEMUser user = TestData.TEST_USER;
        event.addTemUser(user);

        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        ResponseEntity<?> expected = ResponseEntity.internalServerError().body(new MessageResponse(NOT_MEMBER));

        // when
        ResponseEntity<?> actual = teamEventService.removeEventMember(TestData.TEST_MANAGE_MEMBER_REQUEST);

        // then
        verify(eventRepository, times(0)).save(any());
        assertEquals(expected, actual);
    }
}