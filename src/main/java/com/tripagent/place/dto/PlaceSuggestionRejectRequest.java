package com.tripagent.place.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PlaceSuggestionRejectRequest(
        @NotBlank(message = "Place suggestion rejection reason is required.")
        @Size(max = 500, message = "Place suggestion rejection reason must be 500 characters or less.")
        String rejectionReason
) {
}
