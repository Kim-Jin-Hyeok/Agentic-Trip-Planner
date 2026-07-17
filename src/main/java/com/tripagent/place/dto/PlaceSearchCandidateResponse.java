package com.tripagent.place.dto;

import com.tripagent.place.adapter.PlaceSearchCandidate;

public record PlaceSearchCandidateResponse(
        String externalPlaceId,
        String name,
        String address,
        String roadAddress,
        Double latitude,
        Double longitude,
        String category,
        String placeUrl
) {

    public static PlaceSearchCandidateResponse from(PlaceSearchCandidate candidate) {
        return new PlaceSearchCandidateResponse(
                candidate.externalPlaceId(),
                candidate.name(),
                candidate.address(),
                candidate.roadAddress(),
                candidate.latitude(),
                candidate.longitude(),
                candidate.category(),
                candidate.placeUrl()
        );
    }
}
