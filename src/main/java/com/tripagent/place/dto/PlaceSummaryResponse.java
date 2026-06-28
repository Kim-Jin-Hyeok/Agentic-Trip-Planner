package com.tripagent.place.dto;

import com.tripagent.place.domain.Place;

public record PlaceSummaryResponse(
        Long placeId,
        String name,
        String category,
        String address,
        String description
) {

    public static PlaceSummaryResponse from(Place place) {
        return new PlaceSummaryResponse(
                place.getPlaceId(),
                place.getName(),
                place.getCategory(),
                place.getAddress(),
                place.getDescription()
        );
    }
}
