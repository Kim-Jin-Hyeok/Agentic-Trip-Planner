package com.tripagent.auth.support;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tripagent.auth.service.JwtTokenProvider;
import com.tripagent.common.response.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

class OptionalLoginMemberIdArgumentResolverTest {

    private JwtTokenProvider jwtTokenProvider;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = org.mockito.Mockito.mock(JwtTokenProvider.class);
        OptionalLoginMemberIdArgumentResolver argumentResolver =
                new OptionalLoginMemberIdArgumentResolver(jwtTokenProvider, new BearerTokenExtractor());
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setCustomArgumentResolvers(argumentResolver)
                .build();
    }

    @Test
    void resolvesLoginMemberIdFromBearerToken() throws Exception {
        when(jwtTokenProvider.getMemberId("access-token")).thenReturn(1L);

        mockMvc.perform(get("/test/optional-me")
                        .header("Authorization", "Bearer access-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(1L));
    }

    @Test
    void returnsNullWhenAuthorizationHeaderIsMissing() throws Exception {
        mockMvc.perform(get("/test/optional-me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(jwtTokenProvider, never()).getMemberId(any());
    }

    @Test
    void returnsNullWhenAuthorizationHeaderIsNotBearer() throws Exception {
        mockMvc.perform(get("/test/optional-me")
                        .header("Authorization", "Token access-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(jwtTokenProvider, never()).getMemberId(any());
    }

    @Test
    void returnsNullWhenBearerTokenIsBlank() throws Exception {
        mockMvc.perform(get("/test/optional-me")
                        .header("Authorization", "Bearer   "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(jwtTokenProvider, never()).getMemberId(any());
    }

    @RestController
    private static class TestController {

        @GetMapping("/test/optional-me")
        ApiResponse<Long> optionalMe(@OptionalLoginMemberId Long memberId) {
            return ApiResponse.success(memberId);
        }
    }
}
