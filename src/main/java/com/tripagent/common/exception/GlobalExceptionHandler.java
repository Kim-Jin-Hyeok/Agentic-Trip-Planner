package com.tripagent.common.exception;

import com.tripagent.ai.llm.LlmException;
import com.tripagent.auth.support.AuthenticationException;
import com.tripagent.auth.support.AuthorizationException;
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
        return new ErrorResponse("INVALID_REQUEST", userMessageForInvalidRequest(exception.getMessage()));
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

    @ExceptionHandler(LlmException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponse handleLlmException(LlmException exception) {
        return new ErrorResponse(exception.getFailureType().name(), userMessageForLlmFailure(exception));
    }

    private String userMessageForInvalidRequest(String message) {
        if (message == null || message.isBlank()) {
            return "Invalid request.";
        }
        if (message.startsWith("Candidate places are not enough to cover every trip day.")) {
            return "Not enough available places to create an itinerary for every trip day. "
                    + "Please reduce excluded places or add more place data.";
        }
        if (message.startsWith("Candidate places are not enough to satisfy pace policy.")) {
            return "Not enough available places for the selected pace. "
                    + "Please choose a slower pace, reduce excluded places, or add more place data.";
        }
        if (message.startsWith("mustVisitPlaceIds are too many to satisfy pace policy.")) {
            return "Too many must-visit places for the selected pace. "
                    + "Please remove some must-visit places or choose a busier pace.";
        }
        if (message.startsWith("Generated itinerary must include mustVisitPlaceId.")) {
            return "The generated itinerary could not include all must-visit places. "
                    + "Please reduce must-visit places or try regenerating.";
        }
        if (message.startsWith("Generated itinerary must not include excludedPlaceId.")) {
            return "The generated itinerary included an excluded place. Please try regenerating.";
        }
        if (message.startsWith("Generated itinerary")
                || message.startsWith("LLM itinerary")
                || message.startsWith("Failed to parse LLM itinerary JSON.")) {
            return "The itinerary could not be generated in a valid format. "
                    + "Please try again or adjust the trip conditions.";
        }

        return message;
    }

    private String userMessageForLlmFailure(LlmException exception) {
        return switch (exception.getFailureType()) {
            case INSUFFICIENT_QUOTA -> "The itinerary AI quota has been exceeded. "
                    + "Please try again later or contact support.";
            case AUTHENTICATION_FAILED -> "The itinerary AI provider is not configured correctly. "
                    + "Please contact support.";
            case RATE_LIMITED -> "The itinerary AI service is receiving too many requests. "
                    + "Please try again shortly.";
            case MODEL_ERROR -> "The itinerary AI service is temporarily unavailable. "
                    + "Please try again later.";
            case EMPTY_OUTPUT, REFUSAL, UNEXPECTED_RESPONSE -> "The itinerary AI returned an unusable response. "
                    + "Please try again or adjust the trip conditions.";
        };
    }
}
