package com.tripagent.itinerary.dto;

import com.tripagent.place.dto.PlaceCategory;
import jakarta.validation.Valid;
import java.util.List;

public record ItineraryGenerateRequest(
        List<Long> mustVisitPlaceIds,
        List<Long> excludedPlaceIds,
        ItineraryPace pace,
        List<PlaceCategory> preferredCategories,
        @Valid
        List<ItineraryDayTimeWindowRequest> dayTimeWindows
) {

    public ItineraryGenerateRequest(
            List<Long> mustVisitPlaceIds,
            List<Long> excludedPlaceIds
    ) {
        this(mustVisitPlaceIds, excludedPlaceIds, null, null, null);
    }

    public ItineraryGenerateRequest(
            List<Long> mustVisitPlaceIds,
            List<Long> excludedPlaceIds,
            ItineraryPace pace
    ) {
        this(mustVisitPlaceIds, excludedPlaceIds, pace, null, null);
    }

    public ItineraryGenerateRequest(
            List<Long> mustVisitPlaceIds,
            List<Long> excludedPlaceIds,
            ItineraryPace pace,
            List<PlaceCategory> preferredCategories
    ) {
        this(mustVisitPlaceIds, excludedPlaceIds, pace, preferredCategories, null);
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

    public List<PlaceCategory> normalizedPreferredCategories() {
        if (preferredCategories == null) {
            return List.of();
        }
        return preferredCategories;
    }

    public List<ItineraryDayTimeWindowRequest> normalizedDayTimeWindows() {
        if (dayTimeWindows == null) {
            return List.of();
        }
        return dayTimeWindows;
    }
}
