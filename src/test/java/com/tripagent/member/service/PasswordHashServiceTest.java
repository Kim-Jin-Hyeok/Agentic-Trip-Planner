package com.tripagent.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class PasswordHashServiceTest {

    private final PasswordHashService passwordHashService = new PasswordHashService();

    @Test
    void hashCreatesPbkdf2HashAndMatchesRawPassword() {
        String passwordHash = passwordHashService.hash("password123");

        assertThat(passwordHash).startsWith("pbkdf2_sha256$");
        assertThat(passwordHash).isNotEqualTo("password123");
        assertThat(passwordHashService.matches("password123", passwordHash)).isTrue();
        assertThat(passwordHashService.matches("wrong-password", passwordHash)).isFalse();
    }

    @Test
    void hashUsesRandomSalt() {
        String firstHash = passwordHashService.hash("password123");
        String secondHash = passwordHashService.hash("password123");

        assertThat(firstHash).isNotEqualTo(secondHash);
    }

    @Test
    void hashRejectsBlankPassword() {
        assertThatThrownBy(() -> passwordHashService.hash(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Member password is required.");
    }
}
