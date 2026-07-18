package com.tripagent.itinerary.policy;

import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class AccommodationAreaRegionMapper {

    private static final Set<String> PLACE_REGIONS = Set.of("EAST", "WEST", "NORTH", "SOUTH");
    private static final Map<String, String> ACCOMMODATION_AREA_REGION_MAPPINGS = Map.ofEntries(
            Map.entry("SEOGWIPO", "SOUTH"),
            Map.entry("JEJU_CITY", "NORTH"),
            Map.entry("AEWOL", "WEST"),
            Map.entry("HALLIM", "WEST"),
            Map.entry("HANGYEONG", "WEST"),
            Map.entry("DAEJEONG", "WEST"),
            Map.entry("ANDEOK", "WEST"),
            Map.entry("JOCHEON", "EAST"),
            Map.entry("GUJWA", "EAST"),
            Map.entry("UDO", "EAST"),
            Map.entry("SEONGSAN", "EAST"),
            Map.entry("PYOSEON", "EAST"),
            Map.entry("NAMWON", "SOUTH"),
            Map.entry("JUNGMUN", "SOUTH")
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
