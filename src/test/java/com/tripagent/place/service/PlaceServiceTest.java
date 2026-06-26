package com.tripagent.place.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tripagent.place.domain.Place;
import com.tripagent.place.dto.PlaceRecommendConcept;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.repository.PlaceRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlaceServiceTest {

    @Mock
    private PlaceRepository placeRepository;

    @InjectMocks
    private PlaceService placeService;

    @Test
    void recommendPlacesUsesHealingScoreOrder() {
        Place place = place("Healing Place");
        when(placeRepository.findByUseYnTrueOrderByHealingScoreDesc()).thenReturn(List.of(place));

        List<PlaceResponse> responses = placeService.recommendPlaces(PlaceRecommendConcept.HEALING);

        assertThat(responses).extracting(PlaceResponse::name).containsExactly("Healing Place");
        verify(placeRepository).findByUseYnTrueOrderByHealingScoreDesc();
    }

    @Test
    void recommendPlacesUsesFoodScoreOrder() {
        Place place = place("Food Place");
        when(placeRepository.findByUseYnTrueOrderByFoodScoreDesc()).thenReturn(List.of(place));

        List<PlaceResponse> responses = placeService.recommendPlaces(PlaceRecommendConcept.FOOD);

        assertThat(responses).extracting(PlaceResponse::name).containsExactly("Food Place");
        verify(placeRepository).findByUseYnTrueOrderByFoodScoreDesc();
    }

    @Test
    void recommendPlacesUsesCafeScoreOrder() {
        Place place = place("Cafe Place");
        when(placeRepository.findByUseYnTrueOrderByCafeScoreDesc()).thenReturn(List.of(place));

        List<PlaceResponse> responses = placeService.recommendPlaces(PlaceRecommendConcept.CAFE);

        assertThat(responses).extracting(PlaceResponse::name).containsExactly("Cafe Place");
        verify(placeRepository).findByUseYnTrueOrderByCafeScoreDesc();
    }

    @Test
    void recommendPlacesUsesPhotoScoreOrder() {
        Place place = place("Photo Place");
        when(placeRepository.findByUseYnTrueOrderByPhotoScoreDesc()).thenReturn(List.of(place));

        List<PlaceResponse> responses = placeService.recommendPlaces(PlaceRecommendConcept.PHOTO);

        assertThat(responses).extracting(PlaceResponse::name).containsExactly("Photo Place");
        verify(placeRepository).findByUseYnTrueOrderByPhotoScoreDesc();
    }

    @Test
    void recommendPlacesUsesCoupleScoreOrder() {
        Place place = place("Couple Place");
        when(placeRepository.findByUseYnTrueOrderByCoupleScoreDesc()).thenReturn(List.of(place));

        List<PlaceResponse> responses = placeService.recommendPlaces(PlaceRecommendConcept.COUPLE);

        assertThat(responses).extracting(PlaceResponse::name).containsExactly("Couple Place");
        verify(placeRepository).findByUseYnTrueOrderByCoupleScoreDesc();
    }

    @Test
    void recommendPlacesUsesFamilyScoreOrder() {
        Place place = place("Family Place");
        when(placeRepository.findByUseYnTrueOrderByFamilyScoreDesc()).thenReturn(List.of(place));

        List<PlaceResponse> responses = placeService.recommendPlaces(PlaceRecommendConcept.FAMILY);

        assertThat(responses).extracting(PlaceResponse::name).containsExactly("Family Place");
        verify(placeRepository).findByUseYnTrueOrderByFamilyScoreDesc();
    }

    private Place place(String name) {
        return Place.create(
                name,
                "NATURE",
                "EAST",
                "JEJU",
                33.0,
                126.0,
                60,
                false,
                true,
                1,
                2,
                3,
                4,
                5,
                4,
                3,
                "description",
                true
        );
    }
}
