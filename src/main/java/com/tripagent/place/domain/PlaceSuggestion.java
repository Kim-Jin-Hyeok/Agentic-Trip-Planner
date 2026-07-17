package com.tripagent.place.domain;

import com.tripagent.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "place_suggestions")
public class PlaceSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeSuggestionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PlaceSuggestionStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected PlaceSuggestion() {
    }

    private PlaceSuggestion(
            Member member,
            String name,
            String address,
            String description,
            PlaceSuggestionStatus status,
            LocalDateTime createdAt
    ) {
        this.member = member;
        this.name = name;
        this.address = address;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static PlaceSuggestion create(Member member, String name, String address, String description) {
        if (member == null) {
            throw new IllegalArgumentException("Place suggestion member is required.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Place suggestion name is required.");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Place suggestion address is required.");
        }

        return new PlaceSuggestion(
                member,
                name.trim(),
                address.trim(),
                normalizeOptionalText(description),
                PlaceSuggestionStatus.PENDING,
                LocalDateTime.now()
        );
    }

    private static String normalizeOptionalText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    public Long getPlaceSuggestionId() {
        return placeSuggestionId;
    }

    public Member getMember() {
        return member;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public PlaceSuggestionStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
