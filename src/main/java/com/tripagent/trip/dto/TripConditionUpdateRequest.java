package com.tripagent.trip.dto;

import com.tripagent.trip.domain.TripConcept;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

public record TripConditionUpdateRequest(
        @NotNull
        LocalDate startDate,
        @NotNull
        LocalDate endDate,
        @NotNull
        LocalTime dailyStartTime,
        @NotNull
        LocalTime dailyEndTime,
        @NotNull
        TripConcept concept,
        @Size(max = 50)
        String lastAccommodationArea,
        Long startPlaceId,
        Long endPlaceId
) {

    public TripConditionUpdateRequest(
            LocalDate startDate,
            LocalDate endDate,
            LocalTime dailyStartTime,
            LocalTime dailyEndTime,
            TripConcept concept,
            String lastAccommodationArea
    ) {
        this(startDate, endDate, dailyStartTime, dailyEndTime, concept, lastAccommodationArea, null, null);
    }
}
