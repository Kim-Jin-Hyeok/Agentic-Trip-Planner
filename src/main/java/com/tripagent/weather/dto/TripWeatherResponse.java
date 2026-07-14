package com.tripagent.weather.dto;

import java.util.List;

public record TripWeatherResponse(
        boolean available,
        boolean rainyDaySuggested,
        String message,
        List<DailyWeatherResponse> days
) {

    public static TripWeatherResponse available(List<DailyWeatherResponse> days) {
        boolean rainyDaySuggested = days.stream().anyMatch(DailyWeatherResponse::rainy);
        return new TripWeatherResponse(true, rainyDaySuggested, "", List.copyOf(days));
    }

    public static TripWeatherResponse unavailable(String message) {
        return new TripWeatherResponse(false, false, message, List.of());
    }
}
