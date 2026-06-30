package com.tripagent.itinerary.policy;

import static org.assertj.core.api.Assertions.assertThat;

import com.tripagent.itinerary.dto.ItineraryPace;
import org.junit.jupiter.api.Test;

class PaceItineraryPolicyTest {

    @Test
    void findByPaceReturnsRelaxedPolicy() {
        PaceItineraryPolicy policy = PaceItineraryPolicy.findByPace(ItineraryPace.RELAXED).orElseThrow();

        assertThat(policy.minItemsPerDay()).isEqualTo(3);
        assertThat(policy.maxItemsPerDay()).isEqualTo(4);
        assertThat(policy.promptGuideLine())
                .isEqualTo("RELAXED: Plan 3 to 4 itinerary items per day with generous travel and rest time.");
    }

    @Test
    void findByPaceReturnsNormalPolicy() {
        PaceItineraryPolicy policy = PaceItineraryPolicy.findByPace(ItineraryPace.NORMAL).orElseThrow();

        assertThat(policy.minItemsPerDay()).isEqualTo(4);
        assertThat(policy.maxItemsPerDay()).isEqualTo(5);
        assertThat(policy.promptGuideLine())
                .isEqualTo("NORMAL: Plan 4 to 5 itinerary items per day with moderate travel time.");
    }

    @Test
    void findByPaceReturnsBusyPolicy() {
        PaceItineraryPolicy policy = PaceItineraryPolicy.findByPace(ItineraryPace.BUSY).orElseThrow();

        assertThat(policy.minItemsPerDay()).isEqualTo(5);
        assertThat(policy.maxItemsPerDay()).isEqualTo(7);
        assertThat(policy.promptGuideLine())
                .isEqualTo("BUSY: Plan 5 to 7 itinerary items per day to visit more places while staying realistic.");
    }

    @Test
    void existsForReturnsFalseWhenPaceIsNull() {
        assertThat(PaceItineraryPolicy.existsFor(null)).isFalse();
        assertThat(PaceItineraryPolicy.findByPace(null)).isEmpty();
    }

    @Test
    void allReturnsPoliciesInPromptOrder() {
        assertThat(PaceItineraryPolicy.all())
                .extracting(PaceItineraryPolicy::pace)
                .containsExactly(ItineraryPace.RELAXED, ItineraryPace.NORMAL, ItineraryPace.BUSY);
    }
}
