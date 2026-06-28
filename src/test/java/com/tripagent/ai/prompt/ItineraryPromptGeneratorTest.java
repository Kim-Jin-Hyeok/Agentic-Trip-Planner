package com.tripagent.ai.prompt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripConcept;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class ItineraryPromptGeneratorTest {

    private final ItineraryPromptGenerator generator = new ItineraryPromptGenerator();

    @Test
    void generateIncludesTripCandidatePlacesAndJsonFormat() {
        Trip trip = Trip.create(
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                TripConcept.FOOD,
                Transportation.RENT_CAR,
                "SEOGWIPO"
        );
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, "Food Place", "FOOD", "NORTH", 60, "Local food place."),
                place(20L, "Cafe Place", "CAFE", "WEST", 70, "Ocean view cafe.")
        );

        String prompt = generator.generate(trip, candidatePlaces);

        assertThat(prompt).contains("You must select places only from the provided candidatePlaces.");
        assertThat(prompt).contains("Do not create new place names.");
        assertThat(prompt).contains("Do not use any placeId that is not included in candidatePlaces.");
        assertThat(prompt).contains("Do not use the same placeId more than once in one generated itinerary.");
        assertThat(prompt).contains("For each dayNo, the first itinerary item must have orderNo 1 and travelMinutesFromPrevious 0.");
        assertThat(prompt).contains("For each dayNo, the first itinerary item's startTime must be at or after Trip.dailyStartTime.");
        assertThat(prompt).contains("Write every reason in Korean.");
        assertThat(prompt).contains("- concept: FOOD");
        assertThat(prompt).contains("- days: 3");
        assertThat(prompt).contains("- dailyStartTime: 09:00");
        assertThat(prompt).contains("placeId: 10");
        assertThat(prompt).contains("name: Food Place");
        assertThat(prompt).contains("category: FOOD");
        assertThat(prompt).contains("region: NORTH");
        assertThat(prompt).contains("avgStayMinutes: 60");
        assertThat(prompt).contains("description: Local food place.");
        assertThat(prompt).contains("\"placeId\"");
        assertThat(prompt).contains("\"dayNo\"");
        assertThat(prompt).contains("\"orderNo\"");
        assertThat(prompt).contains("\"startTime\"");
        assertThat(prompt).contains("\"endTime\"");
        assertThat(prompt).contains("\"travelMinutesFromPrevious\"");
        assertThat(prompt).contains("\"reason\"");
        assertThat(prompt).contains("한국어 이유");
    }

    @Test
    void generateRejectsNullTrip() {
        assertThatThrownBy(() -> generator.generate(null, List.of(place(10L, "Place", "NATURE", "EAST", 60, "Description"))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip is required.");
    }

    @Test
    void generateRejectsEmptyCandidatePlaces() {
        Trip trip = Trip.create(
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                TripConcept.FOOD,
                Transportation.RENT_CAR,
                "SEOGWIPO"
        );

        assertThatThrownBy(() -> generator.generate(trip, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Candidate places are required.");
    }

    private PlaceResponse place(
            Long placeId,
            String name,
            String category,
            String region,
            Integer avgStayMinutes,
            String description
    ) {
        return new PlaceResponse(
                placeId,
                name,
                category,
                region,
                "JEJU",
                33.0,
                126.0,
                avgStayMinutes,
                false,
                true,
                1,
                2,
                3,
                4,
                5,
                4,
                3,
                description
        );
    }
}
