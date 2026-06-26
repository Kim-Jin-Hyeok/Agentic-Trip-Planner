package com.tripagent.itinerary.dto;

import java.time.LocalTime;

public record ItineraryCreateRequest(
        Long placeId,
        Integer dayNo,
        Integer orderNo,
        LocalTime startTime,
        LocalTime endTime,
        Integer travelMinutesFromPrevious,
        String reason
) {
}
