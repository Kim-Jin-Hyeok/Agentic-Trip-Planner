package com.tripagent.ai.llm.dto;

import java.time.LocalTime;

public record LlmItineraryItemResponse(
        Long placeId,
        Integer dayNo,
        Integer orderNo,
        LocalTime startTime,
        LocalTime endTime,
        Integer travelMinutesFromPrevious,
        String reason
) {
}
