package com.tripagent.trip.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tripagent.common.exception.GlobalExceptionHandler;
import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.domain.TripVisibility;
import com.tripagent.trip.dto.PublicTripSort;
import com.tripagent.trip.dto.TripDetailResponse;
import com.tripagent.trip.dto.TripLikeResponse;
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

class PublicTripControllerTest {

    private TripService tripService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        tripService = org.mockito.Mockito.mock(TripService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new PublicTripController(tripService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void searchPublicTripsReturnsCommonSuccessResponse() throws Exception {
        when(tripService.searchPublicTrips(null, null, null, null, null, null, null, PublicTripSort.LATEST))
                .thenReturn(List.of(trip(2L)));

        mockMvc.perform(get("/api/public/trips"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].tripId").value(2L))
                .andExpect(jsonPath("$.data[0].visibility").value("PUBLIC"));
    }

    @Test
    void searchPublicTripsPassesFilters() throws Exception {
        LocalDate startDateFrom = LocalDate.of(2026, 7, 1);
        LocalDate startDateTo = LocalDate.of(2026, 7, 10);
        when(tripService.searchPublicTrips(
                "JE",
                TripConcept.FOOD,
                startDateFrom,
                startDateTo,
                null,
                null,
                2,
                PublicTripSort.POPULAR
        ))
                .thenReturn(List.of(trip(3L, TripConcept.FOOD)));

        mockMvc.perform(get("/api/public/trips")
                        .param("destination", "JE")
                        .param("concept", "FOOD")
                        .param("startDateFrom", "2026-07-01")
                        .param("startDateTo", "2026-07-10")
                        .param("nights", "2")
                        .param("sort", "POPULAR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].tripId").value(3L))
                .andExpect(jsonPath("$.data[0].concept").value("FOOD"));
    }

    @Test
    void getPublicTripReturnsCommonSuccessResponse() throws Exception {
        when(tripService.getPublicTrip(1L)).thenReturn(new TripDetailResponse(
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
                3L,
                TripVisibility.PUBLIC,
                List.of()
        ));

        mockMvc.perform(get("/api/public/trips/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.tripId").value(1L))
                .andExpect(jsonPath("$.data.visibility").value("PUBLIC"))
                .andExpect(jsonPath("$.data.itineraries").isArray());
    }

    @Test
    void likePublicTripReturnsCommonSuccessResponse() throws Exception {
        when(tripService.likePublicTrip(1L, 100L))
                .thenReturn(new TripLikeResponse(1L, 100L, 1L, true));

        mockMvc.perform(post("/api/public/trips/1/likes")
                        .param("userId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.tripId").value(1L))
                .andExpect(jsonPath("$.data.userId").value(100L))
                .andExpect(jsonPath("$.data.likeCount").value(1L))
                .andExpect(jsonPath("$.data.liked").value(true));

        verify(tripService).likePublicTrip(1L, 100L);
    }

    @Test
    void unlikePublicTripReturnsCommonSuccessResponse() throws Exception {
        when(tripService.unlikePublicTrip(1L, 100L))
                .thenReturn(new TripLikeResponse(1L, 100L, 0L, false));

        mockMvc.perform(delete("/api/public/trips/1/likes")
                        .param("userId", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.tripId").value(1L))
                .andExpect(jsonPath("$.data.userId").value(100L))
                .andExpect(jsonPath("$.data.likeCount").value(0L))
                .andExpect(jsonPath("$.data.liked").value(false));

        verify(tripService).unlikePublicTrip(1L, 100L);
    }

    @Test
    void getPublicTripReturnsNotFoundWhenTripIsPrivateOrMissing() throws Exception {
        when(tripService.getPublicTrip(999L))
                .thenThrow(new NoSuchElementException("Public trip not found. tripId=999"));

        mockMvc.perform(get("/api/public/trips/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Public trip not found. tripId=999"));
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
                TripVisibility.PUBLIC
        );
    }
}
