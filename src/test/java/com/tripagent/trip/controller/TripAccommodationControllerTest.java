package com.tripagent.trip.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tripagent.accommodation.domain.AccommodationType;
import com.tripagent.accommodation.dto.AccommodationResponse;
import com.tripagent.auth.support.LoginMemberId;
import com.tripagent.common.exception.GlobalExceptionHandler;
import com.tripagent.trip.dto.TripAccommodationReplaceRequest;
import com.tripagent.trip.dto.TripAccommodationResponse;
import com.tripagent.trip.service.TripAccommodationService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

class TripAccommodationControllerTest {

    private TripAccommodationService tripAccommodationService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        tripAccommodationService = org.mockito.Mockito.mock(TripAccommodationService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TripAccommodationController(tripAccommodationService))
                .setCustomArgumentResolvers(new TestLoginMemberIdArgumentResolver())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getTripAccommodationsReturnsDateOrderedItems() throws Exception {
        when(tripAccommodationService.getTripAccommodations(1L, 100L)).thenReturn(List.of(response()));

        mockMvc.perform(get("/api/trips/1/accommodations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].stayDate[0]").value(2026))
                .andExpect(jsonPath("$.data[0].stayDate[1]").value(7))
                .andExpect(jsonPath("$.data[0].stayDate[2]").value(1))
                .andExpect(jsonPath("$.data[0].accommodation.accommodationId").value(10L))
                .andExpect(jsonPath("$.data[0].accommodation.name").value("Test Hotel"));
    }

    @Test
    void replaceTripAccommodationsPassesAuthenticatedMemberId() throws Exception {
        when(tripAccommodationService.replaceTripAccommodations(
                org.mockito.ArgumentMatchers.eq(1L),
                org.mockito.ArgumentMatchers.eq(100L),
                any(TripAccommodationReplaceRequest.class)
        )).thenReturn(List.of(response()));

        mockMvc.perform(put("/api/trips/1/accommodations")
                        .contentType("application/json")
                        .content("""
                                {
                                  "accommodations": [
                                    {"stayDate":"2026-07-01","accommodationId":10}
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].accommodation.accommodationId").value(10L));

        verify(tripAccommodationService).replaceTripAccommodations(
                org.mockito.ArgumentMatchers.eq(1L),
                org.mockito.ArgumentMatchers.eq(100L),
                any(TripAccommodationReplaceRequest.class)
        );
    }

    @Test
    void replaceTripAccommodationsRejectsMissingList() throws Exception {
        mockMvc.perform(put("/api/trips/1/accommodations")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("accommodations: Trip accommodations are required."));
    }

    private TripAccommodationResponse response() {
        return new TripAccommodationResponse(
                100L,
                LocalDate.of(2026, 7, 1),
                new AccommodationResponse(
                        10L,
                        "Test Hotel",
                        AccommodationType.HOTEL,
                        "SOUTH",
                        "Jeju",
                        33.25,
                        126.55,
                        "description",
                        "https://example.com/image.jpg",
                        true
                )
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
