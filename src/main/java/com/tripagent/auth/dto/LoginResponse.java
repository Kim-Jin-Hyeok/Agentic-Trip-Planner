package com.tripagent.auth.dto;

import com.tripagent.member.domain.Member;

public record LoginResponse(
        Long memberId,
        String email,
        String nickname
) {

    public static LoginResponse from(Member member) {
        return new LoginResponse(
                member.getMemberId(),
                member.getEmail(),
                member.getNickname()
        );
    }
}
