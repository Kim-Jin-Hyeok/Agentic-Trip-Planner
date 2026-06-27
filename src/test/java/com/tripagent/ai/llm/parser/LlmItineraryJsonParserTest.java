package com.tripagent.ai.llm.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tripagent.ai.llm.dto.LlmItineraryItemResponse;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class LlmItineraryJsonParserTest {

    private final LlmItineraryJsonParser parser = new LlmItineraryJsonParser(
            new ObjectMapper().registerModule(new JavaTimeModule())
    );

    @Test
    void parseReturnsItineraryItemsFromJsonArray() {
        String json = """
                [
                  {
                    "placeId": 10,
                    "dayNo": 1,
                    "orderNo": 1,
                    "startTime": "09:00:00",
                    "endTime": "10:30:00",
                    "travelMinutesFromPrevious": 0,
                    "reason": "Good first stop."
                  },
                  {
                    "placeId": 20,
                    "dayNo": 1,
                    "orderNo": 2,
                    "startTime": "11:00:00",
                    "endTime": "12:00:00",
                    "travelMinutesFromPrevious": 30,
                    "reason": "Fits the trip concept."
                  }
                ]
                """;

        List<LlmItineraryItemResponse> responses = parser.parse(json);

        assertThat(responses).hasSize(2);
        assertThat(responses).extracting(LlmItineraryItemResponse::placeId)
                .containsExactly(10L, 20L);
        assertThat(responses.get(0).startTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(responses.get(0).endTime()).isEqualTo(LocalTime.of(10, 30));
    }

    @Test
    void parseRejectsBlankJson() {
        assertThatThrownBy(() -> parser.parse(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("LLM itinerary JSON is required.");
    }

    @Test
    void parseRejectsNonArrayJson() {
        String json = """
                {
                  "items": []
                }
                """;

        assertThatThrownBy(() -> parser.parse(json))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("LLM itinerary response must be a JSON array.");
    }

    @Test
    void parseRejectsMarkdownWrappedJson() {
        String json = """
                ```json
                []
                ```
                """;

        assertThatThrownBy(() -> parser.parse(json))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("LLM itinerary response must be a JSON array.");
    }

    @Test
    void parseRejectsInvalidJsonArray() {
        assertThatThrownBy(() -> parser.parse("[{\"placeId\":10,]"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Failed to parse LLM itinerary JSON.");
    }

    @Test
    void parseRejectsEmptyArray() {
        assertThatThrownBy(() -> parser.parse("[]"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("LLM itinerary response must not be empty.");
    }

    @Test
    void parseRejectsMissingRequiredField() {
        String json = """
                [
                  {
                    "placeId": 10,
                    "dayNo": 1,
                    "orderNo": 1,
                    "startTime": "09:00:00",
                    "endTime": "10:30:00",
                    "reason": "Missing travel minutes."
                  }
                ]
                """;

        assertThatThrownBy(() -> parser.parse(json))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("LLM itinerary item travelMinutesFromPrevious is required.");
    }

    @Test
    void parseRejectsBlankReason() {
        String json = """
                [
                  {
                    "placeId": 10,
                    "dayNo": 1,
                    "orderNo": 1,
                    "startTime": "09:00:00",
                    "endTime": "10:30:00",
                    "travelMinutesFromPrevious": 0,
                    "reason": ""
                  }
                ]
                """;

        assertThatThrownBy(() -> parser.parse(json))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("LLM itinerary item reason is required.");
    }
}
