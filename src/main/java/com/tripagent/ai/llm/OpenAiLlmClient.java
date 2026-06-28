package com.tripagent.ai.llm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Profile("(prod | openai) & !local & !dev & !test")
public class OpenAiLlmClient implements LlmClient {

    private static final String OPENAI_BASE_URL = "https://api.openai.com";

    private final OpenAiProperties openAiProperties;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public OpenAiLlmClient(OpenAiProperties openAiProperties, RestClient.Builder restClientBuilder) {
        this.openAiProperties = openAiProperties;
        this.objectMapper = new ObjectMapper();
        this.restClient = restClientBuilder
                .baseUrl(OPENAI_BASE_URL)
                .build();
    }

    @Override
    public String generate(String prompt) {
        validateRequiredSettings(prompt);

        OpenAiResponsesRequest request = new OpenAiResponsesRequest(
                openAiProperties.getModel(),
                prompt
        );

        OpenAiResponsesResponse response = restClient.post()
                .uri("/v1/responses")
                .header("Authorization", "Bearer " + openAiProperties.getApiKey())
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (httpRequest, httpResponse) -> {
                    HttpStatusCode statusCode = httpResponse.getStatusCode();
                    String responseBody = readResponseBody(httpResponse.getBody().readAllBytes());
                    throw toLlmException(statusCode, responseBody);
                })
                .body(OpenAiResponsesResponse.class);

        return extractOutputText(response);
    }

    private void validateRequiredSettings(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            throw new IllegalArgumentException("LLM prompt is required.");
        }
        if (openAiProperties.getApiKey() == null || openAiProperties.getApiKey().isBlank()) {
            throw new IllegalStateException("OpenAI API key is required.");
        }
        if (openAiProperties.getModel() == null || openAiProperties.getModel().isBlank()) {
            throw new IllegalStateException("OpenAI model is required.");
        }
    }

    private String extractOutputText(OpenAiResponsesResponse response) {
        if (response == null || response.output() == null || response.output().isEmpty()) {
            throw LlmException.of(LlmFailureType.EMPTY_OUTPUT, "OpenAI response output is empty.");
        }

        StringBuilder outputText = new StringBuilder();
        for (OpenAiOutputItem outputItem : response.output()) {
            if (outputItem == null) {
                continue;
            }
            if (outputItem.content() == null) {
                continue;
            }
            for (OpenAiContentItem contentItem : outputItem.content()) {
                if (contentItem == null) {
                    continue;
                }
                if ("refusal".equals(contentItem.type())) {
                    throw LlmException.of(LlmFailureType.REFUSAL, "OpenAI response was refused.");
                }
                if ("output_text".equals(contentItem.type())) {
                    if (contentItem.text() == null || contentItem.text().isBlank()) {
                        throw LlmException.of(LlmFailureType.EMPTY_OUTPUT, "OpenAI response output_text is empty.");
                    }
                    outputText.append(contentItem.text());
                }
            }
        }

        if (outputText.isEmpty()) {
            throw LlmException.of(
                    LlmFailureType.UNEXPECTED_RESPONSE,
                    "OpenAI response does not contain output_text."
            );
        }

        return outputText.toString();
    }

    private LlmException toLlmException(HttpStatusCode statusCode, String responseBody) {
        OpenAiErrorResponse errorResponse = parseErrorResponse(responseBody);
        OpenAiError error = errorResponse == null ? null : errorResponse.error();
        String providerErrorType = error == null ? null : error.type();
        String providerErrorCode = error == null ? null : error.code();
        String providerMessage = error == null ? null : error.message();
        LlmFailureType failureType = resolveFailureType(statusCode, providerErrorType, providerErrorCode);
        String message = providerMessage == null || providerMessage.isBlank()
                ? "OpenAI Responses API request failed."
                : providerMessage;

        return new LlmException(failureType, message, providerErrorType, providerErrorCode);
    }

    private OpenAiErrorResponse parseErrorResponse(String responseBody) {
        if (responseBody == null || responseBody.isBlank()) {
            return null;
        }

        try {
            return objectMapper.readValue(responseBody, OpenAiErrorResponse.class);
        } catch (JsonProcessingException exception) {
            return null;
        }
    }

    private LlmFailureType resolveFailureType(
            HttpStatusCode statusCode,
            String providerErrorType,
            String providerErrorCode
    ) {
        if ("insufficient_quota".equals(providerErrorCode) || "insufficient_quota".equals(providerErrorType)) {
            return LlmFailureType.INSUFFICIENT_QUOTA;
        }
        if (statusCode.value() == 401
                || "authentication_error".equals(providerErrorType)
                || "invalid_api_key".equals(providerErrorCode)) {
            return LlmFailureType.AUTHENTICATION_FAILED;
        }
        if (statusCode.value() == 429 || "rate_limit_exceeded".equals(providerErrorCode)) {
            return LlmFailureType.RATE_LIMITED;
        }
        if (statusCode.is5xxServerError()
                || "server_error".equals(providerErrorType)
                || "model_error".equals(providerErrorType)) {
            return LlmFailureType.MODEL_ERROR;
        }

        return LlmFailureType.UNEXPECTED_RESPONSE;
    }

    private String readResponseBody(byte[] responseBody) {
        if (responseBody == null || responseBody.length == 0) {
            return "";
        }
        return new String(responseBody, StandardCharsets.UTF_8);
    }

    public record OpenAiResponsesRequest(
            String model,
            String input
    ) {
    }

    public record OpenAiResponsesResponse(
            List<OpenAiOutputItem> output
    ) {
    }

    public record OpenAiOutputItem(
            String type,
            List<OpenAiContentItem> content
    ) {
    }

    public record OpenAiContentItem(
            String type,
            String text,
            String refusal
    ) {
    }

    public record OpenAiErrorResponse(
            OpenAiError error
    ) {
    }

    public record OpenAiError(
            String type,
            String code,
            String message
    ) {
    }
}
