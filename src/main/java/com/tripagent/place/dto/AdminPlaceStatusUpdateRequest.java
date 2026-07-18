package com.tripagent.place.dto;

import jakarta.validation.constraints.NotNull;

public record AdminPlaceStatusUpdateRequest(
        @NotNull Boolean useYn
) {
}
