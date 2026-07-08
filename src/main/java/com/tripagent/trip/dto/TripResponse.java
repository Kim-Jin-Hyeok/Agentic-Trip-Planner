package com.tripagent.trip.dto;

import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.domain.TripVisibility;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record TripResponse(
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
        Long viewCount,
        TripVisibility visibility,
        boolean liked,
        TripAuthorResponse author,
        List<TripPlaceSummaryResponse> representativePlaces
) {

    public TripResponse(
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
            TripVisibility visibility
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
                0L,
                visibility,
                false,
                null,
                List.of()
        );
    }

    public TripResponse(
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
            boolean liked
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
                0L,
                visibility,
                liked,
                null,
                List.of()
        );
    }

    public TripResponse(
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
            Long viewCount,
            TripVisibility visibility,
            boolean liked
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
                viewCount,
                visibility,
                liked,
                null,
                List.of()
        );
    }

    public static TripResponse from(Trip trip) {
        return from(trip, false, null);
    }

    public static TripResponse from(Trip trip, boolean liked) {
        return from(trip, liked, null);
    }

    public static TripResponse from(Trip trip, boolean liked, TripAuthorResponse author) {
        return from(trip, liked, author, List.of());
    }

    public static TripResponse from(
            Trip trip,
            boolean liked,
            TripAuthorResponse author,
            List<TripPlaceSummaryResponse> representativePlaces
    ) {
        return new TripResponse(
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
                trip.getViewCount(),
                trip.getVisibility(),
                liked,
                author,
                representativePlaces
        );
    }
}
