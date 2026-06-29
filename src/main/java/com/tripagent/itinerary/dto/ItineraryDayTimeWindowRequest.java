package com.tripagent.itinerary.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalTime;

public record ItineraryDayTimeWindowRequest(
        @NotNull
        @Positive
        Integer dayNo,

        @NotNull
        LocalTime startTime,

        @NotNull
        LocalTime endTime
) {
}
