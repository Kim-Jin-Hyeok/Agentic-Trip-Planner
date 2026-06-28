package com.tripagent.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String code,
        String message
) {
    public ErrorResponse(String message) {
        this(null, message);
    }
}
