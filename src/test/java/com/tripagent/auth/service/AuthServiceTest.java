package com.tripagent.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tripagent.auth.dto.LoginRequest;
import com.tripagent.auth.dto.LoginResponse;
import com.tripagent.member.domain.Member;
import com.tripagent.member.repository.MemberRepository;
import com.tripagent.member.service.PasswordHashService;
import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordHashService passwordHashService;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginReturnsMemberWhenEmailAndPasswordMatch() {
        Member member = Member.create("test@example.com", "testUser", "hashed-password");
        setId(member, 1L);
        LoginRequest request = new LoginRequest(" Test@Example.com ", "password123");
        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(member));
        when(passwordHashService.matches("password123", "hashed-password")).thenReturn(true);

        LoginResponse response = authService.login(request);

        assertThat(response.memberId()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("test@example.com");
        assertThat(response.nickname()).isEqualTo("testUser");
    }

    @Test
    void loginRejectsUnknownEmailWithGenericMessage() {
        LoginRequest request = new LoginRequest("missing@example.com", "password123");
        when(memberRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email or password is invalid.");
        verify(passwordHashService, never()).matches("password123", "hashed-password");
    }

    @Test
    void loginRejectsWrongPasswordWithGenericMessage() {
        Member member = Member.create("test@example.com", "testUser", "hashed-password");
        LoginRequest request = new LoginRequest("test@example.com", "wrong-password");
        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(member));
        when(passwordHashService.matches("wrong-password", "hashed-password")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email or password is invalid.");
    }

    @Test
    void loginRejectsNullRequest() {
        assertThatThrownBy(() -> authService.login(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Login request is required.");
        verify(memberRepository, never()).findByEmail(org.mockito.ArgumentMatchers.any());
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
