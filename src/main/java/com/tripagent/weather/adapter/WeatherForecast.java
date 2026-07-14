package com.tripagent.weather.adapter;

import java.time.LocalDate;

public record WeatherForecast(
        LocalDate date,
        int weatherCode,
        double maximumTemperature,
        double minimumTemperature,
        int precipitationProbability
) {
}
