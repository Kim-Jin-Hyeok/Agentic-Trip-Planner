package com.tripagent.itinerary.policy;

import com.tripagent.itinerary.dto.ItineraryPace;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record PaceItineraryPolicy(
        ItineraryPace pace,
        int minItemsPerDay,
        int maxItemsPerDay,
        String promptDescription
) {

    private static final Map<ItineraryPace, PaceItineraryPolicy> POLICIES = Map.of(
            ItineraryPace.RELAXED,
            new PaceItineraryPolicy(
                    ItineraryPace.RELAXED,
                    3,
                    4,
                    "with generous travel and rest time"
            ),
            ItineraryPace.NORMAL,
            new PaceItineraryPolicy(
                    ItineraryPace.NORMAL,
                    4,
                    5,
                    "with moderate travel time"
            ),
            ItineraryPace.BUSY,
            new PaceItineraryPolicy(
                    ItineraryPace.BUSY,
                    5,
                    7,
                    "to visit more places while staying realistic"
            )
    );

    public static Optional<PaceItineraryPolicy> findByPace(ItineraryPace pace) {
        if (pace == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(POLICIES.get(pace));
    }

    public static boolean existsFor(ItineraryPace pace) {
        return findByPace(pace).isPresent();
    }

    public static List<PaceItineraryPolicy> all() {
        return List.of(
                POLICIES.get(ItineraryPace.RELAXED),
                POLICIES.get(ItineraryPace.NORMAL),
                POLICIES.get(ItineraryPace.BUSY)
        );
    }

    public String promptGuideLine() {
        return pace + ": Plan "
                + minItemsPerDay
                + " to "
                + maxItemsPerDay
                + " itinerary items per day "
                + promptDescription
                + ".";
    }

    public String explicitPromptPolicyLine() {
        return "Explicit pace item count policy: For every dayNo in the trip, create at least "
                + minItemsPerDay
                + " and at most "
                + maxItemsPerDay
                + " itinerary items per day.";
    }
}
