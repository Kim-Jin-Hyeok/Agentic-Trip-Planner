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

import com.tripagent.common.exception.GlobalExceptionHandler;
import com.tripagent.itinerary.service.ItineraryGenerateService;
import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.domain.TripVisibility;
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

class TripControllerTest {

    private TripService tripService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        tripService = org.mockito.Mockito.mock(TripService.class);
        ItineraryGenerateService itineraryGenerateService = org.mockito.Mockito.mock(ItineraryGenerateService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TripController(tripService, itineraryGenerateService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void searchTripsReturnsCommonSuccessResponse() throws Exception {
        when(tripService.searchTrips(null, null, null, null, null, null))
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
        when(tripService.searchTrips(
                "JE",
                TripConcept.FOOD,
                startDateFrom,
                startDateTo,
                endDateFrom,
                endDateTo
        ))
                .thenReturn(List.of(trip(3L, TripConcept.FOOD)));

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
        when(tripService.searchTrips(null, null, startDateFrom, startDateTo, null, null))
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
        when(tripService.getTrip(1L)).thenReturn(new TripDetailResponse(
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
                .andExpect(jsonPath("$.data.itineraries").isArray());
    }

    @Test
    void updateTripVisibilityReturnsCommonSuccessResponse() throws Exception {
        when(tripService.updateTripVisibility(1L, TripVisibility.PUBLIC)).thenReturn(new TripResponse(
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
    void deleteTripReturnsNoContent() throws Exception {
        doNothing().when(tripService).deleteTrip(1L);

        mockMvc.perform(delete("/api/trips/1"))
                .andExpect(status().isNoContent());

        verify(tripService).deleteTrip(1L);
    }

    @Test
    void deleteTripReturnsNotFoundErrorResponse() throws Exception {
        doThrow(new NoSuchElementException("Trip not found. tripId=999"))
                .when(tripService)
                .deleteTrip(999L);

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
}
