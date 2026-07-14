package com.tripagent.weather.adapter;

import java.time.LocalDate;
import java.util.List;

public interface WeatherAdapter {

    List<WeatherForecast> getForecast(LocalDate startDate, LocalDate endDate);
}
