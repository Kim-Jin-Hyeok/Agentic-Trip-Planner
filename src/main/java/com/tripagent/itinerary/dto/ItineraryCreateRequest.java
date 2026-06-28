package com.tripagent.itinerary.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ItineraryCreateRequest(
        @NotNull
        Long placeId,
        @NotNull
        @Min(1)
        Integer dayNo,
        @NotNull
        @Min(1)
        Integer orderNo,
        @NotNull
        LocalTime startTime,
        @NotNull
        LocalTime endTime,
        @NotNull
        @Min(0)
        Integer travelMinutesFromPrevious,
        String reason
) {
}
