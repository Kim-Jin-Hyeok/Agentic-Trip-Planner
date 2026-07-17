package com.tripagent.place.domain;

import java.util.Arrays;
import java.util.List;

public enum TripEndpointPlace {

    JEJU_INTERNATIONAL_AIRPORT("제주국제공항"),
    JEJU_INTERNATIONAL_FERRY_TERMINAL("제주항국제여객터미널");

    private final String placeName;

    TripEndpointPlace(String placeName) {
        this.placeName = placeName;
    }

    public static List<String> orderedNames() {
        return Arrays.stream(values())
                .map(TripEndpointPlace::placeName)
                .toList();
    }

    public static boolean supports(String placeName) {
        return Arrays.stream(values())
                .anyMatch(endpointPlace -> endpointPlace.placeName.equals(placeName));
    }

    public String placeName() {
        return placeName;
    }
}
