package com.tripagent.place.dto;

import com.tripagent.place.domain.Place;

public record PlaceResponse(
        Long placeId,
        String name,
        String category,
        String region,
        String address,
        Double latitude,
        Double longitude,
        Integer avgStayMinutes,
        Boolean indoorYn,
        Boolean parkingYn,
        Integer rainyDayScore,
        Integer healingScore,
        Integer foodScore,
        Integer cafeScore,
        Integer photoScore,
        Integer coupleScore,
        Integer familyScore,
        String description,
        Boolean useYn
) {

    public PlaceResponse(
            Long placeId,
            String name,
            String category,
            String region,
            String address,
            Double latitude,
            Double longitude,
            Integer avgStayMinutes,
            Boolean indoorYn,
            Boolean parkingYn,
            Integer rainyDayScore,
            Integer healingScore,
            Integer foodScore,
            Integer cafeScore,
            Integer photoScore,
            Integer coupleScore,
            Integer familyScore,
            String description
    ) {
        this(
                placeId, name, category, region, address, latitude, longitude, avgStayMinutes,
                indoorYn, parkingYn, rainyDayScore, healingScore, foodScore, cafeScore, photoScore,
                coupleScore, familyScore, description, true
        );
    }

    public static PlaceResponse from(Place place) {
        return new PlaceResponse(
                place.getPlaceId(),
                place.getName(),
                place.getCategory(),
                place.getRegion(),
                place.getAddress(),
                place.getLatitude(),
                place.getLongitude(),
                place.getAvgStayMinutes(),
                place.getIndoorYn(),
                place.getParkingYn(),
                place.getRainyDayScore(),
                place.getHealingScore(),
                place.getFoodScore(),
                place.getCafeScore(),
                place.getPhotoScore(),
                place.getCoupleScore(),
                place.getFamilyScore(),
                place.getDescription(),
                place.getUseYn()
        );
    }
}
