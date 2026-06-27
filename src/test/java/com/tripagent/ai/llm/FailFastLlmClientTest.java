package com.tripagent.ai.llm;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class FailFastLlmClientTest {

    private final FailFastLlmClient failFastLlmClient = new FailFastLlmClient();

    @Test
    void generateAlwaysRejectsWhenLlmClientIsNotConfigured() {
        assertThatThrownBy(() -> failFastLlmClient.generate("prompt"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("LLM client is not configured.");
    }
}
