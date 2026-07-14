package com.tripagent.ai.prompt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tripagent.itinerary.dto.ItineraryGenerateRequest;
import com.tripagent.itinerary.dto.ItineraryDayTimeWindowRequest;
import com.tripagent.itinerary.dto.ItineraryPace;
import com.tripagent.place.dto.PlaceCategory;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.route.SimpleRouteCalculationAdapter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class ItineraryPromptGeneratorTest {

    private final ItineraryPromptGenerator generator = new ItineraryPromptGenerator(
            new SimpleRouteCalculationAdapter()
    );

    @Test
    void generateIncludesTripCandidatePlacesAndJsonFormat() {
        Trip trip = Trip.create(
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
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
        assertThat(prompt).contains("You must include every placeId listed in mustVisitPlaceIds in the generated itinerary.");
        assertThat(prompt).contains("You must never include any placeId listed in excludedPlaceIds in the generated itinerary.");
        assertThat(prompt).contains("If preferredCategories is not empty, prioritize places in those categories when building the itinerary.");
        assertThat(prompt).contains("If rainyDayMode is true, prioritize indoor places and places with higher rainyDayScore on every trip day.");
        assertThat(prompt).contains("For each dayNo listed in rainyDayNos, prioritize indoor places and places with higher rainyDayScore only on that day.");
        assertThat(prompt).contains("mustVisitPlaceIds must be included even if their categories are not in preferredCategories.");
        assertThat(prompt).contains("excludedPlaceIds must never be included regardless of preferredCategories.");
        assertThat(prompt).contains("Use concept as the overall trip mood and priority, not as the only category to schedule.");
        assertThat(prompt).contains("Do not fill every itinerary item with only one concept category.");
        assertThat(prompt).contains("When possible, include at least one FOOD place per day.");
        assertThat(prompt).contains("On days with 3 or more itinerary items, mix tour places such as NATURE, BEACH, GARDEN, or MUSEUM with rest or meal places such as FOOD or CAFE when possible.");
        assertThat(prompt).contains("Even for a CAFE concept, do not schedule only cafes back-to-back; include meals and tour places when possible.");
        assertThat(prompt).contains("Even for a NATURE concept, do not schedule only nature places back-to-back; include FOOD or CAFE places when possible.");
        assertThat(prompt).contains("For each dayNo, the first itinerary item must have orderNo 1 and travelMinutesFromPrevious 0.");
        assertThat(prompt).contains("the next placeId must be included in the current place's recommendedNextPlaceIdsWithin90Minutes.");
        assertThat(prompt).contains("Never create a same-day route whose calculated travel time exceeds 90 minutes.");
        assertThat(prompt).contains("For each dayNo, the first itinerary item's startTime must be at or after Trip.dailyStartTime.");
        assertThat(prompt).contains("For each dayNo, the last itinerary item's endTime must be at or before Trip.dailyEndTime.");
        assertThat(prompt).contains("Write every reason in Korean.");
        assertThat(prompt).contains("- concept: FOOD");
        assertThat(prompt).contains("- days: 3");
        assertThat(prompt).contains("- dailyStartTime: 09:00");
        assertThat(prompt).contains("- dailyEndTime: 18:00");
        assertThat(prompt).contains("- preferredCategories: []");
        assertThat(prompt).contains("- rainyDayMode: false");
        assertThat(prompt).contains("- rainyDayNos: []");
        assertThat(prompt).contains("- selectedPace: NORMAL");
        assertThat(prompt).contains("RELAXED: Plan 3 to 4 itinerary items per day with generous travel and rest time.");
        assertThat(prompt).contains("NORMAL: Plan 4 to 5 itinerary items per day with moderate travel time.");
        assertThat(prompt).contains("BUSY: Plan 5 to 7 itinerary items per day to visit more places while staying realistic.");
        assertThat(prompt).contains("placeId: 10");
        assertThat(prompt).contains("name: Food Place");
        assertThat(prompt).contains("category: FOOD");
        assertThat(prompt).contains("region: NORTH");
        assertThat(prompt).contains("avgStayMinutes: 60");
        assertThat(prompt).contains("indoorYn: false");
        assertThat(prompt).contains("rainyDayScore: 1");
        assertThat(prompt).contains("recommendedNextPlaceIdsWithin90Minutes:");
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
    void generateIncludesMustVisitAndExcludedPlaceControls() {
        Trip trip = Trip.create(
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.FOOD,
                Transportation.RENT_CAR,
                "SEOGWIPO"
        );
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, "Food Place", "FOOD", "NORTH", 60, "Local food place."),
                place(20L, "Cafe Place", "CAFE", "WEST", 70, "Ocean view cafe.")
        );
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(List.of(10L), List.of(30L));

        String prompt = generator.generate(trip, candidatePlaces, request);

        assertThat(prompt).contains("- mustVisitPlaceIds: [10]");
        assertThat(prompt).contains("- excludedPlaceIds: [30]");
        assertThat(prompt).contains("You must include every placeId listed in mustVisitPlaceIds in the generated itinerary.");
        assertThat(prompt).contains("You must never include any placeId listed in excludedPlaceIds in the generated itinerary.");
    }

    @Test
    void generateIncludesPreferredCategoriesRulesAndValues() {
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(
                List.of(10L),
                List.of(30L),
                ItineraryPace.NORMAL,
                List.of(PlaceCategory.FOOD, PlaceCategory.CAFE)
        );

        String prompt = generator.generate(trip(), candidatePlaces(), request);

        assertThat(prompt).contains("- preferredCategories: [FOOD, CAFE]");
        assertThat(prompt).contains("If preferredCategories is not empty, prioritize places in those categories when building the itinerary.");
        assertThat(prompt).contains("mustVisitPlaceIds must be included even if their categories are not in preferredCategories.");
        assertThat(prompt).contains("excludedPlaceIds must never be included regardless of preferredCategories.");
    }

    @Test
    void generateIncludesRainyDayModeRulesAndValues() {
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(
                null,
                null,
                ItineraryPace.NORMAL,
                null,
                null,
                true
        );

        String prompt = generator.generate(trip(), candidatePlaces(), request);

        assertThat(prompt).contains("- rainyDayMode: true");
        assertThat(prompt).contains("If rainyDayMode is true, prioritize indoor places and places with higher rainyDayScore on every trip day.");
        assertThat(prompt).contains("indoorYn: false");
        assertThat(prompt).contains("rainyDayScore: 1");
    }

    @Test
    void generateIncludesRainyDayNumbersRulesAndValues() {
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(
                null,
                null,
                ItineraryPace.NORMAL,
                null,
                null,
                false,
                List.of(2)
        );

        String prompt = generator.generate(trip(), candidatePlaces(), request);

        assertThat(prompt).contains("- rainyDayMode: false");
        assertThat(prompt).contains("- rainyDayNos: [2]");
        assertThat(prompt).contains(
                "For each dayNo listed in rainyDayNos, prioritize indoor places and places with higher rainyDayScore only on that day."
        );
    }

    @Test
    void generateForTargetDayIncludesOnlyTargetDayScope() {
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(null, null, ItineraryPace.NORMAL);

        String prompt = generator.generate(trip(), candidatePlaces(), request, 2);

        assertThat(prompt).contains("- targetDayNo: 2");
        assertThat(prompt).contains("Generate itinerary items only for targetDayNo.");
        assertThat(prompt).contains("- dayNo: 2, startTime: 09:00, endTime: 18:00");
        assertThat(prompt).doesNotContain("- dayNo: 1, startTime:");
        assertThat(prompt).doesNotContain("- dayNo: 3, startTime:");
    }

    @Test
    void generateIncludesDayTimeWindowsWithDefaultsAndOverrides() {
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(
                null,
                null,
                ItineraryPace.NORMAL,
                null,
                List.of(
                        new ItineraryDayTimeWindowRequest(1, LocalTime.of(14, 0), LocalTime.of(18, 0)),
                        new ItineraryDayTimeWindowRequest(3, LocalTime.of(9, 0), LocalTime.of(17, 0))
                )
        );

        String prompt = generator.generate(trip(), candidatePlaces(), request);

        assertThat(prompt).contains("Day time windows:");
        assertThat(prompt).contains("Every itinerary item's startTime and endTime must be inside that dayNo's available time window.");
        assertThat(prompt).contains("- dayNo: 1, startTime: 14:00, endTime: 18:00");
        assertThat(prompt).contains("- dayNo: 2, startTime: 09:00, endTime: 18:00");
        assertThat(prompt).contains("- dayNo: 3, startTime: 09:00, endTime: 17:00");
    }

    @Test
    void generateDefaultsToNormalPaceWhenRequestIsNull() {
        String prompt = generator.generate(trip(), candidatePlaces());

        assertThat(prompt).contains("- selectedPace: NORMAL");
        assertThat(prompt).contains("NORMAL: Plan 4 to 5 itinerary items per day with moderate travel time.");
        assertThat(prompt).doesNotContain("Explicit pace item count policy:");
    }

    @Test
    void generateDefaultsToNormalPaceWhenPaceIsNull() {
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(null, null, null);

        String prompt = generator.generate(trip(), candidatePlaces(), request);

        assertThat(prompt).contains("- selectedPace: NORMAL");
        assertThat(prompt).contains("NORMAL: Plan 4 to 5 itinerary items per day with moderate travel time.");
        assertThat(prompt).doesNotContain("Explicit pace item count policy:");
    }

    @Test
    void generateIncludesRelaxedPaceRule() {
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(null, null, ItineraryPace.RELAXED);

        String prompt = generator.generate(trip(), candidatePlaces(), request);

        assertThat(prompt).contains("- selectedPace: RELAXED");
        assertThat(prompt).contains("RELAXED: Plan 3 to 4 itinerary items per day with generous travel and rest time.");
        assertThat(prompt).contains(
                "Explicit pace item count policy: For every dayNo in the trip, create at least 3 and at most 4 itinerary items per day."
        );
        assertThat(prompt).contains("This item count policy must be satisfied for each dayNo, including multi-day trips.");
        assertThat(prompt).contains(
                "If dayTimeWindows are provided, still satisfy this item count policy inside each dayNo's available time window."
        );
    }

    @Test
    void generateIncludesNormalPaceRule() {
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(null, null, ItineraryPace.NORMAL);

        String prompt = generator.generate(trip(), candidatePlaces(), request);

        assertThat(prompt).contains("- selectedPace: NORMAL");
        assertThat(prompt).contains("NORMAL: Plan 4 to 5 itinerary items per day with moderate travel time.");
        assertThat(prompt).contains(
                "Explicit pace item count policy: For every dayNo in the trip, create at least 4 and at most 5 itinerary items per day."
        );
    }

    @Test
    void generateIncludesBusyPaceRule() {
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(null, null, ItineraryPace.BUSY);

        String prompt = generator.generate(trip(), candidatePlaces(), request);

        assertThat(prompt).contains("- selectedPace: BUSY");
        assertThat(prompt).contains("BUSY: Plan 5 to 7 itinerary items per day to visit more places while staying realistic.");
        assertThat(prompt).contains(
                "Explicit pace item count policy: For every dayNo in the trip, create at least 5 and at most 7 itinerary items per day."
        );
    }

    @Test
    void generateRejectsEmptyCandidatePlaces() {
        Trip trip = Trip.create(
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.FOOD,
                Transportation.RENT_CAR,
                "SEOGWIPO"
        );

        assertThatThrownBy(() -> generator.generate(trip, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Candidate places are required.");
    }

    private Trip trip() {
        return Trip.create(
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.FOOD,
                Transportation.RENT_CAR,
                "SEOGWIPO"
        );
    }

    private List<PlaceResponse> candidatePlaces() {
        return List.of(
                place(10L, "Food Place", "FOOD", "NORTH", 60, "Local food place."),
                place(20L, "Cafe Place", "CAFE", "WEST", 70, "Ocean view cafe.")
        );
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
