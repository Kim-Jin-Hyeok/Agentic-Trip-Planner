package com.tripagent.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tripagent.member.domain.Member;
import com.tripagent.member.domain.MemberRole;
import com.tripagent.member.dto.MemberCreateRequest;
import com.tripagent.member.dto.MemberResponse;
import com.tripagent.member.repository.MemberRepository;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordHashService passwordHashService;

    @InjectMocks
    private MemberService memberService;

    @Test
    void createMemberSavesNormalizedEmailAndHashedPassword() {
        MemberCreateRequest request = new MemberCreateRequest(
                " Test@Example.com ",
                "password123",
                " testUser "
        );
        when(memberRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordHashService.hash("password123")).thenReturn("hashed-password");
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> {
            Member member = invocation.getArgument(0);
            setId(member, 1L);
            return member;
        });

        MemberResponse response = memberService.createMember(request);

        assertThat(response.memberId()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("test@example.com");
        assertThat(response.nickname()).isEqualTo("testUser");
        assertThat(response.role()).isEqualTo(MemberRole.USER);
        assertThat(response.createdAt()).isNotNull();

        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(memberCaptor.capture());
        assertThat(memberCaptor.getValue().getEmail()).isEqualTo("test@example.com");
        assertThat(memberCaptor.getValue().getNickname()).isEqualTo("testUser");
        assertThat(memberCaptor.getValue().getPasswordHash()).isEqualTo("hashed-password");
        assertThat(memberCaptor.getValue().getRole()).isEqualTo(MemberRole.USER);
    }

    @Test
    void createMemberRejectsDuplicatedEmail() {
        MemberCreateRequest request = new MemberCreateRequest(
                "test@example.com",
                "password123",
                "testUser"
        );
        when(memberRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThatThrownBy(() -> memberService.createMember(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Member email already exists.");
        verify(passwordHashService, never()).hash(any());
        verify(memberRepository, never()).save(any());
    }

    @Test
    void createMemberRejectsNullRequest() {
        assertThatThrownBy(() -> memberService.createMember(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Member request is required.");
        verify(memberRepository, never()).save(any());
    }

    @Test
    void getMemberReturnsMember() {
        Member member = Member.create("test@example.com", "testUser", "hashed-password");
        setId(member, 1L);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        MemberResponse response = memberService.getMember(1L);

        assertThat(response.memberId()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("test@example.com");
        assertThat(response.nickname()).isEqualTo("testUser");
    }

    @Test
    void getMemberRejectsUnknownMemberId() {
        when(memberRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.getMember(999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Member not found. memberId=999");
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
