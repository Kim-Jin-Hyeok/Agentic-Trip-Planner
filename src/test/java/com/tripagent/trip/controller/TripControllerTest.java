package com.tripagent.trip.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tripagent.auth.support.LoginMemberId;
import com.tripagent.common.exception.GlobalExceptionHandler;
import com.tripagent.itinerary.service.ItineraryGenerateService;
import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.domain.TripVisibility;
import com.tripagent.trip.dto.TripConditionUpdateRequest;
import com.tripagent.trip.dto.TripDetailResponse;
import com.tripagent.trip.dto.TripResponse;
import com.tripagent.trip.service.TripService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

class TripControllerTest {

    private TripService tripService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        tripService = org.mockito.Mockito.mock(TripService.class);
        ItineraryGenerateService itineraryGenerateService = org.mockito.Mockito.mock(ItineraryGenerateService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TripController(tripService, itineraryGenerateService))
                .setCustomArgumentResolvers(new TestLoginMemberIdArgumentResolver())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void searchTripsReturnsCommonSuccessResponse() throws Exception {
        when(tripService.searchTripsByOwnerId(100L, null, null, null, null, null, null))
                .thenReturn(List.of(trip(2L), trip(1L)));

        mockMvc.perform(get("/api/trips"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].tripId").value(2L))
                .andExpect(jsonPath("$.data[0].destination").value("JEJU"))
                .andExpect(jsonPath("$.data[0].concept").value("HEALING"))
                .andExpect(jsonPath("$.data[0].dailyEndTime[0]").value(18))
                .andExpect(jsonPath("$.data[0].dailyEndTime[1]").value(0));
    }

    @Test
    void searchTripsPassesFiltersAndReturnsCommonSuccessResponse() throws Exception {
        LocalDate startDateFrom = LocalDate.of(2026, 7, 1);
        LocalDate startDateTo = LocalDate.of(2026, 7, 10);
        LocalDate endDateFrom = LocalDate.of(2026, 7, 3);
        LocalDate endDateTo = LocalDate.of(2026, 7, 12);
        when(tripService.searchTripsByOwnerId(
                100L,
                "JE",
                TripConcept.FOOD,
                startDateFrom,
                startDateTo,
                endDateFrom,
                endDateTo
        )).thenReturn(List.of(trip(3L, TripConcept.FOOD)));

        mockMvc.perform(get("/api/trips")
                        .param("destination", "JE")
                        .param("concept", "FOOD")
                        .param("startDateFrom", "2026-07-01")
                        .param("startDateTo", "2026-07-10")
                        .param("endDateFrom", "2026-07-03")
                        .param("endDateTo", "2026-07-12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].tripId").value(3L))
                .andExpect(jsonPath("$.data[0].concept").value("FOOD"));
    }

    @Test
    void searchTripsReturnsInvalidRequestWhenDateRangeIsInvalid() throws Exception {
        LocalDate startDateFrom = LocalDate.of(2026, 7, 10);
        LocalDate startDateTo = LocalDate.of(2026, 7, 1);
        when(tripService.searchTripsByOwnerId(100L, null, null, startDateFrom, startDateTo, null, null))
                .thenThrow(new IllegalArgumentException(
                        "startDateFrom must be less than or equal to startDateTo."
                ));

        mockMvc.perform(get("/api/trips")
                        .param("startDateFrom", "2026-07-10")
                        .param("startDateTo", "2026-07-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message")
                        .value("startDateFrom must be less than or equal to startDateTo."));
    }

    @Test
    void getTripReturnsCommonSuccessResponse() throws Exception {
        when(tripService.getTrip(1L, 100L)).thenReturn(new TripDetailResponse(
                1L,
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                2,
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "SEOGWIPO",
                0L,
                TripVisibility.PRIVATE,
                List.of()
        ));

        mockMvc.perform(get("/api/trips/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.tripId").value(1L))
                .andExpect(jsonPath("$.data.visibility").value("PRIVATE"))
                .andExpect(jsonPath("$.data.liked").doesNotExist())
                .andExpect(jsonPath("$.data.author").doesNotExist())
                .andExpect(jsonPath("$.data.itineraries").isArray());
    }

    @Test
    void updateTripVisibilityReturnsCommonSuccessResponse() throws Exception {
        when(tripService.updateTripVisibility(1L, 100L, TripVisibility.PUBLIC)).thenReturn(new TripResponse(
                1L,
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                2,
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "SEOGWIPO",
                0L,
                TripVisibility.PUBLIC
        ));

        mockMvc.perform(patch("/api/trips/1/visibility")
                        .contentType("application/json")
                        .content("{\"visibility\":\"PUBLIC\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.tripId").value(1L))
                .andExpect(jsonPath("$.data.visibility").value("PUBLIC"));
    }

    @Test
    void updateTripTitleReturnsUpdatedTitle() throws Exception {
        when(tripService.updateTripTitle(1L, 100L, "부모님과 제주 여행")).thenReturn(new TripResponse(
                1L,
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                2,
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "SEOGWIPO",
                0L,
                0L,
                TripVisibility.PRIVATE,
                "부모님과 제주 여행"
        ));

        mockMvc.perform(patch("/api/trips/1/title")
                        .contentType("application/json")
                        .content("{\"title\":\"부모님과 제주 여행\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("부모님과 제주 여행"));

        verify(tripService).updateTripTitle(1L, 100L, "부모님과 제주 여행");
    }

    @Test
    void updateTripConditionsReturnsUpdatedConditions() throws Exception {
        TripConditionUpdateRequest request = new TripConditionUpdateRequest(
                LocalDate.of(2026, 7, 2),
                LocalDate.of(2026, 7, 5),
                LocalTime.of(8, 30),
                LocalTime.of(19, 30),
                TripConcept.FOOD,
                "JEJU_CITY"
        );
        when(tripService.updateTripConditions(1L, 100L, request)).thenReturn(new TripResponse(
                1L,
                "JEJU",
                LocalDate.of(2026, 7, 2),
                LocalDate.of(2026, 7, 5),
                3,
                LocalTime.of(8, 30),
                LocalTime.of(19, 30),
                TripConcept.FOOD,
                Transportation.RENT_CAR,
                "JEJU_CITY",
                0L,
                0L,
                TripVisibility.PRIVATE,
                "제주 맛집 여행"
        ));

        mockMvc.perform(patch("/api/trips/1/conditions")
                        .contentType("application/json")
                        .content("""
                                {
                                  "startDate":"2026-07-02",
                                  "endDate":"2026-07-05",
                                  "dailyStartTime":"08:30",
                                  "dailyEndTime":"19:30",
                                  "concept":"FOOD",
                                  "lastAccommodationArea":"JEJU_CITY"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.startDate[2]").value(2))
                .andExpect(jsonPath("$.data.endDate[2]").value(5))
                .andExpect(jsonPath("$.data.nights").value(3))
                .andExpect(jsonPath("$.data.concept").value("FOOD"))
                .andExpect(jsonPath("$.data.visibility").value("PRIVATE"));

        verify(tripService).updateTripConditions(1L, 100L, request);
    }

    @Test
    void deleteTripReturnsNoContent() throws Exception {
        doNothing().when(tripService).deleteTrip(1L, 100L);

        mockMvc.perform(delete("/api/trips/1"))
                .andExpect(status().isNoContent());

        verify(tripService).deleteTrip(1L, 100L);
    }

    @Test
    void deleteTripReturnsNotFoundErrorResponse() throws Exception {
        doThrow(new NoSuchElementException("Trip not found. tripId=999"))
                .when(tripService)
                .deleteTrip(999L, 100L);

        mockMvc.perform(delete("/api/trips/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Trip not found. tripId=999"));
    }

    private TripResponse trip(Long tripId) {
        return trip(tripId, TripConcept.HEALING);
    }

    private TripResponse trip(Long tripId, TripConcept concept) {
        return new TripResponse(
                tripId,
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                2,
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                concept,
                Transportation.RENT_CAR,
                "SEOGWIPO",
                0L,
                TripVisibility.PRIVATE
        );
    }

    private static class TestLoginMemberIdArgumentResolver implements HandlerMethodArgumentResolver {

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.hasParameterAnnotation(LoginMemberId.class);
        }

        @Override
        public Object resolveArgument(
                MethodParameter parameter,
                ModelAndViewContainer mavContainer,
                NativeWebRequest webRequest,
                WebDataBinderFactory binderFactory
        ) {
            return 100L;
        }
    }
}
