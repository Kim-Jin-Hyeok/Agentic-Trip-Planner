package com.tripagent.trip.dto;

import com.tripagent.itinerary.domain.Itinerary;
import java.time.LocalTime;

public record TripItineraryResponse(
        Long placeId,
        String placeName,
        Integer dayNo,
        Integer orderNo,
        LocalTime startTime,
        LocalTime endTime,
        Integer travelMinutesFromPrevious,
        String reason
) {

    public static TripItineraryResponse from(Itinerary itinerary) {
        return new TripItineraryResponse(
                itinerary.getPlace().getPlaceId(),
                itinerary.getPlace().getName(),
                itinerary.getDayNo(),
                itinerary.getOrderNo(),
                itinerary.getStartTime(),
                itinerary.getEndTime(),
                itinerary.getTravelMinutesFromPrevious(),
                itinerary.getReason()
        );
    }
}
