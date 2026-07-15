package com.tripagent.trip.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TripAccommodationItemRequest(
        @NotNull(message = "Trip accommodation stayDate is required.")
        LocalDate stayDate,
        @NotNull(message = "Trip accommodation accommodationId is required.")
        Long accommodationId
) {
}
