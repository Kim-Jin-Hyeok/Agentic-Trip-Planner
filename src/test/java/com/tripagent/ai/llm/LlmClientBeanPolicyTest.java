package com.tripagent.ai.llm;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestClient;

class LlmClientBeanPolicyTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestLlmClientConfiguration.class);

    @Test
    void localProfileRegistersMockLlmClientOnly() {
        contextRunner
                .withPropertyValues("spring.profiles.active=local")
                .run(context -> {
                    assertThat(context).hasSingleBean(LlmClient.class);
                    assertThat(context).hasSingleBean(MockLlmClient.class);
                    assertThat(context).doesNotHaveBean(FailFastLlmClient.class);
                });
    }

    @Test
    void devProfileRegistersMockLlmClientOnly() {
        contextRunner
                .withPropertyValues("spring.profiles.active=dev")
                .run(context -> {
                    assertThat(context).hasSingleBean(LlmClient.class);
                    assertThat(context).hasSingleBean(MockLlmClient.class);
                    assertThat(context).doesNotHaveBean(FailFastLlmClient.class);
                });
    }

    @Test
    void testProfileRegistersMockLlmClientOnly() {
        contextRunner
                .withPropertyValues("spring.profiles.active=test")
                .run(context -> {
                    assertThat(context).hasSingleBean(LlmClient.class);
                    assertThat(context).hasSingleBean(MockLlmClient.class);
                    assertThat(context).doesNotHaveBean(FailFastLlmClient.class);
                });
    }

    @Test
    void defaultProfileRegistersFailFastLlmClient() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(LlmClient.class);
            assertThat(context).hasSingleBean(FailFastLlmClient.class);
            assertThat(context).doesNotHaveBean(MockLlmClient.class);
        });
    }

    @Test
    void prodProfileRegistersOpenAiLlmClientOnly() {
        contextRunner
                .withPropertyValues("spring.profiles.active=prod")
                .run(context -> {
                    assertThat(context).hasSingleBean(LlmClient.class);
                    assertThat(context).hasSingleBean(OpenAiLlmClient.class);
                    assertThat(context).doesNotHaveBean(MockLlmClient.class);
                    assertThat(context).doesNotHaveBean(FailFastLlmClient.class);
                });
    }

    @Test
    void openaiProfileRegistersOpenAiLlmClientOnly() {
        contextRunner
                .withPropertyValues("spring.profiles.active=openai")
                .run(context -> {
                    assertThat(context).hasSingleBean(LlmClient.class);
                    assertThat(context).hasSingleBean(OpenAiLlmClient.class);
                    assertThat(context).doesNotHaveBean(MockLlmClient.class);
                    assertThat(context).doesNotHaveBean(FailFastLlmClient.class);
                });
    }

    @Configuration
    @Import({MockLlmClient.class, FailFastLlmClient.class, OpenAiLlmClient.class, OpenAiProperties.class})
    static class TestLlmClientConfiguration {

        @Bean
        RestClient.Builder restClientBuilder() {
            return RestClient.builder();
        }
    }
}
