package com.tripagent.accommodation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.tripagent.accommodation.domain.Accommodation;
import com.tripagent.accommodation.domain.AccommodationType;
import com.tripagent.accommodation.dto.AccommodationDuplicateReason;
import com.tripagent.accommodation.dto.AccommodationResponse;
import com.tripagent.accommodation.dto.AccommodationSearchCandidateResponse;
import com.tripagent.accommodation.dto.AdminAccommodationCreateRequest;
import com.tripagent.accommodation.repository.AccommodationRepository;
import com.tripagent.auth.service.AdminAuthorizationService;
import com.tripagent.common.exception.ConflictException;
import com.tripagent.common.response.PageResponse;
import com.tripagent.place.adapter.PlaceSearchAdapter;
import com.tripagent.place.adapter.PlaceSearchCandidate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

class AdminAccommodationServiceTest {

    private AdminAuthorizationService adminAuthorizationService;
    private PlaceSearchAdapter placeSearchAdapter;
    private AccommodationRepository accommodationRepository;
    private AdminAccommodationService adminAccommodationService;

    @BeforeEach
    void setUp() {
        adminAuthorizationService = org.mockito.Mockito.mock(AdminAuthorizationService.class);
        placeSearchAdapter = org.mockito.Mockito.mock(PlaceSearchAdapter.class);
        accommodationRepository = org.mockito.Mockito.mock(AccommodationRepository.class);
        adminAccommodationService = new AdminAccommodationService(
                adminAuthorizationService,
                placeSearchAdapter,
                accommodationRepository
        );
    }

    @Test
    void searchCandidatesReturnsOnlyJejuAccommodationsAndDuplicateInformation() {
        PlaceSearchCandidate accommodationCandidate = candidate(
                "100",
                "제주 호텔",
                "제주특별자치도 제주시",
                "여행 > 숙박 > 호텔",
                33.48,
                126.49
        );
        Accommodation duplicate = accommodation(10L, "제주 호텔", "제주특별자치도 제주시", 33.48, 126.49);
        duplicate.linkExternalReference("KAKAO_LOCAL", "100", "https://place.map.kakao.com/100");
        when(placeSearchAdapter.search("제주 숙박 제주 호텔", 10)).thenReturn(List.of(
                accommodationCandidate,
                candidate("200", "서울 호텔", "서울특별시", "여행 > 숙박 > 호텔", 37.5, 127.0),
                candidate("300", "제주 카페", "제주특별자치도 제주시", "음식점 > 카페", 33.4, 126.5)
        ));
        when(accommodationRepository.findFirstByExternalProviderAndExternalPlaceId("KAKAO_LOCAL", "100"))
                .thenReturn(Optional.of(duplicate));

        List<AccommodationSearchCandidateResponse> responses = adminAccommodationService
                .searchCandidates(1L, " 제주 호텔 ");

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().alreadyRegistered()).isTrue();
        assertThat(responses.getFirst().duplicateAccommodationId()).isEqualTo(10L);
        assertThat(responses.getFirst().duplicateReason())
                .isEqualTo(AccommodationDuplicateReason.EXTERNAL_PLACE_ID);
        verify(adminAuthorizationService).requireAdmin(1L);
    }

    @Test
    void searchCandidatesRejectsBlankKeyword() {
        assertThatThrownBy(() -> adminAccommodationService.searchCandidates(1L, " "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Accommodation search keyword is required.");

        verifyNoInteractions(placeSearchAdapter);
    }

    @Test
    void searchAccommodationsRequiresAdminAndReturnsFilteredPage() {
        Accommodation accommodation = accommodation(
                10L, "제주 호텔", "제주특별자치도 제주시", 33.48, 126.49
        );
        PageRequest pageable = PageRequest.of(1, 10);
        when(accommodationRepository.searchAdminAccommodations(
                true, AccommodationType.HOTEL, "NORTH", "호텔", pageable
        )).thenReturn(new PageImpl<>(List.of(accommodation), pageable, 11));

        PageResponse<AccommodationResponse> response = adminAccommodationService.searchAccommodations(
                1L, AccommodationType.HOTEL, " north ", " 호텔 ", true, 1, 10
        );

        verify(adminAuthorizationService).requireAdmin(1L);
        assertThat(response.content()).hasSize(1);
        assertThat(response.content().getFirst().useYn()).isTrue();
        assertThat(response.totalElements()).isEqualTo(11);
    }

    @Test
    void updateStatusDeactivatesAccommodation() {
        Accommodation accommodation = accommodation(
                10L, "제주 호텔", "제주특별자치도 제주시", 33.48, 126.49
        );
        when(accommodationRepository.findById(10L)).thenReturn(Optional.of(accommodation));

        AccommodationResponse response = adminAccommodationService.updateStatus(1L, 10L, false);

        verify(adminAuthorizationService).requireAdmin(1L);
        assertThat(accommodation.getUseYn()).isFalse();
        assertThat(response.useYn()).isFalse();
    }

    @Test
    void updateStatusRejectsUnknownAccommodation() {
        when(accommodationRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminAccommodationService.updateStatus(1L, 10L, false))
                .isInstanceOf(java.util.NoSuchElementException.class)
                .hasMessage("Accommodation not found. accommodationId=10");
    }

    @Test
    void registerAccommodationCreatesActiveAccommodationWithExternalReference() {
        when(accommodationRepository.saveAndFlush(any(Accommodation.class))).thenAnswer(invocation -> {
            Accommodation saved = invocation.getArgument(0);
            ReflectionTestUtils.setField(saved, "accommodationId", 20L);
            return saved;
        });

        AccommodationResponse response = adminAccommodationService.registerAccommodation(1L, createRequest());

        ArgumentCaptor<Accommodation> captor = ArgumentCaptor.forClass(Accommodation.class);
        verify(accommodationRepository).saveAndFlush(captor.capture());
        Accommodation saved = captor.getValue();
        assertThat(response.accommodationId()).isEqualTo(20L);
        assertThat(saved.getAccommodationType()).isEqualTo(AccommodationType.HOTEL);
        assertThat(saved.getRegion()).isEqualTo("NORTH");
        assertThat(saved.getUseYn()).isTrue();
        assertThat(saved.getExternalProvider()).isEqualTo("KAKAO_LOCAL");
        assertThat(saved.getExternalPlaceId()).isEqualTo("100");
        assertThat(saved.getExternalPlaceUrl()).isEqualTo("https://place.map.kakao.com/100");
        assertThat(saved.getThumbnailUrl()).isEqualTo("https://images.example.com/jeju-hotel.jpg");
        verify(adminAuthorizationService).requireAdmin(1L);
    }

    @Test
    void registerAccommodationRejectsNonHttpThumbnailUrl() {
        AdminAccommodationCreateRequest request = new AdminAccommodationCreateRequest(
                "100", "제주 호텔", "제주특별자치도 제주시 연동 1", 33.48, 126.49,
                AccommodationType.HOTEL, "NORTH", true, "설명",
                "file:///tmp/hotel.jpg", "https://place.map.kakao.com/100"
        );

        assertThatThrownBy(() -> adminAccommodationService.registerAccommodation(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Accommodation thumbnail URL must use HTTP or HTTPS.");
    }

    @Test
    void registerAccommodationRejectsExistingExternalPlace() {
        Accommodation duplicate = accommodation(10L, "제주 호텔", "제주특별자치도 제주시", 33.48, 126.49);
        when(accommodationRepository.findFirstByExternalProviderAndExternalPlaceId("KAKAO_LOCAL", "100"))
                .thenReturn(Optional.of(duplicate));

        assertThatThrownBy(() -> adminAccommodationService.registerAccommodation(1L, createRequest()))
                .isInstanceOf(ConflictException.class)
                .hasMessage("이미 등록된 숙소입니다. accommodationId=10");
    }

    @Test
    void registerAccommodationRejectsSameNameWithinFiftyMeters() {
        Accommodation duplicate = accommodation(11L, "제주호텔", "제주특별자치도 제주시 다른 주소", 33.4801, 126.4901);
        when(accommodationRepository.findByLatitudeBetweenAndLongitudeBetween(
                anyDouble(), anyDouble(), anyDouble(), anyDouble()
        )).thenReturn(List.of(duplicate));

        assertThatThrownBy(() -> adminAccommodationService.registerAccommodation(1L, createRequest()))
                .isInstanceOf(ConflictException.class)
                .hasMessage("이미 등록된 숙소입니다. accommodationId=11");
    }

    @Test
    void registerAccommodationRejectsLocationOutsideJeju() {
        AdminAccommodationCreateRequest request = new AdminAccommodationCreateRequest(
                "100", "서울 호텔", "서울특별시 강남구", 37.5, 127.0,
                AccommodationType.HOTEL, "NORTH", true, "설명", null, null
        );

        assertThatThrownBy(() -> adminAccommodationService.registerAccommodation(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Accommodation address must be in Jeju.");
    }

    @Test
    void registerAccommodationConvertsUniqueConstraintViolationToConflict() {
        when(accommodationRepository.saveAndFlush(any(Accommodation.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> adminAccommodationService.registerAccommodation(1L, createRequest()))
                .isInstanceOf(ConflictException.class)
                .hasMessage("동일한 숙소가 이미 등록되어 있습니다.");
    }

    private AdminAccommodationCreateRequest createRequest() {
        return new AdminAccommodationCreateRequest(
                "100",
                "제주 호텔",
                "제주특별자치도 제주시 연동 1",
                33.48,
                126.49,
                AccommodationType.HOTEL,
                " north ",
                true,
                "공항 인근 숙소",
                "https://images.example.com/jeju-hotel.jpg",
                "https://place.map.kakao.com/100"
        );
    }

    private Accommodation accommodation(
            Long id,
            String name,
            String address,
            double latitude,
            double longitude
    ) {
        Accommodation accommodation = Accommodation.create(
                name,
                AccommodationType.HOTEL,
                "NORTH",
                address,
                latitude,
                longitude,
                null,
                null,
                true,
                true
        );
        ReflectionTestUtils.setField(accommodation, "accommodationId", id);
        return accommodation;
    }

    private PlaceSearchCandidate candidate(
            String externalPlaceId,
            String name,
            String address,
            String category,
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
                category,
                "https://place.map.kakao.com/" + externalPlaceId
        );
    }
}
