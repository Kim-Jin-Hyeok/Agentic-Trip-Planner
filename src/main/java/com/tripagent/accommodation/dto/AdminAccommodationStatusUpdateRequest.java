package com.tripagent.accommodation.dto;

import jakarta.validation.constraints.NotNull;

public record AdminAccommodationStatusUpdateRequest(
        @NotNull Boolean useYn
) {
}
