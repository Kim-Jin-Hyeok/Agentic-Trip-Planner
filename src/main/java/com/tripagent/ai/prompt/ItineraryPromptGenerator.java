package com.tripagent.ai.prompt;

import com.tripagent.itinerary.dto.ItineraryGenerateRequest;
import com.tripagent.itinerary.dto.ItineraryDayTimeWindowRequest;
import com.tripagent.itinerary.dto.ItineraryPace;
import com.tripagent.itinerary.policy.PaceItineraryPolicy;
import com.tripagent.place.dto.PlaceCategory;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.trip.domain.Trip;
import com.tripagent.route.RouteCalculationAdapter;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import org.springframework.stereotype.Component;

@Component
public class ItineraryPromptGenerator {

    private static final int MAX_TRAVEL_MINUTES_BETWEEN_PLACES = 90;
    private static final int MAX_RECOMMENDED_NEXT_PLACES = 8;

    private final RouteCalculationAdapter routeCalculationAdapter;

    public ItineraryPromptGenerator(RouteCalculationAdapter routeCalculationAdapter) {
        this.routeCalculationAdapter = routeCalculationAdapter;
    }

    public String generate(Trip trip, List<PlaceResponse> candidatePlaces) {
        return generate(trip, candidatePlaces, null, null);
    }

    public String generate(Trip trip, List<PlaceResponse> candidatePlaces, ItineraryGenerateRequest request) {
        return generate(trip, candidatePlaces, request, null);
    }

    public String generate(
            Trip trip,
            List<PlaceResponse> candidatePlaces,
            ItineraryGenerateRequest request,
            Integer targetDayNo
    ) {
        if (trip == null) {
            throw new IllegalArgumentException("Trip is required.");
        }
        if (candidatePlaces == null || candidatePlaces.isEmpty()) {
            throw new IllegalArgumentException("Candidate places are required.");
        }

        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a data-driven Jeju trip itinerary planner.\n");
        prompt.append("Create a realistic itinerary using only the provided candidatePlaces.\n\n");
        prompt.append("Rules:\n");
        prompt.append("- You must select places only from the provided candidatePlaces.\n");
        prompt.append("- Do not create new place names.\n");
        prompt.append("- Do not use any placeId that is not included in candidatePlaces.\n");
        prompt.append("- Every itinerary item must reference an existing placeId from candidatePlaces.\n");
        prompt.append("- Do not use the same placeId more than once in one generated itinerary.\n");
        prompt.append("- You must include every placeId listed in mustVisitPlaceIds in the generated itinerary.\n");
        prompt.append("- You must never include any placeId listed in excludedPlaceIds in the generated itinerary.\n");
        prompt.append("- If preferredCategories is not empty, prioritize places in those categories when building the itinerary.\n");
        prompt.append("- If rainyDayMode is true, prioritize indoor places and places with higher rainyDayScore on every trip day.\n");
        prompt.append("- For each dayNo listed in rainyDayNos, prioritize indoor places and places with higher rainyDayScore only on that day.\n");
        prompt.append("- mustVisitPlaceIds must be included even if their categories are not in preferredCategories.\n");
        prompt.append("- excludedPlaceIds must never be included regardless of preferredCategories.\n");
        prompt.append("- Use concept as the overall trip mood and priority, not as the only category to schedule.\n");
        prompt.append("- Do not fill every itinerary item with only one concept category.\n");
        prompt.append("- When possible, include at least one FOOD place per day.\n");
        prompt.append("- On days with 3 or more itinerary items, mix tour places such as NATURE, BEACH, GARDEN, or MUSEUM with rest or meal places such as FOOD or CAFE when possible.\n");
        prompt.append("- Even for a CAFE concept, do not schedule only cafes back-to-back; include meals and tour places when possible.\n");
        prompt.append("- Even for a NATURE concept, do not schedule only nature places back-to-back; include FOOD or CAFE places when possible.\n");
        prompt.append("- Follow the selected pace when choosing how many places to schedule each day.\n");
        prompt.append("- For each dayNo, the first itinerary item must have orderNo 1 and travelMinutesFromPrevious 0.\n");
        prompt.append("- For consecutive items on the same day, the next placeId must be included in the current place's recommendedNextPlaceIdsWithin90Minutes.\n");
        prompt.append("- Never create a same-day route whose calculated travel time exceeds 90 minutes.\n");
        prompt.append("- For each dayNo, the first itinerary item's startTime must be at or after Trip.dailyStartTime.\n");
        prompt.append("- For each dayNo, the last itinerary item's endTime must be at or before Trip.dailyEndTime.\n");
        prompt.append("- Every itinerary item's startTime and endTime must be inside that dayNo's available time window.\n");
        if (targetDayNo != null) {
            prompt.append("- Generate itinerary items only for targetDayNo.\n");
            prompt.append("- Every returned itinerary item must have dayNo equal to targetDayNo.\n");
        }
        prompt.append("- Write every reason in Korean.\n");
        prompt.append("- Return JSON only. Do not include markdown or explanation outside JSON.\n\n");
        prompt.append("Trip:\n");
        prompt.append("- destination: ").append(trip.getDestination()).append("\n");
        prompt.append("- startDate: ").append(trip.getStartDate()).append("\n");
        prompt.append("- endDate: ").append(trip.getEndDate()).append("\n");
        prompt.append("- days: ").append(calculateTripDays(trip)).append("\n");
        prompt.append("- dailyStartTime: ").append(trip.getDailyStartTime()).append("\n");
        prompt.append("- dailyEndTime: ").append(trip.getDailyEndTime()).append("\n");
        prompt.append("- concept: ").append(trip.getConcept()).append("\n");
        prompt.append("- transportation: ").append(trip.getTransportation()).append("\n");
        prompt.append("- lastAccommodationArea: ").append(trip.getLastAccommodationArea()).append("\n\n");
        if (targetDayNo != null) {
            prompt.append("Generation scope:\n");
            prompt.append("- targetDayNo: ").append(targetDayNo).append("\n\n");
        }
        prompt.append("Day time windows:\n");
        prompt.append("- Use these final available time windows for each dayNo.\n");
        for (DayTimeWindow dayTimeWindow : dayTimeWindows(trip, request, targetDayNo)) {
            prompt.append("- dayNo: ").append(dayTimeWindow.dayNo())
                    .append(", startTime: ").append(dayTimeWindow.startTime())
                    .append(", endTime: ").append(dayTimeWindow.endTime())
                    .append("\n");
        }
        prompt.append("\n");
        prompt.append("Place controls:\n");
        prompt.append("- mustVisitPlaceIds: ").append(mustVisitPlaceIds(request)).append("\n");
        prompt.append("- excludedPlaceIds: ").append(excludedPlaceIds(request)).append("\n\n");
        prompt.append("Category preferences:\n");
        prompt.append("- preferredCategories: ").append(preferredCategories(request)).append("\n\n");
        prompt.append("Weather preference:\n");
        prompt.append("- rainyDayMode: ").append(rainyDayMode(request)).append("\n");
        prompt.append("- rainyDayNos: ").append(rainyDayNos(request)).append("\n\n");
        prompt.append("Pace:\n");
        prompt.append("- selectedPace: ").append(pace(request)).append("\n");
        for (PaceItineraryPolicy pacePolicy : PaceItineraryPolicy.all()) {
            prompt.append("- ").append(pacePolicy.promptGuideLine()).append("\n");
        }
        appendExplicitPacePolicy(prompt, request);
        prompt.append("\n");
        prompt.append("candidatePlaces:\n");
        for (PlaceResponse place : candidatePlaces) {
            prompt.append("- placeId: ").append(place.placeId()).append("\n");
            prompt.append("  name: ").append(place.name()).append("\n");
            prompt.append("  category: ").append(place.category()).append("\n");
            prompt.append("  region: ").append(place.region()).append("\n");
            prompt.append("  avgStayMinutes: ").append(place.avgStayMinutes()).append("\n");
            prompt.append("  indoorYn: ").append(place.indoorYn()).append("\n");
            prompt.append("  rainyDayScore: ").append(place.rainyDayScore()).append("\n");
            prompt.append("  recommendedNextPlaceIdsWithin90Minutes: ")
                    .append(recommendedNextPlaceIds(place, candidatePlaces))
                    .append("\n");
            prompt.append("  description: ").append(place.description()).append("\n");
        }
        prompt.append("\n");
        prompt.append("Response JSON format:\n");
        prompt.append("[\n");
        prompt.append("  {\n");
        prompt.append("    \"placeId\": 1,\n");
        prompt.append("    \"dayNo\": 1,\n");
        prompt.append("    \"orderNo\": 1,\n");
        prompt.append("    \"startTime\": \"09:00:00\",\n");
        prompt.append("    \"endTime\": \"10:30:00\",\n");
        prompt.append("    \"travelMinutesFromPrevious\": 0,\n");
        prompt.append("    \"reason\": \"이 후보 장소를 선택한 한국어 이유입니다.\"\n");
        prompt.append("  }\n");
        prompt.append("]\n");

        return prompt.toString();
    }

    private List<Long> recommendedNextPlaceIds(
            PlaceResponse currentPlace,
            List<PlaceResponse> candidatePlaces
    ) {
        return candidatePlaces.stream()
                .filter(place -> !place.placeId().equals(currentPlace.placeId()))
                .map(place -> new ReachablePlace(
                        place.placeId(),
                        routeCalculationAdapter.calculateTravelMinutes(currentPlace, place)
                ))
                .filter(place -> place.travelMinutes() <= MAX_TRAVEL_MINUTES_BETWEEN_PLACES)
                .sorted(Comparator.comparingInt(ReachablePlace::travelMinutes)
                        .thenComparing(ReachablePlace::placeId))
                .limit(MAX_RECOMMENDED_NEXT_PLACES)
                .map(ReachablePlace::placeId)
                .toList();
    }

    private long calculateTripDays(Trip trip) {
        return ChronoUnit.DAYS.between(trip.getStartDate(), trip.getEndDate()) + 1;
    }

    private List<DayTimeWindow> dayTimeWindows(
            Trip trip,
            ItineraryGenerateRequest request,
            Integer targetDayNo
    ) {
        long tripDays = calculateTripDays(trip);
        Map<Integer, ItineraryDayTimeWindowRequest> overrideByDayNo = new HashMap<>();
        if (request != null) {
            for (ItineraryDayTimeWindowRequest dayTimeWindow : request.normalizedDayTimeWindows()) {
                if (dayTimeWindow != null && dayTimeWindow.dayNo() != null) {
                    overrideByDayNo.put(dayTimeWindow.dayNo(), dayTimeWindow);
                }
            }
        }

        List<DayTimeWindow> dayTimeWindows = new java.util.ArrayList<>();
        for (int dayNo = 1; dayNo <= tripDays; dayNo++) {
            if (targetDayNo != null && dayNo != targetDayNo) {
                continue;
            }
            ItineraryDayTimeWindowRequest override = overrideByDayNo.get(dayNo);
            LocalTime startTime = override == null ? trip.getDailyStartTime() : override.startTime();
            LocalTime endTime = override == null ? trip.getDailyEndTime() : override.endTime();
            dayTimeWindows.add(new DayTimeWindow(dayNo, startTime, endTime));
        }
        return dayTimeWindows;
    }

    private List<Long> mustVisitPlaceIds(ItineraryGenerateRequest request) {
        if (request == null) {
            return List.of();
        }
        return request.normalizedMustVisitPlaceIds();
    }

    private List<Long> excludedPlaceIds(ItineraryGenerateRequest request) {
        if (request == null) {
            return List.of();
        }
        return request.normalizedExcludedPlaceIds();
    }

    private List<PlaceCategory> preferredCategories(ItineraryGenerateRequest request) {
        if (request == null) {
            return List.of();
        }
        return request.normalizedPreferredCategories();
    }

    private ItineraryPace pace(ItineraryGenerateRequest request) {
        if (request == null) {
            return ItineraryPace.NORMAL;
        }
        return request.normalizedPace();
    }

    private boolean rainyDayMode(ItineraryGenerateRequest request) {
        return request != null && request.normalizedRainyDayMode();
    }

    private List<Integer> rainyDayNos(ItineraryGenerateRequest request) {
        if (request == null) {
            return List.of();
        }
        return request.normalizedRainyDayNos();
    }

    private void appendExplicitPacePolicy(StringBuilder prompt, ItineraryGenerateRequest request) {
        if (request == null || request.pace() == null) {
            return;
        }

        PaceItineraryPolicy pacePolicy = PaceItineraryPolicy.findByPace(request.normalizedPace())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Pace itinerary policy is not defined. pace=" + request.normalizedPace()
                ));
        prompt.append("- ").append(pacePolicy.explicitPromptPolicyLine()).append("\n");
        prompt.append("- This item count policy must be satisfied for each dayNo, including multi-day trips.\n");
        prompt.append("- If dayTimeWindows are provided, still satisfy this item count policy inside each dayNo's available time window.\n");
    }

    private record DayTimeWindow(
            Integer dayNo,
            LocalTime startTime,
            LocalTime endTime
    ) {
    }

    private record ReachablePlace(Long placeId, int travelMinutes) {
    }

}
