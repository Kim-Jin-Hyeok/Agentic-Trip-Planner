package com.tripagent.trip.dto;

import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripConcept;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record TripDetailResponse(
        Long tripId,
        String destination,
        LocalDate startDate,
        LocalDate endDate,
        LocalTime dailyStartTime,
        TripConcept concept,
        Transportation transportation,
        String lastAccommodationArea,
        List<TripItineraryResponse> itineraries
) {

    public static TripDetailResponse from(Trip trip, List<TripItineraryResponse> itineraries) {
        return new TripDetailResponse(
                trip.getTripId(),
                trip.getDestination(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getDailyStartTime(),
                trip.getConcept(),
                trip.getTransportation(),
                trip.getLastAccommodationArea(),
                itineraries
        );
    }
}
