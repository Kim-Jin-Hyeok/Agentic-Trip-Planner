package com.tripagent.common.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.tripagent.auth.support.LoginMemberIdArgumentResolver;
import com.tripagent.auth.support.OptionalLoginMemberIdArgumentResolver;
import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

class WebConfigTest {

    @Test
    void corsAllowsPutRequestsForApiEndpoints() {
        WebConfig webConfig = new WebConfig(
                mock(LoginMemberIdArgumentResolver.class),
                mock(OptionalLoginMemberIdArgumentResolver.class)
        );
        TestCorsRegistry corsRegistry = new TestCorsRegistry();

        webConfig.addCorsMappings(corsRegistry);

        CorsConfiguration configuration = corsRegistry.apiConfiguration();
        assertThat(configuration).isNotNull();
        assertThat(configuration.getAllowedMethods()).contains("PUT");
    }

    private static class TestCorsRegistry extends CorsRegistry {

        private CorsConfiguration apiConfiguration() {
            return getCorsConfigurations().get("/api/**");
        }
    }
}
