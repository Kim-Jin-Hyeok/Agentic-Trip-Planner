package com.tripagent.ai.llm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tripagent.ai.llm.dto.LlmItineraryItemResponse;
import com.tripagent.itinerary.dto.ItineraryCreateRequest;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class LlmItineraryResponseConverterTest {

    private final LlmItineraryResponseConverter converter = new LlmItineraryResponseConverter();

    @Test
    void toCreateRequestConvertsLlmResponse() {
        LlmItineraryItemResponse response = response(
                10L,
                1,
                2,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                30,
                "Selected for the trip concept."
        );

        ItineraryCreateRequest request = converter.toCreateRequest(response);

        assertThat(request.placeId()).isEqualTo(10L);
        assertThat(request.dayNo()).isEqualTo(1);
        assertThat(request.orderNo()).isEqualTo(2);
        assertThat(request.startTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(request.endTime()).isEqualTo(LocalTime.of(11, 0));
        assertThat(request.travelMinutesFromPrevious()).isEqualTo(30);
        assertThat(request.reason()).isEqualTo("Selected for the trip concept.");
    }

    @Test
    void toCreateRequestsConvertsLlmResponseList() {
        List<LlmItineraryItemResponse> responses = List.of(
                response(10L, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0, "First"),
                response(20L, 1, 2, LocalTime.of(10, 30), LocalTime.of(11, 30), 30, "Second")
        );

        List<ItineraryCreateRequest> requests = converter.toCreateRequests(responses);

        assertThat(requests).hasSize(2);
        assertThat(requests).extracting(ItineraryCreateRequest::placeId)
                .containsExactly(10L, 20L);
        assertThat(requests).extracting(ItineraryCreateRequest::orderNo)
                .containsExactly(1, 2);
    }

    @Test
    void toCreateRequestRejectsNullResponse() {
        assertThatThrownBy(() -> converter.toCreateRequest(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("LLM itinerary response is required.");
    }

    @Test
    void toCreateRequestsRejectsNullResponseList() {
        assertThatThrownBy(() -> converter.toCreateRequests(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("LLM itinerary responses are required.");
    }

    private LlmItineraryItemResponse response(
            Long placeId,
            Integer dayNo,
            Integer orderNo,
            LocalTime startTime,
            LocalTime endTime,
            Integer travelMinutesFromPrevious,
            String reason
    ) {
        return new LlmItineraryItemResponse(
                placeId,
                dayNo,
                orderNo,
                startTime,
                endTime,
                travelMinutesFromPrevious,
                reason
        );
    }
}
