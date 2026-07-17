package com.tripagent.place.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tripagent.auth.service.AdminAuthorizationService;
import com.tripagent.common.response.PageResponse;
import com.tripagent.member.domain.Member;
import com.tripagent.place.domain.PlaceSuggestion;
import com.tripagent.place.domain.PlaceSuggestionStatus;
import com.tripagent.place.dto.AdminPlaceSuggestionResponse;
import com.tripagent.place.repository.PlaceSuggestionRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

class AdminPlaceSuggestionServiceTest {

    private PlaceSuggestionRepository placeSuggestionRepository;
    private AdminAuthorizationService adminAuthorizationService;
    private AdminPlaceSuggestionService adminPlaceSuggestionService;

    @BeforeEach
    void setUp() {
        placeSuggestionRepository = org.mockito.Mockito.mock(PlaceSuggestionRepository.class);
        adminAuthorizationService = org.mockito.Mockito.mock(AdminAuthorizationService.class);
        adminPlaceSuggestionService = new AdminPlaceSuggestionService(
                placeSuggestionRepository,
                adminAuthorizationService
        );
    }

    @Test
    void getSuggestionsUsesPendingAndDefaultPageOptions() {
        PlaceSuggestion suggestion = suggestion(10L, 100L);
        when(placeSuggestionRepository.findByStatusOrderByPlaceSuggestionIdDesc(
                eq(PlaceSuggestionStatus.PENDING), any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of(suggestion)));

        PageResponse<AdminPlaceSuggestionResponse> response = adminPlaceSuggestionService
                .getSuggestions(1L, null, null, null);

        assertThat(response.content()).hasSize(1);
        assertThat(response.content().getFirst().placeSuggestionId()).isEqualTo(10L);
        assertThat(response.content().getFirst().memberId()).isEqualTo(100L);
        assertThat(response.content().getFirst().memberEmail()).isEqualTo("user@example.com");
        verify(adminAuthorizationService).requireAdmin(1L);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(placeSuggestionRepository).findByStatusOrderByPlaceSuggestionIdDesc(
                eq(PlaceSuggestionStatus.PENDING), pageableCaptor.capture()
        );
        assertThat(pageableCaptor.getValue().getPageNumber()).isZero();
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(20);
    }

    @Test
    void getSuggestionsUsesRequestedStatusAndPageOptions() {
        when(placeSuggestionRepository.findByStatusOrderByPlaceSuggestionIdDesc(
                eq(PlaceSuggestionStatus.REJECTED), any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of()));

        adminPlaceSuggestionService.getSuggestions(1L, PlaceSuggestionStatus.REJECTED, 2, 10);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(placeSuggestionRepository).findByStatusOrderByPlaceSuggestionIdDesc(
                eq(PlaceSuggestionStatus.REJECTED), pageableCaptor.capture()
        );
        assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(2);
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(10);
    }

    @Test
    void getSuggestionsRejectsInvalidPage() {
        assertThatThrownBy(() -> adminPlaceSuggestionService.getSuggestions(1L, null, -1, 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Page must be greater than or equal to 0.");
    }

    @Test
    void getSuggestionsRejectsInvalidPageSize() {
        assertThatThrownBy(() -> adminPlaceSuggestionService.getSuggestions(1L, null, 0, 51))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Page size must be between 1 and 50.");
    }

    private PlaceSuggestion suggestion(Long suggestionId, Long memberId) {
        Member member = Member.create("user@example.com", "user", "password-hash");
        ReflectionTestUtils.setField(member, "memberId", memberId);
        PlaceSuggestion suggestion = PlaceSuggestion.create(
                member,
                "새별오름",
                "제주특별자치도 제주시",
                "노을 명소"
        );
        ReflectionTestUtils.setField(suggestion, "placeSuggestionId", suggestionId);
        return suggestion;
    }
}
