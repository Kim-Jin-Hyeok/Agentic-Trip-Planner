package com.tripagent.itinerary.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record ItineraryReorderRequest(
        @NotEmpty
        List<@Valid ItineraryReorderRequestItem> items
) {
}
