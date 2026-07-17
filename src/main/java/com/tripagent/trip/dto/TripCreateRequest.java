package com.tripagent.trip.dto;

import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.TripConcept;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record TripCreateRequest(
        @NotBlank
        String destination,
        @NotNull
        LocalDate startDate,
        @NotNull
        LocalDate endDate,
        @NotNull
        LocalTime dailyStartTime,
        @NotNull
        LocalTime dailyEndTime,
        @NotNull
        TripConcept concept,
        @NotNull
        Transportation transportation,
        String lastAccommodationArea,
        Long ownerId,
        String title,
        Long startPlaceId,
        Long endPlaceId
) {

    public TripCreateRequest(
            String destination,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime dailyStartTime,
            LocalTime dailyEndTime,
            TripConcept concept,
            Transportation transportation,
            String lastAccommodationArea,
            Long ownerId,
            String title
    ) {
        this(destination, startDate, endDate, dailyStartTime, dailyEndTime, concept, transportation,
                lastAccommodationArea, ownerId, title, null, null);
    }

    public TripCreateRequest(
            String destination,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime dailyStartTime,
            LocalTime dailyEndTime,
            TripConcept concept,
            Transportation transportation,
            String lastAccommodationArea,
            Long ownerId
    ) {
        this(destination, startDate, endDate, dailyStartTime, dailyEndTime, concept, transportation,
                lastAccommodationArea, ownerId, null, null, null);
    }

    public TripCreateRequest(
            String destination,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime dailyStartTime,
            LocalTime dailyEndTime,
            TripConcept concept,
            Transportation transportation,
            String lastAccommodationArea
    ) {
        this(
                destination,
                startDate,
                endDate,
                dailyStartTime,
                dailyEndTime,
                concept,
                transportation,
                lastAccommodationArea,
                null,
                null,
                null,
                null
        );
    }
}
