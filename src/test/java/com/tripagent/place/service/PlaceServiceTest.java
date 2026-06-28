package com.tripagent.place.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tripagent.place.domain.Place;
import com.tripagent.place.dto.PlaceCategory;
import com.tripagent.place.dto.PlaceRecommendConcept;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.repository.PlaceRepository;
import com.tripagent.trip.domain.TripConcept;
import java.lang.reflect.Field;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

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

    @Test
    void findCandidatePlacesMapsHealingTripConcept() {
        Place place = place("Healing Candidate");
        when(placeRepository.findByUseYnTrueOrderByHealingScoreDesc()).thenReturn(List.of(place));

        List<PlaceResponse> responses = placeService.findCandidatePlaces(TripConcept.HEALING);

        assertThat(responses).extracting(PlaceResponse::name).containsExactly("Healing Candidate");
        verify(placeRepository).findByUseYnTrueOrderByHealingScoreDesc();
    }

    @Test
    void findCandidatePlacesMapsFoodTripConcept() {
        Place place = place("Food Candidate");
        when(placeRepository.findByUseYnTrueOrderByFoodScoreDesc()).thenReturn(List.of(place));

        List<PlaceResponse> responses = placeService.findCandidatePlaces(TripConcept.FOOD);

        assertThat(responses).extracting(PlaceResponse::name).containsExactly("Food Candidate");
        verify(placeRepository).findByUseYnTrueOrderByFoodScoreDesc();
    }

    @Test
    void findCandidatePlacesMapsCafeTripConcept() {
        Place place = place("Cafe Candidate");
        when(placeRepository.findByUseYnTrueOrderByCafeScoreDesc()).thenReturn(List.of(place));

        List<PlaceResponse> responses = placeService.findCandidatePlaces(TripConcept.CAFE);

        assertThat(responses).extracting(PlaceResponse::name).containsExactly("Cafe Candidate");
        verify(placeRepository).findByUseYnTrueOrderByCafeScoreDesc();
    }

    @Test
    void findCandidatePlacesMapsPhotoTripConcept() {
        Place place = place("Photo Candidate");
        when(placeRepository.findByUseYnTrueOrderByPhotoScoreDesc()).thenReturn(List.of(place));

        List<PlaceResponse> responses = placeService.findCandidatePlaces(TripConcept.PHOTO);

        assertThat(responses).extracting(PlaceResponse::name).containsExactly("Photo Candidate");
        verify(placeRepository).findByUseYnTrueOrderByPhotoScoreDesc();
    }

    @Test
    void findCandidatePlacesMapsCoupleTripConcept() {
        Place place = place("Couple Candidate");
        when(placeRepository.findByUseYnTrueOrderByCoupleScoreDesc()).thenReturn(List.of(place));

        List<PlaceResponse> responses = placeService.findCandidatePlaces(TripConcept.COUPLE);

        assertThat(responses).extracting(PlaceResponse::name).containsExactly("Couple Candidate");
        verify(placeRepository).findByUseYnTrueOrderByCoupleScoreDesc();
    }

    @Test
    void findCandidatePlacesMapsFamilyTripConcept() {
        Place place = place("Family Candidate");
        when(placeRepository.findByUseYnTrueOrderByFamilyScoreDesc()).thenReturn(List.of(place));

        List<PlaceResponse> responses = placeService.findCandidatePlaces(TripConcept.FAMILY);

        assertThat(responses).extracting(PlaceResponse::name).containsExactly("Family Candidate");
        verify(placeRepository).findByUseYnTrueOrderByFamilyScoreDesc();
    }

    @Test
    void searchPlacesReturnsOnlyActivePlacesByDefault() {
        Place activePlace = place("Active Place", "NATURE", "JEJU", "description", true);
        Place inactivePlace = place("Inactive Place", "NATURE", "JEJU", "description", false);
        when(placeRepository.findAll(any(Sort.class))).thenReturn(List.of(activePlace, inactivePlace));

        List<PlaceResponse> responses = placeService.searchPlaces(null, null, null, null);

        assertThat(responses).extracting(PlaceResponse::name).containsExactly("Active Place");
        ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
        verify(placeRepository).findAll(sortCaptor.capture());
        assertThat(sortCaptor.getValue().getOrderFor("placeId").getDirection()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    void searchPlacesReturnsInactivePlacesWhenUseYnFalse() {
        Place activePlace = place("Active Place", "NATURE", "JEJU", "description", true);
        Place inactivePlace = place("Inactive Place", "NATURE", "JEJU", "description", false);
        when(placeRepository.findAll(any(Sort.class))).thenReturn(List.of(activePlace, inactivePlace));

        List<PlaceResponse> responses = placeService.searchPlaces(null, null, null, false);

        assertThat(responses).extracting(PlaceResponse::name).containsExactly("Inactive Place");
    }

    @Test
    void searchPlacesUsesConceptScoreSort() {
        Place place = place("Food Place", "FOOD", "JEJU", "description", true);
        when(placeRepository.findAll(any(Sort.class))).thenReturn(List.of(place));

        List<PlaceResponse> responses = placeService.searchPlaces(PlaceRecommendConcept.FOOD, null, null, null);

        assertThat(responses).extracting(PlaceResponse::name).containsExactly("Food Place");
        ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
        verify(placeRepository).findAll(sortCaptor.capture());
        assertThat(sortCaptor.getValue().getOrderFor("foodScore").getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void searchPlacesFiltersByCategory() {
        Place naturePlace = place("Nature Place", "NATURE", "JEJU", "description", true);
        Place foodPlace = place("Food Place", "FOOD", "JEJU", "description", true);
        when(placeRepository.findAll(any(Sort.class))).thenReturn(List.of(naturePlace, foodPlace));

        List<PlaceResponse> responses = placeService.searchPlaces(null, PlaceCategory.FOOD, null, null);

        assertThat(responses).extracting(PlaceResponse::name).containsExactly("Food Place");
    }

    @Test
    void searchPlacesFiltersByRegion() {
        Place eastPlace = place("East Place", "NATURE", "EAST", "JEJU", "description", true, 33.0, 126.0);
        Place westPlace = place("West Place", "NATURE", "WEST", "JEJU", "description", true, 33.0, 126.0);
        when(placeRepository.findAll(any(Sort.class))).thenReturn(List.of(eastPlace, westPlace));

        List<PlaceResponse> responses = placeService.searchPlaces(null, null, null, "WEST", null, null, null, null, null, null);

        assertThat(responses).extracting(PlaceResponse::name).containsExactly("West Place");
    }

    @Test
    void searchPlacesFiltersByMultipleCategories() {
        Place naturePlace = place("Nature Place", "NATURE", "JEJU", "description", true);
        Place foodPlace = place("Food Place", "FOOD", "JEJU", "description", true);
        Place cafePlace = place("Cafe Place", "CAFE", "JEJU", "description", true);
        when(placeRepository.findAll(any(Sort.class))).thenReturn(List.of(naturePlace, foodPlace, cafePlace));

        List<PlaceResponse> responses = placeService.searchPlaces(
                null,
                null,
                List.of(PlaceCategory.FOOD, PlaceCategory.CAFE),
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertThat(responses).extracting(PlaceResponse::name).containsExactly("Food Place", "Cafe Place");
    }

    @Test
    void searchPlacesMergesCategoryAndCategories() {
        Place naturePlace = place("Nature Place", "NATURE", "JEJU", "description", true);
        Place foodPlace = place("Food Place", "FOOD", "JEJU", "description", true);
        Place cafePlace = place("Cafe Place", "CAFE", "JEJU", "description", true);
        when(placeRepository.findAll(any(Sort.class))).thenReturn(List.of(naturePlace, foodPlace, cafePlace));

        List<PlaceResponse> responses = placeService.searchPlaces(
                null,
                PlaceCategory.FOOD,
                List.of(PlaceCategory.CAFE),
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertThat(responses).extracting(PlaceResponse::name).containsExactly("Food Place", "Cafe Place");
    }

    @Test
    void searchPlacesFiltersByBounds() {
        Place inBounds = place("In Bounds", "NATURE", "EAST", "JEJU", "description", true, 33.5, 126.5);
        Place outOfBounds = place("Out Of Bounds", "NATURE", "EAST", "JEJU", "description", true, 35.0, 128.0);
        when(placeRepository.findAll(any(Sort.class))).thenReturn(List.of(inBounds, outOfBounds));

        List<PlaceResponse> responses = placeService.searchPlaces(
                null,
                null,
                null,
                null,
                null,
                null,
                33.0,
                34.0,
                126.0,
                127.0
        );

        assertThat(responses).extracting(PlaceResponse::name).containsExactly("In Bounds");
    }

    @Test
    void searchPlacesRejectsPartialBounds() {
        assertThatThrownBy(() -> placeService.searchPlaces(null, null, null, null, null, null, 33.0, null, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("All bounds parameters are required together.");
    }

    @Test
    void searchPlacesRejectsInvalidBoundsOrder() {
        assertThatThrownBy(() -> placeService.searchPlaces(null, null, null, null, null, null, 34.0, 33.0, 126.0, 127.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("minLat must be less than or equal to maxLat.");
    }

    @Test
    void searchPlacesRejectsOutOfRangeBounds() {
        assertThatThrownBy(() -> placeService.searchPlaces(null, null, null, null, null, null, -91.0, 33.0, 126.0, 127.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Latitude bounds must be between -90 and 90.");
    }

    @Test
    void searchPlacesFiltersByKeyword() {
        Place nameMatch = place("Ocean Cafe", "CAFE", "JEJU", "description", true);
        Place addressMatch = place("Other Place", "NATURE", "Seogwipo Ocean Road", "description", true);
        Place descriptionMatch = place("Third Place", "NATURE", "JEJU", "quiet ocean view", true);
        Place noMatch = place("Mountain Place", "NATURE", "JEJU", "description", true);
        when(placeRepository.findAll(any(Sort.class))).thenReturn(List.of(nameMatch, addressMatch, descriptionMatch, noMatch));

        List<PlaceResponse> responses = placeService.searchPlaces(null, null, "ocean", null);

        assertThat(responses).extracting(PlaceResponse::name)
                .containsExactly("Ocean Cafe", "Other Place", "Third Place");
    }

    @Test
    void getPlaceReturnsPlace() {
        Place place = place("Test Place");
        setId(place, "placeId", 10L);
        when(placeRepository.findById(10L)).thenReturn(Optional.of(place));

        PlaceResponse response = placeService.getPlace(10L);

        assertThat(response.placeId()).isEqualTo(10L);
        assertThat(response.name()).isEqualTo("Test Place");
    }

    @Test
    void getPlaceRejectsUnknownPlaceId() {
        when(placeRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> placeService.getPlace(10L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Place not found. placeId=10");
    }

    private Place place(String name) {
        return place(name, "NATURE", "JEJU", "description", true);
    }

    private Place place(String name, String category, String address, String description, Boolean useYn) {
        return place(name, category, "EAST", address, description, useYn, 33.0, 126.0);
    }

    private Place place(
            String name,
            String category,
            String region,
            String address,
            String description,
            Boolean useYn,
            Double latitude,
            Double longitude
    ) {
        return Place.create(
                name,
                category,
                region,
                address,
                latitude,
                longitude,
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
                description,
                useYn
        );
    }

    private void setId(Object target, String fieldName, Long id) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, id);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
