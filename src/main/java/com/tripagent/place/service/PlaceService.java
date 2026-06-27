package com.tripagent.place.service;

import com.tripagent.place.domain.Place;
import com.tripagent.place.dto.PlaceRecommendConcept;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.repository.PlaceRepository;
import com.tripagent.trip.domain.TripConcept;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<PlaceResponse> recommendPlaces(PlaceRecommendConcept concept) {
        List<Place> places = switch (concept) {
            case HEALING -> placeRepository.findByUseYnTrueOrderByHealingScoreDesc();
            case FOOD -> placeRepository.findByUseYnTrueOrderByFoodScoreDesc();
            case CAFE -> placeRepository.findByUseYnTrueOrderByCafeScoreDesc();
            case PHOTO -> placeRepository.findByUseYnTrueOrderByPhotoScoreDesc();
            case COUPLE -> placeRepository.findByUseYnTrueOrderByCoupleScoreDesc();
            case FAMILY -> placeRepository.findByUseYnTrueOrderByFamilyScoreDesc();
        };

        return places.stream()
                .map(PlaceResponse::from)
                .toList();
    }

    public List<PlaceResponse> findCandidatePlaces(TripConcept concept) {
        return recommendPlaces(toPlaceRecommendConcept(concept));
    }

    private PlaceRecommendConcept toPlaceRecommendConcept(TripConcept concept) {
        return switch (concept) {
            case HEALING -> PlaceRecommendConcept.HEALING;
            case FOOD -> PlaceRecommendConcept.FOOD;
            case CAFE -> PlaceRecommendConcept.CAFE;
            case PHOTO -> PlaceRecommendConcept.PHOTO;
            case COUPLE -> PlaceRecommendConcept.COUPLE;
            case FAMILY -> PlaceRecommendConcept.FAMILY;
        };
    }
}
