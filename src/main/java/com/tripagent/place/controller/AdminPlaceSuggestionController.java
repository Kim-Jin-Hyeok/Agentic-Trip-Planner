package com.tripagent.place.controller;

import com.tripagent.auth.support.LoginMemberId;
import com.tripagent.common.response.ApiResponse;
import com.tripagent.common.response.PageResponse;
import com.tripagent.place.domain.PlaceSuggestionStatus;
import com.tripagent.place.dto.AdminPlaceSuggestionResponse;
import com.tripagent.place.dto.PlaceSuggestionRejectRequest;
import com.tripagent.place.dto.PlaceSearchCandidateResponse;
import com.tripagent.place.service.AdminPlaceSuggestionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/place-suggestions")
public class AdminPlaceSuggestionController {

    private final AdminPlaceSuggestionService adminPlaceSuggestionService;

    public AdminPlaceSuggestionController(AdminPlaceSuggestionService adminPlaceSuggestionService) {
        this.adminPlaceSuggestionService = adminPlaceSuggestionService;
    }

    @GetMapping
    public ApiResponse<PageResponse<AdminPlaceSuggestionResponse>> getSuggestions(
            @LoginMemberId Long memberId,
            @RequestParam(required = false) PlaceSuggestionStatus status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        return ApiResponse.success(adminPlaceSuggestionService.getSuggestions(memberId, status, page, size));
    }

    @PatchMapping("/{placeSuggestionId}/reject")
    public ApiResponse<AdminPlaceSuggestionResponse> rejectSuggestion(
            @LoginMemberId Long memberId,
            @PathVariable Long placeSuggestionId,
            @Valid @RequestBody PlaceSuggestionRejectRequest request
    ) {
        return ApiResponse.success(
                adminPlaceSuggestionService.rejectSuggestion(memberId, placeSuggestionId, request)
        );
    }

    @GetMapping("/{placeSuggestionId}/candidates")
    public ApiResponse<List<PlaceSearchCandidateResponse>> searchCandidates(
            @LoginMemberId Long memberId,
            @PathVariable Long placeSuggestionId
    ) {
        return ApiResponse.success(
                adminPlaceSuggestionService.searchCandidates(memberId, placeSuggestionId)
        );
    }
}
