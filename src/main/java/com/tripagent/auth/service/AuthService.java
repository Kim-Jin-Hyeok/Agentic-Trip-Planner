package com.tripagent.auth.service;

import com.tripagent.auth.dto.LoginRequest;
import com.tripagent.auth.dto.LoginResponse;
import com.tripagent.member.domain.Member;
import com.tripagent.member.repository.MemberRepository;
import com.tripagent.member.service.PasswordHashService;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private static final String INVALID_LOGIN_MESSAGE = "Email or password is invalid.";

    private final MemberRepository memberRepository;
    private final PasswordHashService passwordHashService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(
            MemberRepository memberRepository,
            PasswordHashService passwordHashService,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.memberRepository = memberRepository;
        this.passwordHashService = passwordHashService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(LoginRequest request) {
        validateLoginRequest(request);

        String normalizedEmail = normalizeEmail(request.email());
        Member member = memberRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_LOGIN_MESSAGE));
        if (!passwordHashService.matches(request.password(), member.getPasswordHash())) {
            throw new IllegalArgumentException(INVALID_LOGIN_MESSAGE);
        }

        String accessToken = jwtTokenProvider.createAccessToken(member);
        return LoginResponse.from(member, accessToken);
    }

    private void validateLoginRequest(LoginRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Login request is required.");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new IllegalArgumentException("Login email is required.");
        }
        if (request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("Login password is required.");
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
