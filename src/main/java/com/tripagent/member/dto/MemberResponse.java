package com.tripagent.member.dto;

import com.tripagent.member.domain.Member;
import com.tripagent.member.domain.MemberRole;
import java.time.LocalDateTime;

public record MemberResponse(
        Long memberId,
        String email,
        String nickname,
        MemberRole role,
        LocalDateTime createdAt
) {

    public MemberResponse(Long memberId, String email, String nickname, LocalDateTime createdAt) {
        this(memberId, email, nickname, MemberRole.USER, createdAt);
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getMemberId(),
                member.getEmail(),
                member.getNickname(),
                member.getRole(),
                member.getCreatedAt()
        );
    }
}
