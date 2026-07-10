package com.tripagent.trip.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TripTitleUpdateRequest(
        @NotBlank(message = "Trip title is required.")
        @Size(max = 100, message = "Trip title must be less than or equal to 100 characters.")
        String title
) {
}
