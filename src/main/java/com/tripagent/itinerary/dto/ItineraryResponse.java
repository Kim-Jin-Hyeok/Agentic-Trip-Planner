package com.tripagent.itinerary.dto;

import com.tripagent.itinerary.domain.Itinerary;
import java.time.LocalTime;

public record ItineraryResponse(
        Long itineraryId,
        Long tripId,
        Long placeId,
        Integer dayNo,
        Integer orderNo,
        LocalTime startTime,
        LocalTime endTime,
        Integer travelMinutesFromPrevious,
        String reason
) {

    public static ItineraryResponse from(Itinerary itinerary) {
        return new ItineraryResponse(
                itinerary.getItineraryId(),
                itinerary.getTrip().getTripId(),
                itinerary.getPlace().getPlaceId(),
                itinerary.getDayNo(),
                itinerary.getOrderNo(),
                itinerary.getStartTime(),
                itinerary.getEndTime(),
                itinerary.getTravelMinutesFromPrevious(),
                itinerary.getReason()
        );
    }
}
