package com.tripagent.weather.adapter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tripagent.weather.config.OpenMeteoProperties;
import java.net.http.HttpClient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class OpenMeteoWeatherAdapter implements WeatherAdapter {

    private static final double JEJU_LATITUDE = 33.4996;
    private static final double JEJU_LONGITUDE = 126.5312;
    private static final String DAILY_VARIABLES = String.join(",",
            "weather_code",
            "temperature_2m_max",
            "temperature_2m_min",
            "precipitation_probability_max"
    );

    private final RestClient restClient;

    @Autowired
    public OpenMeteoWeatherAdapter(OpenMeteoProperties properties, RestClient.Builder restClientBuilder) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(properties.getConnectTimeout())
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(properties.getReadTimeout());
        this.restClient = restClientBuilder
                .baseUrl(properties.getBaseUrl())
                .requestFactory(requestFactory)
                .build();
    }

    OpenMeteoWeatherAdapter(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<WeatherForecast> getForecast(LocalDate startDate, LocalDate endDate) {
        try {
            OpenMeteoResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/forecast")
                            .queryParam("latitude", JEJU_LATITUDE)
                            .queryParam("longitude", JEJU_LONGITUDE)
                            .queryParam("daily", DAILY_VARIABLES)
                            .queryParam("timezone", "Asia/Seoul")
                            .queryParam("start_date", startDate)
                            .queryParam("end_date", endDate)
                            .build())
                    .retrieve()
                    .body(OpenMeteoResponse.class);
            return toForecasts(response);
        } catch (RestClientException exception) {
            throw new WeatherAdapterException("Open-Meteo forecast request failed.", exception);
        }
    }

    private List<WeatherForecast> toForecasts(OpenMeteoResponse response) {
        if (response == null || response.daily() == null) {
            throw new WeatherAdapterException("Open-Meteo forecast response is empty.");
        }

        OpenMeteoDaily daily = response.daily();
        int size = daily.time() == null ? 0 : daily.time().size();
        if (size == 0
                || sizeOf(daily.weatherCode()) != size
                || sizeOf(daily.maximumTemperature()) != size
                || sizeOf(daily.minimumTemperature()) != size
                || sizeOf(daily.precipitationProbability()) != size) {
            throw new WeatherAdapterException("Open-Meteo daily forecast response is invalid.");
        }

        List<WeatherForecast> forecasts = new ArrayList<>(size);
        for (int index = 0; index < size; index++) {
            forecasts.add(new WeatherForecast(
                    daily.time().get(index),
                    daily.weatherCode().get(index),
                    daily.maximumTemperature().get(index),
                    daily.minimumTemperature().get(index),
                    daily.precipitationProbability().get(index)
            ));
        }
        return List.copyOf(forecasts);
    }

    private int sizeOf(List<?> values) {
        return values == null ? 0 : values.size();
    }

    record OpenMeteoResponse(OpenMeteoDaily daily) {
    }

    record OpenMeteoDaily(
            List<LocalDate> time,
            @JsonProperty("weather_code") List<Integer> weatherCode,
            @JsonProperty("temperature_2m_max") List<Double> maximumTemperature,
            @JsonProperty("temperature_2m_min") List<Double> minimumTemperature,
            @JsonProperty("precipitation_probability_max") List<Integer> precipitationProbability
    ) {
    }
}
