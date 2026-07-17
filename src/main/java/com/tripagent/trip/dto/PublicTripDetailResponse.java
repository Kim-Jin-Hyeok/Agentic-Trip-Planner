package com.tripagent.trip.dto;

import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.domain.TripVisibility;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record PublicTripDetailResponse(
        Long tripId, String destination, LocalDate startDate, LocalDate endDate, Integer nights,
        LocalTime dailyStartTime, LocalTime dailyEndTime, TripConcept concept, Transportation transportation,
        String lastAccommodationArea, Long likeCount, Long viewCount, TripVisibility visibility, boolean liked,
        TripAuthorResponse author, List<ItineraryResponse> itineraries, String title,
        Long startPlaceId, Long endPlaceId, List<DayEndRouteResponse> dayEndRoutes
) {

    public PublicTripDetailResponse(
            Long tripId, String destination, LocalDate startDate, LocalDate endDate, Integer nights,
            LocalTime dailyStartTime, LocalTime dailyEndTime, TripConcept concept, Transportation transportation,
            String lastAccommodationArea, Long likeCount, Long viewCount, TripVisibility visibility, boolean liked,
            TripAuthorResponse author, List<ItineraryResponse> itineraries
    ) {
        this(tripId, destination, startDate, endDate, nights, dailyStartTime, dailyEndTime, concept, transportation,
                lastAccommodationArea, likeCount, viewCount, visibility, liked, author, itineraries,
                destination + " 여행", null, null, List.of());
    }

    public static PublicTripDetailResponse from(
            Trip trip,
            List<ItineraryResponse> itineraries,
            boolean liked,
            TripAuthorResponse author
    ) {
        return from(trip, itineraries, liked, author, List.of());
    }

    public static PublicTripDetailResponse from(
            Trip trip,
            List<ItineraryResponse> itineraries,
            boolean liked,
            TripAuthorResponse author,
            List<DayEndRouteResponse> dayEndRoutes
    ) {
        return new PublicTripDetailResponse(
                trip.getTripId(), trip.getDestination(), trip.getStartDate(), trip.getEndDate(), trip.getNights(),
                trip.getDailyStartTime(), trip.getDailyEndTime(), trip.getConcept(), trip.getTransportation(),
                trip.getLastAccommodationArea(), trip.getLikeCount(), trip.getViewCount(), trip.getVisibility(),
                liked, author, itineraries, trip.getTitle(), trip.getStartPlaceId(), trip.getEndPlaceId(), dayEndRoutes
        );
    }
}
