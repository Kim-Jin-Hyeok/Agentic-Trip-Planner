package com.tripagent.accommodation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tripagent.accommodation.domain.AccommodationType;
import com.tripagent.accommodation.dto.AccommodationResponse;
import com.tripagent.accommodation.dto.AccommodationSearchCandidateResponse;
import com.tripagent.accommodation.dto.AdminAccommodationCreateRequest;
import com.tripagent.accommodation.dto.AdminAccommodationUpdateRequest;
import com.tripagent.accommodation.service.AdminAccommodationService;
import com.tripagent.auth.support.LoginMemberId;
import com.tripagent.common.exception.ConflictException;
import com.tripagent.common.exception.GlobalExceptionHandler;
import com.tripagent.common.response.PageResponse;
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

class AdminAccommodationControllerTest {

    private AdminAccommodationService adminAccommodationService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        adminAccommodationService = org.mockito.Mockito.mock(AdminAccommodationService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AdminAccommodationController(adminAccommodationService))
                .setCustomArgumentResolvers(new TestLoginMemberIdArgumentResolver())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void searchCandidatesReturnsAccommodationCandidates() throws Exception {
        when(adminAccommodationService.searchCandidates(1L, "제주 호텔")).thenReturn(List.of(
                new AccommodationSearchCandidateResponse(
                        "100", "제주 호텔", "제주특별자치도 제주시", "",
                        33.48, 126.49, "여행 > 숙박 > 호텔",
                        "https://place.map.kakao.com/100", false, null, null
                )
        ));

        mockMvc.perform(get("/api/admin/accommodations/candidates").param("keyword", "제주 호텔"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].externalPlaceId").value("100"))
                .andExpect(jsonPath("$.data[0].alreadyRegistered").value(false));

        verify(adminAccommodationService).searchCandidates(1L, "제주 호텔");
    }

    @Test
    void searchAccommodationsReturnsFilteredPage() throws Exception {
        when(adminAccommodationService.searchAccommodations(
                1L, AccommodationType.HOTEL, "NORTH", "호텔", false, 0, 20
        )).thenReturn(new PageResponse<>(List.of(response(false)), 0, 20, 1, 1, true, true));

        mockMvc.perform(get("/api/admin/accommodations")
                        .param("type", "HOTEL")
                        .param("region", "NORTH")
                        .param("keyword", "호텔")
                        .param("useYn", "false")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].accommodationId").value(20L))
                .andExpect(jsonPath("$.data.content[0].useYn").value(false));
    }

    @Test
    void registerAccommodationReturnsCreatedAccommodation() throws Exception {
        when(adminAccommodationService.registerAccommodation(
                eq(1L), any(AdminAccommodationCreateRequest.class)
        )).thenReturn(response());

        mockMvc.perform(post("/api/admin/accommodations")
                        .contentType("application/json")
                        .content(createRequestJson("HOTEL")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accommodationId").value(20L))
                .andExpect(jsonPath("$.data.accommodationType").value("HOTEL"))
                .andExpect(jsonPath("$.data.thumbnailUrl").value("https://images.example.com/jeju-hotel.jpg"));
    }

    @Test
    void registerAccommodationRejectsMissingType() throws Exception {
        mockMvc.perform(post("/api/admin/accommodations")
                        .contentType("application/json")
                        .content(createRequestJson(null)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }

    @Test
    void registerAccommodationReturnsConflictForDuplicate() throws Exception {
        when(adminAccommodationService.registerAccommodation(
                eq(1L), any(AdminAccommodationCreateRequest.class)
        )).thenThrow(new ConflictException("이미 등록된 숙소입니다. accommodationId=10"));

        mockMvc.perform(post("/api/admin/accommodations")
                        .contentType("application/json")
                        .content(createRequestJson("HOTEL")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("CONFLICT"));
    }

    @Test
    void updateStatusReturnsUpdatedAccommodation() throws Exception {
        when(adminAccommodationService.updateStatus(1L, 20L, false)).thenReturn(response(false));

        mockMvc.perform(patch("/api/admin/accommodations/20/status")
                        .contentType("application/json")
                        .content("{\"useYn\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accommodationId").value(20L))
                .andExpect(jsonPath("$.data.useYn").value(false));
    }

    @Test
    void updateStatusRejectsMissingUseYn() throws Exception {
        mockMvc.perform(patch("/api/admin/accommodations/20/status")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }

    @Test
    void updateAccommodationReturnsUpdatedDetails() throws Exception {
        when(adminAccommodationService.updateAccommodation(
                eq(1L), eq(20L), any(AdminAccommodationUpdateRequest.class)
        )).thenReturn(response());

        mockMvc.perform(patch("/api/admin/accommodations/20")
                        .contentType("application/json")
                        .content(updateRequestJson("HOTEL")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accommodationId").value(20L))
                .andExpect(jsonPath("$.data.thumbnailUrl")
                        .value("https://images.example.com/jeju-hotel.jpg"));
    }

    @Test
    void updateAccommodationRejectsMissingType() throws Exception {
        mockMvc.perform(patch("/api/admin/accommodations/20")
                        .contentType("application/json")
                        .content(updateRequestJson(null)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }

    private String createRequestJson(String accommodationType) {
        String typeJson = accommodationType == null ? "null" : "\"" + accommodationType + "\"";
        return """
                {
                  "externalPlaceId": "100",
                  "name": "제주 호텔",
                  "address": "제주특별자치도 제주시 연동 1",
                  "latitude": 33.48,
                  "longitude": 126.49,
                  "accommodationType": %s,
                  "region": "NORTH",
                  "parkingYn": true,
                  "description": "공항 인근 숙소",
                  "thumbnailUrl": "https://images.example.com/jeju-hotel.jpg",
                  "placeUrl": "https://place.map.kakao.com/100"
                }
                """.formatted(typeJson);
    }

    private String updateRequestJson(String accommodationType) {
        String typeJson = accommodationType == null ? "null" : "\"" + accommodationType + "\"";
        return """
                {
                  "accommodationType": %s,
                  "region": "NORTH",
                  "parkingYn": true,
                  "description": "공항 인근 숙소",
                  "thumbnailUrl": "https://images.example.com/jeju-hotel.jpg"
                }
                """.formatted(typeJson);
    }

    private AccommodationResponse response() {
        return response(true);
    }

    private AccommodationResponse response(boolean useYn) {
        return new AccommodationResponse(
                20L, "제주 호텔", AccommodationType.HOTEL, "NORTH",
                "제주특별자치도 제주시 연동 1", 33.48, 126.49,
                "공항 인근 숙소", "https://images.example.com/jeju-hotel.jpg", true, useYn
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
            return 1L;
        }
    }
}
