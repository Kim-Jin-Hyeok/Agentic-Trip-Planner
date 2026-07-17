package com.tripagent.trip.dto;

import java.time.LocalTime;

public record DayStartRouteResponse(
        Integer dayNo,
        String originType,
        String originName,
        Double originLatitude,
        Double originLongitude,
        Integer travelMinutes,
        LocalTime estimatedDepartureTime,
        boolean departureBeforeDailyStartTime
) {
}
