package com.tripagent.place.controller;

import com.tripagent.place.dto.PlaceRecommendConcept;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.service.PlaceService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
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
    public List<PlaceResponse> recommendPlaces(@RequestParam PlaceRecommendConcept concept) {
        return placeService.recommendPlaces(concept);
    }
}
