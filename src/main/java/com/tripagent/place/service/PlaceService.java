package com.tripagent.place.service;

import com.tripagent.place.domain.Place;
import com.tripagent.place.dto.PlaceCategory;
import com.tripagent.place.dto.PlaceRecommendConcept;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.repository.PlaceRepository;
import com.tripagent.trip.domain.TripConcept;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PlaceService {

    private static final String DEFAULT_TRIP_ENDPOINT_NAME = "제주국제공항";

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
        return searchPlaces(concept, category, null, null, keyword, useYn, null, null, null, null);
    }

    public List<PlaceResponse> searchPlaces(
            PlaceRecommendConcept concept,
            PlaceCategory category,
            List<PlaceCategory> categories,
            String region,
            String keyword,
            Boolean useYn,
            Double minLat,
            Double maxLat,
            Double minLng,
            Double maxLng
    ) {
        boolean effectiveUseYn = useYn == null || useYn;
        Set<String> categoryNames = mergeCategoryNames(category, categories);
        List<String> categoryNameFilters = categoryNames.isEmpty()
                ? List.of("__NO_CATEGORY_FILTER__")
                : new ArrayList<>(categoryNames);
        String normalizedKeyword = normalizeKeyword(keyword);
        validateBounds(minLat, maxLat, minLng, maxLng);
        boolean applyBounds = hasAllBounds(minLat, maxLat, minLng, maxLng);
        List<Place> places = placeRepository.searchPlaces(
                effectiveUseYn,
                region,
                categoryNameFilters,
                categoryNames.isEmpty(),
                normalizedKeyword,
                applyBounds,
                minLat,
                maxLat,
                minLng,
                maxLng,
                sortByConceptOrPlaceId(concept)
        );

        return places.stream()
                .map(PlaceResponse::from)
                .toList();
    }

    public PlaceResponse getPlace(Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NoSuchElementException("Place not found. placeId=" + placeId));

        return PlaceResponse.from(place);
    }

    public List<PlaceResponse> findTripEndpointPlaces() {
        return placeRepository.findFirstByNameAndUseYnTrueOrderByPlaceIdDesc(DEFAULT_TRIP_ENDPOINT_NAME)
                .map(place -> List.of(PlaceResponse.from(place)))
                .orElseGet(List::of);
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

    private Set<String> mergeCategoryNames(PlaceCategory category, List<PlaceCategory> categories) {
        Set<String> categoryNames = new HashSet<>();
        if (category != null) {
            categoryNames.add(category.name());
        }
        if (categories != null) {
            for (PlaceCategory selectedCategory : categories) {
                if (selectedCategory != null) {
                    categoryNames.add(selectedCategory.name());
                }
            }
        }

        return categoryNames;
    }

    private void validateBounds(Double minLat, Double maxLat, Double minLng, Double maxLng) {
        boolean hasAnyBounds = minLat != null || maxLat != null || minLng != null || maxLng != null;
        if (!hasAnyBounds) {
            return;
        }
        if (!hasAllBounds(minLat, maxLat, minLng, maxLng)) {
            throw new IllegalArgumentException("All bounds parameters are required together.");
        }
        if (minLat < -90 || minLat > 90 || maxLat < -90 || maxLat > 90) {
            throw new IllegalArgumentException("Latitude bounds must be between -90 and 90.");
        }
        if (minLng < -180 || minLng > 180 || maxLng < -180 || maxLng > 180) {
            throw new IllegalArgumentException("Longitude bounds must be between -180 and 180.");
        }
        if (minLat > maxLat) {
            throw new IllegalArgumentException("minLat must be less than or equal to maxLat.");
        }
        if (minLng > maxLng) {
            throw new IllegalArgumentException("minLng must be less than or equal to maxLng.");
        }
    }

    private boolean hasAllBounds(Double minLat, Double maxLat, Double minLng, Double maxLng) {
        return minLat != null && maxLat != null && minLng != null && maxLng != null;
    }
}
