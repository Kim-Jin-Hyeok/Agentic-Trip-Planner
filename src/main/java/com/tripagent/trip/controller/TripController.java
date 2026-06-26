package com.tripagent.trip.controller;

import com.tripagent.trip.dto.TripCreateRequest;
import com.tripagent.trip.dto.TripResponse;
import com.tripagent.trip.service.TripService;
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

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TripResponse createTrip(@RequestBody TripCreateRequest request) {
        return tripService.createTrip(request);
    }

    @GetMapping("/{tripId}")
    public TripResponse getTrip(@PathVariable Long tripId) {
        return tripService.getTrip(tripId);
    }
}
