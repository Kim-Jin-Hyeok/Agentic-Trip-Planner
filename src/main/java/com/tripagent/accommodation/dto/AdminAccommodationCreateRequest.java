package com.tripagent.accommodation.dto;

import com.tripagent.accommodation.domain.AccommodationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AdminAccommodationCreateRequest(
        @NotBlank @Size(max = 100) String externalPlaceId,
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Size(max = 255) String address,
        @NotNull Double latitude,
        @NotNull Double longitude,
        @NotNull AccommodationType accommodationType,
        @NotBlank @Size(max = 50) String region,
        @NotNull Boolean parkingYn,
        @Size(max = 1000) String description,
        @Size(max = 1000) String thumbnailUrl,
        @Size(max = 500) String placeUrl
) {
}
