package com.tripagent.place.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.tripagent.auth.service.AdminAuthorizationService;
import com.tripagent.common.exception.ConflictException;
import com.tripagent.common.response.PageResponse;
import com.tripagent.member.domain.Member;
import com.tripagent.place.adapter.PlaceSearchAdapter;
import com.tripagent.place.adapter.PlaceSearchCandidate;
import com.tripagent.place.domain.Place;
import com.tripagent.place.domain.PlaceSuggestion;
import com.tripagent.place.domain.PlaceSuggestionStatus;
import com.tripagent.place.dto.AdminPlaceSuggestionResponse;
import com.tripagent.place.dto.PlaceSuggestionRejectRequest;
import com.tripagent.place.dto.PlaceSuggestionApproveRequest;
import com.tripagent.place.dto.PlaceSuggestionApprovalResponse;
import com.tripagent.place.dto.PlaceSearchCandidateResponse;
import com.tripagent.place.dto.PlaceDuplicateReason;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.repository.PlaceRepository;
import com.tripagent.place.repository.PlaceSuggestionRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

class AdminPlaceSuggestionServiceTest {

    private PlaceSuggestionRepository placeSuggestionRepository;
    private AdminAuthorizationService adminAuthorizationService;
    private PlaceSearchAdapter placeSearchAdapter;
    private PlaceRepository placeRepository;
    private AdminPlaceSuggestionService adminPlaceSuggestionService;

    @BeforeEach
    void setUp() {
        placeSuggestionRepository = org.mockito.Mockito.mock(PlaceSuggestionRepository.class);
        adminAuthorizationService = org.mockito.Mockito.mock(AdminAuthorizationService.class);
        placeSearchAdapter = org.mockito.Mockito.mock(PlaceSearchAdapter.class);
        placeRepository = org.mockito.Mockito.mock(PlaceRepository.class);
        adminPlaceSuggestionService = new AdminPlaceSuggestionService(
                placeSuggestionRepository,
                adminAuthorizationService,
                placeSearchAdapter,
                placeRepository
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
        assertThat(response.getFirst().alreadyRegistered()).isFalse();
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

    @Test
    void searchCandidatesDetectsDuplicateByExternalPlaceId() {
        PlaceSuggestion suggestion = suggestion(10L, 100L);
        Place duplicatePlace = place(200L, "새별오름", "제주특별자치도 제주시", 33.3661, 126.3577);
        duplicatePlace.linkExternalReference("KAKAO_LOCAL", "1");
        when(placeSuggestionRepository.findById(10L)).thenReturn(Optional.of(suggestion));
        when(placeSearchAdapter.search("새별오름 제주특별자치도 제주시", 5)).thenReturn(List.of(
                candidate("1", "새별오름", "제주특별자치도 제주시", 33.3661, 126.3577)
        ));
        when(placeRepository.findFirstByExternalProviderAndExternalPlaceId("KAKAO_LOCAL", "1"))
                .thenReturn(Optional.of(duplicatePlace));

        PlaceSearchCandidateResponse response = adminPlaceSuggestionService
                .searchCandidates(1L, 10L)
                .getFirst();

        assertThat(response.alreadyRegistered()).isTrue();
        assertThat(response.duplicatePlaceId()).isEqualTo(200L);
        assertThat(response.duplicateReason()).isEqualTo(PlaceDuplicateReason.EXTERNAL_PLACE_ID);
    }

    @Test
    void searchCandidatesDetectsDuplicateByNameAndAddress() {
        PlaceSuggestion suggestion = suggestion(10L, 100L);
        Place duplicatePlace = place(201L, "새별오름", "제주특별자치도 제주시", 33.3661, 126.3577);
        when(placeSuggestionRepository.findById(10L)).thenReturn(Optional.of(suggestion));
        when(placeSearchAdapter.search("새별오름 제주특별자치도 제주시", 5)).thenReturn(List.of(
                candidate("1", "새별오름", "제주특별자치도 제주시", 33.3661, 126.3577)
        ));
        when(placeRepository.findFirstByNameIgnoreCaseAndAddressIgnoreCaseOrderByPlaceIdDesc(
                "새별오름",
                "제주특별자치도 제주시"
        )).thenReturn(Optional.of(duplicatePlace));

        PlaceSearchCandidateResponse response = adminPlaceSuggestionService
                .searchCandidates(1L, 10L)
                .getFirst();

        assertThat(response.alreadyRegistered()).isTrue();
        assertThat(response.duplicateReason()).isEqualTo(PlaceDuplicateReason.NAME_AND_ADDRESS);
    }

    @Test
    void searchCandidatesDetectsSameNameWithinFiftyMeters() {
        PlaceSuggestion suggestion = suggestion(10L, 100L);
        Place duplicatePlace = place(202L, "새별 오름", "제주특별자치도 제주시 다른 주소", 33.3663, 126.3578);
        when(placeSuggestionRepository.findById(10L)).thenReturn(Optional.of(suggestion));
        when(placeSearchAdapter.search("새별오름 제주특별자치도 제주시", 5)).thenReturn(List.of(
                candidate("1", "새별오름", "제주특별자치도 제주시", 33.3661, 126.3577)
        ));
        when(placeRepository.findByLatitudeBetweenAndLongitudeBetween(
                anyDouble(),
                anyDouble(),
                anyDouble(),
                anyDouble()
        )).thenReturn(List.of(duplicatePlace));

        PlaceSearchCandidateResponse response = adminPlaceSuggestionService
                .searchCandidates(1L, 10L)
                .getFirst();

        assertThat(response.alreadyRegistered()).isTrue();
        assertThat(response.duplicateReason()).isEqualTo(PlaceDuplicateReason.NEARBY_NAME);
    }

    @Test
    void approveSuggestionCreatesActivePlaceAndApprovesSuggestion() {
        PlaceSuggestion suggestion = suggestion(10L, 100L);
        when(placeSuggestionRepository.findById(10L)).thenReturn(Optional.of(suggestion));
        when(placeRepository.saveAndFlush(any(Place.class))).thenAnswer(invocation -> {
            Place savedPlace = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedPlace, "placeId", 300L);
            return savedPlace;
        });

        PlaceSuggestionApprovalResponse response = adminPlaceSuggestionService.approveSuggestion(
                1L,
                10L,
                approveRequest()
        );

        ArgumentCaptor<Place> placeCaptor = ArgumentCaptor.forClass(Place.class);
        verify(placeRepository).saveAndFlush(placeCaptor.capture());
        Place savedPlace = placeCaptor.getValue();
        assertThat(savedPlace.getName()).isEqualTo("새별오름");
        assertThat(savedPlace.getCategory()).isEqualTo("NATURE");
        assertThat(savedPlace.getRegion()).isEqualTo("WEST");
        assertThat(savedPlace.getUseYn()).isTrue();
        assertThat(savedPlace.getExternalProvider()).isEqualTo("KAKAO_LOCAL");
        assertThat(savedPlace.getExternalPlaceId()).isEqualTo("25274725");
        assertThat(response.placeId()).isEqualTo(300L);
        assertThat(response.status()).isEqualTo(PlaceSuggestionStatus.APPROVED);
        assertThat(suggestion.getStatus()).isEqualTo(PlaceSuggestionStatus.APPROVED);
    }

    @Test
    void approveSuggestionRejectsDuplicateCandidate() {
        PlaceSuggestion suggestion = suggestion(10L, 100L);
        Place duplicatePlace = place(200L, "새별오름", "제주특별자치도 제주시", 33.3661, 126.3577);
        duplicatePlace.linkExternalReference("KAKAO_LOCAL", "25274725");
        when(placeSuggestionRepository.findById(10L)).thenReturn(Optional.of(suggestion));
        when(placeRepository.findFirstByExternalProviderAndExternalPlaceId("KAKAO_LOCAL", "25274725"))
                .thenReturn(Optional.of(duplicatePlace));

        assertThatThrownBy(() -> adminPlaceSuggestionService.approveSuggestion(1L, 10L, approveRequest()))
                .isInstanceOf(ConflictException.class)
                .hasMessage("이미 등록된 장소입니다. placeId=200");
    }

    @Test
    void approveSuggestionRejectsLocationOutsideJeju() {
        PlaceSuggestion suggestion = suggestion(10L, 100L);
        when(placeSuggestionRepository.findById(10L)).thenReturn(Optional.of(suggestion));
        PlaceSuggestionApproveRequest request = approveRequest(
                "서울특별시 강남구",
                37.5,
                127.0
        );

        assertThatThrownBy(() -> adminPlaceSuggestionService.approveSuggestion(1L, 10L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Place address must be in Jeju.");
    }

    @Test
    void approveSuggestionConvertsUniqueConstraintViolationToConflict() {
        PlaceSuggestion suggestion = suggestion(10L, 100L);
        when(placeSuggestionRepository.findById(10L)).thenReturn(Optional.of(suggestion));
        when(placeRepository.saveAndFlush(any(Place.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate external place"));

        assertThatThrownBy(() -> adminPlaceSuggestionService.approveSuggestion(1L, 10L, approveRequest()))
                .isInstanceOf(ConflictException.class)
                .hasMessage("동일한 장소가 이미 등록되어 있습니다.");
    }

    @Test
    void searchDirectCandidatesSearchesJejuAndReturnsDuplicateInformation() {
        Place duplicatePlace = place(200L, "새별오름", "제주특별자치도 제주시", 33.3661, 126.3577);
        duplicatePlace.linkExternalReference("KAKAO_LOCAL", "25274725");
        when(placeSearchAdapter.search("제주 새별오름", 5)).thenReturn(List.of(
                candidate("25274725", "새별오름", "제주특별자치도 제주시", 33.3661, 126.3577)
        ));
        when(placeRepository.findFirstByExternalProviderAndExternalPlaceId("KAKAO_LOCAL", "25274725"))
                .thenReturn(Optional.of(duplicatePlace));

        List<PlaceSearchCandidateResponse> responses = adminPlaceSuggestionService
                .searchDirectCandidates(1L, " 새별오름 ");

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().alreadyRegistered()).isTrue();
        assertThat(responses.getFirst().duplicatePlaceId()).isEqualTo(200L);
        verify(adminAuthorizationService).requireAdmin(1L);
    }

    @Test
    void searchDirectCandidatesRejectsBlankKeyword() {
        assertThatThrownBy(() -> adminPlaceSuggestionService.searchDirectCandidates(1L, " "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Place search keyword is required.");

        verifyNoInteractions(placeSearchAdapter);
    }

    @Test
    void registerPlaceCreatesActivePlaceWithoutSuggestion() {
        when(placeRepository.saveAndFlush(any(Place.class))).thenAnswer(invocation -> {
            Place savedPlace = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedPlace, "placeId", 301L);
            return savedPlace;
        });

        PlaceResponse response = adminPlaceSuggestionService.registerPlace(1L, approveRequest());

        assertThat(response.placeId()).isEqualTo(301L);
        assertThat(response.name()).isEqualTo("새별오름");
        verify(adminAuthorizationService).requireAdmin(1L);
        verify(placeRepository).saveAndFlush(any(Place.class));
        verifyNoInteractions(placeSuggestionRepository);
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

    private Place place(
            Long placeId,
            String name,
            String address,
            double latitude,
            double longitude
    ) {
        Place place = Place.create(
                name,
                "NATURE",
                "WEST",
                address,
                latitude,
                longitude,
                60,
                false,
                true,
                1,
                5,
                1,
                1,
                5,
                4,
                4,
                "설명",
                true
        );
        ReflectionTestUtils.setField(place, "placeId", placeId);
        return place;
    }

    private PlaceSuggestionApproveRequest approveRequest() {
        return approveRequest("제주특별자치도 제주시 애월읍 봉성리 산 59-8", 33.366277, 126.357731);
    }

    private PlaceSuggestionApproveRequest approveRequest(String address, double latitude, double longitude) {
        return new PlaceSuggestionApproveRequest(
                "25274725",
                "새별오름",
                address,
                latitude,
                longitude,
                "NATURE",
                "WEST",
                90,
                false,
                true,
                2,
                5,
                1,
                1,
                5,
                4,
                4,
                "노을 명소"
        );
    }
}
