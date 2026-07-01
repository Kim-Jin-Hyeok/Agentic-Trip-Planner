package com.tripagent.auth.dto;

import com.tripagent.member.domain.Member;

public record LoginResponse(
        Long memberId,
        String email,
        String nickname,
        String accessToken,
        String tokenType
) {

    public static LoginResponse from(Member member, String accessToken) {
        return new LoginResponse(
                member.getMemberId(),
                member.getEmail(),
                member.getNickname(),
                accessToken,
                "Bearer"
        );
    }
}
