package com.tripagent.accommodation.controller;

import com.tripagent.accommodation.dto.AccommodationResponse;
import com.tripagent.accommodation.dto.AccommodationSearchCandidateResponse;
import com.tripagent.accommodation.dto.AdminAccommodationCreateRequest;
import com.tripagent.accommodation.service.AdminAccommodationService;
import com.tripagent.auth.support.LoginMemberId;
import com.tripagent.common.response.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping
    public ApiResponse<AccommodationResponse> registerAccommodation(
            @LoginMemberId Long memberId,
            @Valid @RequestBody AdminAccommodationCreateRequest request
    ) {
        return ApiResponse.success(adminAccommodationService.registerAccommodation(memberId, request));
    }
}
