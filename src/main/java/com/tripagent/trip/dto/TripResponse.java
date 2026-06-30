package com.tripagent.trip.dto;

import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.domain.TripVisibility;
import java.time.LocalDate;
import java.time.LocalTime;

public record TripResponse(
        Long tripId,
        String destination,
        LocalDate startDate,
        LocalDate endDate,
        LocalTime dailyStartTime,
        LocalTime dailyEndTime,
        TripConcept concept,
        Transportation transportation,
        String lastAccommodationArea,
        Long likeCount,
        TripVisibility visibility
) {

    public static TripResponse from(Trip trip) {
        return new TripResponse(
                trip.getTripId(),
                trip.getDestination(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getDailyStartTime(),
                trip.getDailyEndTime(),
                trip.getConcept(),
                trip.getTransportation(),
                trip.getLastAccommodationArea(),
                trip.getLikeCount(),
                trip.getVisibility()
        );
    }
}
