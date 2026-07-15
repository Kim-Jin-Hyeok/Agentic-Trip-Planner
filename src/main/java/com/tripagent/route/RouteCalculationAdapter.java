package com.tripagent.route;

import com.tripagent.place.dto.PlaceResponse;

public interface RouteCalculationAdapter {

    int calculateTravelMinutes(PlaceResponse previousPlace, PlaceResponse currentPlace);

    int calculateTravelMinutes(
            Double previousLatitude,
            Double previousLongitude,
            Double currentLatitude,
            Double currentLongitude
    );
}
