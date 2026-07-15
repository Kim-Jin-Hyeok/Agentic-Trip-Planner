package com.tripagent.accommodation.service;

import com.tripagent.accommodation.domain.Accommodation;
import com.tripagent.accommodation.domain.AccommodationType;
import com.tripagent.accommodation.dto.AccommodationResponse;
import com.tripagent.accommodation.repository.AccommodationRepository;
import com.tripagent.common.response.PageResponse;
import java.util.Locale;
import java.util.NoSuchElementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AccommodationService {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 50;

    private final AccommodationRepository accommodationRepository;

    public AccommodationService(AccommodationRepository accommodationRepository) {
        this.accommodationRepository = accommodationRepository;
    }

    public PageResponse<AccommodationResponse> searchAccommodations(
            AccommodationType accommodationType,
            String region,
            String keyword,
            Integer page,
            Integer size
    ) {
        Pageable pageable = PageRequest.of(
                normalizePage(page),
                normalizePageSize(size),
                Sort.by(Sort.Direction.ASC, "accommodationId")
        );
        Page<Accommodation> accommodationPage = accommodationRepository.searchAccommodations(
                true,
                accommodationType,
                normalizeRegion(region),
                normalizeKeyword(keyword),
                pageable
        );

        return PageResponse.from(accommodationPage.map(AccommodationResponse::from));
    }

    public AccommodationResponse getAccommodation(Long accommodationId) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
                .filter(item -> Boolean.TRUE.equals(item.getUseYn()))
                .orElseThrow(() -> new NoSuchElementException(
                        "Accommodation not found. accommodationId=" + accommodationId
                ));

        return AccommodationResponse.from(accommodation);
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

    private int normalizePageSize(Integer size) {
        if (size == null) {
            return DEFAULT_PAGE_SIZE;
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("Page size must be between 1 and 50.");
        }
        return size;
    }

    private String normalizeRegion(String region) {
        if (region == null || region.isBlank()) {
            return null;
        }
        return region.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }
        return keyword.trim().toLowerCase(Locale.ROOT);
    }
}
