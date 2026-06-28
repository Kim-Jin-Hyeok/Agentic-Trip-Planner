package com.tripagent.ai.llm;

public class LlmException extends RuntimeException {

    private final LlmFailureType failureType;
    private final String providerErrorType;
    private final String providerErrorCode;

    public LlmException(
            LlmFailureType failureType,
            String message,
            String providerErrorType,
            String providerErrorCode
    ) {
        super(message);
        this.failureType = failureType;
        this.providerErrorType = providerErrorType;
        this.providerErrorCode = providerErrorCode;
    }

    public static LlmException of(LlmFailureType failureType, String message) {
        return new LlmException(failureType, message, null, null);
    }

    public LlmFailureType getFailureType() {
        return failureType;
    }

    public String getProviderErrorType() {
        return providerErrorType;
    }

    public String getProviderErrorCode() {
        return providerErrorCode;
    }
}
