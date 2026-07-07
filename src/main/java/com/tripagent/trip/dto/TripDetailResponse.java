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
        Integer nights,
        LocalTime dailyStartTime,
        LocalTime dailyEndTime,
        TripConcept concept,
        Transportation transportation,
        String lastAccommodationArea,
        Long likeCount,
        TripVisibility visibility,
        boolean liked,
        List<ItineraryResponse> itineraries
) {

    public TripDetailResponse(
            Long tripId,
            String destination,
            LocalDate startDate,
            LocalDate endDate,
            Integer nights,
            LocalTime dailyStartTime,
            LocalTime dailyEndTime,
            TripConcept concept,
            Transportation transportation,
            String lastAccommodationArea,
            Long likeCount,
            TripVisibility visibility,
            List<ItineraryResponse> itineraries
    ) {
        this(
                tripId,
                destination,
                startDate,
                endDate,
                nights,
                dailyStartTime,
                dailyEndTime,
                concept,
                transportation,
                lastAccommodationArea,
                likeCount,
                visibility,
                false,
                itineraries
        );
    }

    public static TripDetailResponse from(Trip trip, List<ItineraryResponse> itineraries) {
        return from(trip, itineraries, false);
    }

    public static TripDetailResponse from(Trip trip, List<ItineraryResponse> itineraries, boolean liked) {
        return new TripDetailResponse(
                trip.getTripId(),
                trip.getDestination(),
                trip.getStartDate(),
                trip.getEndDate(),
                trip.getNights(),
                trip.getDailyStartTime(),
                trip.getDailyEndTime(),
                trip.getConcept(),
                trip.getTransportation(),
                trip.getLastAccommodationArea(),
                trip.getLikeCount(),
                trip.getVisibility(),
                liked,
                itineraries
        );
    }
}
