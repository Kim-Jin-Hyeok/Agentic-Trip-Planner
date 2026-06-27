package com.tripagent.ai.llm;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

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
    void prodProfileRegistersFailFastLlmClient() {
        contextRunner
                .withPropertyValues("spring.profiles.active=prod")
                .run(context -> {
                    assertThat(context).hasSingleBean(LlmClient.class);
                    assertThat(context).hasSingleBean(FailFastLlmClient.class);
                    assertThat(context).doesNotHaveBean(MockLlmClient.class);
                });
    }

    @Configuration
    @Import({MockLlmClient.class, FailFastLlmClient.class})
    static class TestLlmClientConfiguration {
    }
}
