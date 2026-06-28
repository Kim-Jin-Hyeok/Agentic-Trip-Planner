package com.tripagent.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        boolean success,
        String code,
        String message
) {
    public ErrorResponse(String message) {
        this("INTERNAL_ERROR", message);
    }

    public ErrorResponse(String code, String message) {
        this(false, code, message);
    }
}
