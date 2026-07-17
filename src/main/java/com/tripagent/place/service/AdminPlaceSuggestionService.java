package com.tripagent.place.service;

import com.tripagent.auth.service.AdminAuthorizationService;
import com.tripagent.common.response.PageResponse;
import com.tripagent.place.domain.PlaceSuggestion;
import com.tripagent.place.domain.PlaceSuggestionStatus;
import com.tripagent.place.dto.AdminPlaceSuggestionResponse;
import com.tripagent.place.dto.PlaceSuggestionRejectRequest;
import com.tripagent.place.repository.PlaceSuggestionRepository;
import java.util.NoSuchElementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminPlaceSuggestionService {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 50;

    private final PlaceSuggestionRepository placeSuggestionRepository;
    private final AdminAuthorizationService adminAuthorizationService;

    public AdminPlaceSuggestionService(
            PlaceSuggestionRepository placeSuggestionRepository,
            AdminAuthorizationService adminAuthorizationService
    ) {
        this.placeSuggestionRepository = placeSuggestionRepository;
        this.adminAuthorizationService = adminAuthorizationService;
    }

    public PageResponse<AdminPlaceSuggestionResponse> getSuggestions(
            Long memberId,
            PlaceSuggestionStatus status,
            Integer page,
            Integer size
    ) {
        adminAuthorizationService.requireAdmin(memberId);
        int normalizedPage = normalizePage(page);
        int normalizedSize = normalizeSize(size);
        PlaceSuggestionStatus normalizedStatus = status == null ? PlaceSuggestionStatus.PENDING : status;
        Pageable pageable = PageRequest.of(normalizedPage, normalizedSize);
        Page<PlaceSuggestion> suggestionPage = placeSuggestionRepository
                .findByStatusOrderByPlaceSuggestionIdDesc(normalizedStatus, pageable);
        return PageResponse.from(suggestionPage.map(AdminPlaceSuggestionResponse::from));
    }

    @Transactional
    public AdminPlaceSuggestionResponse rejectSuggestion(
            Long memberId,
            Long placeSuggestionId,
            PlaceSuggestionRejectRequest request
    ) {
        adminAuthorizationService.requireAdmin(memberId);
        PlaceSuggestion suggestion = placeSuggestionRepository.findById(placeSuggestionId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Place suggestion not found. placeSuggestionId=" + placeSuggestionId
                ));
        suggestion.reject(request.rejectionReason());
        return AdminPlaceSuggestionResponse.from(suggestion);
    }

    private int normalizePage(Integer page) {
        int normalizedPage = page == null ? DEFAULT_PAGE : page;
        if (normalizedPage < 0) {
            throw new IllegalArgumentException("Page must be greater than or equal to 0.");
        }
        return normalizedPage;
    }

    private int normalizeSize(Integer size) {
        int normalizedSize = size == null ? DEFAULT_PAGE_SIZE : size;
        if (normalizedSize < 1 || normalizedSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("Page size must be between 1 and 50.");
        }
        return normalizedSize;
    }
}
