package com.tripagent.place.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tripagent.auth.support.LoginMemberId;
import com.tripagent.common.exception.ConflictException;
import com.tripagent.common.exception.GlobalExceptionHandler;
import com.tripagent.common.response.PageResponse;
import com.tripagent.place.domain.PlaceSuggestionStatus;
import com.tripagent.place.dto.AdminPlaceSuggestionResponse;
import com.tripagent.place.dto.PlaceSuggestionRejectRequest;
import com.tripagent.place.dto.PlaceSuggestionApproveRequest;
import com.tripagent.place.dto.PlaceSuggestionApprovalResponse;
import com.tripagent.place.dto.PlaceDuplicateReason;
import com.tripagent.place.dto.PlaceSearchCandidateResponse;
import com.tripagent.place.service.AdminPlaceSuggestionService;
import java.time.LocalDateTime;
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

class AdminPlaceSuggestionControllerTest {

    private AdminPlaceSuggestionService adminPlaceSuggestionService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        adminPlaceSuggestionService = org.mockito.Mockito.mock(AdminPlaceSuggestionService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AdminPlaceSuggestionController(adminPlaceSuggestionService))
                .setCustomArgumentResolvers(new TestLoginMemberIdArgumentResolver())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getSuggestionsReturnsDefaultPendingPage() throws Exception {
        when(adminPlaceSuggestionService.getSuggestions(1L, null, null, null))
                .thenReturn(pageResponse());

        mockMvc.perform(get("/api/admin/place-suggestions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].placeSuggestionId").value(10L))
                .andExpect(jsonPath("$.data.content[0].memberId").value(100L))
                .andExpect(jsonPath("$.data.content[0].memberEmail").value("user@example.com"))
                .andExpect(jsonPath("$.data.content[0].status").value("PENDING"));

        verify(adminPlaceSuggestionService).getSuggestions(1L, null, null, null);
    }

    @Test
    void getSuggestionsPassesStatusAndPageOptions() throws Exception {
        when(adminPlaceSuggestionService.getSuggestions(1L, PlaceSuggestionStatus.REJECTED, 2, 10))
                .thenReturn(emptyPageResponse(2, 10));

        mockMvc.perform(get("/api/admin/place-suggestions")
                        .param("status", "REJECTED")
                        .param("page", "2")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.page").value(2))
                .andExpect(jsonPath("$.data.size").value(10));

        verify(adminPlaceSuggestionService).getSuggestions(1L, PlaceSuggestionStatus.REJECTED, 2, 10);
    }

    @Test
    void rejectSuggestionReturnsRejectedSuggestion() throws Exception {
        when(adminPlaceSuggestionService.rejectSuggestion(
                org.mockito.ArgumentMatchers.eq(1L),
                org.mockito.ArgumentMatchers.eq(10L),
                org.mockito.ArgumentMatchers.any(PlaceSuggestionRejectRequest.class)
        )).thenReturn(rejectedResponse());

        mockMvc.perform(patch("/api/admin/place-suggestions/10/reject")
                        .contentType("application/json")
                        .content("""
                                {
                                  "rejectionReason": "주소가 제주 지역이 아닙니다."
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("REJECTED"))
                .andExpect(jsonPath("$.data.rejectionReason").value("주소가 제주 지역이 아닙니다."))
                .andExpect(jsonPath("$.data.reviewedAt").exists());
    }

    @Test
    void rejectSuggestionRejectsBlankReason() throws Exception {
        mockMvc.perform(patch("/api/admin/place-suggestions/10/reject")
                        .contentType("application/json")
                        .content("""
                                {
                                  "rejectionReason": " "
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }

    @Test
    void searchCandidatesReturnsExternalPlaceCandidates() throws Exception {
        when(adminPlaceSuggestionService.searchCandidates(1L, 10L)).thenReturn(List.of(
                new PlaceSearchCandidateResponse(
                        "7936768",
                        "새별오름",
                        "제주특별자치도 제주시 애월읍 봉성리 산 59-8",
                        "",
                        33.3661276358495,
                        126.3577306657398,
                        "여행 > 관광,명소 > 오름",
                        "http://place.map.kakao.com/7936768",
                        true,
                        200L,
                        PlaceDuplicateReason.EXTERNAL_PLACE_ID
                )
        ));

        mockMvc.perform(get("/api/admin/place-suggestions/10/candidates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].externalPlaceId").value("7936768"))
                .andExpect(jsonPath("$.data[0].name").value("새별오름"))
                .andExpect(jsonPath("$.data[0].latitude").value(33.3661276358495))
                .andExpect(jsonPath("$.data[0].longitude").value(126.3577306657398))
                .andExpect(jsonPath("$.data[0].alreadyRegistered").value(true))
                .andExpect(jsonPath("$.data[0].duplicatePlaceId").value(200L))
                .andExpect(jsonPath("$.data[0].duplicateReason").value("EXTERNAL_PLACE_ID"));

        verify(adminPlaceSuggestionService).searchCandidates(1L, 10L);
    }

    @Test
    void approveSuggestionCreatesPlaceAndReturnsApprovedStatus() throws Exception {
        when(adminPlaceSuggestionService.approveSuggestion(
                org.mockito.ArgumentMatchers.eq(1L),
                org.mockito.ArgumentMatchers.eq(10L),
                org.mockito.ArgumentMatchers.any(PlaceSuggestionApproveRequest.class)
        )).thenReturn(new PlaceSuggestionApprovalResponse(
                10L,
                300L,
                PlaceSuggestionStatus.APPROVED,
                LocalDateTime.of(2026, 7, 17, 12, 0)
        ));

        mockMvc.perform(patch("/api/admin/place-suggestions/10/approve")
                        .contentType("application/json")
                        .content(approvalRequestJson(5)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.placeSuggestionId").value(10L))
                .andExpect(jsonPath("$.data.placeId").value(300L))
                .andExpect(jsonPath("$.data.status").value("APPROVED"));
    }

    @Test
    void approveSuggestionRejectsScoreOutsideAllowedRange() throws Exception {
        mockMvc.perform(patch("/api/admin/place-suggestions/10/approve")
                        .contentType("application/json")
                        .content(approvalRequestJson(6)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }

    @Test
    void approveSuggestionReturnsConflictForDuplicatePlace() throws Exception {
        when(adminPlaceSuggestionService.approveSuggestion(
                org.mockito.ArgumentMatchers.eq(1L),
                org.mockito.ArgumentMatchers.eq(10L),
                org.mockito.ArgumentMatchers.any(PlaceSuggestionApproveRequest.class)
        )).thenThrow(new ConflictException("이미 등록된 장소입니다. placeId=200"));

        mockMvc.perform(patch("/api/admin/place-suggestions/10/approve")
                        .contentType("application/json")
                        .content(approvalRequestJson(5)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("이미 등록된 장소입니다. placeId=200"));
    }

    private String approvalRequestJson(int photoScore) {
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

    private PageResponse<AdminPlaceSuggestionResponse> pageResponse() {
        AdminPlaceSuggestionResponse suggestion = new AdminPlaceSuggestionResponse(
                10L,
                100L,
                "user@example.com",
                "여행자",
                "새별오름",
                "제주특별자치도 제주시",
                "노을 명소",
                PlaceSuggestionStatus.PENDING,
                LocalDateTime.of(2026, 7, 17, 10, 0),
                null,
                null
        );
        return new PageResponse<>(List.of(suggestion), 0, 20, 1, 1, true, true);
    }

    private AdminPlaceSuggestionResponse rejectedResponse() {
        return new AdminPlaceSuggestionResponse(
                10L,
                100L,
                "user@example.com",
                "여행자",
                "새별오름",
                "제주특별자치도 제주시",
                "노을 명소",
                PlaceSuggestionStatus.REJECTED,
                LocalDateTime.of(2026, 7, 17, 10, 0),
                "주소가 제주 지역이 아닙니다.",
                LocalDateTime.of(2026, 7, 17, 11, 0)
        );
    }

    private PageResponse<AdminPlaceSuggestionResponse> emptyPageResponse(int page, int size) {
        return new PageResponse<>(List.of(), page, size, 0, 0, false, true);
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
