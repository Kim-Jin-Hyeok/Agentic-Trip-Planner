package com.tripagent.accommodation.dto;

import com.tripagent.place.adapter.PlaceSearchCandidate;

public record AccommodationSearchCandidateResponse(
        String externalPlaceId,
        String name,
        String address,
        String roadAddress,
        Double latitude,
        Double longitude,
        String category,
        String placeUrl,
        boolean alreadyRegistered,
        Long duplicateAccommodationId,
        AccommodationDuplicateReason duplicateReason
) {

    public static AccommodationSearchCandidateResponse from(
            PlaceSearchCandidate candidate,
            boolean alreadyRegistered,
            Long duplicateAccommodationId,
            AccommodationDuplicateReason duplicateReason
    ) {
        return new AccommodationSearchCandidateResponse(
                candidate.externalPlaceId(),
                candidate.name(),
                candidate.address(),
                candidate.roadAddress(),
                candidate.latitude(),
                candidate.longitude(),
                candidate.category(),
                candidate.placeUrl(),
                alreadyRegistered,
                duplicateAccommodationId,
                duplicateReason
        );
    }
}
