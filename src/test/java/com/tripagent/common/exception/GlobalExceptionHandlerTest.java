package com.tripagent.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.tripagent.ai.llm.LlmException;
import com.tripagent.ai.llm.LlmFailureType;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleIllegalArgumentExceptionReturnsInvalidRequestErrorResponse() {
        ErrorResponse response = handler.handleIllegalArgumentException(
                new IllegalArgumentException("Invalid request.")
        );

        assertThat(response.success()).isFalse();
        assertThat(response.code()).isEqualTo("INVALID_REQUEST");
        assertThat(response.message()).isEqualTo("Invalid request.");
    }

    @Test
    void handleNoSuchElementExceptionReturnsNotFoundErrorResponse() {
        ErrorResponse response = handler.handleNoSuchElementException(
                new NoSuchElementException("Trip not found.")
        );

        assertThat(response.success()).isFalse();
        assertThat(response.code()).isEqualTo("NOT_FOUND");
        assertThat(response.message()).isEqualTo("Trip not found.");
    }

    @Test
    void handleLlmExceptionReturnsLlmFailureTypeAsCodeAndUserMessage() {
        ErrorResponse response = handler.handleLlmException(
                LlmException.of(LlmFailureType.INSUFFICIENT_QUOTA, "OpenAI quota exceeded.")
        );

        assertThat(response.success()).isFalse();
        assertThat(response.code()).isEqualTo("INSUFFICIENT_QUOTA");
        assertThat(response.message()).isEqualTo(
                "The itinerary AI quota has been exceeded. Please try again later or contact support."
        );
    }

    @Test
    void handleLlmExceptionHidesProviderAuthenticationMessage() {
        ErrorResponse response = handler.handleLlmException(
                LlmException.of(LlmFailureType.AUTHENTICATION_FAILED, "Invalid API key.")
        );

        assertThat(response.success()).isFalse();
        assertThat(response.code()).isEqualTo("AUTHENTICATION_FAILED");
        assertThat(response.message()).isEqualTo(
                "The itinerary AI provider is not configured correctly. Please contact support."
        );
    }

    @Test
    void handleIllegalArgumentExceptionReturnsUserMessageForTooFewCandidatePlaces() {
        ErrorResponse response = handler.handleIllegalArgumentException(
                new IllegalArgumentException(
                        "Candidate places are not enough to satisfy pace policy. pace=BUSY, tripDays=2, "
                                + "requiredMinItems=10, candidateCount=9."
                )
        );

        assertThat(response.success()).isFalse();
        assertThat(response.code()).isEqualTo("INVALID_REQUEST");
        assertThat(response.message()).isEqualTo(
                "Not enough available places for the selected pace. "
                        + "Please choose a slower pace, reduce excluded places, or add more place data."
        );
    }

    @Test
    void handleIllegalArgumentExceptionReturnsUserMessageForInvalidGeneratedItinerary() {
        ErrorResponse response = handler.handleIllegalArgumentException(
                new IllegalArgumentException(
                        "Generated itinerary item count per day does not match pace policy. pace=NORMAL"
                )
        );

        assertThat(response.success()).isFalse();
        assertThat(response.code()).isEqualTo("INVALID_REQUEST");
        assertThat(response.message()).isEqualTo(
                "The itinerary could not be generated in a valid format. "
                        + "Please try again or adjust the trip conditions."
        );
    }
}
