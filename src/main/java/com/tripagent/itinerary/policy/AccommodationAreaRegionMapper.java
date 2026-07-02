package com.tripagent.itinerary.policy;

import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class AccommodationAreaRegionMapper {

    private static final Set<String> PLACE_REGIONS = Set.of("EAST", "WEST", "NORTH");
    private static final Map<String, String> ACCOMMODATION_AREA_REGION_MAPPINGS = Map.of(
            "SEOGWIPO", "WEST",
            "JEJU_CITY", "NORTH",
            "AEWOL", "WEST",
            "JOCHEON", "EAST",
            "SEONGSAN", "EAST"
    );

    public String toPlaceRegion(String accommodationArea) {
        String normalizedArea = normalizeArea(accommodationArea);
        if (normalizedArea == null) {
            return null;
        }
        if (PLACE_REGIONS.contains(normalizedArea)) {
            return normalizedArea;
        }

        return ACCOMMODATION_AREA_REGION_MAPPINGS.get(normalizedArea);
    }

    private String normalizeArea(String area) {
        if (area == null || area.isBlank()) {
            return null;
        }

        return area.trim().toUpperCase();
    }
}
