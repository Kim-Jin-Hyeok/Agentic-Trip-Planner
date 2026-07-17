package com.tripagent.auth.service;

import com.tripagent.auth.support.AuthorizationException;
import com.tripagent.member.domain.MemberRole;
import com.tripagent.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminAuthorizationService {

    private static final String ADMIN_ACCESS_REQUIRED_MESSAGE = "Admin access is required.";

    private final MemberRepository memberRepository;

    public AdminAuthorizationService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void requireAdmin(Long memberId) {
        boolean admin = memberId != null
                && memberRepository.findById(memberId)
                .map(member -> MemberRole.ADMIN == member.getRole())
                .orElse(false);
        if (!admin) {
            throw new AuthorizationException(ADMIN_ACCESS_REQUIRED_MESSAGE);
        }
    }
}
