package com.tripagent.auth.dto;

import com.tripagent.member.domain.Member;
import com.tripagent.member.domain.MemberRole;

public record LoginResponse(
        Long memberId,
        String email,
        String nickname,
        MemberRole role,
        String accessToken,
        String tokenType
) {

    public LoginResponse(
            Long memberId,
            String email,
            String nickname,
            String accessToken,
            String tokenType
    ) {
        this(memberId, email, nickname, MemberRole.USER, accessToken, tokenType);
    }

    public static LoginResponse from(Member member, String accessToken) {
        return new LoginResponse(
                member.getMemberId(),
                member.getEmail(),
                member.getNickname(),
                member.getRole(),
                accessToken,
                "Bearer"
        );
    }
}
