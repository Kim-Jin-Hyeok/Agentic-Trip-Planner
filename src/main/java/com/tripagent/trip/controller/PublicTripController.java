package com.tripagent.trip.controller;

import com.tripagent.auth.support.LoginMemberId;
import com.tripagent.auth.service.JwtTokenProvider;
import com.tripagent.common.response.ApiResponse;
import com.tripagent.common.response.PageResponse;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.dto.PublicTripDetailResponse;
import com.tripagent.trip.dto.PublicTripResponse;
import com.tripagent.trip.dto.PublicTripSort;
import com.tripagent.trip.dto.TripLikeResponse;
import com.tripagent.trip.service.TripService;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/trips")
public class PublicTripController {

    private static final String BEARER_PREFIX = "Bearer ";

    private final TripService tripService;
    private final JwtTokenProvider jwtTokenProvider;

    public PublicTripController(TripService tripService, JwtTokenProvider jwtTokenProvider) {
        this.tripService = tripService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping
    public ApiResponse<PageResponse<PublicTripResponse>> searchPublicTrips(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) TripConcept concept,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateTo,
            @RequestParam(required = false) Integer nights,
            @RequestParam(required = false, defaultValue = "LATEST") PublicTripSort sort,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        return ApiResponse.success(tripService.searchPublicTripPage(
                destination,
                concept,
                startDateFrom,
                startDateTo,
                endDateFrom,
                endDateTo,
                nights,
                sort,
                page,
                size,
                resolveOptionalMemberId(authorization)
        ));
    }

    @GetMapping("/{tripId}")
    public ApiResponse<PublicTripDetailResponse> getPublicTrip(
            @PathVariable Long tripId,
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        return ApiResponse.success(tripService.getPublicTrip(tripId, resolveOptionalMemberId(authorization)));
    }

    @GetMapping("/likes")
    public ApiResponse<PageResponse<PublicTripResponse>> searchLikedPublicTrips(
            @LoginMemberId Long memberId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        return ApiResponse.success(tripService.searchLikedPublicTripPage(memberId, page, size));
    }

    @PostMapping("/{tripId}/likes")
    public ApiResponse<TripLikeResponse> likePublicTrip(
            @PathVariable Long tripId,
            @LoginMemberId Long memberId
    ) {
        return ApiResponse.success(tripService.likePublicTrip(tripId, memberId));
    }

    @DeleteMapping("/{tripId}/likes")
    public ApiResponse<TripLikeResponse> unlikePublicTrip(
            @PathVariable Long tripId,
            @LoginMemberId Long memberId
    ) {
        return ApiResponse.success(tripService.unlikePublicTrip(tripId, memberId));
    }

    private Long resolveOptionalMemberId(String authorization) {
        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            return null;
        }

        String accessToken = authorization.substring(BEARER_PREFIX.length()).trim();
        if (accessToken.isBlank()) {
            return null;
        }

        return jwtTokenProvider.getMemberId(accessToken);
    }
}
