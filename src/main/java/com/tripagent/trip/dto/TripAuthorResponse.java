package com.tripagent.trip.dto;

import com.tripagent.member.domain.Member;

public record TripAuthorResponse(
        Long memberId,
        String nickname
) {

    public static TripAuthorResponse from(Member member) {
        return new TripAuthorResponse(
                member.getMemberId(),
                member.getNickname()
        );
    }
}
