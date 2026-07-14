package com.tripagent.weather.controller;

import com.tripagent.auth.support.LoginMemberId;
import com.tripagent.common.response.ApiResponse;
import com.tripagent.weather.dto.TripWeatherResponse;
import com.tripagent.weather.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trips/{tripId}/weather")
public class TripWeatherController {

    private final WeatherService weatherService;

    public TripWeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public ApiResponse<TripWeatherResponse> getTripWeather(
            @PathVariable Long tripId,
            @LoginMemberId Long memberId
    ) {
        return ApiResponse.success(weatherService.getTripWeather(tripId, memberId));
    }
}
