package com.tripagent.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.tripagent.ai.llm.LlmException;
import com.tripagent.ai.llm.LlmFailureType;
import com.tripagent.auth.support.AuthorizationException;
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
    void handleAuthorizationExceptionReturnsForbiddenErrorResponse() {
        ErrorResponse response = handler.handleAuthorizationException(
                new AuthorizationException("Admin access is required.")
        );

        assertThat(response.success()).isFalse();
        assertThat(response.code()).isEqualTo("FORBIDDEN");
        assertThat(response.message()).isEqualTo("Admin access is required.");
    }

    @Test
    void handleLlmExceptionReturnsLlmFailureTypeAsCodeAndUserMessage() {
        ErrorResponse response = handler.handleLlmException(
                LlmException.of(LlmFailureType.INSUFFICIENT_QUOTA, "OpenAI quota exceeded.")
        );

        assertThat(response.success()).isFalse();
        assertThat(response.code()).isEqualTo("INSUFFICIENT_QUOTA");
        assertThat(response.message()).isEqualTo(
                "AI 일정 생성 사용량이 소진되었습니다. 잠시 후 다시 시도하거나 관리자에게 문의해 주세요."
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
                "AI 일정 생성 설정에 문제가 있습니다. 관리자에게 문의해 주세요."
        );
    }

    @Test
    void handleLlmTimeoutReturnsRetryMessage() {
        ErrorResponse response = handler.handleLlmException(
                LlmException.of(LlmFailureType.TIMEOUT, "OpenAI response timed out.")
        );

        assertThat(response.code()).isEqualTo("TIMEOUT");
        assertThat(response.message()).isEqualTo(
                "AI 일정 생성 응답이 지연되고 있습니다. 잠시 후 다시 시도해 주세요."
        );
    }

    @Test
    void handleLlmConnectionFailureReturnsRetryMessage() {
        ErrorResponse response = handler.handleLlmException(
                LlmException.of(LlmFailureType.CONNECTION_FAILED, "OpenAI connection failed.")
        );

        assertThat(response.code()).isEqualTo("CONNECTION_FAILED");
        assertThat(response.message()).isEqualTo(
                "AI 일정 생성 서비스에 연결할 수 없습니다. 잠시 후 다시 시도해 주세요."
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
        assertThat(response.code()).isEqualTo("ITINERARY_CANDIDATES_INSUFFICIENT");
        assertThat(response.message()).isEqualTo(
                "선택한 일정 밀도를 구성할 후보 장소가 부족합니다. 일정 밀도를 낮추거나 제외 장소를 줄여 주세요."
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
        assertThat(response.code()).isEqualTo("ITINERARY_GENERATION_INVALID");
        assertThat(response.message()).isEqualTo(
                "여행 조건을 만족하는 일정을 만들지 못했습니다. 잠시 후 다시 시도하거나 생성 조건을 조정해 주세요."
        );
    }

    @Test
    void handleIllegalArgumentExceptionReturnsCodeForUnavailableSelectedPlace() {
        ErrorResponse response = handler.handleIllegalArgumentException(
                new IllegalArgumentException(
                        "mustVisitPlaceIds must be included in candidate places. placeId=10"
                )
        );

        assertThat(response.code()).isEqualTo("ITINERARY_SELECTED_PLACE_UNAVAILABLE");
        assertThat(response.message()).isEqualTo(
                "선택한 장소가 현재 일정 후보에서 제외되었습니다. 후보를 다시 조회하고 선택을 확인해 주세요."
        );
    }

    @Test
    void handleIllegalArgumentExceptionReturnsCodeForInvalidDayTimeWindow() {
        ErrorResponse response = handler.handleIllegalArgumentException(
                new IllegalArgumentException("dayTimeWindows.startTime must be before endTime. dayNo=1")
        );

        assertThat(response.code()).isEqualTo("ITINERARY_TIME_INVALID");
        assertThat(response.message()).isEqualTo(
                "일자별 시작·종료 시간이 올바르지 않습니다. 생성 옵션의 시간을 확인해 주세요."
        );
    }
}
