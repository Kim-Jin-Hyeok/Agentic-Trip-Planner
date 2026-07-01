package com.tripagent.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripagent.member.domain.Member;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createAccessTokenCreatesSignedJwtWithMemberClaims() throws Exception {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret("test-secret");
        jwtProperties.setAccessTokenExpirationMinutes(30);
        Clock clock = Clock.fixed(Instant.parse("2026-07-01T00:00:00Z"), ZoneOffset.UTC);
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(jwtProperties, objectMapper, clock);
        Member member = Member.create("test@example.com", "testUser", "hashed-password");
        setId(member, 1L);

        String accessToken = jwtTokenProvider.createAccessToken(member);

        String[] tokenParts = accessToken.split("\\.");
        assertThat(tokenParts).hasSize(3);

        Map<String, Object> header = decode(tokenParts[0]);
        Map<String, Object> payload = decode(tokenParts[1]);
        assertThat(header).containsEntry("alg", "HS256")
                .containsEntry("typ", "JWT");
        assertThat(payload).containsEntry("sub", "1")
                .containsEntry("email", "test@example.com")
                .containsEntry("nickname", "testUser")
                .containsEntry("iat", 1782864000)
                .containsEntry("exp", 1782865800);
    }

    @Test
    void getMemberIdReturnsSubjectWhenTokenIsValid() {
        JwtTokenProvider jwtTokenProvider = jwtTokenProvider(
                "test-secret",
                30,
                Instant.parse("2026-07-01T00:00:00Z")
        );
        Member member = Member.create("test@example.com", "testUser", "hashed-password");
        setId(member, 1L);
        String accessToken = jwtTokenProvider.createAccessToken(member);

        Long memberId = jwtTokenProvider.getMemberId(accessToken);

        assertThat(memberId).isEqualTo(1L);
    }

    @Test
    void getMemberIdRejectsTamperedToken() {
        JwtTokenProvider jwtTokenProvider = jwtTokenProvider(
                "test-secret",
                30,
                Instant.parse("2026-07-01T00:00:00Z")
        );
        Member member = Member.create("test@example.com", "testUser", "hashed-password");
        setId(member, 1L);
        String accessToken = jwtTokenProvider.createAccessToken(member);
        String tamperedToken = accessToken.substring(0, accessToken.length() - 1) + "x";

        assertThatThrownBy(() -> jwtTokenProvider.getMemberId(tamperedToken))
                .isInstanceOf(com.tripagent.auth.support.AuthenticationException.class)
                .hasMessage("Access token is invalid.");
    }

    @Test
    void getMemberIdRejectsExpiredToken() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret("test-secret");
        jwtProperties.setAccessTokenExpirationMinutes(30);
        JwtTokenProvider issuer = new JwtTokenProvider(
                jwtProperties,
                objectMapper,
                Clock.fixed(Instant.parse("2026-07-01T00:00:00Z"), ZoneOffset.UTC)
        );
        JwtTokenProvider verifier = new JwtTokenProvider(
                jwtProperties,
                objectMapper,
                Clock.fixed(Instant.parse("2026-07-01T00:31:00Z"), ZoneOffset.UTC)
        );
        Member member = Member.create("test@example.com", "testUser", "hashed-password");
        setId(member, 1L);
        String accessToken = issuer.createAccessToken(member);

        assertThatThrownBy(() -> verifier.getMemberId(accessToken))
                .isInstanceOf(com.tripagent.auth.support.AuthenticationException.class)
                .hasMessage("Access token is expired.");
    }

    @Test
    void createAccessTokenRejectsBlankSecret() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret(" ");
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(
                jwtProperties,
                objectMapper,
                Clock.fixed(Instant.parse("2026-07-01T00:00:00Z"), ZoneOffset.UTC)
        );
        Member member = Member.create("test@example.com", "testUser", "hashed-password");
        setId(member, 1L);

        assertThatThrownBy(() -> jwtTokenProvider.createAccessToken(member))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("JWT secret is required.");
    }

    private Map<String, Object> decode(String encodedValue) throws Exception {
        byte[] decoded = Base64.getUrlDecoder().decode(encodedValue.getBytes(StandardCharsets.UTF_8));
        return objectMapper.readValue(decoded, new TypeReference<>() {
        });
    }

    private JwtTokenProvider jwtTokenProvider(String secret, long expirationMinutes, Instant now) {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret(secret);
        jwtProperties.setAccessTokenExpirationMinutes(expirationMinutes);
        return new JwtTokenProvider(
                jwtProperties,
                objectMapper,
                Clock.fixed(now, ZoneOffset.UTC)
        );
    }

    private void setId(Member member, Long memberId) {
        try {
            Field field = Member.class.getDeclaredField("memberId");
            field.setAccessible(true);
            field.set(member, memberId);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
