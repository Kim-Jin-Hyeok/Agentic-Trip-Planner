package com.tripagent.itinerary.dto;

import com.tripagent.itinerary.domain.Itinerary;
import com.tripagent.itinerary.domain.ItineraryGenerationSource;
import com.tripagent.place.dto.PlaceSummaryResponse;
import java.time.LocalTime;

public record ItineraryResponse(
        Long itineraryId,
        Long tripId,
        Long placeId,
        PlaceSummaryResponse place,
        Integer dayNo,
        Integer orderNo,
        LocalTime startTime,
        LocalTime endTime,
        Integer travelMinutesFromPrevious,
        String reason,
        ItineraryGenerationSource generationSource
) {

    public ItineraryResponse(
            Long itineraryId,
            Long tripId,
            Long placeId,
            PlaceSummaryResponse place,
            Integer dayNo,
            Integer orderNo,
            LocalTime startTime,
            LocalTime endTime,
            Integer travelMinutesFromPrevious,
            String reason
    ) {
        this(
                itineraryId, tripId, placeId, place, dayNo, orderNo, startTime, endTime,
                travelMinutesFromPrevious, reason, ItineraryGenerationSource.MANUAL
        );
    }

    public static ItineraryResponse from(Itinerary itinerary) {
        return new ItineraryResponse(
                itinerary.getItineraryId(),
                itinerary.getTrip().getTripId(),
                itinerary.getPlace().getPlaceId(),
                PlaceSummaryResponse.from(itinerary.getPlace()),
                itinerary.getDayNo(),
                itinerary.getOrderNo(),
                itinerary.getStartTime(),
                itinerary.getEndTime(),
                itinerary.getTravelMinutesFromPrevious(),
                itinerary.getReason(),
                itinerary.getGenerationSource()
        );
    }
}
