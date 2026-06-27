package com.tripagent.ai.llm.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripagent.ai.llm.dto.LlmItineraryItemResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class LlmItineraryJsonParser {

    private final ObjectMapper objectMapper;

    public LlmItineraryJsonParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<LlmItineraryItemResponse> parse(String json) {
        if (json == null || json.isBlank()) {
            throw new IllegalArgumentException("LLM itinerary JSON is required.");
        }

        String trimmedJson = json.trim();
        if (!trimmedJson.startsWith("[") || !trimmedJson.endsWith("]")) {
            throw new IllegalArgumentException("LLM itinerary response must be a JSON array.");
        }

        List<LlmItineraryItemResponse> responses = parseArray(trimmedJson);
        if (responses.isEmpty()) {
            throw new IllegalArgumentException("LLM itinerary response must not be empty.");
        }

        for (LlmItineraryItemResponse response : responses) {
            validateRequiredFields(response);
        }

        return responses;
    }

    private List<LlmItineraryItemResponse> parseArray(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Failed to parse LLM itinerary JSON.", exception);
        }
    }

    private void validateRequiredFields(LlmItineraryItemResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("LLM itinerary item is required.");
        }
        if (response.placeId() == null) {
            throw new IllegalArgumentException("LLM itinerary item placeId is required.");
        }
        if (response.dayNo() == null) {
            throw new IllegalArgumentException("LLM itinerary item dayNo is required.");
        }
        if (response.orderNo() == null) {
            throw new IllegalArgumentException("LLM itinerary item orderNo is required.");
        }
        if (response.startTime() == null) {
            throw new IllegalArgumentException("LLM itinerary item startTime is required.");
        }
        if (response.endTime() == null) {
            throw new IllegalArgumentException("LLM itinerary item endTime is required.");
        }
        if (response.travelMinutesFromPrevious() == null) {
            throw new IllegalArgumentException("LLM itinerary item travelMinutesFromPrevious is required.");
        }
        if (response.reason() == null || response.reason().isBlank()) {
            throw new IllegalArgumentException("LLM itinerary item reason is required.");
        }
    }
}
