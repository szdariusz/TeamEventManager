package com.DariusApp.TeamEventManager.TestData;

import com.DariusApp.TeamEventManager.Entity.AwaitingQueueElement;
import com.DariusApp.TeamEventManager.Entity.TEMUser;
import com.DariusApp.TeamEventManager.Entity.TeamEvent;
import com.DariusApp.TeamEventManager.Payload.Request.CreateEventRequest;
import com.DariusApp.TeamEventManager.Payload.Request.ManageEventRequest;
import com.DariusApp.TeamEventManager.Payload.Request.ManageMemberByIdRequest;
import com.DariusApp.TeamEventManager.Payload.Response.EventDetailResponse;
import com.DariusApp.TeamEventManager.Payload.Response.EventListResponse;

import java.time.LocalDate;

public class TestData {

    public static final String TEST_EVENT_NAME = "Test event name";
    public static final String TEST_USER_NAME = "Johhny Test";
    public static final String TEST_USER_PASSWORD = "password";
    public static final String TEST_EMAIL = "johhnytest@email.com";
    public static final Integer TEST_EVENT_ID = 1;
    public static final Integer TEST_USER_ID = 1;
    public static final Integer TEST_USER_2_ID = 2;
    public static final String TEST_DESCRIPTION = "Test description";
    public static final LocalDate TEST_DATE = LocalDate.of(2023, 1, 1);
    public static final String TEST_LOCATION = "GROUND FLOOR G1 ROOM";

    public static final TEMUser TEST_USER = new TEMUser(TEST_USER_ID, TEST_USER_NAME, TEST_EMAIL, TEST_USER_PASSWORD);
    public static final TEMUser TEST_USER_2 = new TEMUser(TEST_USER_2_ID, TEST_USER_NAME, TEST_EMAIL, TEST_USER_PASSWORD);
    public static final TeamEvent TEST_TEAM_EVENT = new TeamEvent(TEST_EVENT_NAME, TEST_DESCRIPTION, TEST_DATE, TEST_LOCATION, true);
    public static final TeamEvent TEST_TEAM_EVENT_2 = new TeamEvent(TEST_EVENT_NAME, TEST_DESCRIPTION, TEST_DATE, TEST_LOCATION, false);

    public static final EventListResponse TEST_EVENTLIST_RESPONSE = new EventListResponse(null, TEST_EVENT_NAME);

    public static final EventDetailResponse TEST_EVENTDETAIL_RESPONSE = new EventDetailResponse(TEST_EVENT_NAME, TEST_DESCRIPTION, TEST_LOCATION, true, null);

    public static final ManageEventRequest TEST_MANAGE_EVENT_REQUEST = ManageEventRequest.builder().eventId(TEST_EVENT_ID).userId(TEST_USER_ID).build();

    public static final ManageMemberByIdRequest TEST_MANAGE_MEMBER_REQUEST =
            ManageMemberByIdRequest.builder()
                    .memberToManageId(TEST_USER_2_ID)
                    .userId(TEST_USER_ID)
                    .eventId(TEST_EVENT_ID)
                    .build();

    public static final AwaitingQueueElement TEST_AWAITING_QUEUE_ELEMENT =
            AwaitingQueueElement.builder()
                    .eventId(TEST_EVENT_ID)
                    .userId(TEST_USER_ID)
                    .id(1)
                    .build();
    public static final AwaitingQueueElement TEST_AWAITING_QUEUE_ELEMENT_2 =
            AwaitingQueueElement.builder()
                    .eventId(TEST_EVENT_ID)
                    .userId(TEST_USER_2_ID)
                    .id(2)
                    .build();

    public static final CreateEventRequest TEST_CREATE_EVENT_REQUEST =
            CreateEventRequest.builder()
                    .name(TEST_EVENT_NAME)
                    .date(TEST_DATE)
                    .description(TEST_DESCRIPTION)
                    .location(TEST_LOCATION)
                    .userId(TEST_USER_ID)
                    .isPublic(true).build();
}
