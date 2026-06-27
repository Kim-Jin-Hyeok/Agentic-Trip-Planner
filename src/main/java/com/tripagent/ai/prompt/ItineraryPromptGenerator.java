package com.tripagent.ai.prompt;

import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.trip.domain.Trip;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ItineraryPromptGenerator {

    public String generate(Trip trip, List<PlaceResponse> candidatePlaces) {
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
        prompt.append("- Return JSON only. Do not include markdown or explanation outside JSON.\n\n");
        prompt.append("Trip:\n");
        prompt.append("- destination: ").append(trip.getDestination()).append("\n");
        prompt.append("- startDate: ").append(trip.getStartDate()).append("\n");
        prompt.append("- endDate: ").append(trip.getEndDate()).append("\n");
        prompt.append("- days: ").append(calculateTripDays(trip)).append("\n");
        prompt.append("- dailyStartTime: ").append(trip.getDailyStartTime()).append("\n");
        prompt.append("- concept: ").append(trip.getConcept()).append("\n");
        prompt.append("- transportation: ").append(trip.getTransportation()).append("\n");
        prompt.append("- lastAccommodationArea: ").append(trip.getLastAccommodationArea()).append("\n\n");
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
        prompt.append("    \"reason\": \"Reason for selecting this candidate place.\"\n");
        prompt.append("  }\n");
        prompt.append("]\n");

        return prompt.toString();
    }

    private long calculateTripDays(Trip trip) {
        return ChronoUnit.DAYS.between(trip.getStartDate(), trip.getEndDate()) + 1;
    }
}
