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
    void calculateTravelMinutesReturnsMinimumMinutesForVeryCloseDifferentPlaces() {
        assertThat(adapter.calculateTravelMinutes(place(10L), place(20L))).isEqualTo(5);
    }

    @Test
    void calculateTravelMinutesReturnsDistanceBasedMinutes() {
        PlaceResponse previousPlace = place(10L, 33.458056, 126.942500);
        PlaceResponse currentPlace = place(20L, 33.305833, 126.289444);

        assertThat(adapter.calculateTravelMinutes(previousPlace, currentPlace)).isEqualTo(146);
    }

    @Test
    void calculateTravelMinutesSupportsCoordinateAnchors() {
        assertThat(adapter.calculateTravelMinutes(33.2394018, 126.3039897, 33.55384, 126.75357))
                .isEqualTo(127);
    }

    @Test
    void calculateTravelMinutesReturnsDefaultMinutesWhenCoordinatesAreMissing() {
        PlaceResponse previousPlace = place(10L, null, 126.942500);
        PlaceResponse currentPlace = place(20L, 33.305833, 126.289444);

        assertThat(adapter.calculateTravelMinutes(previousPlace, currentPlace)).isEqualTo(30);
    }

    private PlaceResponse place(Long placeId) {
        return place(placeId, 33.0, 126.0);
    }

    private PlaceResponse place(Long placeId, Double latitude, Double longitude) {
        return new PlaceResponse(
                placeId,
                "Place " + placeId,
                "NATURE",
                "EAST",
                "JEJU",
                latitude,
                longitude,
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
