package com.tripagent.ai.llm;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local | dev | test")
public class MockLlmClient implements LlmClient {

    private static final Pattern PLACE_ID_PATTERN = Pattern.compile("placeId:\\s*(\\d+)");
    private static final Pattern DAYS_PATTERN = Pattern.compile("days:\\s*(\\d+)");
    private static final Pattern DAILY_START_TIME_PATTERN = Pattern.compile("dailyStartTime:\\s*(\\d{2}:\\d{2}(?::\\d{2})?)");
    private static final Pattern DAILY_END_TIME_PATTERN = Pattern.compile("dailyEndTime:\\s*(\\d{2}:\\d{2}(?::\\d{2})?)");
    private static final int MOCK_PLACE_LIMIT = 3;
    private static final int DEFAULT_STAY_MINUTES = 60;
    private static final int DEFAULT_TRAVEL_MINUTES = 30;
    private static final int DEFAULT_DAYS = 1;
    private static final LocalTime DEFAULT_DAILY_START_TIME = LocalTime.of(9, 0);
    private static final LocalTime DEFAULT_DAILY_END_TIME = LocalTime.of(18, 0);

    @Override
    public String generate(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            throw new IllegalArgumentException("LLM prompt is required.");
        }

        int days = extractDays(prompt);
        List<Long> placeIds = extractPlaceIds(prompt, days);
        if (placeIds.isEmpty()) {
            throw new IllegalArgumentException("Mock LLM prompt must include candidate placeIds.");
        }
        if (placeIds.size() < days) {
            throw new IllegalArgumentException("Mock LLM prompt must include at least one candidate placeId for every trip day.");
        }

        LocalTime dailyStartTime = extractTime(prompt, DAILY_START_TIME_PATTERN, DEFAULT_DAILY_START_TIME);
        LocalTime dailyEndTime = extractTime(prompt, DAILY_END_TIME_PATTERN, DEFAULT_DAILY_END_TIME);

        return createJsonResponse(placeIds, days, dailyStartTime, dailyEndTime);
    }

    private List<Long> extractPlaceIds(String prompt, int days) {
        Matcher matcher = PLACE_ID_PATTERN.matcher(prompt);
        List<Long> placeIds = new ArrayList<>();
        int placeLimit = Math.max(MOCK_PLACE_LIMIT, days);

        while (matcher.find() && placeIds.size() < placeLimit) {
            placeIds.add(Long.parseLong(matcher.group(1)));
        }

        return placeIds;
    }

    private int extractDays(String prompt) {
        Matcher matcher = DAYS_PATTERN.matcher(prompt);
        if (!matcher.find()) {
            return DEFAULT_DAYS;
        }

        return Math.max(DEFAULT_DAYS, Integer.parseInt(matcher.group(1)));
    }

    private LocalTime extractTime(String prompt, Pattern pattern, LocalTime defaultTime) {
        Matcher matcher = pattern.matcher(prompt);
        if (!matcher.find()) {
            return defaultTime;
        }

        return LocalTime.parse(matcher.group(1));
    }

    private String createJsonResponse(
            List<Long> placeIds,
            int days,
            LocalTime dailyStartTime,
            LocalTime dailyEndTime
    ) {
        StringBuilder json = new StringBuilder();
        int[] orderNosByDay = new int[days + 1];
        LocalTime[] nextStartTimesByDay = new LocalTime[days + 1];
        for (int dayNo = 1; dayNo <= days; dayNo++) {
            nextStartTimesByDay[dayNo] = dailyStartTime;
        }

        json.append("[");
        for (int index = 0; index < placeIds.size(); index++) {
            if (index > 0) {
                json.append(",");
            }

            int dayNo = (index % days) + 1;
            int orderNo = ++orderNosByDay[dayNo];
            int travelMinutesFromPrevious = orderNo == 1 ? 0 : DEFAULT_TRAVEL_MINUTES;
            LocalTime startTime = orderNo == 1
                    ? nextStartTimesByDay[dayNo]
                    : nextStartTimesByDay[dayNo].plusMinutes(DEFAULT_TRAVEL_MINUTES);
            LocalTime endTime = calculateEndTime(startTime, dailyEndTime);

            json.append("{")
                    .append("\"placeId\":").append(placeIds.get(index)).append(",")
                    .append("\"dayNo\":").append(dayNo).append(",")
                    .append("\"orderNo\":").append(orderNo).append(",")
                    .append("\"startTime\":\"").append(startTime).append(":00\",")
                    .append("\"endTime\":\"").append(endTime).append(":00\",")
                    .append("\"travelMinutesFromPrevious\":").append(travelMinutesFromPrevious).append(",")
                    .append("\"reason\":\"Mock LLM itinerary item selected from candidate places.\"")
                    .append("}");

            nextStartTimesByDay[dayNo] = endTime;
        }
        json.append("]");

        return json.toString();
    }

    private LocalTime calculateEndTime(LocalTime startTime, LocalTime dailyEndTime) {
        LocalTime defaultEndTime = startTime.plusMinutes(DEFAULT_STAY_MINUTES);
        if (!defaultEndTime.isAfter(dailyEndTime)) {
            return defaultEndTime;
        }

        return dailyEndTime;
    }
}
