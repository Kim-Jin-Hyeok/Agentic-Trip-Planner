package com.tripagent.ai.llm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tripagent.ai.llm.dto.LlmItineraryItemResponse;
import com.tripagent.ai.llm.parser.LlmItineraryJsonParser;
import java.util.List;
import org.junit.jupiter.api.Test;

class MockLlmClientTest {

    private final MockLlmClient mockLlmClient = new MockLlmClient();
    private final LlmItineraryJsonParser parser = new LlmItineraryJsonParser(
            new ObjectMapper().registerModule(new JavaTimeModule())
    );

    @Test
    void generateReturnsParsableJsonArrayUsingPromptPlaceIds() {
        String prompt = """
                candidatePlaces:
                - placeId: 10
                  name: First Place
                - placeId: 20
                  name: Second Place
                - placeId: 30
                  name: Third Place
                """;

        String response = mockLlmClient.generate(prompt);
        List<LlmItineraryItemResponse> parsedResponses = parser.parse(response);

        assertThat(response).startsWith("[");
        assertThat(response).endsWith("]");
        assertThat(parsedResponses).hasSize(3);
        assertThat(parsedResponses).extracting(LlmItineraryItemResponse::placeId)
                .containsExactly(10L, 20L, 30L);
        assertThat(parsedResponses).extracting(LlmItineraryItemResponse::orderNo)
                .containsExactly(1, 2, 3);
    }

    @Test
    void generateLimitsResponseToThreePlaces() {
        String prompt = """
                candidatePlaces:
                - placeId: 10
                - placeId: 20
                - placeId: 30
                - placeId: 40
                """;

        List<LlmItineraryItemResponse> parsedResponses = parser.parse(mockLlmClient.generate(prompt));

        assertThat(parsedResponses).extracting(LlmItineraryItemResponse::placeId)
                .containsExactly(10L, 20L, 30L);
    }

    @Test
    void generateRejectsBlankPrompt() {
        assertThatThrownBy(() -> mockLlmClient.generate(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("LLM prompt is required.");
    }

    @Test
    void generateRejectsPromptWithoutCandidatePlaceIds() {
        assertThatThrownBy(() -> mockLlmClient.generate("candidatePlaces:"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Mock LLM prompt must include candidate placeIds.");
    }
}
