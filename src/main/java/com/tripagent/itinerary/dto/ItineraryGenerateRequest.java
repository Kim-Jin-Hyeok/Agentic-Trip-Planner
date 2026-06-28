package com.tripagent.itinerary.dto;

import java.util.List;

public record ItineraryGenerateRequest(
        List<Long> mustVisitPlaceIds,
        List<Long> excludedPlaceIds
) {

    public List<Long> normalizedMustVisitPlaceIds() {
        if (mustVisitPlaceIds == null) {
            return List.of();
        }
        return mustVisitPlaceIds;
    }

    public List<Long> normalizedExcludedPlaceIds() {
        if (excludedPlaceIds == null) {
            return List.of();
        }
        return excludedPlaceIds;
    }
}
