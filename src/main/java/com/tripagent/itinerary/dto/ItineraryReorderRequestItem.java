package com.tripagent.itinerary.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItineraryReorderRequestItem(
        @NotNull
        Long itineraryId,
        @NotNull
        @Min(1)
        Integer dayNo,
        @NotNull
        @Min(1)
        Integer orderNo
) {
}
