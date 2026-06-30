package com.tripagent.trip.controller;

import com.tripagent.common.response.ApiResponse;
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.itinerary.dto.ItineraryGenerateRequest;
import com.tripagent.itinerary.service.ItineraryGenerateService;
import com.tripagent.trip.dto.TripCreateRequest;
import com.tripagent.trip.dto.TripDetailResponse;
import com.tripagent.trip.dto.TripResponse;
import com.tripagent.trip.dto.TripVisibilityUpdateRequest;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.service.TripService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ApiResponse<TripResponse> createTrip(@Valid @RequestBody TripCreateRequest request) {
        return ApiResponse.success(tripService.createTrip(request));
    }

    @GetMapping
    public ApiResponse<List<TripResponse>> searchTrips(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) TripConcept concept,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateTo
    ) {
        return ApiResponse.success(tripService.searchTrips(
                destination,
                concept,
                startDateFrom,
                startDateTo,
                endDateFrom,
                endDateTo
        ));
    }

    @GetMapping("/{tripId}")
    public ApiResponse<TripDetailResponse> getTrip(@PathVariable Long tripId) {
        return ApiResponse.success(tripService.getTrip(tripId));
    }

    @DeleteMapping("/{tripId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrip(@PathVariable Long tripId) {
        tripService.deleteTrip(tripId);
    }

    @PatchMapping("/{tripId}/visibility")
    public ApiResponse<TripResponse> updateTripVisibility(
            @PathVariable Long tripId,
            @Valid @RequestBody TripVisibilityUpdateRequest request
    ) {
        return ApiResponse.success(tripService.updateTripVisibility(tripId, request.visibility()));
    }

    @PostMapping("/{tripId}/generate")
    public ApiResponse<List<ItineraryResponse>> generateItineraries(
            @PathVariable Long tripId,
            @Valid @RequestBody(required = false) ItineraryGenerateRequest request
    ) {
        return ApiResponse.success(itineraryGenerateService.generateItineraries(tripId, request));
    }

    @PostMapping("/{tripId}/regenerate")
    public ApiResponse<List<ItineraryResponse>> regenerateItineraries(
            @PathVariable Long tripId,
            @Valid @RequestBody(required = false) ItineraryGenerateRequest request
    ) {
        return ApiResponse.success(itineraryGenerateService.regenerateItineraries(tripId, request));
    }
}
