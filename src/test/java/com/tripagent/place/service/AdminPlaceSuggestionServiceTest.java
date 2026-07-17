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
import com.tripagent.place.adapter.PlaceSearchAdapter;
import com.tripagent.place.adapter.PlaceSearchCandidate;
import com.tripagent.place.domain.PlaceSuggestion;
import com.tripagent.place.domain.PlaceSuggestionStatus;
import com.tripagent.place.dto.AdminPlaceSuggestionResponse;
import com.tripagent.place.dto.PlaceSuggestionRejectRequest;
import com.tripagent.place.dto.PlaceSearchCandidateResponse;
import com.tripagent.place.repository.PlaceSuggestionRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

class AdminPlaceSuggestionServiceTest {

    private PlaceSuggestionRepository placeSuggestionRepository;
    private AdminAuthorizationService adminAuthorizationService;
    private PlaceSearchAdapter placeSearchAdapter;
    private AdminPlaceSuggestionService adminPlaceSuggestionService;

    @BeforeEach
    void setUp() {
        placeSuggestionRepository = org.mockito.Mockito.mock(PlaceSuggestionRepository.class);
        adminAuthorizationService = org.mockito.Mockito.mock(AdminAuthorizationService.class);
        placeSearchAdapter = org.mockito.Mockito.mock(PlaceSearchAdapter.class);
        adminPlaceSuggestionService = new AdminPlaceSuggestionService(
                placeSuggestionRepository,
                adminAuthorizationService,
                placeSearchAdapter
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

    @Test
    void rejectSuggestionChangesPendingSuggestionToRejected() {
        PlaceSuggestion suggestion = suggestion(10L, 100L);
        when(placeSuggestionRepository.findById(10L)).thenReturn(Optional.of(suggestion));

        AdminPlaceSuggestionResponse response = adminPlaceSuggestionService.rejectSuggestion(
                1L,
                10L,
                new PlaceSuggestionRejectRequest("  주소가 제주 지역이 아닙니다.  ")
        );

        verify(adminAuthorizationService).requireAdmin(1L);
        assertThat(response.status()).isEqualTo(PlaceSuggestionStatus.REJECTED);
        assertThat(response.rejectionReason()).isEqualTo("주소가 제주 지역이 아닙니다.");
        assertThat(response.reviewedAt()).isNotNull();
    }

    @Test
    void rejectSuggestionRejectsMissingSuggestion() {
        when(placeSuggestionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminPlaceSuggestionService.rejectSuggestion(
                1L,
                999L,
                new PlaceSuggestionRejectRequest("등록 정보가 부족합니다.")
        ))
                .isInstanceOf(java.util.NoSuchElementException.class)
                .hasMessage("Place suggestion not found. placeSuggestionId=999");
    }

    @Test
    void searchCandidatesReturnsOnlyJejuCandidates() {
        PlaceSuggestion suggestion = suggestion(10L, 100L);
        when(placeSuggestionRepository.findById(10L)).thenReturn(Optional.of(suggestion));
        when(placeSearchAdapter.search("새별오름 제주특별자치도 제주시", 5)).thenReturn(List.of(
                candidate("1", "새별오름", "제주특별자치도 제주시", 33.3661, 126.3577),
                candidate("2", "서울 새별공원", "서울특별시 강남구", 37.5, 127.0)
        ));

        List<PlaceSearchCandidateResponse> response = adminPlaceSuggestionService.searchCandidates(1L, 10L);

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().externalPlaceId()).isEqualTo("1");
        assertThat(response.getFirst().latitude()).isEqualTo(33.3661);
        verify(adminAuthorizationService).requireAdmin(1L);
    }

    @Test
    void searchCandidatesRetriesWithJejuAndNameWhenDetailedQueryHasNoResult() {
        PlaceSuggestion suggestion = suggestion(10L, 100L);
        when(placeSuggestionRepository.findById(10L)).thenReturn(Optional.of(suggestion));
        when(placeSearchAdapter.search("새별오름 제주특별자치도 제주시", 5)).thenReturn(List.of());
        when(placeSearchAdapter.search("제주 새별오름", 5)).thenReturn(List.of(
                candidate("1", "새별오름", "제주특별자치도 제주시", 33.3661, 126.3577)
        ));

        List<PlaceSearchCandidateResponse> response = adminPlaceSuggestionService.searchCandidates(1L, 10L);

        assertThat(response).hasSize(1);
        verify(placeSearchAdapter).search("제주 새별오름", 5);
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

    private PlaceSearchCandidate candidate(
            String externalPlaceId,
            String name,
            String address,
            double latitude,
            double longitude
    ) {
        return new PlaceSearchCandidate(
                externalPlaceId,
                name,
                address,
                address,
                latitude,
                longitude,
                "여행 > 관광",
                "https://place.map.kakao.com/" + externalPlaceId
        );
    }
}
