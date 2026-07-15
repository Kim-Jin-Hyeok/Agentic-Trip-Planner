package com.tripagent.accommodation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tripagent.accommodation.domain.Accommodation;
import com.tripagent.accommodation.domain.AccommodationType;
import com.tripagent.accommodation.dto.AccommodationResponse;
import com.tripagent.accommodation.repository.AccommodationRepository;
import com.tripagent.common.response.PageResponse;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

class AccommodationServiceTest {

    private AccommodationRepository accommodationRepository;
    private AccommodationService accommodationService;

    @BeforeEach
    void setUp() {
        accommodationRepository = org.mockito.Mockito.mock(AccommodationRepository.class);
        accommodationService = new AccommodationService(accommodationRepository);
    }

    @Test
    void searchAccommodationsNormalizesFiltersAndReturnsPageResponse() {
        Accommodation accommodation = accommodation(10L, true);
        when(accommodationRepository.searchAccommodations(
                eq(true),
                eq(AccommodationType.HOTEL),
                eq("SOUTH"),
                eq("ocean"),
                any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of(accommodation)));

        PageResponse<AccommodationResponse> response = accommodationService.searchAccommodations(
                AccommodationType.HOTEL,
                " south ",
                " Ocean ",
                null,
                null
        );

        assertThat(response.content()).hasSize(1);
        assertThat(response.content().getFirst().accommodationId()).isEqualTo(10L);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(accommodationRepository).searchAccommodations(
                eq(true),
                eq(AccommodationType.HOTEL),
                eq("SOUTH"),
                eq("ocean"),
                pageableCaptor.capture()
        );
        assertThat(pageableCaptor.getValue().getPageNumber()).isZero();
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(20);
        assertThat(pageableCaptor.getValue().getSort().getOrderFor("accommodationId")).isNotNull();
    }

    @Test
    void searchAccommodationsUsesRequestedPageAndSize() {
        when(accommodationRepository.searchAccommodations(
                eq(true),
                eq(null),
                eq(null),
                eq(null),
                any(Pageable.class)
        )).thenReturn(new PageImpl<>(List.of()));

        accommodationService.searchAccommodations(null, null, null, 2, 40);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(accommodationRepository).searchAccommodations(
                eq(true),
                eq(null),
                eq(null),
                eq(null),
                pageableCaptor.capture()
        );
        assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(2);
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(40);
    }

    @Test
    void searchAccommodationsRejectsInvalidPage() {
        assertThatThrownBy(() -> accommodationService.searchAccommodations(null, null, null, -1, 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Page must be greater than or equal to 0.");
    }

    @Test
    void searchAccommodationsRejectsInvalidPageSize() {
        assertThatThrownBy(() -> accommodationService.searchAccommodations(null, null, null, 0, 51))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Page size must be between 1 and 50.");
    }

    @Test
    void getAccommodationReturnsActiveAccommodation() {
        when(accommodationRepository.findById(10L)).thenReturn(Optional.of(accommodation(10L, true)));

        AccommodationResponse response = accommodationService.getAccommodation(10L);

        assertThat(response.accommodationId()).isEqualTo(10L);
        assertThat(response.name()).isEqualTo("Ocean Hotel");
        assertThat(response.accommodationType()).isEqualTo(AccommodationType.HOTEL);
    }

    @Test
    void getAccommodationRejectsInactiveAccommodation() {
        when(accommodationRepository.findById(10L)).thenReturn(Optional.of(accommodation(10L, false)));

        assertThatThrownBy(() -> accommodationService.getAccommodation(10L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Accommodation not found. accommodationId=10");
    }

    private Accommodation accommodation(Long accommodationId, boolean useYn) {
        Accommodation accommodation = Accommodation.create(
                "Ocean Hotel",
                AccommodationType.HOTEL,
                "SOUTH",
                "제주특별자치도 서귀포시 테스트로 1",
                33.25,
                126.56,
                "description",
                "https://example.com/thumbnail.jpg",
                true,
                useYn
        );
        ReflectionTestUtils.setField(accommodation, "accommodationId", accommodationId);
        return accommodation;
    }
}
