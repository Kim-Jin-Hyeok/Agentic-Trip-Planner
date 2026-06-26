package com.tripagent.trip.dto;

import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.TripConcept;
import java.time.LocalDate;
import java.time.LocalTime;

public record TripCreateRequest(
        String destination,
        LocalDate startDate,
        LocalDate endDate,
        LocalTime dailyStartTime,
        TripConcept concept,
        Transportation transportation,
        String lastAccommodationArea
) {
}
