package com.tripagent.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String nickname;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected Member() {
    }

    private Member(String email, String nickname, String passwordHash, LocalDateTime createdAt) {
        this.email = email;
        this.nickname = nickname;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    public static Member create(String email, String nickname, String passwordHash) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Member email is required.");
        }
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("Member nickname is required.");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("Member passwordHash is required.");
        }

        return new Member(
                email.trim().toLowerCase(),
                nickname.trim(),
                passwordHash,
                LocalDateTime.now()
        );
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
