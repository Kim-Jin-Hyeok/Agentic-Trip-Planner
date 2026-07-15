package com.tripagent.accommodation.controller;

import com.tripagent.accommodation.domain.AccommodationType;
import com.tripagent.accommodation.dto.AccommodationResponse;
import com.tripagent.accommodation.service.AccommodationService;
import com.tripagent.common.response.ApiResponse;
import com.tripagent.common.response.PageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accommodations")
public class AccommodationController {

    private final AccommodationService accommodationService;

    public AccommodationController(AccommodationService accommodationService) {
        this.accommodationService = accommodationService;
    }

    @GetMapping
    public ApiResponse<PageResponse<AccommodationResponse>> searchAccommodations(
            @RequestParam(required = false) AccommodationType type,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        return ApiResponse.success(accommodationService.searchAccommodations(type, region, keyword, page, size));
    }

    @GetMapping("/{accommodationId}")
    public ApiResponse<AccommodationResponse> getAccommodation(@PathVariable Long accommodationId) {
        return ApiResponse.success(accommodationService.getAccommodation(accommodationId));
    }
}
