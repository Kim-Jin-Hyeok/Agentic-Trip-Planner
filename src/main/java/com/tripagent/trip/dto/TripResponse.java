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
        Integer nights,
        LocalTime dailyStartTime,
        LocalTime dailyEndTime,
        TripConcept concept,
        Transportation transportation,
        String lastAccommodationArea,
        Long likeCount,
        Long viewCount,
        TripVisibility visibility,
        String title,
        Long startPlaceId,
        Long endPlaceId
) {

    public TripResponse(
            Long tripId, String destination, LocalDate startDate, LocalDate endDate, Integer nights,
            LocalTime dailyStartTime, LocalTime dailyEndTime, TripConcept concept, Transportation transportation,
            String lastAccommodationArea, Long likeCount, Long viewCount, TripVisibility visibility, String title
    ) {
        this(tripId, destination, startDate, endDate, nights, dailyStartTime, dailyEndTime, concept, transportation,
                lastAccommodationArea, likeCount, viewCount, visibility, title, null, null);
    }

    public TripResponse(
            Long tripId, String destination, LocalDate startDate, LocalDate endDate, Integer nights,
            LocalTime dailyStartTime, LocalTime dailyEndTime, TripConcept concept, Transportation transportation,
            String lastAccommodationArea, Long likeCount, Long viewCount, TripVisibility visibility
    ) {
        this(tripId, destination, startDate, endDate, nights, dailyStartTime, dailyEndTime, concept, transportation,
                lastAccommodationArea, likeCount, viewCount, visibility, destination + " 여행", null, null);
    }

    public TripResponse(
            Long tripId, String destination, LocalDate startDate, LocalDate endDate, Integer nights,
            LocalTime dailyStartTime, LocalTime dailyEndTime, TripConcept concept, Transportation transportation,
            String lastAccommodationArea, Long likeCount, TripVisibility visibility
    ) {
        this(tripId, destination, startDate, endDate, nights, dailyStartTime, dailyEndTime, concept, transportation,
                lastAccommodationArea, likeCount, 0L, visibility, destination + " 여행", null, null);
    }

    public static TripResponse from(Trip trip) {
        return new TripResponse(
                trip.getTripId(), trip.getDestination(), trip.getStartDate(), trip.getEndDate(), trip.getNights(),
                trip.getDailyStartTime(), trip.getDailyEndTime(), trip.getConcept(), trip.getTransportation(),
                trip.getLastAccommodationArea(), trip.getLikeCount(), trip.getViewCount(), trip.getVisibility(),
                trip.getTitle(), trip.getStartPlaceId(), trip.getEndPlaceId()
        );
    }
}
