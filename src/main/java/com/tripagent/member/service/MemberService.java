package com.tripagent.member.service;

import com.tripagent.member.domain.Member;
import com.tripagent.member.dto.MemberCreateRequest;
import com.tripagent.member.dto.MemberResponse;
import com.tripagent.member.repository.MemberRepository;
import java.util.Locale;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordHashService passwordHashService;

    public MemberService(MemberRepository memberRepository, PasswordHashService passwordHashService) {
        this.memberRepository = memberRepository;
        this.passwordHashService = passwordHashService;
    }

    @Transactional
    public MemberResponse createMember(MemberCreateRequest request) {
        validateCreateRequest(request);

        String normalizedEmail = normalizeEmail(request.email());
        if (memberRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Member email already exists.");
        }

        Member member = Member.create(
                normalizedEmail,
                request.nickname(),
                passwordHashService.hash(request.password())
        );
        return MemberResponse.from(memberRepository.save(member));
    }

    public MemberResponse getMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("Member not found. memberId=" + memberId));

        return MemberResponse.from(member);
    }

    private void validateCreateRequest(MemberCreateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Member request is required.");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new IllegalArgumentException("Member email is required.");
        }
        if (request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("Member password is required.");
        }
        if (request.nickname() == null || request.nickname().isBlank()) {
            throw new IllegalArgumentException("Member nickname is required.");
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
