package com.tripagent.itinerary.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tripagent.itinerary.dto.ItineraryDayTimeWindowRequest;
import com.tripagent.itinerary.dto.ItineraryGenerateRequest;
import com.tripagent.itinerary.dto.ItineraryPace;
import com.tripagent.place.dto.PlaceCategory;
import com.tripagent.trip.domain.Trip;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class ItineraryGenerationPreferenceTest {

    @Test
    void storesAndRestoresAllGenerationOptions() {
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(
                List.of(10L, 20L),
                List.of(30L),
                ItineraryPace.RELAXED,
                List.of(PlaceCategory.NATURE, PlaceCategory.CAFE),
                List.of(new ItineraryDayTimeWindowRequest(
                        1, LocalTime.of(10, 0), LocalTime.of(18, 0))),
                true,
                List.of(1)
        );

        ItineraryGenerationPreference preference = ItineraryGenerationPreference.create(null, request);

        assertThat(preference.toRequest()).isEqualTo(request);
    }

    @Test
    void updateReplacesPreviousOptions() {
        ItineraryGenerationPreference preference = ItineraryGenerationPreference.create(
                null,
                new ItineraryGenerateRequest(List.of(10L), List.of(20L), ItineraryPace.BUSY)
        );
        ItineraryGenerateRequest updated = new ItineraryGenerateRequest(
                List.of(30L),
                List.of(),
                ItineraryPace.NORMAL,
                List.of(PlaceCategory.FOOD),
                List.of(),
                false,
                List.of()
        );

        preference.update(updated);

        assertThat(preference.toRequest()).isEqualTo(updated);
    }
}
