package com.tripagent.trip.dto;

public record TripLikeResponse(
        Long tripId,
        Long userId,
        Long likeCount,
        boolean liked
) {
}
