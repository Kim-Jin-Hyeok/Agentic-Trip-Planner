package com.tripagent.place.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tripagent.auth.support.LoginMemberId;
import com.tripagent.common.exception.ConflictException;
import com.tripagent.common.exception.GlobalExceptionHandler;
import com.tripagent.place.dto.PlaceDuplicateReason;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.dto.PlaceSearchCandidateResponse;
import com.tripagent.place.dto.PlaceSuggestionApproveRequest;
import com.tripagent.place.service.AdminPlaceSuggestionService;
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

class AdminPlaceControllerTest {

    private AdminPlaceSuggestionService adminPlaceSuggestionService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        adminPlaceSuggestionService = org.mockito.Mockito.mock(AdminPlaceSuggestionService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AdminPlaceController(adminPlaceSuggestionService))
                .setCustomArgumentResolvers(new TestLoginMemberIdArgumentResolver())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void searchCandidatesReturnsDirectRegistrationCandidates() throws Exception {
        when(adminPlaceSuggestionService.searchDirectCandidates(1L, "새별오름")).thenReturn(List.of(
                new PlaceSearchCandidateResponse(
                        "25274725",
                        "새별오름",
                        "제주특별자치도 제주시",
                        "",
                        33.3661,
                        126.3577,
                        "여행 > 관광",
                        "https://place.map.kakao.com/25274725",
                        false,
                        null,
                        null
                )
        ));

        mockMvc.perform(get("/api/admin/places/candidates").param("keyword", "새별오름"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].externalPlaceId").value("25274725"))
                .andExpect(jsonPath("$.data[0].alreadyRegistered").value(false));

        verify(adminPlaceSuggestionService).searchDirectCandidates(1L, "새별오름");
    }

    @Test
    void registerPlaceReturnsCreatedPlace() throws Exception {
        when(adminPlaceSuggestionService.registerPlace(eq(1L), any(PlaceSuggestionApproveRequest.class)))
                .thenReturn(placeResponse());

        mockMvc.perform(post("/api/admin/places")
                        .contentType("application/json")
                        .content(registrationRequestJson(5)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.placeId").value(301L))
                .andExpect(jsonPath("$.data.name").value("새별오름"));
    }

    @Test
    void registerPlaceRejectsInvalidScore() throws Exception {
        mockMvc.perform(post("/api/admin/places")
                        .contentType("application/json")
                        .content(registrationRequestJson(6)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }

    @Test
    void registerPlaceReturnsConflictForDuplicate() throws Exception {
        when(adminPlaceSuggestionService.registerPlace(eq(1L), any(PlaceSuggestionApproveRequest.class)))
                .thenThrow(new ConflictException("이미 등록된 장소입니다. placeId=200"));

        mockMvc.perform(post("/api/admin/places")
                        .contentType("application/json")
                        .content(registrationRequestJson(5)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("CONFLICT"));
    }

    private String registrationRequestJson(int photoScore) {
        return """
                {
                  "externalPlaceId": "25274725",
                  "name": "새별오름",
                  "address": "제주특별자치도 제주시 애월읍 봉성리 산 59-8",
                  "latitude": 33.366277,
                  "longitude": 126.357731,
                  "category": "NATURE",
                  "region": "WEST",
                  "avgStayMinutes": 90,
                  "indoorYn": false,
                  "parkingYn": true,
                  "rainyDayScore": 2,
                  "healingScore": 5,
                  "foodScore": 1,
                  "cafeScore": 1,
                  "photoScore": %d,
                  "coupleScore": 4,
                  "familyScore": 4,
                  "description": "노을 명소"
                }
                """.formatted(photoScore);
    }

    private PlaceResponse placeResponse() {
        return new PlaceResponse(
                301L, "새별오름", "NATURE", "WEST", "제주특별자치도 제주시",
                33.3661, 126.3577, 90, false, true,
                2, 5, 1, 1, 5, 4, 4, "노을 명소"
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
