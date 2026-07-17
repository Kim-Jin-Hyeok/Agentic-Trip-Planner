package com.tripagent.place.controller;

import com.tripagent.auth.support.LoginMemberId;
import com.tripagent.common.response.ApiResponse;
import com.tripagent.place.dto.PlaceSuggestionCreateRequest;
import com.tripagent.place.dto.PlaceSuggestionResponse;
import com.tripagent.place.service.PlaceSuggestionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/place-suggestions")
public class PlaceSuggestionController {

    private final PlaceSuggestionService placeSuggestionService;

    public PlaceSuggestionController(PlaceSuggestionService placeSuggestionService) {
        this.placeSuggestionService = placeSuggestionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PlaceSuggestionResponse> createSuggestion(
            @LoginMemberId Long memberId,
            @Valid @RequestBody PlaceSuggestionCreateRequest request
    ) {
        return ApiResponse.success(placeSuggestionService.createSuggestion(memberId, request));
    }

    @GetMapping
    public ApiResponse<List<PlaceSuggestionResponse>> getMySuggestions(@LoginMemberId Long memberId) {
        return ApiResponse.success(placeSuggestionService.getMySuggestions(memberId));
    }
}
