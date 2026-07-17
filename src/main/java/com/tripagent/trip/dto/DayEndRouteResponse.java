package com.tripagent.trip.dto;

import java.time.LocalTime;

public record DayEndRouteResponse(
        Integer dayNo,
        String destinationType,
        String destinationName,
        Integer travelMinutes,
        LocalTime estimatedArrivalTime,
        boolean arrivalAfterDailyEndTime
) {
}
