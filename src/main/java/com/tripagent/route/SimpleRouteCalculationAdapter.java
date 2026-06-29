package com.tripagent.route;

import com.tripagent.place.dto.PlaceResponse;
import org.springframework.stereotype.Component;

@Component
public class SimpleRouteCalculationAdapter implements RouteCalculationAdapter {

    private static final int DEFAULT_TRAVEL_MINUTES = 30;

    @Override
    public int calculateTravelMinutes(PlaceResponse previousPlace, PlaceResponse currentPlace) {
        if (previousPlace == null || currentPlace == null) {
            return 0;
        }
        if (previousPlace.placeId().equals(currentPlace.placeId())) {
            return 0;
        }

        return DEFAULT_TRAVEL_MINUTES;
    }
}
