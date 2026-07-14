package com.tripagent.weather.config;

import java.time.Clock;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeatherConfig {

    @Bean
    public Clock weatherClock() {
        return Clock.system(ZoneId.of("Asia/Seoul"));
    }
}
