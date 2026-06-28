package com.tripagent.ai.validator;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tripagent.place.dto.PlaceResponse;
import java.util.List;
import org.junit.jupiter.api.Test;

class CandidatePlaceValidatorTest {

    private final CandidatePlaceValidator validator = new CandidatePlaceValidator();

    @Test
    void validatePlaceIdsAllowsIdsIncludedInCandidatePlaces() {
        List<PlaceResponse> candidatePlaces = List.of(
                place(1L),
                place(2L)
        );

        assertThatCode(() -> validator.validatePlaceIds(candidatePlaces, List.of(1L, 2L)))
                .doesNotThrowAnyException();
    }

    @Test
    void validatePlaceIdsRejectsDuplicatedIdsEvenWhenIncludedInCandidatePlaces() {
        List<PlaceResponse> candidatePlaces = List.of(
                place(1L),
                place(2L)
        );

        assertThatThrownBy(() -> validator.validatePlaceIds(candidatePlaces, List.of(1L, 1L, 2L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Place id must not be duplicated in generated itinerary. placeId=1");
    }

    @Test
    void validatePlaceIdsRejectsIdNotIncludedInCandidatePlaces() {
        List<PlaceResponse> candidatePlaces = List.of(
                place(1L),
                place(2L)
        );

        assertThatThrownBy(() -> validator.validatePlaceIds(candidatePlaces, List.of(1L, 99L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Place id is not included in candidate places. placeId=99");
    }

    @Test
    void validatePlaceIdsRejectsEmptyCandidatePlaces() {
        assertThatThrownBy(() -> validator.validatePlaceIds(List.of(), List.of(1L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Candidate places are required.");
    }

    @Test
    void validatePlaceIdsRejectsEmptyPlaceIds() {
        assertThatThrownBy(() -> validator.validatePlaceIds(List.of(place(1L)), List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Place ids are required.");
    }

    @Test
    void validatePlaceIdsRejectsNullPlaceId() {
        List<PlaceResponse> candidatePlaces = List.of(place(1L));
        List<Long> placeIds = new java.util.ArrayList<>();
        placeIds.add(null);

        assertThatThrownBy(() -> validator.validatePlaceIds(candidatePlaces, placeIds))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Place id must not be null.");
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
