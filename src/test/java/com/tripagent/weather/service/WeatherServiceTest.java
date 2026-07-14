package com.tripagent.weather.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.domain.TripVisibility;
import com.tripagent.trip.dto.TripDetailResponse;
import com.tripagent.trip.service.TripService;
import com.tripagent.weather.adapter.WeatherAdapter;
import com.tripagent.weather.adapter.WeatherAdapterException;
import com.tripagent.weather.adapter.WeatherForecast;
import com.tripagent.weather.dto.TripWeatherResponse;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WeatherServiceTest {

    private static final Long TRIP_ID = 1L;
    private static final Long MEMBER_ID = 10L;
    private static final LocalDate TODAY = LocalDate.of(2026, 7, 14);

    private TripService tripService;
    private WeatherAdapter weatherAdapter;
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        tripService = mock(TripService.class);
        weatherAdapter = mock(WeatherAdapter.class);
        Clock clock = Clock.fixed(
                Instant.parse("2026-07-14T00:00:00Z"),
                ZoneId.of("Asia/Seoul")
        );
        weatherService = new WeatherService(tripService, weatherAdapter, clock);
    }

    @Test
    void suggestsRainyDayModeWhenRainProbabilityIsHigh() {
        LocalDate startDate = TODAY.plusDays(1);
        LocalDate endDate = TODAY.plusDays(2);
        when(tripService.getTrip(TRIP_ID, MEMBER_ID)).thenReturn(trip(startDate, endDate));
        when(weatherAdapter.getForecast(startDate, endDate)).thenReturn(List.of(
                new WeatherForecast(startDate, 2, 28.4, 23.2, 20),
                new WeatherForecast(endDate, 3, 26.1, 22.8, 60)
        ));

        TripWeatherResponse response = weatherService.getTripWeather(TRIP_ID, MEMBER_ID);

        assertThat(response.available()).isTrue();
        assertThat(response.rainyDaySuggested()).isTrue();
        assertThat(response.days()).hasSize(2);
        assertThat(response.days().get(1).rainy()).isTrue();
    }

    @Test
    void doesNotCallWeatherProviderWhenTripIsOutsideForecastRange() {
        LocalDate startDate = TODAY.plusDays(16);
        LocalDate endDate = TODAY.plusDays(17);
        when(tripService.getTrip(TRIP_ID, MEMBER_ID)).thenReturn(trip(startDate, endDate));

        TripWeatherResponse response = weatherService.getTripWeather(TRIP_ID, MEMBER_ID);

        assertThat(response.available()).isFalse();
        assertThat(response.days()).isEmpty();
        verifyNoInteractions(weatherAdapter);
    }

    @Test
    void returnsUnavailableResponseWhenWeatherProviderFails() {
        LocalDate startDate = TODAY.plusDays(1);
        LocalDate endDate = TODAY.plusDays(2);
        when(tripService.getTrip(TRIP_ID, MEMBER_ID)).thenReturn(trip(startDate, endDate));
        when(weatherAdapter.getForecast(startDate, endDate))
                .thenThrow(new WeatherAdapterException("provider unavailable"));

        TripWeatherResponse response = weatherService.getTripWeather(TRIP_ID, MEMBER_ID);

        assertThat(response.available()).isFalse();
        assertThat(response.message()).contains("일정 생성은 계속 이용");
    }

    private TripDetailResponse trip(LocalDate startDate, LocalDate endDate) {
        return new TripDetailResponse(
                TRIP_ID,
                "제주",
                startDate,
                endDate,
                Math.toIntExact(startDate.until(endDate).getDays()),
                LocalTime.of(9, 0),
                LocalTime.of(20, 0),
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "제주시",
                0L,
                0L,
                TripVisibility.PRIVATE,
                List.of(),
                "제주 여행"
        );
    }
}
