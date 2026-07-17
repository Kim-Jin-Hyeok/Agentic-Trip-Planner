package com.tripagent.auth.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.tripagent.auth.support.AuthorizationException;
import com.tripagent.member.domain.Member;
import com.tripagent.member.domain.MemberRole;
import com.tripagent.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class AdminAuthorizationServiceTest {

    private MemberRepository memberRepository;
    private AdminAuthorizationService adminAuthorizationService;

    @BeforeEach
    void setUp() {
        memberRepository = org.mockito.Mockito.mock(MemberRepository.class);
        adminAuthorizationService = new AdminAuthorizationService(memberRepository);
    }

    @Test
    void requireAdminAllowsAdminMember() {
        Member admin = member(1L, MemberRole.ADMIN);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(admin));

        assertThatCode(() -> adminAuthorizationService.requireAdmin(1L)).doesNotThrowAnyException();
    }

    @Test
    void requireAdminRejectsUserMember() {
        Member user = member(1L, MemberRole.USER);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> adminAuthorizationService.requireAdmin(1L))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("Admin access is required.");
    }

    @Test
    void requireAdminRejectsUnknownMember() {
        when(memberRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminAuthorizationService.requireAdmin(999L))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("Admin access is required.");
    }

    private Member member(Long memberId, MemberRole role) {
        Member member = Member.create("member@example.com", "member", "password-hash");
        ReflectionTestUtils.setField(member, "memberId", memberId);
        ReflectionTestUtils.setField(member, "role", role);
        return member;
    }
}
