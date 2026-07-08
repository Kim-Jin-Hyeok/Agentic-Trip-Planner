package com.tripagent.auth.support;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tripagent.auth.service.JwtTokenProvider;
import com.tripagent.common.exception.GlobalExceptionHandler;
import com.tripagent.common.response.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

class LoginMemberIdArgumentResolverTest {

    private JwtTokenProvider jwtTokenProvider;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = org.mockito.Mockito.mock(JwtTokenProvider.class);
        LoginMemberIdArgumentResolver argumentResolver = new LoginMemberIdArgumentResolver(
                jwtTokenProvider,
                new BearerTokenExtractor()
        );
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setCustomArgumentResolvers(argumentResolver)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void resolvesLoginMemberIdFromBearerToken() throws Exception {
        when(jwtTokenProvider.getMemberId("access-token")).thenReturn(1L);

        mockMvc.perform(get("/test/me")
                        .header("Authorization", "Bearer access-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(1L));
    }

    @Test
    void returnsUnauthorizedWhenAuthorizationHeaderIsMissing() throws Exception {
        mockMvc.perform(get("/test/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("Authentication is required."));
    }

    @Test
    void returnsUnauthorizedWhenTokenIsInvalid() throws Exception {
        when(jwtTokenProvider.getMemberId("invalid-token"))
                .thenThrow(new AuthenticationException("Access token is invalid."));

        mockMvc.perform(get("/test/me")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.message").value("Access token is invalid."));
    }

    @RestController
    private static class TestController {

        @GetMapping("/test/me")
        ApiResponse<Long> me(@LoginMemberId Long memberId) {
            return ApiResponse.success(memberId);
        }
    }
}
