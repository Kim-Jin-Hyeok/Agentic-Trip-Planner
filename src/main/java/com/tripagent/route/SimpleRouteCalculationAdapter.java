package com.tripagent.route;

import com.tripagent.place.dto.PlaceResponse;
import org.springframework.stereotype.Component;

@Component
public class SimpleRouteCalculationAdapter implements RouteCalculationAdapter {

    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final double ROAD_DISTANCE_FACTOR = 1.35;
    private static final double AVERAGE_CAR_SPEED_KM_PER_HOUR = 35.0;
    private static final int DEFAULT_TRAVEL_MINUTES = 30;
    private static final int MIN_TRAVEL_MINUTES = 5;

    @Override
    public int calculateTravelMinutes(PlaceResponse previousPlace, PlaceResponse currentPlace) {
        if (previousPlace == null || currentPlace == null) {
            return 0;
        }
        if (previousPlace.placeId().equals(currentPlace.placeId())) {
            return 0;
        }
        if (!hasCoordinates(previousPlace) || !hasCoordinates(currentPlace)) {
            return DEFAULT_TRAVEL_MINUTES;
        }

        double straightDistanceKm = calculateStraightDistanceKm(
                previousPlace.latitude(),
                previousPlace.longitude(),
                currentPlace.latitude(),
                currentPlace.longitude()
        );
        double estimatedRoadDistanceKm = straightDistanceKm * ROAD_DISTANCE_FACTOR;
        double travelHours = estimatedRoadDistanceKm / AVERAGE_CAR_SPEED_KM_PER_HOUR;
        int travelMinutes = (int) Math.ceil(travelHours * 60);

        return Math.max(MIN_TRAVEL_MINUTES, travelMinutes);
    }

    private boolean hasCoordinates(PlaceResponse place) {
        return place.latitude() != null && place.longitude() != null;
    }

    private double calculateStraightDistanceKm(
            double previousLatitude,
            double previousLongitude,
            double currentLatitude,
            double currentLongitude
    ) {
        double previousLatitudeRadians = Math.toRadians(previousLatitude);
        double currentLatitudeRadians = Math.toRadians(currentLatitude);
        double latitudeDifferenceRadians = Math.toRadians(currentLatitude - previousLatitude);
        double longitudeDifferenceRadians = Math.toRadians(currentLongitude - previousLongitude);

        double haversine = Math.sin(latitudeDifferenceRadians / 2) * Math.sin(latitudeDifferenceRadians / 2)
                + Math.cos(previousLatitudeRadians)
                * Math.cos(currentLatitudeRadians)
                * Math.sin(longitudeDifferenceRadians / 2)
                * Math.sin(longitudeDifferenceRadians / 2);
        double centralAngle = 2 * Math.atan2(Math.sqrt(haversine), Math.sqrt(1 - haversine));

        return EARTH_RADIUS_KM * centralAngle;
    }
}
