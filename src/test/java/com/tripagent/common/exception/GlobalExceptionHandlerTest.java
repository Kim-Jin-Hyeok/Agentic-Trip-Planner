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
    void handleLlmExceptionReturnsLlmFailureTypeAsCode() {
        ErrorResponse response = handler.handleLlmException(
                LlmException.of(LlmFailureType.INSUFFICIENT_QUOTA, "OpenAI quota exceeded.")
        );

        assertThat(response.success()).isFalse();
        assertThat(response.code()).isEqualTo("INSUFFICIENT_QUOTA");
        assertThat(response.message()).isEqualTo("OpenAI quota exceeded.");
    }
}
