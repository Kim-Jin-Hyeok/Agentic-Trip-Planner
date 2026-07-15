package com.tripagent.trip.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record TripAccommodationReplaceRequest(
        @NotNull(message = "Trip accommodations are required.")
        List<@Valid TripAccommodationItemRequest> accommodations
) {
}
