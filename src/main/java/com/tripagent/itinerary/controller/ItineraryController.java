package com.tripagent.itinerary.controller;

import com.tripagent.common.response.ApiResponse;
import com.tripagent.itinerary.dto.ItineraryCreateRequest;
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.itinerary.dto.ItineraryUpdateRequest;
import com.tripagent.itinerary.service.ItineraryService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trips/{tripId}/itineraries")
public class ItineraryController {

    private final ItineraryService itineraryService;

    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ItineraryResponse> createItinerary(
            @PathVariable Long tripId,
            @RequestBody ItineraryCreateRequest request
    ) {
        return ApiResponse.success(itineraryService.createItinerary(tripId, request));
    }

    @GetMapping
    public ApiResponse<List<ItineraryResponse>> getItineraries(@PathVariable Long tripId) {
        return ApiResponse.success(itineraryService.getItineraries(tripId));
    }

    @PatchMapping("/{itineraryId}")
    public ApiResponse<ItineraryResponse> updateItinerary(
            @PathVariable Long tripId,
            @PathVariable Long itineraryId,
            @RequestBody ItineraryUpdateRequest request
    ) {
        return ApiResponse.success(itineraryService.updateItinerary(tripId, itineraryId, request));
    }

    @DeleteMapping("/{itineraryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItinerary(
            @PathVariable Long tripId,
            @PathVariable Long itineraryId
    ) {
        itineraryService.deleteItinerary(tripId, itineraryId);
    }
}
