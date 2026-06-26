package com.tripagent.place.service;

import com.tripagent.place.domain.Place;
import com.tripagent.place.dto.PlaceRecommendConcept;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.repository.PlaceRepository;
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
        };

        return places.stream()
                .map(PlaceResponse::from)
                .toList();
    }
}
