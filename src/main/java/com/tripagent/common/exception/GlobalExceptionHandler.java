package com.tripagent.common.exception;

import com.tripagent.ai.llm.LlmException;
import com.tripagent.auth.support.AuthenticationException;
import com.tripagent.auth.support.AuthorizationException;
import com.tripagent.place.adapter.PlaceSearchAdapterException;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException exception) {
        return errorResponseForInvalidRequest(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .sorted()
                .findFirst()
                .orElse("Invalid request.");

        return new ErrorResponse("INVALID_REQUEST", message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return new ErrorResponse("INVALID_REQUEST", "Request body is invalid.");
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchElementException(NoSuchElementException exception) {
        return new ErrorResponse("NOT_FOUND", exception.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthenticationException(AuthenticationException exception) {
        return new ErrorResponse("UNAUTHORIZED", exception.getMessage());
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAuthorizationException(AuthorizationException exception) {
        return new ErrorResponse("FORBIDDEN", exception.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(ConflictException exception) {
        return new ErrorResponse("CONFLICT", exception.getMessage());
    }

    @ExceptionHandler(LlmException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponse handleLlmException(LlmException exception) {
        return new ErrorResponse(exception.getFailureType().name(), userMessageForLlmFailure(exception));
    }

    @ExceptionHandler(PlaceSearchAdapterException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponse handlePlaceSearchAdapterException(PlaceSearchAdapterException exception) {
        return new ErrorResponse(
                "EXTERNAL_PLACE_SEARCH_FAILED",
                "외부 장소 검색 서비스를 이용할 수 없습니다. 잠시 후 다시 시도해 주세요."
        );
    }

    private ErrorResponse errorResponseForInvalidRequest(String message) {
        if (message == null || message.isBlank()) {
            return new ErrorResponse("INVALID_REQUEST", "요청 내용을 확인해 주세요.");
        }
        if (message.startsWith("Candidate places are not enough to cover every trip day.")) {
            return new ErrorResponse(
                    "ITINERARY_CANDIDATES_INSUFFICIENT",
                    "모든 여행일을 구성할 후보 장소가 부족합니다. 제외 장소를 줄이거나 관리자에게 장소 추가를 요청해 주세요."
            );
        }
        if (message.startsWith("Candidate places are not enough to satisfy pace policy.")) {
            return new ErrorResponse(
                    "ITINERARY_CANDIDATES_INSUFFICIENT",
                    "선택한 일정 밀도를 구성할 후보 장소가 부족합니다. 일정 밀도를 낮추거나 제외 장소를 줄여 주세요."
            );
        }
        if (message.startsWith("mustVisitPlaceIds are too many to satisfy pace policy.")) {
            return new ErrorResponse(
                    "ITINERARY_MUST_VISIT_EXCESS",
                    "선택한 일정 밀도에 비해 필수 장소가 너무 많습니다. 필수 장소를 줄이거나 일정 밀도를 높여 주세요."
            );
        }
        if (message.startsWith("Generated itinerary must include mustVisitPlaceId.")) {
            return new ErrorResponse(
                    "ITINERARY_MUST_VISIT_UNSATISFIED",
                    "필수 장소를 모두 포함한 일정을 만들지 못했습니다. 필수 장소를 줄이거나 다시 시도해 주세요."
            );
        }
        if (message.startsWith("Generated itinerary must not include excludedPlaceId.")) {
            return new ErrorResponse(
                    "ITINERARY_EXCLUDED_PLACE_INCLUDED",
                    "제외 장소가 일정에 포함되어 저장을 중단했습니다. 잠시 후 다시 시도해 주세요."
            );
        }
        if ((message.startsWith("mustVisitPlaceIds") || message.startsWith("excludedPlaceIds"))
                && message.contains("must be included in candidate places")) {
            return new ErrorResponse(
                    "ITINERARY_SELECTED_PLACE_UNAVAILABLE",
                    "선택한 장소가 현재 일정 후보에서 제외되었습니다. 후보를 다시 조회하고 선택을 확인해 주세요."
            );
        }
        if (message.startsWith("dayTimeWindows")) {
            return new ErrorResponse(
                    "ITINERARY_TIME_INVALID",
                    "일자별 시작·종료 시간이 올바르지 않습니다. 생성 옵션의 시간을 확인해 주세요."
            );
        }
        if (message.startsWith("Itinerary already exists for this trip.")) {
            return new ErrorResponse(
                    "ITINERARY_ALREADY_EXISTS",
                    "이미 생성된 일정이 있습니다. 기존 일정을 다시 만들거나 직접 수정해 주세요."
            );
        }
        if (message.startsWith("Generated itinerary")
                || message.startsWith("LLM itinerary")
                || message.startsWith("Fallback itinerary")
                || message.startsWith("Failed to parse LLM itinerary JSON.")) {
            return new ErrorResponse(
                    "ITINERARY_GENERATION_INVALID",
                    "여행 조건을 만족하는 일정을 만들지 못했습니다. 잠시 후 다시 시도하거나 생성 조건을 조정해 주세요."
            );
        }

        return new ErrorResponse("INVALID_REQUEST", message);
    }

    private String userMessageForLlmFailure(LlmException exception) {
        return switch (exception.getFailureType()) {
            case INSUFFICIENT_QUOTA -> "AI 일정 생성 사용량이 소진되었습니다. 잠시 후 다시 시도하거나 관리자에게 문의해 주세요.";
            case AUTHENTICATION_FAILED -> "AI 일정 생성 설정에 문제가 있습니다. 관리자에게 문의해 주세요.";
            case RATE_LIMITED -> "현재 AI 일정 생성 요청이 많습니다. 잠시 후 다시 시도해 주세요.";
            case CONNECTION_FAILED -> "AI 일정 생성 서비스에 연결할 수 없습니다. 잠시 후 다시 시도해 주세요.";
            case TIMEOUT -> "AI 일정 생성 응답이 지연되고 있습니다. 잠시 후 다시 시도해 주세요.";
            case MODEL_ERROR -> "AI 일정 생성 서비스를 일시적으로 사용할 수 없습니다. 잠시 후 다시 시도해 주세요.";
            case EMPTY_OUTPUT, REFUSAL, UNEXPECTED_RESPONSE ->
                    "AI가 사용할 수 있는 일정을 반환하지 않았습니다. 다시 시도하거나 여행 조건을 조정해 주세요.";
        };
    }
}
