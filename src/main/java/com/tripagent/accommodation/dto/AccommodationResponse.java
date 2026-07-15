package com.tripagent.accommodation.dto;

import com.tripagent.accommodation.domain.Accommodation;
import com.tripagent.accommodation.domain.AccommodationType;

public record AccommodationResponse(
        Long accommodationId,
        String name,
        AccommodationType accommodationType,
        String region,
        String address,
        Double latitude,
        Double longitude,
        String description,
        String thumbnailUrl,
        Boolean parkingYn
) {

    public static AccommodationResponse from(Accommodation accommodation) {
        return new AccommodationResponse(
                accommodation.getAccommodationId(),
                accommodation.getName(),
                accommodation.getAccommodationType(),
                accommodation.getRegion(),
                accommodation.getAddress(),
                accommodation.getLatitude(),
                accommodation.getLongitude(),
                accommodation.getDescription(),
                accommodation.getThumbnailUrl(),
                accommodation.getParkingYn()
        );
    }
}
