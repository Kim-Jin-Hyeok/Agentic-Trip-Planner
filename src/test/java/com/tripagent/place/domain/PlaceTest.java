package com.tripagent.place.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class PlaceTest {

    @Test
    void linkExternalReferenceStoresNormalizedValues() {
        Place place = place();

        place.linkExternalReference("  KAKAO_LOCAL  ", "  25274725  ");

        assertThat(place.getExternalProvider()).isEqualTo("KAKAO_LOCAL");
        assertThat(place.getExternalPlaceId()).isEqualTo("25274725");
    }

    @Test
    void linkExternalReferenceRequiresProviderAndPlaceId() {
        assertThatThrownBy(() -> place().linkExternalReference(" ", "25274725"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("External place provider is required.");
        assertThatThrownBy(() -> place().linkExternalReference("KAKAO_LOCAL", " "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("External place id is required.");
    }

    private Place place() {
        return Place.create(
                "새별오름",
                "NATURE",
                "WEST",
                "제주특별자치도 제주시",
                33.3661,
                126.3577,
                60,
                false,
                true,
                1,
                5,
                1,
                1,
                5,
                4,
                4,
                "설명",
                true
        );
    }
}
