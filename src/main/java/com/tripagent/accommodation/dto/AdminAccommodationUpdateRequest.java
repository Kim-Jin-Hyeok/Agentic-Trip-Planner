package com.tripagent.accommodation.dto;

import com.tripagent.accommodation.domain.AccommodationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AdminAccommodationUpdateRequest(
        @NotNull AccommodationType accommodationType,
        @NotBlank @Size(max = 50) String region,
        @NotNull Boolean parkingYn,
        @Size(max = 1000) String description,
        @Size(max = 1000) String thumbnailUrl
) {
}
