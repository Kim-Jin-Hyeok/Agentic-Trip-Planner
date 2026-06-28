package com.tripagent.itinerary.dto;

import java.time.LocalTime;

public record ItineraryUpdateRequest(
        Long placeId,
        Integer dayNo,
        Integer orderNo,
        LocalTime startTime,
        LocalTime endTime,
        Integer travelMinutesFromPrevious,
        String reason
) {
}
