package com.tripagent.ai.llm;

public enum LlmFailureType {
    INSUFFICIENT_QUOTA,
    AUTHENTICATION_FAILED,
    RATE_LIMITED,
    MODEL_ERROR,
    EMPTY_OUTPUT,
    REFUSAL,
    UNEXPECTED_RESPONSE
}
