package com.tripagent.trip.dto;

import java.time.LocalTime;

public record DayStartRouteResponse(
        Integer dayNo,
        String originType,
        String originName,
        Integer travelMinutes,
        LocalTime estimatedDepartureTime,
        boolean departureBeforeDailyStartTime
) {
}
