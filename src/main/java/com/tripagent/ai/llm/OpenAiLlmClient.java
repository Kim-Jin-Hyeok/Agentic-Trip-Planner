package com.tripagent.ai.llm;

import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Profile({"prod", "openai"})
public class OpenAiLlmClient implements LlmClient {

    private static final String OPENAI_BASE_URL = "https://api.openai.com";

    private final OpenAiProperties openAiProperties;
    private final RestClient restClient;

    public OpenAiLlmClient(OpenAiProperties openAiProperties, RestClient.Builder restClientBuilder) {
        this.openAiProperties = openAiProperties;
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
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (httpRequest, httpResponse) -> {
                    String responseBody = readResponseBody(httpResponse.getBody().readAllBytes());
                    throw new IllegalStateException("OpenAI Responses API request failed. body=" + responseBody);
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
            throw new IllegalStateException("OpenAI response output is empty.");
        }

        StringBuilder outputText = new StringBuilder();
        for (OpenAiOutputItem outputItem : response.output()) {
            if (outputItem.content() == null) {
                continue;
            }
            for (OpenAiContentItem contentItem : outputItem.content()) {
                if ("refusal".equals(contentItem.type())) {
                    throw new IllegalStateException("OpenAI response was refused.");
                }
                if ("output_text".equals(contentItem.type())) {
                    if (contentItem.text() == null || contentItem.text().isBlank()) {
                        throw new IllegalStateException("OpenAI response output_text is empty.");
                    }
                    outputText.append(contentItem.text());
                }
            }
        }

        if (outputText.isEmpty()) {
            throw new IllegalStateException("OpenAI response does not contain output_text.");
        }

        return outputText.toString();
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
}
