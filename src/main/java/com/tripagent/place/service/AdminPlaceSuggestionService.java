package com.tripagent.place.service;

import com.tripagent.auth.service.AdminAuthorizationService;
import com.tripagent.common.exception.ConflictException;
import com.tripagent.common.response.PageResponse;
import com.tripagent.place.adapter.PlaceSearchAdapter;
import com.tripagent.place.adapter.PlaceSearchCandidate;
import com.tripagent.place.domain.PlaceSuggestion;
import com.tripagent.place.domain.PlaceSuggestionStatus;
import com.tripagent.place.domain.Place;
import com.tripagent.place.dto.AdminPlaceSuggestionResponse;
import com.tripagent.place.dto.PlaceDuplicateReason;
import com.tripagent.place.dto.PlaceSuggestionRejectRequest;
import com.tripagent.place.dto.PlaceSearchCandidateResponse;
import com.tripagent.place.dto.PlaceSuggestionApproveRequest;
import com.tripagent.place.dto.PlaceSuggestionApprovalResponse;
import com.tripagent.place.repository.PlaceSuggestionRepository;
import com.tripagent.place.repository.PlaceRepository;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import org.springframework.dao.DataIntegrityViolationException;
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
    private static final String KAKAO_LOCAL_PROVIDER = "KAKAO_LOCAL";
    private static final double NEARBY_DISTANCE_METERS = 50.0;
    private static final double LATITUDE_MARGIN = 0.0005;
    private static final double LONGITUDE_MARGIN = 0.0006;
    private static final double EARTH_RADIUS_METERS = 6_371_000.0;
    private static final double MINIMUM_JEJU_LATITUDE = 33.0;
    private static final double MAXIMUM_JEJU_LATITUDE = 33.6;
    private static final double MINIMUM_JEJU_LONGITUDE = 126.0;
    private static final double MAXIMUM_JEJU_LONGITUDE = 127.0;
    private static final Set<String> ALLOWED_CATEGORIES = Set.of("NATURE", "FOOD", "CAFE");
    private static final Set<String> ALLOWED_REGIONS = Set.of("EAST", "WEST", "NORTH", "SOUTH");

    private final PlaceSuggestionRepository placeSuggestionRepository;
    private final AdminAuthorizationService adminAuthorizationService;
    private final PlaceSearchAdapter placeSearchAdapter;
    private final PlaceRepository placeRepository;

    public AdminPlaceSuggestionService(
            PlaceSuggestionRepository placeSuggestionRepository,
            AdminAuthorizationService adminAuthorizationService,
            PlaceSearchAdapter placeSearchAdapter,
            PlaceRepository placeRepository
    ) {
        this.placeSuggestionRepository = placeSuggestionRepository;
        this.adminAuthorizationService = adminAuthorizationService;
        this.placeSearchAdapter = placeSearchAdapter;
        this.placeRepository = placeRepository;
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

    @Transactional
    public PlaceSuggestionApprovalResponse approveSuggestion(
            Long memberId,
            Long placeSuggestionId,
            PlaceSuggestionApproveRequest request
    ) {
        adminAuthorizationService.requireAdmin(memberId);
        PlaceSuggestion suggestion = placeSuggestionRepository.findById(placeSuggestionId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Place suggestion not found. placeSuggestionId=" + placeSuggestionId
                ));
        if (suggestion.getStatus() != PlaceSuggestionStatus.PENDING) {
            throw new IllegalArgumentException("Only pending place suggestions can be approved.");
        }

        String category = normalizeAllowedValue("Place category", request.category(), ALLOWED_CATEGORIES);
        String region = normalizeAllowedValue("Place region", request.region(), ALLOWED_REGIONS);
        validateJejuLocation(request.address(), request.latitude(), request.longitude());
        PlaceSearchCandidate candidate = approvalCandidate(request);
        findDuplicate(candidate).ifPresent(match -> {
            throw new ConflictException("이미 등록된 장소입니다. placeId=" + match.place().getPlaceId());
        });

        Place place = Place.create(
                request.name().trim(),
                category,
                region,
                request.address().trim(),
                request.latitude(),
                request.longitude(),
                request.avgStayMinutes(),
                request.indoorYn(),
                request.parkingYn(),
                request.rainyDayScore(),
                request.healingScore(),
                request.foodScore(),
                request.cafeScore(),
                request.photoScore(),
                request.coupleScore(),
                request.familyScore(),
                normalizeDescription(request.description()),
                true
        );
        place.linkExternalReference(KAKAO_LOCAL_PROVIDER, request.externalPlaceId());

        try {
            placeRepository.saveAndFlush(place);
        } catch (DataIntegrityViolationException exception) {
            throw new ConflictException("동일한 외부 장소가 이미 등록되어 있습니다.", exception);
        }
        suggestion.approve();
        return new PlaceSuggestionApprovalResponse(
                suggestion.getPlaceSuggestionId(),
                place.getPlaceId(),
                suggestion.getStatus(),
                suggestion.getReviewedAt()
        );
    }

    private PlaceSearchCandidate approvalCandidate(PlaceSuggestionApproveRequest request) {
        return new PlaceSearchCandidate(
                request.externalPlaceId().trim(),
                request.name().trim(),
                request.address().trim(),
                request.address().trim(),
                request.latitude(),
                request.longitude(),
                request.category(),
                null
        );
    }

    private String normalizeAllowedValue(String fieldName, String value, Set<String> allowedValues) {
        String normalizedValue = value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
        if (!allowedValues.contains(normalizedValue)) {
            throw new IllegalArgumentException(fieldName + " is invalid. value=" + value);
        }
        return normalizedValue;
    }

    private void validateJejuLocation(String address, Double latitude, Double longitude) {
        if (address == null || !address.contains("제주")) {
            throw new IllegalArgumentException("Place address must be in Jeju.");
        }
        if (latitude == null || longitude == null
                || !Double.isFinite(latitude) || !Double.isFinite(longitude)
                || latitude < MINIMUM_JEJU_LATITUDE || latitude > MAXIMUM_JEJU_LATITUDE
                || longitude < MINIMUM_JEJU_LONGITUDE || longitude > MAXIMUM_JEJU_LONGITUDE) {
            throw new IllegalArgumentException("Place coordinates must be within Jeju.");
        }
    }

    private String normalizeDescription(String description) {
        return description == null || description.isBlank() ? null : description.trim();
    }

    public List<PlaceSearchCandidateResponse> searchCandidates(Long memberId, Long placeSuggestionId) {
        adminAuthorizationService.requireAdmin(memberId);
        PlaceSuggestion suggestion = placeSuggestionRepository.findById(placeSuggestionId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Place suggestion not found. placeSuggestionId=" + placeSuggestionId
                ));
        if (suggestion.getStatus() != PlaceSuggestionStatus.PENDING) {
            throw new IllegalArgumentException("Only pending place suggestions can search external candidates.");
        }

        List<PlaceSearchCandidate> candidates = searchJejuCandidates(
                suggestion.getName() + " " + suggestion.getAddress()
        );
        if (candidates.isEmpty()) {
            candidates = searchJejuCandidates("제주 " + suggestion.getName());
        }
        return candidates.stream()
                .limit(5)
                .map(this::toCandidateResponse)
                .toList();
    }

    private PlaceSearchCandidateResponse toCandidateResponse(PlaceSearchCandidate candidate) {
        Optional<DuplicateMatch> duplicateMatch = findDuplicate(candidate);
        return duplicateMatch
                .map(match -> PlaceSearchCandidateResponse.from(
                        candidate,
                        true,
                        match.place().getPlaceId(),
                        match.reason()
                ))
                .orElseGet(() -> PlaceSearchCandidateResponse.from(candidate, false, null, null));
    }

    private Optional<DuplicateMatch> findDuplicate(PlaceSearchCandidate candidate) {
        Optional<Place> externalIdMatch = placeRepository.findFirstByExternalProviderAndExternalPlaceId(
                KAKAO_LOCAL_PROVIDER,
                candidate.externalPlaceId()
        );
        if (externalIdMatch.isPresent()) {
            return Optional.of(new DuplicateMatch(externalIdMatch.get(), PlaceDuplicateReason.EXTERNAL_PLACE_ID));
        }

        Optional<Place> addressMatch = findNameAndAddressMatch(candidate);
        if (addressMatch.isPresent()) {
            return Optional.of(new DuplicateMatch(addressMatch.get(), PlaceDuplicateReason.NAME_AND_ADDRESS));
        }

        return findNearbyNameMatch(candidate)
                .map(place -> new DuplicateMatch(place, PlaceDuplicateReason.NEARBY_NAME));
    }

    private Optional<Place> findNameAndAddressMatch(PlaceSearchCandidate candidate) {
        if (candidate.roadAddress() != null && !candidate.roadAddress().isBlank()) {
            Optional<Place> roadAddressMatch = placeRepository
                    .findFirstByNameIgnoreCaseAndAddressIgnoreCaseOrderByPlaceIdDesc(
                            candidate.name(),
                            candidate.roadAddress()
                    );
            if (roadAddressMatch.isPresent()) {
                return roadAddressMatch;
            }
        }
        if (candidate.address() == null || candidate.address().isBlank()) {
            return Optional.empty();
        }
        return placeRepository.findFirstByNameIgnoreCaseAndAddressIgnoreCaseOrderByPlaceIdDesc(
                candidate.name(),
                candidate.address()
        );
    }

    private Optional<Place> findNearbyNameMatch(PlaceSearchCandidate candidate) {
        List<Place> nearbyPlaces = placeRepository.findByLatitudeBetweenAndLongitudeBetween(
                candidate.latitude() - LATITUDE_MARGIN,
                candidate.latitude() + LATITUDE_MARGIN,
                candidate.longitude() - LONGITUDE_MARGIN,
                candidate.longitude() + LONGITUDE_MARGIN
        );
        String normalizedCandidateName = normalizeName(candidate.name());
        return nearbyPlaces.stream()
                .filter(place -> normalizeName(place.getName()).equals(normalizedCandidateName))
                .filter(place -> distanceMeters(candidate, place) <= NEARBY_DISTANCE_METERS)
                .findFirst();
    }

    private String normalizeName(String name) {
        return name == null
                ? ""
                : name.replaceAll("\\s+", "").toLowerCase(Locale.ROOT);
    }

    private double distanceMeters(PlaceSearchCandidate candidate, Place place) {
        double latitudeDifference = Math.toRadians(place.getLatitude() - candidate.latitude());
        double longitudeDifference = Math.toRadians(place.getLongitude() - candidate.longitude());
        double candidateLatitude = Math.toRadians(candidate.latitude());
        double placeLatitude = Math.toRadians(place.getLatitude());
        double haversine = Math.sin(latitudeDifference / 2) * Math.sin(latitudeDifference / 2)
                + Math.cos(candidateLatitude) * Math.cos(placeLatitude)
                * Math.sin(longitudeDifference / 2) * Math.sin(longitudeDifference / 2);
        double centralAngle = 2 * Math.atan2(Math.sqrt(haversine), Math.sqrt(1 - haversine));
        return EARTH_RADIUS_METERS * centralAngle;
    }

    private List<PlaceSearchCandidate> searchJejuCandidates(String query) {
        return placeSearchAdapter.search(query, 5).stream()
                .filter(this::hasJejuAddress)
                .toList();
    }

    private boolean hasJejuAddress(PlaceSearchCandidate candidate) {
        return containsJeju(candidate.address()) || containsJeju(candidate.roadAddress());
    }

    private boolean containsJeju(String address) {
        return address != null && address.contains("제주");
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

    private record DuplicateMatch(Place place, PlaceDuplicateReason reason) {
    }
}
