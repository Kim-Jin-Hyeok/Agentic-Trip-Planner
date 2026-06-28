package com.tripagent.trip.controller;

import com.tripagent.common.response.ApiResponse;
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.itinerary.dto.ItineraryGenerateRequest;
import com.tripagent.itinerary.service.ItineraryGenerateService;
import com.tripagent.trip.dto.TripCreateRequest;
import com.tripagent.trip.dto.TripDetailResponse;
import com.tripagent.trip.dto.TripResponse;
import com.tripagent.trip.service.TripService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;
    private final ItineraryGenerateService itineraryGenerateService;

    public TripController(
            TripService tripService,
            ItineraryGenerateService itineraryGenerateService
    ) {
        this.tripService = tripService;
        this.itineraryGenerateService = itineraryGenerateService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TripResponse> createTrip(@RequestBody TripCreateRequest request) {
        return ApiResponse.success(tripService.createTrip(request));
    }

    @GetMapping("/{tripId}")
    public ApiResponse<TripDetailResponse> getTrip(@PathVariable Long tripId) {
        return ApiResponse.success(tripService.getTrip(tripId));
    }

    @PostMapping("/{tripId}/generate")
    public ApiResponse<List<ItineraryResponse>> generateItineraries(
            @PathVariable Long tripId,
            @RequestBody(required = false) ItineraryGenerateRequest request
    ) {
        return ApiResponse.success(itineraryGenerateService.generateItineraries(tripId, request));
    }

    @PostMapping("/{tripId}/regenerate")
    public ApiResponse<List<ItineraryResponse>> regenerateItineraries(
            @PathVariable Long tripId,
            @RequestBody(required = false) ItineraryGenerateRequest request
    ) {
        return ApiResponse.success(itineraryGenerateService.regenerateItineraries(tripId, request));
    }
}
