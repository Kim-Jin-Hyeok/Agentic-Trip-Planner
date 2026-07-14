package com.tripagent.weather.dto;

import java.time.LocalDate;

public record DailyWeatherResponse(
        LocalDate date,
        int weatherCode,
        String summary,
        double maximumTemperature,
        double minimumTemperature,
        int precipitationProbability,
        boolean rainy
) {
}
