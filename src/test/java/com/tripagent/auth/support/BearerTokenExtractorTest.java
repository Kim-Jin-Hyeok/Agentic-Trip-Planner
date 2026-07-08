package com.tripagent.auth.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

class BearerTokenExtractorTest {

    private final BearerTokenExtractor bearerTokenExtractor = new BearerTokenExtractor();

    @Test
    void extractsBearerToken() {
        HttpServletRequest request = requestWithAuthorization("Bearer access-token");

        String accessToken = bearerTokenExtractor.extractOrNull(request);

        assertThat(accessToken).isEqualTo("access-token");
    }

    @Test
    void returnsNullWhenRequestIsNull() {
        String accessToken = bearerTokenExtractor.extractOrNull(null);

        assertThat(accessToken).isNull();
    }

    @Test
    void returnsNullWhenAuthorizationHeaderIsMissing() {
        HttpServletRequest request = requestWithAuthorization(null);

        String accessToken = bearerTokenExtractor.extractOrNull(request);

        assertThat(accessToken).isNull();
    }

    @Test
    void returnsNullWhenAuthorizationHeaderIsNotBearer() {
        HttpServletRequest request = requestWithAuthorization("Token access-token");

        String accessToken = bearerTokenExtractor.extractOrNull(request);

        assertThat(accessToken).isNull();
    }

    @Test
    void returnsNullWhenBearerTokenIsBlank() {
        HttpServletRequest request = requestWithAuthorization("Bearer   ");

        String accessToken = bearerTokenExtractor.extractOrNull(request);

        assertThat(accessToken).isNull();
    }

    @Test
    void trimsBearerToken() {
        HttpServletRequest request = requestWithAuthorization("Bearer   access-token   ");

        String accessToken = bearerTokenExtractor.extractOrNull(request);

        assertThat(accessToken).isEqualTo("access-token");
    }

    private HttpServletRequest requestWithAuthorization(String authorization) {
        HttpServletRequest request = org.mockito.Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(authorization);
        return request;
    }
}
