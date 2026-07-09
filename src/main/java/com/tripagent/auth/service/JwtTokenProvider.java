package com.tripagent.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripagent.auth.support.AuthenticationException;
import com.tripagent.member.domain.Member;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final Base64.Encoder BASE64_URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder BASE64_URL_DECODER = Base64.getUrlDecoder();
    private static final String INVALID_TOKEN_MESSAGE = "Access token is invalid.";

    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    @Autowired
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

    public Long getMemberId(String accessToken) {
        Map<String, Object> payload = parseAndValidate(accessToken);
        Object subject = payload.get("sub");
        if (!(subject instanceof String subjectValue) || subjectValue.isBlank()) {
            throw new AuthenticationException(INVALID_TOKEN_MESSAGE);
        }

        try {
            return Long.parseLong(subjectValue);
        } catch (NumberFormatException exception) {
            throw new AuthenticationException(INVALID_TOKEN_MESSAGE);
        }
    }

    private Map<String, Object> parseAndValidate(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            throw new AuthenticationException("Authentication is required.");
        }
        validateSecret();

        String[] tokenParts = accessToken.split("\\.");
        if (tokenParts.length != 3) {
            throw new AuthenticationException(INVALID_TOKEN_MESSAGE);
        }

        String signingInput = tokenParts[0] + "." + tokenParts[1];
        String expectedSignature = sign(signingInput);
        if (!java.security.MessageDigest.isEqual(
                expectedSignature.getBytes(StandardCharsets.UTF_8),
                tokenParts[2].getBytes(StandardCharsets.UTF_8)
        )) {
            throw new AuthenticationException(INVALID_TOKEN_MESSAGE);
        }

        Map<String, Object> header = decodeJson(tokenParts[0]);
        if (!"HS256".equals(header.get("alg")) || !"JWT".equals(header.get("typ"))) {
            throw new AuthenticationException(INVALID_TOKEN_MESSAGE);
        }

        Map<String, Object> payload = decodeJson(tokenParts[1]);
        validateExpiration(payload);
        return payload;
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

    private Map<String, Object> decodeJson(String encodedValue) {
        try {
            byte[] decoded = BASE64_URL_DECODER.decode(encodedValue.getBytes(StandardCharsets.UTF_8));
            return objectMapper.readValue(decoded, new TypeReference<>() {
            });
        } catch (IllegalArgumentException | IOException exception) {
            throw new AuthenticationException(INVALID_TOKEN_MESSAGE);
        }
    }

    private void validateExpiration(Map<String, Object> payload) {
        Object expiration = payload.get("exp");
        if (!(expiration instanceof Number expirationValue)) {
            throw new AuthenticationException(INVALID_TOKEN_MESSAGE);
        }

        Instant expiresAt = Instant.ofEpochSecond(expirationValue.longValue());
        if (!expiresAt.isAfter(Instant.now(clock))) {
            throw new AuthenticationException("Access token is expired.");
        }
    }

    private void validateSecret() {
        if (jwtProperties.getSecret() == null || jwtProperties.getSecret().isBlank()) {
            throw new IllegalStateException("JWT secret is required.");
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
