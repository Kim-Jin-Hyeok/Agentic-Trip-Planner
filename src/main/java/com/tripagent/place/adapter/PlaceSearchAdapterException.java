package com.tripagent.place.adapter;

public class PlaceSearchAdapterException extends RuntimeException {

    private final PlaceSearchFailureType failureType;

    public PlaceSearchAdapterException(String message) {
        this(PlaceSearchFailureType.UNAVAILABLE, message, null);
    }

    public PlaceSearchAdapterException(String message, Throwable cause) {
        this(PlaceSearchFailureType.UNAVAILABLE, message, cause);
    }

    public PlaceSearchAdapterException(
            PlaceSearchFailureType failureType,
            String message
    ) {
        this(failureType, message, null);
    }

    public PlaceSearchAdapterException(
            PlaceSearchFailureType failureType,
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.failureType = failureType;
    }

    public PlaceSearchFailureType getFailureType() {
        return failureType;
    }
}
