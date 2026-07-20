package com.tripagent.accommodation.controller;

import com.tripagent.accommodation.dto.AccommodationResponse;
import com.tripagent.accommodation.dto.AccommodationSearchCandidateResponse;
import com.tripagent.accommodation.dto.AdminAccommodationCreateRequest;
import com.tripagent.accommodation.dto.AdminAccommodationStatusUpdateRequest;
import com.tripagent.accommodation.domain.AccommodationType;
import com.tripagent.accommodation.service.AdminAccommodationService;
import com.tripagent.auth.support.LoginMemberId;
import com.tripagent.common.response.ApiResponse;
import com.tripagent.common.response.PageResponse;
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
@RequestMapping("/api/admin/accommodations")
public class AdminAccommodationController {

    private final AdminAccommodationService adminAccommodationService;

    public AdminAccommodationController(AdminAccommodationService adminAccommodationService) {
        this.adminAccommodationService = adminAccommodationService;
    }

    @GetMapping("/candidates")
    public ApiResponse<List<AccommodationSearchCandidateResponse>> searchCandidates(
            @LoginMemberId Long memberId,
            @RequestParam String keyword
    ) {
        return ApiResponse.success(adminAccommodationService.searchCandidates(memberId, keyword));
    }

    @GetMapping
    public ApiResponse<PageResponse<AccommodationResponse>> searchAccommodations(
            @LoginMemberId Long memberId,
            @RequestParam(required = false) AccommodationType type,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean useYn,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        return ApiResponse.success(adminAccommodationService.searchAccommodations(
                memberId, type, region, keyword, useYn, page, size
        ));
    }

    @PostMapping
    public ApiResponse<AccommodationResponse> registerAccommodation(
            @LoginMemberId Long memberId,
            @Valid @RequestBody AdminAccommodationCreateRequest request
    ) {
        return ApiResponse.success(adminAccommodationService.registerAccommodation(memberId, request));
    }

    @PatchMapping("/{accommodationId}/status")
    public ApiResponse<AccommodationResponse> updateStatus(
            @LoginMemberId Long memberId,
            @PathVariable Long accommodationId,
            @Valid @RequestBody AdminAccommodationStatusUpdateRequest request
    ) {
        return ApiResponse.success(
                adminAccommodationService.updateStatus(memberId, accommodationId, request.useYn())
        );
    }
}
