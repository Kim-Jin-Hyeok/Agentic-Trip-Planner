package com.tripagent.place.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tripagent.auth.support.LoginMemberId;
import com.tripagent.common.exception.GlobalExceptionHandler;
import com.tripagent.common.response.PageResponse;
import com.tripagent.place.domain.PlaceSuggestionStatus;
import com.tripagent.place.dto.AdminPlaceSuggestionResponse;
import com.tripagent.place.dto.PlaceSuggestionRejectRequest;
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
