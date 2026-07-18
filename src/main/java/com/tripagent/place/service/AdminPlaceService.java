package com.tripagent.place.service;

import com.tripagent.auth.service.AdminAuthorizationService;
import com.tripagent.common.response.PageResponse;
import com.tripagent.place.domain.Place;
import com.tripagent.place.domain.TripEndpointPlace;
import com.tripagent.place.dto.PlaceCategory;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.repository.PlaceRepository;
import java.util.Locale;
import java.util.NoSuchElementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminPlaceService {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 50;

    private final AdminAuthorizationService adminAuthorizationService;
    private final PlaceRepository placeRepository;

    public AdminPlaceService(
            AdminAuthorizationService adminAuthorizationService,
            PlaceRepository placeRepository
    ) {
        this.adminAuthorizationService = adminAuthorizationService;
        this.placeRepository = placeRepository;
    }

    public PageResponse<PlaceResponse> searchPlaces(
            Long memberId,
            String keyword,
            PlaceCategory category,
            String region,
            Boolean useYn,
            Integer page,
            Integer size
    ) {
        adminAuthorizationService.requireAdmin(memberId);
        Pageable pageable = PageRequest.of(normalizePage(page), normalizeSize(size));
        Page<Place> places = placeRepository.searchAdminPlaces(
                normalizeKeyword(keyword),
                category == null ? null : category.name(),
                normalizeRegion(region),
                useYn,
                pageable
        );
        return PageResponse.from(places.map(PlaceResponse::from));
    }

    @Transactional
    public PlaceResponse updateStatus(Long memberId, Long placeId, Boolean useYn) {
        adminAuthorizationService.requireAdmin(memberId);
        if (useYn == null) {
            throw new IllegalArgumentException("Place useYn is required.");
        }
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new NoSuchElementException("Place not found. placeId=" + placeId));
        if (!useYn && TripEndpointPlace.supports(place.getName())) {
            throw new IllegalArgumentException(
                    "Trip endpoint place cannot be deactivated. placeId=" + placeId
            );
        }
        place.changeUseYn(useYn);
        return PlaceResponse.from(place);
    }

    private int normalizePage(Integer page) {
        if (page == null) {
            return DEFAULT_PAGE;
        }
        if (page < 0) {
            throw new IllegalArgumentException("Page must be greater than or equal to 0.");
        }
        return page;
    }

    private int normalizeSize(Integer size) {
        if (size == null) {
            return DEFAULT_PAGE_SIZE;
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("Size must be between 1 and 50.");
        }
        return size;
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return keyword.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizeRegion(String region) {
        if (region == null || region.isBlank()) {
            return null;
        }
        return region.trim().toUpperCase(Locale.ROOT);
    }
}
