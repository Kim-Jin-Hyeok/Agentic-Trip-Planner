package com.tripagent.place.adapter;

public record PlaceSearchCandidate(
        String externalPlaceId,
        String name,
        String address,
        String roadAddress,
        Double latitude,
        Double longitude,
        String category,
        String placeUrl
) {
}
