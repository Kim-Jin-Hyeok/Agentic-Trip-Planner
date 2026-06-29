package com.tripagent.route;

import static org.assertj.core.api.Assertions.assertThat;

import com.tripagent.place.dto.PlaceResponse;
import org.junit.jupiter.api.Test;

class SimpleRouteCalculationAdapterTest {

    private final SimpleRouteCalculationAdapter adapter = new SimpleRouteCalculationAdapter();

    @Test
    void calculateTravelMinutesReturnsZeroWhenPreviousPlaceIsMissing() {
        assertThat(adapter.calculateTravelMinutes(null, place(10L))).isZero();
    }

    @Test
    void calculateTravelMinutesReturnsZeroWhenPlacesAreSame() {
        PlaceResponse place = place(10L);

        assertThat(adapter.calculateTravelMinutes(place, place)).isZero();
    }

    @Test
    void calculateTravelMinutesReturnsDefaultMinutesForDifferentPlaces() {
        assertThat(adapter.calculateTravelMinutes(place(10L), place(20L))).isEqualTo(30);
    }

    private PlaceResponse place(Long placeId) {
        return new PlaceResponse(
                placeId,
                "Place " + placeId,
                "NATURE",
                "EAST",
                "JEJU",
                33.0,
                126.0,
                60,
                false,
                true,
                1,
                2,
                3,
                4,
                5,
                4,
                3,
                "description"
        );
    }
}
