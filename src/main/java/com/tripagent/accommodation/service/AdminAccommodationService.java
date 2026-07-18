package com.tripagent.accommodation.service;

import com.tripagent.accommodation.domain.Accommodation;
import com.tripagent.accommodation.dto.AccommodationDuplicateReason;
import com.tripagent.accommodation.dto.AccommodationResponse;
import com.tripagent.accommodation.dto.AccommodationSearchCandidateResponse;
import com.tripagent.accommodation.dto.AdminAccommodationCreateRequest;
import com.tripagent.accommodation.repository.AccommodationRepository;
import com.tripagent.auth.service.AdminAuthorizationService;
import com.tripagent.common.exception.ConflictException;
import com.tripagent.place.adapter.PlaceSearchAdapter;
import com.tripagent.place.adapter.PlaceSearchCandidate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminAccommodationService {

    private static final String KAKAO_LOCAL_PROVIDER = "KAKAO_LOCAL";
    private static final double NEARBY_DISTANCE_METERS = 50.0;
    private static final double LATITUDE_MARGIN = 0.0005;
    private static final double LONGITUDE_MARGIN = 0.0006;
    private static final double EARTH_RADIUS_METERS = 6_371_000.0;
    private static final double MINIMUM_JEJU_LATITUDE = 33.0;
    private static final double MAXIMUM_JEJU_LATITUDE = 33.6;
    private static final double MINIMUM_JEJU_LONGITUDE = 126.0;
    private static final double MAXIMUM_JEJU_LONGITUDE = 127.0;
    private static final Set<String> ALLOWED_REGIONS = Set.of("EAST", "WEST", "NORTH", "SOUTH");

    private final AdminAuthorizationService adminAuthorizationService;
    private final PlaceSearchAdapter placeSearchAdapter;
    private final AccommodationRepository accommodationRepository;

    public AdminAccommodationService(
            AdminAuthorizationService adminAuthorizationService,
            PlaceSearchAdapter placeSearchAdapter,
            AccommodationRepository accommodationRepository
    ) {
        this.adminAuthorizationService = adminAuthorizationService;
        this.placeSearchAdapter = placeSearchAdapter;
        this.accommodationRepository = accommodationRepository;
    }

    public List<AccommodationSearchCandidateResponse> searchCandidates(Long memberId, String keyword) {
        adminAuthorizationService.requireAdmin(memberId);
        String normalizedKeyword = normalizeSearchKeyword(keyword);
        return placeSearchAdapter.search("제주 숙박 " + normalizedKeyword, 10).stream()
                .filter(this::hasJejuAddress)
                .filter(this::isAccommodationCategory)
                .limit(5)
                .map(this::toCandidateResponse)
                .toList();
    }

    @Transactional
    public AccommodationResponse registerAccommodation(
            Long memberId,
            AdminAccommodationCreateRequest request
    ) {
        adminAuthorizationService.requireAdmin(memberId);
        String region = normalizeRegion(request.region());
        validateJejuLocation(request.address(), request.latitude(), request.longitude());
        PlaceSearchCandidate candidate = registrationCandidate(request);
        findDuplicate(candidate).ifPresent(match -> {
            throw new ConflictException(
                    "이미 등록된 숙소입니다. accommodationId=" + match.accommodation().getAccommodationId()
            );
        });

        Accommodation accommodation = Accommodation.create(
                request.name().trim(),
                request.accommodationType(),
                region,
                request.address().trim(),
                request.latitude(),
                request.longitude(),
                normalizeOptional(request.description()),
                null,
                request.parkingYn(),
                true
        );
        accommodation.linkExternalReference(
                KAKAO_LOCAL_PROVIDER,
                request.externalPlaceId(),
                request.placeUrl()
        );

        try {
            return AccommodationResponse.from(accommodationRepository.saveAndFlush(accommodation));
        } catch (DataIntegrityViolationException exception) {
            throw new ConflictException("동일한 숙소가 이미 등록되어 있습니다.", exception);
        }
    }

    private AccommodationSearchCandidateResponse toCandidateResponse(PlaceSearchCandidate candidate) {
        Optional<DuplicateMatch> duplicateMatch = findDuplicate(candidate);
        return duplicateMatch
                .map(match -> AccommodationSearchCandidateResponse.from(
                        candidate,
                        true,
                        match.accommodation().getAccommodationId(),
                        match.reason()
                ))
                .orElseGet(() -> AccommodationSearchCandidateResponse.from(candidate, false, null, null));
    }

    private Optional<DuplicateMatch> findDuplicate(PlaceSearchCandidate candidate) {
        Optional<Accommodation> externalIdMatch = accommodationRepository
                .findFirstByExternalProviderAndExternalPlaceId(
                        KAKAO_LOCAL_PROVIDER,
                        candidate.externalPlaceId()
                );
        if (externalIdMatch.isPresent()) {
            return Optional.of(new DuplicateMatch(
                    externalIdMatch.get(),
                    AccommodationDuplicateReason.EXTERNAL_PLACE_ID
            ));
        }

        Optional<Accommodation> addressMatch = findNameAndAddressMatch(candidate);
        if (addressMatch.isPresent()) {
            return Optional.of(new DuplicateMatch(
                    addressMatch.get(),
                    AccommodationDuplicateReason.NAME_AND_ADDRESS
            ));
        }

        return findNearbyNameMatch(candidate)
                .map(accommodation -> new DuplicateMatch(
                        accommodation,
                        AccommodationDuplicateReason.NEARBY_NAME
                ));
    }

    private Optional<Accommodation> findNameAndAddressMatch(PlaceSearchCandidate candidate) {
        if (candidate.roadAddress() != null && !candidate.roadAddress().isBlank()) {
            Optional<Accommodation> roadAddressMatch = accommodationRepository
                    .findFirstByNameIgnoreCaseAndAddressIgnoreCaseOrderByAccommodationIdDesc(
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
        return accommodationRepository.findFirstByNameIgnoreCaseAndAddressIgnoreCaseOrderByAccommodationIdDesc(
                candidate.name(),
                candidate.address()
        );
    }

    private Optional<Accommodation> findNearbyNameMatch(PlaceSearchCandidate candidate) {
        List<Accommodation> nearbyAccommodations = accommodationRepository
                .findByLatitudeBetweenAndLongitudeBetween(
                        candidate.latitude() - LATITUDE_MARGIN,
                        candidate.latitude() + LATITUDE_MARGIN,
                        candidate.longitude() - LONGITUDE_MARGIN,
                        candidate.longitude() + LONGITUDE_MARGIN
                );
        String normalizedCandidateName = normalizeName(candidate.name());
        return nearbyAccommodations.stream()
                .filter(item -> normalizeName(item.getName()).equals(normalizedCandidateName))
                .filter(item -> distanceMeters(candidate, item) <= NEARBY_DISTANCE_METERS)
                .findFirst();
    }

    private PlaceSearchCandidate registrationCandidate(AdminAccommodationCreateRequest request) {
        return new PlaceSearchCandidate(
                request.externalPlaceId().trim(),
                request.name().trim(),
                request.address().trim(),
                request.address().trim(),
                request.latitude(),
                request.longitude(),
                "숙박",
                request.placeUrl()
        );
    }

    private String normalizeSearchKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Accommodation search keyword is required.");
        }
        String normalizedKeyword = keyword.trim();
        if (normalizedKeyword.length() > 100) {
            throw new IllegalArgumentException("Accommodation search keyword must be 100 characters or less.");
        }
        return normalizedKeyword;
    }

    private String normalizeRegion(String region) {
        String normalizedRegion = region == null ? "" : region.trim().toUpperCase(Locale.ROOT);
        if (!ALLOWED_REGIONS.contains(normalizedRegion)) {
            throw new IllegalArgumentException("Accommodation region is invalid. value=" + region);
        }
        return normalizedRegion;
    }

    private void validateJejuLocation(String address, Double latitude, Double longitude) {
        if (address == null || !address.contains("제주")) {
            throw new IllegalArgumentException("Accommodation address must be in Jeju.");
        }
        if (latitude == null || longitude == null
                || !Double.isFinite(latitude) || !Double.isFinite(longitude)
                || latitude < MINIMUM_JEJU_LATITUDE || latitude > MAXIMUM_JEJU_LATITUDE
                || longitude < MINIMUM_JEJU_LONGITUDE || longitude > MAXIMUM_JEJU_LONGITUDE) {
            throw new IllegalArgumentException("Accommodation coordinates must be within Jeju.");
        }
    }

    private boolean hasJejuAddress(PlaceSearchCandidate candidate) {
        return containsJeju(candidate.address()) || containsJeju(candidate.roadAddress());
    }

    private boolean containsJeju(String address) {
        return address != null && address.contains("제주");
    }

    private boolean isAccommodationCategory(PlaceSearchCandidate candidate) {
        return candidate.category() != null && candidate.category().contains("숙박");
    }

    private String normalizeOptional(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private String normalizeName(String name) {
        return name == null ? "" : name.replaceAll("\\s+", "").toLowerCase(Locale.ROOT);
    }

    private double distanceMeters(PlaceSearchCandidate candidate, Accommodation accommodation) {
        double latitudeDifference = Math.toRadians(accommodation.getLatitude() - candidate.latitude());
        double longitudeDifference = Math.toRadians(accommodation.getLongitude() - candidate.longitude());
        double candidateLatitude = Math.toRadians(candidate.latitude());
        double accommodationLatitude = Math.toRadians(accommodation.getLatitude());
        double haversine = Math.sin(latitudeDifference / 2) * Math.sin(latitudeDifference / 2)
                + Math.cos(candidateLatitude) * Math.cos(accommodationLatitude)
                * Math.sin(longitudeDifference / 2) * Math.sin(longitudeDifference / 2);
        double centralAngle = 2 * Math.atan2(Math.sqrt(haversine), Math.sqrt(1 - haversine));
        return EARTH_RADIUS_METERS * centralAngle;
    }

    private record DuplicateMatch(
            Accommodation accommodation,
            AccommodationDuplicateReason reason
    ) {
    }
}
