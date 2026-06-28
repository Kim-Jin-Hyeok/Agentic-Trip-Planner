package com.tripagent.place.controller;

import com.tripagent.common.response.ApiResponse;
import com.tripagent.place.dto.PlaceCategory;
import com.tripagent.place.dto.PlaceRecommendConcept;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.service.PlaceService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/recommend")
    public ApiResponse<List<PlaceResponse>> recommendPlaces(@RequestParam PlaceRecommendConcept concept) {
        return ApiResponse.success(placeService.recommendPlaces(concept));
    }

    @GetMapping
    public ApiResponse<List<PlaceResponse>> searchPlaces(
            @RequestParam(required = false) PlaceRecommendConcept concept,
            @RequestParam(required = false) PlaceCategory category,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean useYn
    ) {
        return ApiResponse.success(placeService.searchPlaces(concept, category, keyword, useYn));
    }

    @GetMapping("/{placeId}")
    public ApiResponse<PlaceResponse> getPlace(@PathVariable Long placeId) {
        return ApiResponse.success(placeService.getPlace(placeId));
    }
}
