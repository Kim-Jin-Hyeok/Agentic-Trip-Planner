package com.tripagent.place.service;

import com.tripagent.place.domain.Place;
import com.tripagent.place.dto.PlaceCategory;
import com.tripagent.place.dto.PlaceRecommendConcept;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.repository.PlaceRepository;
import com.tripagent.trip.domain.TripConcept;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import org.springframework.data.domain.Sort;
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

    public List<PlaceResponse> searchPlaces(
            PlaceRecommendConcept concept,
            PlaceCategory category,
            String keyword,
            Boolean useYn
    ) {
        boolean effectiveUseYn = useYn == null || useYn;
        List<Place> places = placeRepository.findAll(sortByConceptOrPlaceId(concept));
        String normalizedKeyword = normalizeKeyword(keyword);

        return places.stream()
                .filter(place -> place.getUseYn().equals(effectiveUseYn))
                .filter(place -> category == null || place.getCategory().equals(category.name()))
                .filter(place -> normalizedKeyword == null || containsKeyword(place, normalizedKeyword))
                .map(PlaceResponse::from)
                .toList();
    }

    public PlaceResponse getPlace(Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NoSuchElementException("Place not found. placeId=" + placeId));

        return PlaceResponse.from(place);
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

    private Sort sortByConceptOrPlaceId(PlaceRecommendConcept concept) {
        if (concept == null) {
            return Sort.by(Sort.Direction.ASC, "placeId");
        }

        String scoreField = switch (concept) {
            case HEALING -> "healingScore";
            case FOOD -> "foodScore";
            case CAFE -> "cafeScore";
            case PHOTO -> "photoScore";
            case COUPLE -> "coupleScore";
            case FAMILY -> "familyScore";
        };

        return Sort.by(Sort.Order.desc(scoreField), Sort.Order.asc("placeId"));
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return keyword.trim().toLowerCase(Locale.ROOT);
    }

    private boolean containsKeyword(Place place, String keyword) {
        return containsIgnoreCase(place.getName(), keyword)
                || containsIgnoreCase(place.getAddress(), keyword)
                || containsIgnoreCase(place.getDescription(), keyword);
    }

    private boolean containsIgnoreCase(String value, String keyword) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(keyword);
    }
}
