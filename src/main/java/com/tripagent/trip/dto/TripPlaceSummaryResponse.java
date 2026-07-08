package com.tripagent.trip.dto;

import com.tripagent.place.domain.Place;

public record TripPlaceSummaryResponse(
        Long placeId,
        String name,
        String category,
        String region
) {

    public static TripPlaceSummaryResponse from(Place place) {
        return new TripPlaceSummaryResponse(
                place.getPlaceId(),
                place.getName(),
                place.getCategory(),
                place.getRegion()
        );
    }
}
