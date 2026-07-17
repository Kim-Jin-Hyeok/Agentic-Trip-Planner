package com.tripagent.place.dto;

import com.tripagent.place.domain.PlaceSuggestion;
import com.tripagent.place.domain.PlaceSuggestionStatus;
import java.time.LocalDateTime;

public record PlaceSuggestionResponse(
        Long placeSuggestionId,
        String name,
        String address,
        String description,
        PlaceSuggestionStatus status,
        LocalDateTime createdAt,
        String rejectionReason,
        LocalDateTime reviewedAt
) {

    public static PlaceSuggestionResponse from(PlaceSuggestion placeSuggestion) {
        return new PlaceSuggestionResponse(
                placeSuggestion.getPlaceSuggestionId(),
                placeSuggestion.getName(),
                placeSuggestion.getAddress(),
                placeSuggestion.getDescription(),
                placeSuggestion.getStatus(),
                placeSuggestion.getCreatedAt(),
                placeSuggestion.getRejectionReason(),
                placeSuggestion.getReviewedAt()
        );
    }
}
