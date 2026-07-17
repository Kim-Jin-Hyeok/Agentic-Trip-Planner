package com.tripagent.place.dto;

import com.tripagent.place.domain.PlaceSuggestionStatus;
import java.time.LocalDateTime;

public record PlaceSuggestionApprovalResponse(
        Long placeSuggestionId,
        Long placeId,
        PlaceSuggestionStatus status,
        LocalDateTime reviewedAt
) {
}
