package com.tripagent.itinerary.dto;

import jakarta.validation.constraints.Min;
import java.time.LocalTime;

public record ItineraryUpdateRequest(
        Long placeId,
        @Min(1)
        Integer dayNo,
        @Min(1)
        Integer orderNo,
        LocalTime startTime,
        LocalTime endTime,
        @Min(0)
        Integer travelMinutesFromPrevious,
        String reason
) {
}
