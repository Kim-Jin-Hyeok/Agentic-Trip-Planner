package com.tripagent.trip.dto;

import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.domain.TripVisibility;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record PublicTripResponse(
        Long tripId, String destination, LocalDate startDate, LocalDate endDate, Integer nights,
        LocalTime dailyStartTime, LocalTime dailyEndTime, TripConcept concept, Transportation transportation,
        String lastAccommodationArea, Long likeCount, Long viewCount, TripVisibility visibility, boolean liked,
        TripAuthorResponse author, List<TripPlaceSummaryResponse> representativePlaces, String title,
        Long startPlaceId, Long endPlaceId
) {

    public PublicTripResponse(
            Long tripId, String destination, LocalDate startDate, LocalDate endDate, Integer nights,
            LocalTime dailyStartTime, LocalTime dailyEndTime, TripConcept concept, Transportation transportation,
            String lastAccommodationArea, Long likeCount, Long viewCount, TripVisibility visibility, boolean liked,
            TripAuthorResponse author, List<TripPlaceSummaryResponse> representativePlaces
    ) {
        this(tripId, destination, startDate, endDate, nights, dailyStartTime, dailyEndTime, concept, transportation,
                lastAccommodationArea, likeCount, viewCount, visibility, liked, author, representativePlaces,
                destination + " 여행", null, null);
    }

    public static PublicTripResponse from(
            Trip trip,
            boolean liked,
            TripAuthorResponse author,
            List<TripPlaceSummaryResponse> representativePlaces
    ) {
        return new PublicTripResponse(
                trip.getTripId(), trip.getDestination(), trip.getStartDate(), trip.getEndDate(), trip.getNights(),
                trip.getDailyStartTime(), trip.getDailyEndTime(), trip.getConcept(), trip.getTransportation(),
                trip.getLastAccommodationArea(), trip.getLikeCount(), trip.getViewCount(), trip.getVisibility(),
                liked, author, representativePlaces, trip.getTitle(), trip.getStartPlaceId(), trip.getEndPlaceId()
        );
    }
}
