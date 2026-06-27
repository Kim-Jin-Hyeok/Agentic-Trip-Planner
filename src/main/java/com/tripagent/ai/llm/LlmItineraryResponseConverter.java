package com.tripagent.ai.llm;

import com.tripagent.ai.llm.dto.LlmItineraryItemResponse;
import com.tripagent.itinerary.dto.ItineraryCreateRequest;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class LlmItineraryResponseConverter {

    public List<ItineraryCreateRequest> toCreateRequests(List<LlmItineraryItemResponse> responses) {
        if (responses == null) {
            throw new IllegalArgumentException("LLM itinerary responses are required.");
        }

        return responses.stream()
                .map(this::toCreateRequest)
                .toList();
    }

    public ItineraryCreateRequest toCreateRequest(LlmItineraryItemResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("LLM itinerary response is required.");
        }

        return new ItineraryCreateRequest(
                response.placeId(),
                response.dayNo(),
                response.orderNo(),
                response.startTime(),
                response.endTime(),
                response.travelMinutesFromPrevious(),
                response.reason()
        );
    }
}
