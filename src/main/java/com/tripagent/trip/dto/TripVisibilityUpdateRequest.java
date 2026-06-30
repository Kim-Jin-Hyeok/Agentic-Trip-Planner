package com.tripagent.trip.dto;

import com.tripagent.trip.domain.TripVisibility;
import jakarta.validation.constraints.NotNull;

public record TripVisibilityUpdateRequest(
        @NotNull(message = "Trip visibility is required.")
        TripVisibility visibility
) {
}
