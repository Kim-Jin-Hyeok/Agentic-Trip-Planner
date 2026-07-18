package com.tripagent.place.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tripagent.auth.service.AdminAuthorizationService;
import com.tripagent.common.response.PageResponse;
import com.tripagent.place.domain.Place;
import com.tripagent.place.dto.PlaceCategory;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.repository.PlaceRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class AdminPlaceServiceTest {

    @Mock
    private AdminAuthorizationService adminAuthorizationService;

    @Mock
    private PlaceRepository placeRepository;

    private AdminPlaceService adminPlaceService;

    @BeforeEach
    void setUp() {
        adminPlaceService = new AdminPlaceService(adminAuthorizationService, placeRepository);
    }

    @Test
    void searchPlacesRequiresAdminAndReturnsFilteredPage() {
        Place place = place(true);
        PageRequest pageable = PageRequest.of(1, 10);
        when(placeRepository.searchAdminPlaces(
                "오름", "NATURE", "WEST", true, pageable
        )).thenReturn(new PageImpl<>(List.of(place), pageable, 11));

        PageResponse<PlaceResponse> response = adminPlaceService.searchPlaces(
                1L, " 오름 ", PlaceCategory.NATURE, "west", true, 1, 10
        );

        verify(adminAuthorizationService).requireAdmin(1L);
        assertThat(response.content()).hasSize(1);
        assertThat(response.content().getFirst().useYn()).isTrue();
        assertThat(response.totalElements()).isEqualTo(11);
    }

    @Test
    void updateStatusDeactivatesPlace() {
        Place place = place(true);
        when(placeRepository.findById(10L)).thenReturn(Optional.of(place));

        PlaceResponse response = adminPlaceService.updateStatus(1L, 10L, false);

        verify(adminAuthorizationService).requireAdmin(1L);
        assertThat(place.getUseYn()).isFalse();
        assertThat(response.useYn()).isFalse();
    }

    @Test
    void updateStatusRejectsUnknownPlace() {
        when(placeRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminPlaceService.updateStatus(1L, 10L, false))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Place not found. placeId=10");
    }

    @Test
    void updateStatusRejectsDeactivatingTripEndpointPlace() {
        Place airport = place("제주국제공항", true);
        when(placeRepository.findById(10L)).thenReturn(Optional.of(airport));

        assertThatThrownBy(() -> adminPlaceService.updateStatus(1L, 10L, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip endpoint place cannot be deactivated. placeId=10");
        assertThat(airport.getUseYn()).isTrue();
    }

    private Place place(boolean useYn) {
        return place("새별오름", useYn);
    }

    private Place place(String name, boolean useYn) {
        return Place.create(
                name, "NATURE", "WEST", "제주특별자치도 제주시",
                33.36, 126.35, 90, false, true,
                2, 5, 1, 1, 5, 4, 4, "노을 명소", useYn
        );
    }
}
