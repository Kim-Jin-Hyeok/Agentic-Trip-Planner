package com.tripagent.place.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PlaceSuggestionApproveRequest(
        @NotBlank(message = "External place id is required.")
        @Size(max = 100, message = "External place id must be 100 characters or less.")
        String externalPlaceId,

        @NotBlank(message = "Place name is required.")
        @Size(max = 100, message = "Place name must be 100 characters or less.")
        String name,

        @NotBlank(message = "Place address is required.")
        @Size(max = 255, message = "Place address must be 255 characters or less.")
        String address,

        @NotNull(message = "Place latitude is required.")
        Double latitude,

        @NotNull(message = "Place longitude is required.")
        Double longitude,

        @NotBlank(message = "Place category is required.")
        String category,

        @NotBlank(message = "Place region is required.")
        String region,

        @NotNull(message = "Average stay minutes is required.")
        @Min(value = 10, message = "Average stay minutes must be at least 10.")
        @Max(value = 480, message = "Average stay minutes must be 480 or less.")
        Integer avgStayMinutes,

        @NotNull(message = "Indoor status is required.")
        Boolean indoorYn,

        @NotNull(message = "Parking status is required.")
        Boolean parkingYn,

        @NotNull @Min(1) @Max(5) Integer rainyDayScore,
        @NotNull @Min(1) @Max(5) Integer healingScore,
        @NotNull @Min(1) @Max(5) Integer foodScore,
        @NotNull @Min(1) @Max(5) Integer cafeScore,
        @NotNull @Min(1) @Max(5) Integer photoScore,
        @NotNull @Min(1) @Max(5) Integer coupleScore,
        @NotNull @Min(1) @Max(5) Integer familyScore,

        @Size(max = 1000, message = "Place description must be 1000 characters or less.")
        String description
) {
}
