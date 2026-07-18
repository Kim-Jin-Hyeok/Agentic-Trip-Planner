package com.tripagent.place.controller;

import com.tripagent.auth.support.LoginMemberId;
import com.tripagent.common.response.ApiResponse;
import com.tripagent.common.response.PageResponse;
import com.tripagent.place.dto.AdminPlaceStatusUpdateRequest;
import com.tripagent.place.dto.PlaceCategory;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.dto.PlaceSearchCandidateResponse;
import com.tripagent.place.dto.PlaceSuggestionApproveRequest;
import com.tripagent.place.service.AdminPlaceSuggestionService;
import com.tripagent.place.service.AdminPlaceService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/places")
public class AdminPlaceController {

    private final AdminPlaceSuggestionService adminPlaceSuggestionService;
    private final AdminPlaceService adminPlaceService;

    public AdminPlaceController(
            AdminPlaceSuggestionService adminPlaceSuggestionService,
            AdminPlaceService adminPlaceService
    ) {
        this.adminPlaceSuggestionService = adminPlaceSuggestionService;
        this.adminPlaceService = adminPlaceService;
    }

    @GetMapping
    public ApiResponse<PageResponse<PlaceResponse>> searchPlaces(
            @LoginMemberId Long memberId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) PlaceCategory category,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) Boolean useYn,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        return ApiResponse.success(adminPlaceService.searchPlaces(
                memberId, keyword, category, region, useYn, page, size));
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

    @PatchMapping("/{placeId}/status")
    public ApiResponse<PlaceResponse> updateStatus(
            @LoginMemberId Long memberId,
            @PathVariable Long placeId,
            @Valid @RequestBody AdminPlaceStatusUpdateRequest request
    ) {
        return ApiResponse.success(adminPlaceService.updateStatus(memberId, placeId, request.useYn()));
    }
}
