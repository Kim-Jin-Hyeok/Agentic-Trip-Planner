package com.tripagent.ai.llm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tripagent.ai.llm.dto.LlmItineraryItemResponse;
import com.tripagent.ai.llm.parser.LlmItineraryJsonParser;
import java.time.LocalTime;
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
    void generateDistributesPlacesAcrossTripDays() {
        String prompt = """
                Trip:
                - days: 3
                - dailyStartTime: 10:00
                - dailyEndTime: 17:00

                candidatePlaces:
                - placeId: 10
                - placeId: 20
                - placeId: 30
                """;

        List<LlmItineraryItemResponse> parsedResponses = parser.parse(mockLlmClient.generate(prompt));

        assertThat(parsedResponses).hasSize(3);
        assertThat(parsedResponses).extracting(LlmItineraryItemResponse::placeId)
                .containsExactly(10L, 20L, 30L);
        assertThat(parsedResponses).extracting(LlmItineraryItemResponse::dayNo)
                .containsExactly(1, 2, 3);
        assertThat(parsedResponses).extracting(LlmItineraryItemResponse::orderNo)
                .containsExactly(1, 1, 1);
        assertThat(parsedResponses).extracting(LlmItineraryItemResponse::travelMinutesFromPrevious)
                .containsExactly(0, 0, 0);
        assertThat(parsedResponses).extracting(LlmItineraryItemResponse::startTime)
                .containsExactly(
                        java.time.LocalTime.of(10, 0),
                        java.time.LocalTime.of(10, 0),
                        java.time.LocalTime.of(10, 0)
                );
        assertThat(parsedResponses).extracting(LlmItineraryItemResponse::endTime)
                .containsExactly(
                        java.time.LocalTime.of(11, 0),
                        java.time.LocalTime.of(11, 0),
                        java.time.LocalTime.of(11, 0)
                );
    }

    @Test
    void generateUsesEnoughPlacesToCoverTripDays() {
        String prompt = """
                Trip:
                - days: 4
                - dailyStartTime: 09:00
                - dailyEndTime: 18:00

                candidatePlaces:
                - placeId: 10
                - placeId: 20
                - placeId: 30
                - placeId: 40
                - placeId: 50
                """;

        List<LlmItineraryItemResponse> parsedResponses = parser.parse(mockLlmClient.generate(prompt));

        assertThat(parsedResponses).hasSize(4);
        assertThat(parsedResponses).extracting(LlmItineraryItemResponse::placeId)
                .containsExactly(10L, 20L, 30L, 40L);
        assertThat(parsedResponses).extracting(LlmItineraryItemResponse::dayNo)
                .containsExactly(1, 2, 3, 4);
    }

    @Test
    void generateUsesDayTimeWindowsWhenPromptIncludesOverrides() {
        String prompt = """
                Trip:
                - days: 3
                - dailyStartTime: 09:00
                - dailyEndTime: 18:00

                Day time windows:
                - dayNo: 1, startTime: 14:00, endTime: 18:00
                - dayNo: 2, startTime: 11:00, endTime: 18:00
                - dayNo: 3, startTime: 09:00, endTime: 17:00

                candidatePlaces:
                - placeId: 10
                - placeId: 20
                - placeId: 30
                """;

        List<LlmItineraryItemResponse> parsedResponses = parser.parse(mockLlmClient.generate(prompt));

        assertThat(parsedResponses).extracting(LlmItineraryItemResponse::dayNo)
                .containsExactly(1, 2, 3);
        assertThat(parsedResponses).extracting(LlmItineraryItemResponse::startTime)
                .containsExactly(
                        LocalTime.of(14, 0),
                        LocalTime.of(11, 0),
                        LocalTime.of(9, 0)
                );
        assertThat(parsedResponses).extracting(LlmItineraryItemResponse::endTime)
                .containsExactly(
                        LocalTime.of(15, 0),
                        LocalTime.of(12, 0),
                        LocalTime.of(10, 0)
                );
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

    @Test
    void generateRejectsPromptWithoutEnoughPlacesForTripDays() {
        String prompt = """
                Trip:
                - days: 3

                candidatePlaces:
                - placeId: 10
                - placeId: 20
                """;

        assertThatThrownBy(() -> mockLlmClient.generate(prompt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Mock LLM prompt must include at least one candidate placeId for every trip day.");
    }
}
