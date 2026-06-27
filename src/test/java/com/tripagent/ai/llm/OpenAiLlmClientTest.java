package com.tripagent.ai.llm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class OpenAiLlmClientTest {

    @Test
    void generateCallsResponsesApiAndReturnsOutputText() {
        OpenAiProperties properties = properties("test-api-key", "test-model");
        RestClient.Builder restClientBuilder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        OpenAiLlmClient client = new OpenAiLlmClient(properties, restClientBuilder);
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
                RestClient.builder()
        );

        assertThatThrownBy(() -> client.generate("prompt"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("OpenAI API key is required.");
    }

    @Test
    void generateRejectsMissingModel() {
        OpenAiLlmClient client = new OpenAiLlmClient(
                properties("test-api-key", null),
                RestClient.builder()
        );

        assertThatThrownBy(() -> client.generate("prompt"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("OpenAI model is required.");
    }

    @Test
    void generateRejectsBlankPrompt() {
        OpenAiLlmClient client = new OpenAiLlmClient(
                properties("test-api-key", "test-model"),
                RestClient.builder()
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
        OpenAiLlmClient client = new OpenAiLlmClient(properties, restClientBuilder);
        server.expect(requestTo("https://api.openai.com/v1/responses"))
                .andRespond(withBadRequest().body("{\"error\":{\"message\":\"bad request\"}}"));

        assertThatThrownBy(() -> client.generate("prompt"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("OpenAI Responses API request failed.");
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
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("OpenAI response output is empty.");
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
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("OpenAI response was refused.");
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
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("OpenAI response does not contain output_text.");
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
        OpenAiLlmClient client = new OpenAiLlmClient(properties, restClientBuilder);
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
