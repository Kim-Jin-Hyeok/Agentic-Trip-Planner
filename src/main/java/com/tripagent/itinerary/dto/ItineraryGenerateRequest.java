package com.tripagent.itinerary.dto;

import java.util.List;

public record ItineraryGenerateRequest(
        List<Long> mustVisitPlaceIds,
        List<Long> excludedPlaceIds,
        ItineraryPace pace
) {

    public ItineraryGenerateRequest(
            List<Long> mustVisitPlaceIds,
            List<Long> excludedPlaceIds
    ) {
        this(mustVisitPlaceIds, excludedPlaceIds, null);
    }

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

    public ItineraryPace normalizedPace() {
        if (pace == null) {
            return ItineraryPace.NORMAL;
        }
        return pace;
    }
}
