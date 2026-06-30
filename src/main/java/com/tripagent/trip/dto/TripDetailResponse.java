package com.tripagent.trip.dto;

import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.domain.TripVisibility;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record TripDetailResponse(
        Long tripId,
        String destination,
        LocalDate startDate,
        LocalDate endDate,
        LocalTime dailyStartTime,
        LocalTime dailyEndTime,
        TripConcept concept,
        Transportation transportation,
        String lastAccommodationArea,
        TripVisibility visibility,
        List<ItineraryResponse> itineraries
) {

    public static TripDetailResponse from(Trip trip, List<ItineraryResponse> itineraries) {
        return new TripDetailResponse(
                trip.getTripId(),
                trip.getDestination(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getDailyStartTime(),
                trip.getDailyEndTime(),
                trip.getConcept(),
                trip.getTransportation(),
                trip.getLastAccommodationArea(),
                trip.getVisibility(),
                itineraries
        );
    }
}
