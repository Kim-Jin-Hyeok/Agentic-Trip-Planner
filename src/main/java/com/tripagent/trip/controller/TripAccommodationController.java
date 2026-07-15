package com.tripagent.trip.controller;

import com.tripagent.auth.support.LoginMemberId;
import com.tripagent.common.response.ApiResponse;
import com.tripagent.trip.dto.TripAccommodationReplaceRequest;
import com.tripagent.trip.dto.TripAccommodationResponse;
import com.tripagent.trip.service.TripAccommodationService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trips/{tripId}/accommodations")
public class TripAccommodationController {

    private final TripAccommodationService tripAccommodationService;

    public TripAccommodationController(TripAccommodationService tripAccommodationService) {
        this.tripAccommodationService = tripAccommodationService;
    }

    @GetMapping
    public ApiResponse<List<TripAccommodationResponse>> getTripAccommodations(
            @PathVariable Long tripId,
            @LoginMemberId Long memberId
    ) {
        return ApiResponse.success(tripAccommodationService.getTripAccommodations(tripId, memberId));
    }

    @PutMapping
    public ApiResponse<List<TripAccommodationResponse>> replaceTripAccommodations(
            @PathVariable Long tripId,
            @LoginMemberId Long memberId,
            @Valid @RequestBody TripAccommodationReplaceRequest request
    ) {
        return ApiResponse.success(
                tripAccommodationService.replaceTripAccommodations(tripId, memberId, request)
        );
    }
}
