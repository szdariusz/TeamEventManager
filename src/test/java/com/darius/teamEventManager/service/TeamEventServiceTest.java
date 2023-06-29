package com.darius.teamEventManager.service;

import com.darius.teamEventManager.TestData.TestData;
import com.darius.teamEventManager.entity.AwaitingQueueElement;
import com.darius.teamEventManager.entity.TEMUser;
import com.darius.teamEventManager.entity.TeamEvent;
import com.darius.teamEventManager.exceptions.*;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.darius.teamEventManager.TestData.TestData.*;
import static com.darius.teamEventManager.payload.response.ResponseMessages.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        TEMUser user = TEST_USER;
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // when then
        assertThrows(NotFoundEventException.class, () -> {
            teamEventService.addUserToAwaitQueue(TEST_MANAGE_EVENT_REQUEST);
        });

    }

    @Test
    void addUserToAwaitQueueWithUserWhoIsMemberAlready() {
        // given
        TeamEvent event = TEST_TEAM_EVENT;
        TEMUser user = TEST_USER;
        event.addTemUser(user);

        when(awaitingQueueRepository.existsByUserIdAndEventId(any(), any())).thenReturn(true);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        ResponseEntity<?> expected = ResponseEntity.internalServerError().body(new MessageResponse(ALREADY_MEMBER));

        // when
        assertThrows(RequestAlreadyExistsException.class, () -> {
            teamEventService.addUserToAwaitQueue(TEST_MANAGE_EVENT_REQUEST);
        });
    }

    @Test
    void addUserToAwaitQueueWithGoodParameters() {
        // given
        TeamEvent event = TEST_TEAM_EVENT;
        event.setTemUsers(new HashSet<>());
        TEMUser user = TEST_USER;

        when(awaitingQueueRepository.existsByUserIdAndEventId(any(), any())).thenReturn(false);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        ResponseEntity<?> expected = ResponseEntity.ok().build();

        // when
        ResponseEntity<?> actual = teamEventService.addUserToAwaitQueue(TEST_MANAGE_EVENT_REQUEST);

        // then
        verify(awaitingQueueRepository, times(1)).save(any(AwaitingQueueElement.class));
        assertEquals(expected, actual);
    }

    @Test
    void addUserToAwaitQueueWithInvalidEventId() {
        // given
        TEMUser user = TEST_USER;

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // when
        assertThrows(NotFoundEventException.class, () -> teamEventService.addUserToAwaitQueue(TEST_MANAGE_EVENT_REQUEST));
    }

    @Test
    void addUserToAwaitQueueWithInvalidUserId() {
        // given
        when(userRepository.findById(any())).thenThrow(NotFoundTEMUserException.class);

        // when
        assertThrows(NotFoundTEMUserException.class, () -> teamEventService.addUserToAwaitQueue(TEST_MANAGE_EVENT_REQUEST));
    }

    @Test
    void addUserToEventValidRequest() {
        // given
        TEMUser user = TEST_USER;
        TeamEvent event = TEST_TEAM_EVENT;
        event.addTemUser(user);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));

        // when
        assertThrows(NotFoundQueueElementException.class, () -> teamEventService.addUserToEvent(TEST_MANAGE_MEMBER_REQUEST));
    }

    @Test
    void addUserToEventNonExistingUser() {
        // given
        TeamEvent event = TEST_TEAM_EVENT;

        when(userRepository.findById(any())).thenThrow(NotFoundTEMUserException.class);

        // when
        assertThrows(NotFoundTEMUserException.class, () -> teamEventService.addUserToEvent(TEST_MANAGE_MEMBER_REQUEST));
    }

    @Test
    void addUserToEventNonExistingEvent() {
        // given
        TEMUser user = TEST_USER;

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(eventRepository.findById(any())).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundEventException.class, () -> teamEventService.addUserToEvent(TEST_MANAGE_MEMBER_REQUEST));
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

        // when
        assertThrows(NotFoundEventException.class, () -> teamEventService.declineAwaitingUser(TEST_MANAGE_MEMBER_REQUEST));

        // then
        verify(awaitingQueueRepository, times(0)).delete(TEST_AWAITING_QUEUE_ELEMENT);
    }

    @Test
    void createEventSuccessful() {
        // given
        when(userRepository.findById(any())).thenReturn(Optional.of(TEST_USER));
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

        // when
        assertThrows(NotFoundTEMUserException.class, () -> teamEventService.createEvent(TestData.TEST_CREATE_EVENT_REQUEST));

        // then
        verify(eventRepository, times(0)).save(any());
    }

    @Test
    void getAllPublicEventsValidId() {
        // given
        TeamEvent event = TEST_TEAM_EVENT;
        event.addTemUser(TEST_USER);
        when(userRepository.findById(TestData.TEST_USER_ID)).thenReturn(Optional.of(TEST_USER));
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

        // when
        assertThrows(NotFoundTEMUserException.class, () -> teamEventService.getAllPublicEvents(TestData.TEST_USER_ID));
    }

    @Test
    void getAllEventsForUserValidId() {
        // given
        TeamEvent event = TEST_TEAM_EVENT;
        event.addTemUser(TEST_USER);
        when(userRepository.findById(TestData.TEST_USER_ID)).thenReturn(Optional.of(TEST_USER));
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
        TeamEvent event = TEST_TEAM_EVENT;
        event.addTemUser(TEST_USER);
        when(userRepository.findById(TestData.TEST_USER_ID)).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundTEMUserException.class, () -> teamEventService.getAllEventsForUser(TestData.TEST_USER_ID));
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
        TeamEvent event = TestData.TEST_TEAM_EVENT_2;
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));

        // when
        assertThrows(NotMemberException.class, () -> teamEventService.getAwaitingEventMembers(TEST_MANAGE_EVENT_REQUEST));
    }

    @Test
    void getEventDetailsValidEventId() {
        // given
        TeamEvent event = TEST_TEAM_EVENT;
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
        assertThrows(NotFoundEventException.class, () -> teamEventService.getEventDetails(new EventIdRequest(TestData.TEST_EVENT_ID)));

        // then
        verify(converterService, times(0)).eventToEventDetail(any());
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
        TEMUser user = TestData.TEST_USER_2;
        event.addTemUser(user);

        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        Set<TEMUser> members = event.getTemUsers();

        // when
        assertThrows(NotMemberException.class, () -> teamEventService.getEventMembers(TEST_MANAGE_EVENT_REQUEST));
    }

    @Test
    void removeEventMemberValidRequest() {
        // given
        TeamEvent event = TEST_TEAM_EVENT;
        event.setTemUsers(new HashSet<>());
        TEMUser user = TEST_USER;
        TEMUser user2 = TestData.TEST_USER_2;
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