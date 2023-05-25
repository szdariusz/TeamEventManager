package com.darius.teamEventManager.service;

import com.darius.teamEventManager.TestData.TestData;
import com.darius.teamEventManager.entity.TeamEvent;
import com.darius.teamEventManager.payload.response.EventDetailResponse;
import com.darius.teamEventManager.payload.response.EventListResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConverterServiceTest {

    @Test
    void teamEventListToEventListResponse_ReturnsGoodType() {
        List<TeamEvent> input = new ArrayList<>();
        List<EventListResponse> expected = new ArrayList<>();

        ConverterService service = new ConverterService();


        assertEquals(expected, service.teamEventListToEventListResponse(input));
    }

    @Test
    void teamEventListToEventListResponse_ReturnsGoodValues() {
        List<TeamEvent> input = new ArrayList<>();
        input.add(TestData.TEST_TEAM_EVENT);
        ConverterService service = new ConverterService();

        List<EventListResponse> expected = new ArrayList<>();
        EventListResponse expectedItem = TestData.TEST_EVENTLIST_RESPONSE;
        expected.add(expectedItem);

        List<EventListResponse> actual = service.teamEventListToEventListResponse(input);

        assertEquals(expected, actual);
    }

    @Test
    void eventToEventDetail_ReturnsGoodType() {
        ConverterService service = new ConverterService();

        assertTrue(service.eventToEventDetail(TestData.TEST_TEAM_EVENT) instanceof EventDetailResponse);
    }

    @Test
    void eventToEventDetail_ReturnsGoodValues() {
        ConverterService service = new ConverterService();

        EventDetailResponse expected = TestData.TEST_EVENTDETAIL_RESPONSE;
        EventDetailResponse actual = service.eventToEventDetail(TestData.TEST_TEAM_EVENT);

        assertEquals(expected, actual);
    }
}