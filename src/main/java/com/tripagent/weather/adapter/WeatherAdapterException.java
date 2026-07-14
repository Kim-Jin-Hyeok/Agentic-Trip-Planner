package com.tripagent.weather.adapter;

public class WeatherAdapterException extends RuntimeException {

    public WeatherAdapterException(String message) {
        super(message);
    }

    public WeatherAdapterException(String message, Throwable cause) {
        super(message, cause);
    }
}
