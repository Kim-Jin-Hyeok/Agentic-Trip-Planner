package com.tripagent.ai.llm;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class OpenAiPropertiesTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(org.springframework.boot.autoconfigure.AutoConfigurations.of(
                    ConfigurationPropertiesAutoConfiguration.class
            ))
            .withUserConfiguration(OpenAiProperties.class);

    @Test
    void bindsOpenAiProperties() {
        contextRunner
                .withPropertyValues(
                        "ai.openai.api-key=test-api-key",
                        "ai.openai.model=test-model"
                )
                .run(context -> {
                    OpenAiProperties properties = context.getBean(OpenAiProperties.class);

                    assertThat(properties.getApiKey()).isEqualTo("test-api-key");
                    assertThat(properties.getModel()).isEqualTo("test-model");
                });
    }

    @Test
    void doesNotSetDefaultApiKey() {
        contextRunner.run(context -> {
            OpenAiProperties properties = context.getBean(OpenAiProperties.class);

            assertThat(properties.getApiKey()).isNull();
            assertThat(properties.getModel()).isNull();
        });
    }
}
