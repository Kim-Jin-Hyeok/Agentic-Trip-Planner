package com.tripagent.place.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tripagent.member.domain.Member;
import org.junit.jupiter.api.Test;

class PlaceSuggestionTest {

    @Test
    void rejectStoresNormalizedReasonAndReviewTime() {
        PlaceSuggestion suggestion = suggestion();

        suggestion.reject("  주소가 제주 지역이 아닙니다.  ");

        assertThat(suggestion.getStatus()).isEqualTo(PlaceSuggestionStatus.REJECTED);
        assertThat(suggestion.getRejectionReason()).isEqualTo("주소가 제주 지역이 아닙니다.");
        assertThat(suggestion.getReviewedAt()).isNotNull();
    }

    @Test
    void rejectRequiresReason() {
        assertThatThrownBy(() -> suggestion().reject(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Place suggestion rejection reason is required.");
    }

    @Test
    void rejectDoesNotAllowRepeatedReview() {
        PlaceSuggestion suggestion = suggestion();
        suggestion.reject("등록 정보가 부족합니다.");

        assertThatThrownBy(() -> suggestion.reject("다시 거절합니다."))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Only pending place suggestions can be rejected.");
    }

    private PlaceSuggestion suggestion() {
        return PlaceSuggestion.create(
                Member.create("user@example.com", "user", "password-hash"),
                "새별오름",
                "제주특별자치도 제주시",
                "노을 명소"
        );
    }
}
