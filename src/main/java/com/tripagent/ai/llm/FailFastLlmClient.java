package com.tripagent.ai.llm;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(LlmClient.class)
public class FailFastLlmClient implements LlmClient {

    @Override
    public String generate(String prompt) {
        throw new IllegalStateException("LLM client is not configured.");
    }
}
