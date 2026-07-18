package com.tripagent.place.controller;

import com.tripagent.auth.support.LoginMemberId;
import com.tripagent.common.response.ApiResponse;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.dto.PlaceSearchCandidateResponse;
import com.tripagent.place.dto.PlaceSuggestionApproveRequest;
import com.tripagent.place.service.AdminPlaceSuggestionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/places")
public class AdminPlaceController {

    private final AdminPlaceSuggestionService adminPlaceSuggestionService;

    public AdminPlaceController(AdminPlaceSuggestionService adminPlaceSuggestionService) {
        this.adminPlaceSuggestionService = adminPlaceSuggestionService;
    }

    @GetMapping("/candidates")
    public ApiResponse<List<PlaceSearchCandidateResponse>> searchCandidates(
            @LoginMemberId Long memberId,
            @RequestParam String keyword
    ) {
        return ApiResponse.success(adminPlaceSuggestionService.searchDirectCandidates(memberId, keyword));
    }

    @PostMapping
    public ApiResponse<PlaceResponse> registerPlace(
            @LoginMemberId Long memberId,
            @Valid @RequestBody PlaceSuggestionApproveRequest request
    ) {
        return ApiResponse.success(adminPlaceSuggestionService.registerPlace(memberId, request));
    }
}
