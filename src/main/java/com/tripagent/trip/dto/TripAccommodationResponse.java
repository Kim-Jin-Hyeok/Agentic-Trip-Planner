package com.tripagent.trip.dto;

import com.tripagent.accommodation.dto.AccommodationResponse;
import com.tripagent.trip.domain.TripAccommodation;
import java.time.LocalDate;

public record TripAccommodationResponse(
        Long tripAccommodationId,
        LocalDate stayDate,
        AccommodationResponse accommodation
) {

    public static TripAccommodationResponse from(TripAccommodation tripAccommodation) {
        return new TripAccommodationResponse(
                tripAccommodation.getTripAccommodationId(),
                tripAccommodation.getStayDate(),
                AccommodationResponse.from(tripAccommodation.getAccommodation())
        );
    }
}
