package com.tripagent.place.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PlaceSuggestionCreateRequest(
        @NotBlank(message = "Place suggestion name is required.")
        @Size(max = 200, message = "Place suggestion name must be 200 characters or less.")
        String name,

        @NotBlank(message = "Place suggestion address is required.")
        @Size(max = 500, message = "Place suggestion address must be 500 characters or less.")
        String address,

        @Size(max = 1000, message = "Place suggestion description must be 1000 characters or less.")
        String description
) {
}
