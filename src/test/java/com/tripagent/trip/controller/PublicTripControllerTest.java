package com.tripagent.trip.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tripagent.auth.support.LoginMemberId;
import com.tripagent.auth.support.OptionalLoginMemberId;
import com.tripagent.auth.support.OptionalLoginMemberIdArgumentResolver;
import com.tripagent.auth.support.BearerTokenExtractor;
import com.tripagent.auth.support.AuthenticationException;
import com.tripagent.auth.service.JwtTokenProvider;
import com.tripagent.common.exception.GlobalExceptionHandler;
import com.tripagent.common.response.PageResponse;
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.place.dto.PlaceSummaryResponse;
import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.domain.TripVisibility;
import com.tripagent.trip.dto.PublicTripDetailResponse;
import com.tripagent.trip.dto.PublicTripResponse;
import com.tripagent.trip.dto.PublicTripSort;
import com.tripagent.trip.dto.TripAuthorResponse;
import com.tripagent.trip.dto.TripLikeResponse;
import com.tripagent.trip.service.TripService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

class PublicTripControllerTest {

    private TripService tripService;
    private JwtTokenProvider jwtTokenProvider;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        tripService = org.mockito.Mockito.mock(TripService.class);
        jwtTokenProvider = org.mockito.Mockito.mock(JwtTokenProvider.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new PublicTripController(tripService))
                .setCustomArgumentResolvers(new TestLoginMemberIdArgumentResolver(), new TestOptionalLoginMemberIdArgumentResolver())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void searchPublicTripsReturnsCommonSuccessResponse() throws Exception {
        when(tripService.searchPublicTripPage(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                PublicTripSort.LATEST,
                null,
                null,
                null
        ))
                .thenReturn(new PageResponse<>(List.of(trip(2L)), 0, 20, 1, 1, true, true));

        mockMvc.perform(get("/api/public/trips"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].tripId").value(2L))
                .andExpect(jsonPath("$.data.content[0].visibility").value("PUBLIC"))
                .andExpect(jsonPath("$.data.content[0].liked").value(false))
                .andExpect(jsonPath("$.data.content[0].viewCount").value(0L))
                .andExpect(jsonPath("$.data.page").value(0))
                .andExpect(jsonPath("$.data.size").value(20))
                .andExpect(jsonPath("$.data.totalElements").value(1L))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.first").value(true))
                .andExpect(jsonPath("$.data.last").value(true));
    }

    @Test
    void searchPublicTripsPassesFilters() throws Exception {
        LocalDate startDateFrom = LocalDate.of(2026, 7, 1);
        LocalDate startDateTo = LocalDate.of(2026, 7, 10);
        when(tripService.searchPublicTripPage(
                "JE",
                TripConcept.FOOD,
                startDateFrom,
                startDateTo,
                null,
                null,
                2,
                PublicTripSort.POPULAR,
                1,
                10,
                100L
        ))
                .thenReturn(new PageResponse<>(List.of(trip(3L, TripConcept.FOOD, true)), 1, 10, 11, 2, false, true));

        mockMvc.perform(get("/api/public/trips")
                        .param("destination", "JE")
                        .param("concept", "FOOD")
                        .param("startDateFrom", "2026-07-01")
                        .param("startDateTo", "2026-07-10")
                        .param("nights", "2")
                        .param("sort", "POPULAR")
                        .param("page", "1")
                        .param("size", "10")
                        .header("Authorization", "Bearer access-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].tripId").value(3L))
                .andExpect(jsonPath("$.data.content[0].concept").value("FOOD"))
                .andExpect(jsonPath("$.data.content[0].liked").value(true))
                .andExpect(jsonPath("$.data.content[0].viewCount").value(0L))
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.size").value(10));
    }

    @Test
    void searchPublicTripsTreatsInvalidAuthorizationHeaderAsGuest() throws Exception {
        when(tripService.searchPublicTripPage(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                PublicTripSort.LATEST,
                null,
                null,
                null
        ))
                .thenReturn(new PageResponse<>(List.of(trip(2L)), 0, 20, 1, 1, true, true));

        mockMvc.perform(get("/api/public/trips")
                        .header("Authorization", "Token access-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].liked").value(false));

        verify(tripService).searchPublicTripPage(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                PublicTripSort.LATEST,
                null,
                null,
                null
        );
    }

    @Test
    void searchPublicTripsTreatsBlankBearerTokenAsGuest() throws Exception {
        when(tripService.searchPublicTripPage(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                PublicTripSort.LATEST,
                null,
                null,
                null
        ))
                .thenReturn(new PageResponse<>(List.of(trip(2L)), 0, 20, 1, 1, true, true));

        mockMvc.perform(get("/api/public/trips")
                        .header("Authorization", "Bearer   "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].liked").value(false));

        verify(tripService).searchPublicTripPage(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                PublicTripSort.LATEST,
                null,
                null,
                null
        );
    }

    @Test
    void getPublicTripReturnsCommonSuccessResponse() throws Exception {
        when(tripService.getPublicTrip(1L, 100L)).thenReturn(new PublicTripDetailResponse(
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
                0L,
                TripVisibility.PUBLIC,
                true,
                new TripAuthorResponse(100L, "trip-author"),
                List.of(new ItineraryResponse(
                        10L,
                        1L,
                        100L,
                        new PlaceSummaryResponse(
                                100L,
                                "Seongsan Sunrise Peak",
                                "NATURE",
                                "EAST",
                                "Jeju",
                                33.458,
                                126.942,
                                "UNESCO heritage site."
                        ),
                        1,
                        1,
                        LocalTime.of(9, 0),
                        LocalTime.of(10, 30),
                        0,
                        "첫 일정으로 동쪽 대표 명소를 방문합니다."
                ))
        ));

        mockMvc.perform(get("/api/public/trips/1")
                        .header("Authorization", "Bearer access-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.tripId").value(1L))
                .andExpect(jsonPath("$.data.visibility").value("PUBLIC"))
                .andExpect(jsonPath("$.data.liked").value(true))
                .andExpect(jsonPath("$.data.viewCount").value(0L))
                .andExpect(jsonPath("$.data.author.memberId").value(100L))
                .andExpect(jsonPath("$.data.author.nickname").value("trip-author"))
                .andExpect(jsonPath("$.data.itineraries").isArray())
                .andExpect(jsonPath("$.data.itineraries[0].placeId").value(100L))
                .andExpect(jsonPath("$.data.itineraries[0].place.name").value("Seongsan Sunrise Peak"))
                .andExpect(jsonPath("$.data.itineraries[0].dayNo").value(1))
                .andExpect(jsonPath("$.data.itineraries[0].orderNo").value(1));
    }

    @Test
    void getPublicTripReturnsAuthorForGuest() throws Exception {
        when(tripService.getPublicTrip(1L, null)).thenReturn(new PublicTripDetailResponse(
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
                0L,
                TripVisibility.PUBLIC,
                false,
                new TripAuthorResponse(100L, "trip-author"),
                List.of()
        ));

        mockMvc.perform(get("/api/public/trips/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.tripId").value(1L))
                .andExpect(jsonPath("$.data.liked").value(false))
                .andExpect(jsonPath("$.data.author.memberId").value(100L))
                .andExpect(jsonPath("$.data.author.nickname").value("trip-author"));

        verify(tripService).getPublicTrip(1L, null);
    }

    @Test
    void getPublicTripTreatsInvalidAuthorizationHeaderAsGuest() throws Exception {
        when(tripService.getPublicTrip(1L, null)).thenReturn(new PublicTripDetailResponse(
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
                0L,
                TripVisibility.PUBLIC,
                false,
                new TripAuthorResponse(100L, "trip-author"),
                List.of()
        ));

        mockMvc.perform(get("/api/public/trips/1")
                        .header("Authorization", "Token access-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.liked").value(false))
                .andExpect(jsonPath("$.data.author.memberId").value(100L));

        verify(tripService).getPublicTrip(1L, null);
    }

    @Test
    void getPublicTripReturnsUnauthorizedWhenBearerTokenIsInvalid() throws Exception {
        MockMvc authMockMvc = MockMvcBuilders
                .standaloneSetup(new PublicTripController(tripService))
                .setCustomArgumentResolvers(
                        new TestLoginMemberIdArgumentResolver(),
                        new OptionalLoginMemberIdArgumentResolver(jwtTokenProvider, new BearerTokenExtractor())
                )
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        when(jwtTokenProvider.getMemberId("invalid-token"))
                .thenThrow(new AuthenticationException("Access token is invalid."));

        authMockMvc.perform(get("/api/public/trips/1")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("Access token is invalid."));

        verify(tripService, never()).getPublicTrip(any(), any());
    }

    @Test
    void searchPublicTripsReturnsUnauthorizedWhenBearerTokenIsInvalid() throws Exception {
        MockMvc authMockMvc = MockMvcBuilders
                .standaloneSetup(new PublicTripController(tripService))
                .setCustomArgumentResolvers(
                        new TestLoginMemberIdArgumentResolver(),
                        new OptionalLoginMemberIdArgumentResolver(jwtTokenProvider, new BearerTokenExtractor())
                )
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        when(jwtTokenProvider.getMemberId("invalid-token"))
                .thenThrow(new AuthenticationException("Access token is invalid."));

        authMockMvc.perform(get("/api/public/trips")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("Access token is invalid."));

        verify(tripService, never()).searchPublicTripPage(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
        );
    }

    @Test
    void searchLikedPublicTripsReturnsCommonSuccessResponse() throws Exception {
        when(tripService.searchLikedPublicTripPage(100L, 1, 10))
                .thenReturn(new PageResponse<>(List.of(new PublicTripResponse(
                                3L,
                                "JEJU",
                                LocalDate.of(2026, 7, 1),
                                LocalDate.of(2026, 7, 3),
                                2,
                                LocalTime.of(9, 0),
                                LocalTime.of(18, 0),
                                TripConcept.FOOD,
                                Transportation.RENT_CAR,
                                "SEOGWIPO",
                                5L,
                                0L,
                                TripVisibility.PUBLIC,
                                true,
                                new TripAuthorResponse(200L, "liked-author"),
                                List.of()
                        )),
                        1,
                        10,
                        11,
                        2,
                        false,
                        true
                ));

        mockMvc.perform(get("/api/public/trips/likes")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].tripId").value(3L))
                .andExpect(jsonPath("$.data.content[0].visibility").value("PUBLIC"))
                .andExpect(jsonPath("$.data.content[0].likeCount").value(5L))
                .andExpect(jsonPath("$.data.content[0].liked").value(true))
                .andExpect(jsonPath("$.data.content[0].author.memberId").value(200L))
                .andExpect(jsonPath("$.data.content[0].author.nickname").value("liked-author"))
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.size").value(10))
                .andExpect(jsonPath("$.data.totalElements").value(11L))
                .andExpect(jsonPath("$.data.totalPages").value(2))
                .andExpect(jsonPath("$.data.first").value(false))
                .andExpect(jsonPath("$.data.last").value(true));

        verify(tripService).searchLikedPublicTripPage(100L, 1, 10);
    }

    @Test
    void likePublicTripReturnsCommonSuccessResponse() throws Exception {
        when(tripService.likePublicTrip(1L, 100L))
                .thenReturn(new TripLikeResponse(1L, 100L, 1L, true));

        mockMvc.perform(post("/api/public/trips/1/likes"))
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

        mockMvc.perform(delete("/api/public/trips/1/likes"))
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
        when(tripService.getPublicTrip(999L, null))
                .thenThrow(new NoSuchElementException("Public trip not found. tripId=999"));

        mockMvc.perform(get("/api/public/trips/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Public trip not found. tripId=999"));
    }

    private PublicTripResponse trip(Long tripId) {
        return trip(tripId, TripConcept.HEALING, false);
    }

    private PublicTripResponse trip(Long tripId, TripConcept concept) {
        return trip(tripId, concept, false);
    }

    private PublicTripResponse trip(Long tripId, TripConcept concept, boolean liked) {
        return new PublicTripResponse(
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
                0L,
                TripVisibility.PUBLIC,
                liked,
                null,
                List.of()
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

    private static class TestOptionalLoginMemberIdArgumentResolver implements HandlerMethodArgumentResolver {

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.hasParameterAnnotation(OptionalLoginMemberId.class);
        }

        @Override
        public Object resolveArgument(
                MethodParameter parameter,
                ModelAndViewContainer mavContainer,
                NativeWebRequest webRequest,
                WebDataBinderFactory binderFactory
        ) {
            String authorization = webRequest.getHeader("Authorization");
            if ("Bearer access-token".equals(authorization)) {
                return 100L;
            }

            return null;
        }
    }
}
