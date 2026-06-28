package com.tripagent.ai.llm;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!local & !dev & !test & !(prod | openai)")
@ConditionalOnMissingBean(LlmClient.class)
public class FailFastLlmClient implements LlmClient {

    @Override
    public String generate(String prompt) {
        throw LlmException.of(LlmFailureType.UNEXPECTED_RESPONSE, "LLM client is not configured.");
    }
}
