package com.tripagent.ai.llm;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"local", "test"})
public class MockLlmClient implements LlmClient {

    private static final Pattern PLACE_ID_PATTERN = Pattern.compile("placeId:\\s*(\\d+)");
    private static final int MOCK_PLACE_LIMIT = 3;
    private static final int DEFAULT_STAY_MINUTES = 60;
    private static final int DEFAULT_TRAVEL_MINUTES = 30;

    @Override
    public String generate(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            throw new IllegalArgumentException("LLM prompt is required.");
        }

        List<Long> placeIds = extractPlaceIds(prompt);
        if (placeIds.isEmpty()) {
            throw new IllegalArgumentException("Mock LLM prompt must include candidate placeIds.");
        }

        return createJsonResponse(placeIds);
    }

    private List<Long> extractPlaceIds(String prompt) {
        Matcher matcher = PLACE_ID_PATTERN.matcher(prompt);
        List<Long> placeIds = new ArrayList<>();

        while (matcher.find() && placeIds.size() < MOCK_PLACE_LIMIT) {
            placeIds.add(Long.parseLong(matcher.group(1)));
        }

        return placeIds;
    }

    private String createJsonResponse(List<Long> placeIds) {
        StringBuilder json = new StringBuilder();
        LocalTime nextStartTime = LocalTime.of(9, 0);

        json.append("[");
        for (int index = 0; index < placeIds.size(); index++) {
            if (index > 0) {
                json.append(",");
            }

            int travelMinutesFromPrevious = index == 0 ? 0 : DEFAULT_TRAVEL_MINUTES;
            LocalTime startTime = index == 0
                    ? nextStartTime
                    : nextStartTime.plusMinutes(DEFAULT_TRAVEL_MINUTES);
            LocalTime endTime = startTime.plusMinutes(DEFAULT_STAY_MINUTES);

            json.append("{")
                    .append("\"placeId\":").append(placeIds.get(index)).append(",")
                    .append("\"dayNo\":1,")
                    .append("\"orderNo\":").append(index + 1).append(",")
                    .append("\"startTime\":\"").append(startTime).append(":00\",")
                    .append("\"endTime\":\"").append(endTime).append(":00\",")
                    .append("\"travelMinutesFromPrevious\":").append(travelMinutesFromPrevious).append(",")
                    .append("\"reason\":\"Mock LLM itinerary item selected from candidate places.\"")
                    .append("}");

            nextStartTime = endTime;
        }
        json.append("]");

        return json.toString();
    }
}
