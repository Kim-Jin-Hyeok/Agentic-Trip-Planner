package com.tripagent.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripagent.member.domain.Member;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final Base64.Encoder BASE64_URL_ENCODER = Base64.getUrlEncoder().withoutPadding();

    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    public JwtTokenProvider(JwtProperties jwtProperties, ObjectMapper objectMapper) {
        this(jwtProperties, objectMapper, Clock.systemUTC());
    }

    JwtTokenProvider(JwtProperties jwtProperties, ObjectMapper objectMapper, Clock clock) {
        this.jwtProperties = jwtProperties;
        this.objectMapper = objectMapper;
        this.clock = clock;
    }

    public String createAccessToken(Member member) {
        validateSettings(member);

        Instant issuedAt = Instant.now(clock).truncatedTo(ChronoUnit.SECONDS);
        Instant expiresAt = issuedAt.plus(jwtProperties.getAccessTokenExpirationMinutes(), ChronoUnit.MINUTES);
        Map<String, Object> header = Map.of(
                "alg", "HS256",
                "typ", "JWT"
        );
        Map<String, Object> payload = Map.of(
                "sub", member.getMemberId().toString(),
                "email", member.getEmail(),
                "nickname", member.getNickname(),
                "iat", issuedAt.getEpochSecond(),
                "exp", expiresAt.getEpochSecond()
        );

        String encodedHeader = encodeJson(header);
        String encodedPayload = encodeJson(payload);
        String signingInput = encodedHeader + "." + encodedPayload;
        return signingInput + "." + sign(signingInput);
    }

    private void validateSettings(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member is required.");
        }
        if (member.getMemberId() == null) {
            throw new IllegalArgumentException("Member memberId is required.");
        }
        if (jwtProperties.getSecret() == null || jwtProperties.getSecret().isBlank()) {
            throw new IllegalStateException("JWT secret is required.");
        }
        if (jwtProperties.getAccessTokenExpirationMinutes() <= 0) {
            throw new IllegalStateException("JWT access token expiration minutes must be positive.");
        }
    }

    private String encodeJson(Map<String, Object> value) {
        try {
            byte[] json = objectMapper.writeValueAsBytes(value);
            return BASE64_URL_ENCODER.encodeToString(json);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("JWT JSON serialization failed.", exception);
        }
    }

    private String sign(String signingInput) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKey = new SecretKeySpec(
                    jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8),
                    HMAC_SHA256
            );
            mac.init(secretKey);
            byte[] signature = mac.doFinal(signingInput.getBytes(StandardCharsets.UTF_8));
            return BASE64_URL_ENCODER.encodeToString(signature);
        } catch (Exception exception) {
            throw new IllegalStateException("JWT signing failed.", exception);
        }
    }
}
