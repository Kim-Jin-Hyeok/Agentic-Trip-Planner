package com.tripagent.ai.llm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class OpenAiLlmClientTest {

    @Test
    void generateCallsResponsesApiAndReturnsOutputText() {
        OpenAiProperties properties = properties("test-api-key", "test-model");
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        OpenAiLlmClient client = new OpenAiLlmClient(
                properties, restClientBuilder.baseUrl("https://api.openai.com").build());
        server.expect(requestTo("https://api.openai.com/v1/responses"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Authorization", "Bearer test-api-key"))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                        {
                          "model": "test-model",
                          "input": "prompt"
                        }
                        """))
                .andRespond(withSuccess("""
                        {
                          "output": [
                            {
                              "type": "message",
                              "content": [
                                {
                                  "type": "output_text",
                                  "text": "[{\\\"placeId\\\":10}]"
                                }
                              ]
                            }
                          ]
                        }
                        """, MediaType.APPLICATION_JSON));

        String response = client.generate("prompt");

        assertThat(response).isEqualTo("[{\"placeId\":10}]");
        server.verify();
    }

    @Test
    void generateRejectsMissingApiKey() {
        OpenAiLlmClient client = new OpenAiLlmClient(
                properties(null, "test-model"),
                RestClient.builder().build()
        );

        assertThatThrownBy(() -> client.generate("prompt"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("OpenAI API key is required.");
    }

    @Test
    void generateRejectsMissingModel() {
        OpenAiLlmClient client = new OpenAiLlmClient(
                properties("test-api-key", null),
                RestClient.builder().build()
        );

        assertThatThrownBy(() -> client.generate("prompt"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("OpenAI model is required.");
    }

    @Test
    void generateRejectsBlankPrompt() {
        OpenAiLlmClient client = new OpenAiLlmClient(
                properties("test-api-key", "test-model"),
                RestClient.builder().build()
        );

        assertThatThrownBy(() -> client.generate(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("LLM prompt is required.");
    }

    @Test
    void generateRejectsHttpError() {
        OpenAiProperties properties = properties("test-api-key", "test-model");
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        OpenAiLlmClient client = new OpenAiLlmClient(
                properties, restClientBuilder.baseUrl("https://api.openai.com").build());
        server.expect(requestTo("https://api.openai.com/v1/responses"))
                .andRespond(withBadRequest().body("{\"error\":{\"message\":\"bad request\"}}"));

        assertThatThrownBy(() -> client.generate("prompt"))
                .isInstanceOf(LlmException.class)
                .hasMessage("bad request")
                .extracting(exception -> ((LlmException) exception).getFailureType())
                .isEqualTo(LlmFailureType.UNEXPECTED_RESPONSE);
        server.verify();
    }

    @Test
    void generateRejectsInsufficientQuota() {
        OpenAiProperties properties = properties("test-api-key", "test-model");
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        OpenAiLlmClient client = new OpenAiLlmClient(
                properties, restClientBuilder.baseUrl("https://api.openai.com").build());
        server.expect(requestTo("https://api.openai.com/v1/responses"))
                .andRespond(withStatus(HttpStatus.TOO_MANY_REQUESTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("""
                                {
                                  "error": {
                                    "message": "You exceeded your current quota.",
                                    "type": "insufficient_quota",
                                    "code": "insufficient_quota"
                                  }
                                }
                                """));

        assertThatThrownBy(() -> client.generate("prompt"))
                .isInstanceOf(LlmException.class)
                .hasMessage("You exceeded your current quota.")
                .satisfies(exception -> {
                    LlmException llmException = (LlmException) exception;
                    assertThat(llmException.getFailureType()).isEqualTo(LlmFailureType.INSUFFICIENT_QUOTA);
                    assertThat(llmException.getProviderErrorType()).isEqualTo("insufficient_quota");
                    assertThat(llmException.getProviderErrorCode()).isEqualTo("insufficient_quota");
                });
        server.verify();
    }

    @Test
    void generateRejectsAuthenticationFailure() {
        OpenAiProperties properties = properties("test-api-key", "test-model");
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        OpenAiLlmClient client = new OpenAiLlmClient(
                properties, restClientBuilder.baseUrl("https://api.openai.com").build());
        server.expect(requestTo("https://api.openai.com/v1/responses"))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("""
                                {
                                  "error": {
                                    "message": "Invalid API key.",
                                    "type": "authentication_error",
                                    "code": "invalid_api_key"
                                  }
                                }
                                """));

        assertThatThrownBy(() -> client.generate("prompt"))
                .isInstanceOf(LlmException.class)
                .hasMessage("Invalid API key.")
                .extracting(exception -> ((LlmException) exception).getFailureType())
                .isEqualTo(LlmFailureType.AUTHENTICATION_FAILED);
        server.verify();
    }

    @Test
    void generateRejectsRateLimit() {
        OpenAiProperties properties = properties("test-api-key", "test-model");
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        OpenAiLlmClient client = new OpenAiLlmClient(
                properties, restClientBuilder.baseUrl("https://api.openai.com").build());
        server.expect(requestTo("https://api.openai.com/v1/responses"))
                .andRespond(withStatus(HttpStatus.TOO_MANY_REQUESTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("""
                                {
                                  "error": {
                                    "message": "Rate limit reached.",
                                    "type": "requests",
                                    "code": "rate_limit_exceeded"
                                  }
                                }
                                """));

        assertThatThrownBy(() -> client.generate("prompt"))
                .isInstanceOf(LlmException.class)
                .hasMessage("Rate limit reached.")
                .extracting(exception -> ((LlmException) exception).getFailureType())
                .isEqualTo(LlmFailureType.RATE_LIMITED);
        server.verify();
    }

    @Test
    void generateRejectsModelError() {
        OpenAiProperties properties = properties("test-api-key", "test-model");
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        OpenAiLlmClient client = new OpenAiLlmClient(
                properties, restClientBuilder.baseUrl("https://api.openai.com").build());
        server.expect(requestTo("https://api.openai.com/v1/responses"))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("""
                                {
                                  "error": {
                                    "message": "Model temporarily unavailable.",
                                    "type": "server_error",
                                    "code": "server_error"
                                  }
                                }
                                """));

        assertThatThrownBy(() -> client.generate("prompt"))
                .isInstanceOf(LlmException.class)
                .hasMessage("Model temporarily unavailable.")
                .extracting(exception -> ((LlmException) exception).getFailureType())
                .isEqualTo(LlmFailureType.MODEL_ERROR);
        server.verify();
    }

    @Test
    void generateConvertsResponseTimeoutToLlmTimeout() {
        OpenAiProperties properties = properties("test-api-key", "test-model");
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        OpenAiLlmClient client = new OpenAiLlmClient(
                properties, restClientBuilder.baseUrl("https://api.openai.com").build());
        server.expect(requestTo("https://api.openai.com/v1/responses"))
                .andRespond(request -> {
                    throw new SocketTimeoutException("Read timed out");
                });

        assertThatThrownBy(() -> client.generate("prompt"))
                .isInstanceOf(LlmException.class)
                .hasMessage("OpenAI response timed out.")
                .extracting(exception -> ((LlmException) exception).getFailureType())
                .isEqualTo(LlmFailureType.TIMEOUT);
        server.verify();
    }

    @Test
    void generateConvertsConnectionFailureToLlmConnectionFailure() {
        OpenAiProperties properties = properties("test-api-key", "test-model");
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        OpenAiLlmClient client = new OpenAiLlmClient(
                properties, restClientBuilder.baseUrl("https://api.openai.com").build());
        server.expect(requestTo("https://api.openai.com/v1/responses"))
                .andRespond(request -> {
                    throw new ConnectException("Connection refused");
                });

        assertThatThrownBy(() -> client.generate("prompt"))
                .isInstanceOf(LlmException.class)
                .hasMessage("OpenAI connection failed.")
                .extracting(exception -> ((LlmException) exception).getFailureType())
                .isEqualTo(LlmFailureType.CONNECTION_FAILED);
        server.verify();
    }

    @Test
    void generateRejectsEmptyOutput() {
        OpenAiLlmClient client = clientWithResponse("""
                {
                  "output": []
                }
                """);

        assertThatThrownBy(() -> client.generate("prompt"))
                .isInstanceOf(LlmException.class)
                .hasMessage("OpenAI response output is empty.")
                .extracting(exception -> ((LlmException) exception).getFailureType())
                .isEqualTo(LlmFailureType.EMPTY_OUTPUT);
    }

    @Test
    void generateRejectsRefusal() {
        OpenAiLlmClient client = clientWithResponse("""
                {
                  "output": [
                    {
                      "type": "message",
                      "content": [
                        {
                          "type": "refusal",
                          "refusal": "Cannot comply."
                        }
                      ]
                    }
                  ]
                }
                """);

        assertThatThrownBy(() -> client.generate("prompt"))
                .isInstanceOf(LlmException.class)
                .hasMessage("OpenAI response was refused.")
                .extracting(exception -> ((LlmException) exception).getFailureType())
                .isEqualTo(LlmFailureType.REFUSAL);
    }

    @Test
    void generateRejectsUnexpectedResponseWithoutOutputText() {
        OpenAiLlmClient client = clientWithResponse("""
                {
                  "output": [
                    {
                      "type": "message",
                      "content": [
                        {
                          "type": "summary_text",
                          "text": "not expected"
                        }
                      ]
                    }
                  ]
                }
                """);

        assertThatThrownBy(() -> client.generate("prompt"))
                .isInstanceOf(LlmException.class)
                .hasMessage("OpenAI response does not contain output_text.")
                .extracting(exception -> ((LlmException) exception).getFailureType())
                .isEqualTo(LlmFailureType.UNEXPECTED_RESPONSE);
    }

    @Test
    void generateIgnoresNullOutputAndContentItems() {
        OpenAiLlmClient client = clientWithResponse("""
                {
                  "output": [
                    null,
                    {
                      "type": "message",
                      "content": [
                        null,
                        {
                          "type": "output_text",
                          "text": "[{\\\"placeId\\\":10}]"
                        }
                      ]
                    }
                  ]
                }
                """);

        String response = client.generate("prompt");

        assertThat(response).isEqualTo("[{\"placeId\":10}]");
    }

    private OpenAiLlmClient clientWithResponse(String responseBody) {
        OpenAiProperties properties = properties("test-api-key", "test-model");
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        OpenAiLlmClient client = new OpenAiLlmClient(
                properties, restClientBuilder.baseUrl("https://api.openai.com").build());
        server.expect(requestTo("https://api.openai.com/v1/responses"))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));
        return client;
    }

    private OpenAiProperties properties(String apiKey, String model) {
        OpenAiProperties properties = new OpenAiProperties();
        properties.setApiKey(apiKey);
        properties.setModel(model);
        return properties;
    }
}
