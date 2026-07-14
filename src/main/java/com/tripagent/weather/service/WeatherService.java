package com.tripagent.weather.service;

import com.tripagent.trip.dto.TripDetailResponse;
import com.tripagent.trip.service.TripService;
import com.tripagent.weather.adapter.WeatherAdapter;
import com.tripagent.weather.adapter.WeatherAdapterException;
import com.tripagent.weather.adapter.WeatherForecast;
import com.tripagent.weather.dto.DailyWeatherResponse;
import com.tripagent.weather.dto.TripWeatherResponse;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private static final int FORECAST_DAYS = 16;
    private static final int RAIN_PROBABILITY_THRESHOLD = 50;

    private final TripService tripService;
    private final WeatherAdapter weatherAdapter;
    private final Clock clock;

    public WeatherService(TripService tripService, WeatherAdapter weatherAdapter, Clock clock) {
        this.tripService = tripService;
        this.weatherAdapter = weatherAdapter;
        this.clock = clock;
    }

    public TripWeatherResponse getTripWeather(Long tripId, Long memberId) {
        TripDetailResponse trip = tripService.getTrip(tripId, memberId);
        LocalDate today = LocalDate.now(clock);
        LocalDate lastForecastDate = today.plusDays(FORECAST_DAYS - 1L);
        if (trip.startDate().isBefore(today) || trip.endDate().isAfter(lastForecastDate)) {
            return TripWeatherResponse.unavailable("오늘부터 16일 이내의 여행만 날씨 예보를 확인할 수 있습니다.");
        }

        try {
            List<DailyWeatherResponse> days = weatherAdapter
                    .getForecast(trip.startDate(), trip.endDate())
                    .stream()
                    .map(this::toResponse)
                    .toList();
            return TripWeatherResponse.available(days);
        } catch (WeatherAdapterException exception) {
            return TripWeatherResponse.unavailable("날씨 정보를 불러오지 못했습니다. 일정 생성은 계속 이용할 수 있습니다.");
        }
    }

    private DailyWeatherResponse toResponse(WeatherForecast forecast) {
        boolean rainy = forecast.precipitationProbability() >= RAIN_PROBABILITY_THRESHOLD
                || isRainWeatherCode(forecast.weatherCode());
        return new DailyWeatherResponse(
                forecast.date(),
                forecast.weatherCode(),
                weatherSummary(forecast.weatherCode()),
                forecast.maximumTemperature(),
                forecast.minimumTemperature(),
                forecast.precipitationProbability(),
                rainy
        );
    }

    private boolean isRainWeatherCode(int weatherCode) {
        return (weatherCode >= 51 && weatherCode <= 67)
                || (weatherCode >= 80 && weatherCode <= 82)
                || (weatherCode >= 95 && weatherCode <= 99);
    }

    private String weatherSummary(int weatherCode) {
        if (weatherCode == 0) {
            return "맑음";
        }
        if (weatherCode <= 3) {
            return "구름";
        }
        if (weatherCode == 45 || weatherCode == 48) {
            return "안개";
        }
        if (weatherCode >= 51 && weatherCode <= 57) {
            return "이슬비";
        }
        if (weatherCode >= 61 && weatherCode <= 67) {
            return "비";
        }
        if (weatherCode >= 71 && weatherCode <= 77) {
            return "눈";
        }
        if (weatherCode >= 80 && weatherCode <= 82) {
            return "소나기";
        }
        if (weatherCode >= 85 && weatherCode <= 86) {
            return "눈 소나기";
        }
        if (weatherCode >= 95) {
            return "뇌우";
        }
        return "변화 가능";
    }
}
