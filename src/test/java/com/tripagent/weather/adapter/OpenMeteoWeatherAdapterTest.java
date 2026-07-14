package com.tripagent.weather.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.tripagent.weather.config.OpenMeteoProperties;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class OpenMeteoWeatherAdapterTest {

    @Test
    void springCreatesAdapterWithConfiguredConstructor() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.getBeanFactory().registerSingleton("openMeteoProperties", new OpenMeteoProperties());
            context.getBeanFactory().registerSingleton("restClientBuilder", RestClient.builder());
            context.register(OpenMeteoWeatherAdapter.class);

            context.refresh();

            assertThat(context.getBean(OpenMeteoWeatherAdapter.class)).isNotNull();
        }
    }

    @Test
    void returnsDailyForecastsFromOpenMeteoResponse() {
        RestClient.Builder restClientBuilder = RestClient.builder().baseUrl("https://api.open-meteo.com");
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        OpenMeteoWeatherAdapter adapter = new OpenMeteoWeatherAdapter(restClientBuilder.build());
        server.expect(requestTo(containsString("/v1/forecast")))
                .andExpect(requestTo(containsString("timezone=Asia/Seoul")))
                .andExpect(requestTo(containsString("start_date=2026-07-15")))
                .andRespond(withSuccess("""
                        {
                          "daily": {
                            "time": ["2026-07-15", "2026-07-16"],
                            "weather_code": [2, 61],
                            "temperature_2m_max": [28.4, 26.1],
                            "temperature_2m_min": [23.2, 22.8],
                            "precipitation_probability_max": [20, 70]
                          }
                        }
                        """, MediaType.APPLICATION_JSON));

        List<WeatherForecast> forecasts = adapter.getForecast(
                LocalDate.of(2026, 7, 15),
                LocalDate.of(2026, 7, 16)
        );

        assertThat(forecasts).containsExactly(
                new WeatherForecast(LocalDate.of(2026, 7, 15), 2, 28.4, 23.2, 20),
                new WeatherForecast(LocalDate.of(2026, 7, 16), 61, 26.1, 22.8, 70)
        );
        server.verify();
    }
}
