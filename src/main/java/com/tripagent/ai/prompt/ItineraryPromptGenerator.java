package com.tripagent.ai.prompt;

import com.tripagent.itinerary.dto.ItineraryGenerateRequest;
import com.tripagent.itinerary.dto.ItineraryPace;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.trip.domain.Trip;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ItineraryPromptGenerator {

    public String generate(Trip trip, List<PlaceResponse> candidatePlaces) {
        return generate(trip, candidatePlaces, null);
    }

    public String generate(Trip trip, List<PlaceResponse> candidatePlaces, ItineraryGenerateRequest request) {
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
        prompt.append("- Follow the selected pace when choosing how many places to schedule each day.\n");
        prompt.append("- For each dayNo, the first itinerary item must have orderNo 1 and travelMinutesFromPrevious 0.\n");
        prompt.append("- For each dayNo, the first itinerary item's startTime must be at or after Trip.dailyStartTime.\n");
        prompt.append("- For each dayNo, the last itinerary item's endTime must be at or before Trip.dailyEndTime.\n");
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
        prompt.append("Place controls:\n");
        prompt.append("- mustVisitPlaceIds: ").append(mustVisitPlaceIds(request)).append("\n");
        prompt.append("- excludedPlaceIds: ").append(excludedPlaceIds(request)).append("\n\n");
        prompt.append("Pace:\n");
        prompt.append("- selectedPace: ").append(pace(request)).append("\n");
        prompt.append("- RELAXED: Plan about 2 itinerary items per day with generous travel and rest time.\n");
        prompt.append("- NORMAL: Plan about 2 to 3 itinerary items per day with moderate travel time.\n");
        prompt.append("- BUSY: Plan about 3 to 4 itinerary items per day to visit more places while staying realistic.\n\n");
        prompt.append("candidatePlaces:\n");
        for (PlaceResponse place : candidatePlaces) {
            prompt.append("- placeId: ").append(place.placeId()).append("\n");
            prompt.append("  name: ").append(place.name()).append("\n");
            prompt.append("  category: ").append(place.category()).append("\n");
            prompt.append("  region: ").append(place.region()).append("\n");
            prompt.append("  avgStayMinutes: ").append(place.avgStayMinutes()).append("\n");
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

    private long calculateTripDays(Trip trip) {
        return ChronoUnit.DAYS.between(trip.getStartDate(), trip.getEndDate()) + 1;
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

    private ItineraryPace pace(ItineraryGenerateRequest request) {
        if (request == null) {
            return ItineraryPace.NORMAL;
        }
        return request.normalizedPace();
    }
}
