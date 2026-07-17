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
import com.tripagent.common.exception.GlobalExceptionHandler;
import com.tripagent.place.domain.PlaceSuggestionStatus;
import com.tripagent.place.dto.PlaceSuggestionCreateRequest;
import com.tripagent.place.dto.PlaceSuggestionResponse;
import com.tripagent.place.service.PlaceSuggestionService;
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

class PlaceSuggestionControllerTest {

    private PlaceSuggestionService placeSuggestionService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        placeSuggestionService = org.mockito.Mockito.mock(PlaceSuggestionService.class);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new PlaceSuggestionController(placeSuggestionService))
                .setCustomArgumentResolvers(new TestLoginMemberIdArgumentResolver())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createSuggestionUsesAuthenticatedMemberAndReturnsCreatedResponse() throws Exception {
        when(placeSuggestionService.createSuggestion(eq(100L), any(PlaceSuggestionCreateRequest.class)))
                .thenReturn(response(10L, "새별오름"));

        mockMvc.perform(post("/api/place-suggestions")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "새별오름",
                                  "address": "제주특별자치도 제주시 애월읍 봉성리",
                                  "description": "노을을 보기 좋은 장소"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.placeSuggestionId").value(10L))
                .andExpect(jsonPath("$.data.name").value("새별오름"))
                .andExpect(jsonPath("$.data.status").value("PENDING"));

        verify(placeSuggestionService).createSuggestion(eq(100L), any(PlaceSuggestionCreateRequest.class));
    }

    @Test
    void getMySuggestionsUsesAuthenticatedMember() throws Exception {
        when(placeSuggestionService.getMySuggestions(100L)).thenReturn(List.of(response(10L, "새별오름")));

        mockMvc.perform(get("/api/place-suggestions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].placeSuggestionId").value(10L))
                .andExpect(jsonPath("$.data[0].name").value("새별오름"));

        verify(placeSuggestionService).getMySuggestions(100L);
    }

    @Test
    void createSuggestionRejectsBlankNameAndAddress() throws Exception {
        mockMvc.perform(post("/api/place-suggestions")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": " ",
                                  "address": " "
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }

    @Test
    void createSuggestionRejectsDescriptionOverMaximumLength() throws Exception {
        String description = "a".repeat(1001);

        mockMvc.perform(post("/api/place-suggestions")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "새별오름",
                                  "address": "제주특별자치도 제주시",
                                  "description": "%s"
                                }
                                """.formatted(description)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("description: Place suggestion description must be 1000 characters or less."));
    }

    private PlaceSuggestionResponse response(Long suggestionId, String name) {
        return new PlaceSuggestionResponse(
                suggestionId,
                name,
                "제주특별자치도 제주시",
                "설명",
                PlaceSuggestionStatus.PENDING,
                LocalDateTime.of(2026, 7, 17, 10, 0)
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
