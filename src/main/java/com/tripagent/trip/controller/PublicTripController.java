package com.tripagent.trip.controller;

import com.tripagent.common.response.ApiResponse;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.dto.PublicTripSort;
import com.tripagent.trip.dto.TripDetailResponse;
import com.tripagent.trip.dto.TripLikeResponse;
import com.tripagent.trip.dto.TripResponse;
import com.tripagent.trip.service.TripService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/trips")
public class PublicTripController {

    private final TripService tripService;

    public PublicTripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping
    public ApiResponse<List<TripResponse>> searchPublicTrips(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) TripConcept concept,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateTo,
            @RequestParam(required = false) Integer nights,
            @RequestParam(required = false, defaultValue = "LATEST") PublicTripSort sort
    ) {
        return ApiResponse.success(tripService.searchPublicTrips(
                destination,
                concept,
                startDateFrom,
                startDateTo,
                endDateFrom,
                endDateTo,
                nights,
                sort
        ));
    }

    @GetMapping("/{tripId}")
    public ApiResponse<TripDetailResponse> getPublicTrip(@PathVariable Long tripId) {
        return ApiResponse.success(tripService.getPublicTrip(tripId));
    }

    @PostMapping("/{tripId}/likes")
    public ApiResponse<TripLikeResponse> likePublicTrip(
            @PathVariable Long tripId,
            @RequestParam Long userId
    ) {
        return ApiResponse.success(tripService.likePublicTrip(tripId, userId));
    }

    @DeleteMapping("/{tripId}/likes")
    public ApiResponse<TripLikeResponse> unlikePublicTrip(
            @PathVariable Long tripId,
            @RequestParam Long userId
    ) {
        return ApiResponse.success(tripService.unlikePublicTrip(tripId, userId));
    }
}
