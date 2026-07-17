package com.tripagent.place.dto;

import com.tripagent.member.domain.Member;
import com.tripagent.place.domain.PlaceSuggestion;
import com.tripagent.place.domain.PlaceSuggestionStatus;
import java.time.LocalDateTime;

public record AdminPlaceSuggestionResponse(
        Long placeSuggestionId,
        Long memberId,
        String memberEmail,
        String memberNickname,
        String name,
        String address,
        String description,
        PlaceSuggestionStatus status,
        LocalDateTime createdAt,
        String rejectionReason,
        LocalDateTime reviewedAt
) {

    public static AdminPlaceSuggestionResponse from(PlaceSuggestion placeSuggestion) {
        Member member = placeSuggestion.getMember();
        return new AdminPlaceSuggestionResponse(
                placeSuggestion.getPlaceSuggestionId(),
                member.getMemberId(),
                member.getEmail(),
                member.getNickname(),
                placeSuggestion.getName(),
                placeSuggestion.getAddress(),
                placeSuggestion.getDescription(),
                placeSuggestion.getStatus(),
                placeSuggestion.getCreatedAt(),
                placeSuggestion.getRejectionReason(),
                placeSuggestion.getReviewedAt()
        );
    }
}
