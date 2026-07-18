package com.tripagent.itinerary.policy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AccommodationAreaRegionMapperTest {

    private final AccommodationAreaRegionMapper mapper = new AccommodationAreaRegionMapper();

    @Test
    void mapsKnownAccommodationAreasToPlaceRegions() {
        assertThat(mapper.toPlaceRegion("SEOGWIPO")).isEqualTo("SOUTH");
        assertThat(mapper.toPlaceRegion("JEJU_CITY")).isEqualTo("NORTH");
        assertThat(mapper.toPlaceRegion("AEWOL")).isEqualTo("WEST");
        assertThat(mapper.toPlaceRegion("JOCHEON")).isEqualTo("EAST");
        assertThat(mapper.toPlaceRegion("SEONGSAN")).isEqualTo("EAST");
        assertThat(mapper.toPlaceRegion("PYOSEON")).isEqualTo("EAST");
        assertThat(mapper.toPlaceRegion("ANDEOK")).isEqualTo("WEST");
        assertThat(mapper.toPlaceRegion("NAMWON")).isEqualTo("SOUTH");
    }

    @Test
    void returnsRegionWhenAreaAlreadyUsesPlaceRegionValue() {
        assertThat(mapper.toPlaceRegion("EAST")).isEqualTo("EAST");
        assertThat(mapper.toPlaceRegion("WEST")).isEqualTo("WEST");
        assertThat(mapper.toPlaceRegion("NORTH")).isEqualTo("NORTH");
        assertThat(mapper.toPlaceRegion("SOUTH")).isEqualTo("SOUTH");
    }

    @Test
    void normalizesAreaBeforeMapping() {
        assertThat(mapper.toPlaceRegion(" seogwipo ")).isEqualTo("SOUTH");
        assertThat(mapper.toPlaceRegion(" west ")).isEqualTo("WEST");
    }

    @Test
    void returnsNullWhenAreaCannotBeMapped() {
        assertThat(mapper.toPlaceRegion(null)).isNull();
        assertThat(mapper.toPlaceRegion(" ")).isNull();
        assertThat(mapper.toPlaceRegion("UNKNOWN")).isNull();
    }
}
