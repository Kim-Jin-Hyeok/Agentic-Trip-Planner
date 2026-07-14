package com.tripagent.trip.service;

import com.tripagent.itinerary.domain.Itinerary;
import com.tripagent.itinerary.repository.ItineraryRepository;
import com.tripagent.common.response.PageResponse;
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.member.domain.Member;
import com.tripagent.member.repository.MemberRepository;
import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.domain.TripLike;
import com.tripagent.trip.domain.TripView;
import com.tripagent.trip.domain.TripVisibility;
import com.tripagent.trip.dto.PublicTripDetailResponse;
import com.tripagent.trip.dto.PublicTripSort;
import com.tripagent.trip.dto.PublicTripResponse;
import com.tripagent.trip.dto.TripAuthorResponse;
import com.tripagent.trip.dto.TripConditionUpdateRequest;
import com.tripagent.trip.dto.TripCreateRequest;
import com.tripagent.trip.dto.TripDetailResponse;
import com.tripagent.trip.dto.TripLikeResponse;
import com.tripagent.trip.dto.TripPlaceSummaryResponse;
import com.tripagent.trip.dto.TripResponse;
import com.tripagent.trip.repository.TripLikeRepository;
import com.tripagent.trip.repository.TripRepository;
import com.tripagent.trip.repository.TripViewRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TripService {

    private static final long MIN_NIGHTS = 1;
    private static final long MAX_NIGHTS = 3;
    private static final int DEFAULT_PUBLIC_TRIP_PAGE = 0;
    private static final int DEFAULT_PUBLIC_TRIP_PAGE_SIZE = 20;
    private static final int MAX_PUBLIC_TRIP_PAGE_SIZE = 50;
    private static final int REPRESENTATIVE_PLACE_LIMIT = 3;

    private final TripRepository tripRepository;
    private final ItineraryRepository itineraryRepository;
    private final TripLikeRepository tripLikeRepository;
    private final TripViewRepository tripViewRepository;
    private final MemberRepository memberRepository;

    public TripService(
            TripRepository tripRepository,
            ItineraryRepository itineraryRepository,
            TripLikeRepository tripLikeRepository,
            TripViewRepository tripViewRepository,
            MemberRepository memberRepository
    ) {
        this.tripRepository = tripRepository;
        this.itineraryRepository = itineraryRepository;
        this.tripLikeRepository = tripLikeRepository;
        this.tripViewRepository = tripViewRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public TripResponse createTrip(TripCreateRequest request) {
        return createTrip(request, request == null ? null : request.ownerId());
    }

    @Transactional
    public TripResponse createTrip(TripCreateRequest request, Long ownerId) {
        validateCreateRequest(request);

        Trip trip = Trip.create(
                normalizeTripTitle(request.title(), request.destination()),
                request.destination(),
                request.startDate(),
                request.endDate(),
                request.dailyStartTime(),
                request.dailyEndTime(),
                request.concept(),
                request.transportation(),
                request.lastAccommodationArea(),
                ownerId
        );

        return TripResponse.from(tripRepository.save(trip));
    }

    private String normalizeTripTitle(String title, String destination) {
        if (title == null || title.isBlank()) {
            return destination.trim() + " 여행";
        }
        String normalizedTitle = title.trim();
        if (normalizedTitle.length() > 100) {
            throw new IllegalArgumentException("Trip title must be less than or equal to 100 characters.");
        }
        return normalizedTitle;
    }

    public List<TripResponse> searchTripsByOwnerId(Long ownerId) {
        if (ownerId == null) {
            throw new IllegalArgumentException("Trip ownerId is required.");
        }

        return tripRepository.findByOwnerIdOrderByTripIdDesc(ownerId)
                .stream()
                .map(TripResponse::from)
                .toList();
    }

    public List<TripResponse> searchTripsByOwnerId(
            Long ownerId,
            String destination,
            TripConcept concept,
            LocalDate startDateFrom,
            LocalDate startDateTo,
            LocalDate endDateFrom,
            LocalDate endDateTo
    ) {
        if (ownerId == null) {
            throw new IllegalArgumentException("Trip ownerId is required.");
        }
        validateDateRange("startDate", startDateFrom, startDateTo);
        validateDateRange("endDate", endDateFrom, endDateTo);

        String normalizedDestination = normalizeKeyword(destination);

        return tripRepository.searchTripsByOwnerId(
                        ownerId,
                        normalizedDestination,
                        concept,
                        startDateFrom,
                        startDateTo,
                        endDateFrom,
                        endDateTo,
                        Sort.by(Sort.Direction.DESC, "tripId")
                )
                .stream()
                .map(TripResponse::from)
                .toList();
    }

    public List<TripResponse> searchTrips(
            String destination,
            TripConcept concept,
            LocalDate startDateFrom,
            LocalDate startDateTo,
            LocalDate endDateFrom,
            LocalDate endDateTo
    ) {
        validateDateRange("startDate", startDateFrom, startDateTo);
        validateDateRange("endDate", endDateFrom, endDateTo);

        String normalizedDestination = normalizeKeyword(destination);

        return tripRepository.searchTrips(
                        normalizedDestination,
                        concept,
                        startDateFrom,
                        startDateTo,
                        endDateFrom,
                        endDateTo,
                        Sort.by(Sort.Direction.DESC, "tripId")
                )
                .stream()
                .map(TripResponse::from)
                .toList();
    }

    public TripDetailResponse getTrip(Long tripId) {
        return getTrip(tripId, null);
    }

    public TripDetailResponse getTrip(Long tripId, Long ownerId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));
        validateTripOwner(trip, ownerId);
        List<ItineraryResponse> itineraries = itineraryRepository
                .findByTrip_TripIdOrderByDayNoAscOrderNoAsc(tripId)
                .stream()
                .map(ItineraryResponse::from)
                .toList();

        return TripDetailResponse.from(trip, itineraries);
    }

    @Transactional
    public TripDetailResponse copyPublicTrip(Long tripId, Long ownerId) {
        if (ownerId == null) {
            throw new IllegalArgumentException("Trip copy ownerId is required.");
        }

        Trip sourceTrip = findPublicTrip(tripId);
        Trip copiedTrip = tripRepository.save(Trip.create(
                createCopiedTripTitle(sourceTrip.getTitle()),
                sourceTrip.getDestination(),
                sourceTrip.getStartDate(),
                sourceTrip.getEndDate(),
                sourceTrip.getDailyStartTime(),
                sourceTrip.getDailyEndTime(),
                sourceTrip.getConcept(),
                sourceTrip.getTransportation(),
                sourceTrip.getLastAccommodationArea(),
                ownerId
        ));

        List<Itinerary> copiedItineraries = itineraryRepository
                .findByTrip_TripIdOrderByDayNoAscOrderNoAsc(tripId)
                .stream()
                .map(source -> Itinerary.create(
                        copiedTrip,
                        source.getPlace(),
                        source.getDayNo(),
                        source.getOrderNo(),
                        source.getStartTime(),
                        source.getEndTime(),
                        source.getTravelMinutesFromPrevious(),
                        source.getReason()
                ))
                .toList();
        List<ItineraryResponse> copiedItineraryResponses = itineraryRepository.saveAll(copiedItineraries)
                .stream()
                .map(ItineraryResponse::from)
                .toList();

        return TripDetailResponse.from(copiedTrip, copiedItineraryResponses);
    }

    public List<TripResponse> searchPublicTrips(
            String destination,
            TripConcept concept,
            LocalDate startDateFrom,
            LocalDate startDateTo,
            LocalDate endDateFrom,
            LocalDate endDateTo
    ) {
        return searchPublicTrips(
                destination,
                concept,
                startDateFrom,
                startDateTo,
                endDateFrom,
                endDateTo,
                null,
                PublicTripSort.LATEST
        );
    }

    public List<TripResponse> searchPublicTrips(
            String destination,
            TripConcept concept,
            LocalDate startDateFrom,
            LocalDate startDateTo,
            LocalDate endDateFrom,
            LocalDate endDateTo,
            PublicTripSort publicTripSort
    ) {
        return searchPublicTrips(
                destination,
                concept,
                startDateFrom,
                startDateTo,
                endDateFrom,
                endDateTo,
                null,
                publicTripSort
        );
    }

    public List<TripResponse> searchPublicTrips(
            String destination,
            TripConcept concept,
            LocalDate startDateFrom,
            LocalDate startDateTo,
            LocalDate endDateFrom,
            LocalDate endDateTo,
            Integer nights,
            PublicTripSort publicTripSort
    ) {
        validateDateRange("startDate", startDateFrom, startDateTo);
        validateDateRange("endDate", endDateFrom, endDateTo);
        validateNights(nights);

        String normalizedDestination = normalizeKeyword(destination);
        Sort sort = publicTripSort(publicTripSort);

        return tripRepository.searchTripsByVisibility(
                        TripVisibility.PUBLIC,
                        normalizedDestination,
                        concept,
                        startDateFrom,
                        startDateTo,
                        endDateFrom,
                        endDateTo,
                        nights,
                        sort
                )
                .stream()
                .map(TripResponse::from)
                .toList();
    }

    public PageResponse<PublicTripResponse> searchPublicTripPage(
            String destination,
            TripConcept concept,
            LocalDate startDateFrom,
            LocalDate startDateTo,
            LocalDate endDateFrom,
            LocalDate endDateTo,
            Integer nights,
            PublicTripSort publicTripSort,
            Integer page,
            Integer size
    ) {
        return searchPublicTripPage(
                destination,
                concept,
                startDateFrom,
                startDateTo,
                endDateFrom,
                endDateTo,
                nights,
                publicTripSort,
                page,
                size,
                null
        );
    }

    public PageResponse<PublicTripResponse> searchPublicTripPage(
            String destination,
            TripConcept concept,
            LocalDate startDateFrom,
            LocalDate startDateTo,
            LocalDate endDateFrom,
            LocalDate endDateTo,
            Integer nights,
            PublicTripSort publicTripSort,
            Integer page,
            Integer size,
            Long currentUserId
    ) {
        validateDateRange("startDate", startDateFrom, startDateTo);
        validateDateRange("endDate", endDateFrom, endDateTo);
        validateNights(nights);

        String normalizedDestination = normalizeKeyword(destination);
        Pageable pageable = PageRequest.of(
                normalizedPublicTripPage(page),
                normalizedPublicTripPageSize(size),
                publicTripSort(publicTripSort)
        );

        Page<Trip> tripPage = tripRepository.searchTripsByVisibility(
                        TripVisibility.PUBLIC,
                        normalizedDestination,
                        concept,
                        startDateFrom,
                        startDateTo,
                        endDateFrom,
                        endDateTo,
                        nights,
                        pageable
                );
        if (tripPage.isEmpty()) {
            return PageResponse.empty(tripPage);
        }

        Set<Long> likedTripIds = findLikedTripIds(currentUserId, tripPage.getContent());
        Map<Long, TripAuthorResponse> authorsByMemberId = findAuthorsByOwnerId(tripPage.getContent());
        Map<Long, List<TripPlaceSummaryResponse>> representativePlacesByTripId = findRepresentativePlacesByTripId(
                tripPage.getContent()
        );
        Page<PublicTripResponse> responsePage = tripPage
                .map(trip -> PublicTripResponse.from(
                        trip,
                        likedTripIds.contains(trip.getTripId()),
                        getAuthor(authorsByMemberId, trip.getOwnerId()),
                        representativePlacesByTripId.getOrDefault(trip.getTripId(), List.of())
                ));

        return PageResponse.from(responsePage);
    }

    @Transactional
    public PublicTripDetailResponse getPublicTrip(Long tripId) {
        return getPublicTrip(tripId, null);
    }

    @Transactional
    public PublicTripDetailResponse getPublicTrip(Long tripId, Long currentUserId) {
        Trip trip = tripRepository.findByTripIdAndVisibility(tripId, TripVisibility.PUBLIC)
                .orElseThrow(() -> new NoSuchElementException("Public trip not found. tripId=" + tripId));
        recordPublicTripViewIfNeeded(trip, currentUserId);
        List<ItineraryResponse> itineraries = itineraryRepository
                .findByTrip_TripIdOrderByDayNoAscOrderNoAsc(tripId)
                .stream()
                .map(ItineraryResponse::from)
                .toList();
        boolean liked = currentUserId != null
                && tripLikeRepository.existsByTrip_TripIdAndUserId(tripId, currentUserId);
        TripAuthorResponse author = findAuthorByOwnerId(trip.getOwnerId());

        return PublicTripDetailResponse.from(trip, itineraries, liked, author);
    }

    public PageResponse<PublicTripResponse> searchLikedPublicTripPage(Long userId, Integer page, Integer size) {
        validateLikeUserId(userId);
        Pageable pageable = PageRequest.of(
                normalizedPublicTripPage(page),
                normalizedPublicTripPageSize(size)
        );

        Page<TripLike> tripLikePage = tripLikeRepository.findByUserIdAndTrip_VisibilityOrderByTrip_TripIdDesc(
                        userId,
                        TripVisibility.PUBLIC,
                        pageable
                );
        if (tripLikePage.isEmpty()) {
            return PageResponse.empty(tripLikePage);
        }

        List<Trip> trips = tripLikePage.getContent()
                .stream()
                .map(TripLike::getTrip)
                .toList();
        Map<Long, List<TripPlaceSummaryResponse>> representativePlacesByTripId = findRepresentativePlacesByTripId(
                trips
        );
        Map<Long, TripAuthorResponse> authorsByMemberId = findAuthorsByOwnerId(trips);
        Page<PublicTripResponse> tripPage = tripLikePage
                .map(TripLike::getTrip)
                .map(trip -> PublicTripResponse.from(
                        trip,
                        true,
                        getAuthor(authorsByMemberId, trip.getOwnerId()),
                        representativePlacesByTripId.getOrDefault(trip.getTripId(), List.of())
                ));

        return PageResponse.from(tripPage);
    }

    @Transactional
    public TripLikeResponse likePublicTrip(Long tripId, Long userId) {
        validateLikeUserId(userId);
        Trip trip = findPublicTrip(tripId);

        if (tripLikeRepository.existsByTrip_TripIdAndUserId(tripId, userId)) {
            throw new IllegalArgumentException("Trip like already exists. tripId=" + tripId + ", userId=" + userId);
        }

        tripLikeRepository.save(TripLike.create(trip, userId));
        trip.increaseLikeCount();
        return new TripLikeResponse(trip.getTripId(), userId, trip.getLikeCount(), true);
    }

    @Transactional
    public TripLikeResponse unlikePublicTrip(Long tripId, Long userId) {
        validateLikeUserId(userId);
        Trip trip = findPublicTrip(tripId);
        TripLike tripLike = tripLikeRepository.findByTrip_TripIdAndUserId(tripId, userId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Trip like not found. tripId=" + tripId + ", userId=" + userId
                ));

        tripLikeRepository.delete(tripLike);
        trip.decreaseLikeCount();
        return new TripLikeResponse(trip.getTripId(), userId, trip.getLikeCount(), false);
    }

    @Transactional
    public TripResponse updateTripVisibility(Long tripId, TripVisibility visibility) {
        return updateTripVisibility(tripId, null, visibility);
    }

    @Transactional
    public TripResponse updateTripTitle(Long tripId, Long ownerId, String title) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));
        validateTripOwner(trip, ownerId);
        trip.changeTitle(title);
        return TripResponse.from(trip);
    }

    @Transactional
    public TripResponse updateTripConditions(Long tripId, Long ownerId, TripConditionUpdateRequest request) {
        validateConditionUpdateRequest(request);

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));
        validateTripOwner(trip, ownerId);

        String lastAccommodationArea = normalizeAccommodationArea(request.lastAccommodationArea());
        boolean conditionsChanged = !Objects.equals(trip.getStartDate(), request.startDate())
                || !Objects.equals(trip.getEndDate(), request.endDate())
                || !Objects.equals(trip.getDailyStartTime(), request.dailyStartTime())
                || !Objects.equals(trip.getDailyEndTime(), request.dailyEndTime())
                || trip.getConcept() != request.concept()
                || !Objects.equals(normalizeAccommodationArea(trip.getLastAccommodationArea()), lastAccommodationArea);
        if (!conditionsChanged) {
            return TripResponse.from(trip);
        }

        trip.changeConditions(
                request.startDate(),
                request.endDate(),
                request.dailyStartTime(),
                request.dailyEndTime(),
                request.concept(),
                lastAccommodationArea
        );

        if (itineraryRepository.existsByTrip_TripId(tripId)) {
            itineraryRepository.deleteByTrip_TripId(tripId);
            trip.changeVisibility(TripVisibility.PRIVATE);
        }

        return TripResponse.from(trip);
    }

    @Transactional
    public TripResponse updateTripVisibility(Long tripId, Long ownerId, TripVisibility visibility) {
        if (visibility == null) {
            throw new IllegalArgumentException("Trip visibility is required.");
        }

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));
        validateTripOwner(trip, ownerId);
        validateCanUpdateVisibility(trip, visibility);
        trip.changeVisibility(visibility);
        return TripResponse.from(trip);
    }

    @Transactional
    public void deleteTrip(Long tripId) {
        deleteTrip(tripId, null);
    }

    @Transactional
    public void deleteTrip(Long tripId, Long ownerId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));
        validateTripOwner(trip, ownerId);

        itineraryRepository.deleteByTrip_TripId(tripId);
        tripRepository.delete(trip);
    }

    private void validateCreateRequest(TripCreateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Trip request is required.");
        }
        if (!isJejuDestination(request.destination())) {
            throw new IllegalArgumentException("Only Jeju trips are supported in MVP.");
        }
        if (request.startDate() == null || request.endDate() == null) {
            throw new IllegalArgumentException("Trip startDate and endDate are required.");
        }
        long nights = ChronoUnit.DAYS.between(request.startDate(), request.endDate());
        if (nights < MIN_NIGHTS || nights > MAX_NIGHTS) {
            throw new IllegalArgumentException("Trip duration must be 1 night 2 days to 3 nights 4 days.");
        }
        if (request.dailyStartTime() == null) {
            throw new IllegalArgumentException("Trip dailyStartTime is required.");
        }
        if (request.dailyEndTime() == null) {
            throw new IllegalArgumentException("Trip dailyEndTime is required.");
        }
        if (!request.dailyStartTime().isBefore(request.dailyEndTime())) {
            throw new IllegalArgumentException("Trip dailyStartTime must be before dailyEndTime.");
        }
        if (request.concept() == null) {
            throw new IllegalArgumentException("Trip concept is required.");
        }
        if (request.transportation() != Transportation.RENT_CAR) {
            throw new IllegalArgumentException("Only RENT_CAR transportation is supported in MVP.");
        }
    }

    private void validateConditionUpdateRequest(TripConditionUpdateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Trip condition request is required.");
        }
        if (request.startDate() == null || request.endDate() == null) {
            throw new IllegalArgumentException("Trip startDate and endDate are required.");
        }
        long nights = ChronoUnit.DAYS.between(request.startDate(), request.endDate());
        if (nights < MIN_NIGHTS || nights > MAX_NIGHTS) {
            throw new IllegalArgumentException("Trip duration must be 1 night 2 days to 3 nights 4 days.");
        }
        if (request.dailyStartTime() == null) {
            throw new IllegalArgumentException("Trip dailyStartTime is required.");
        }
        if (request.dailyEndTime() == null) {
            throw new IllegalArgumentException("Trip dailyEndTime is required.");
        }
        if (!request.dailyStartTime().isBefore(request.dailyEndTime())) {
            throw new IllegalArgumentException("Trip dailyStartTime must be before dailyEndTime.");
        }
        if (request.concept() == null) {
            throw new IllegalArgumentException("Trip concept is required.");
        }
        if (request.lastAccommodationArea() != null && request.lastAccommodationArea().trim().length() > 50) {
            throw new IllegalArgumentException("Trip lastAccommodationArea must be less than or equal to 50 characters.");
        }
    }

    private String normalizeAccommodationArea(String lastAccommodationArea) {
        if (lastAccommodationArea == null || lastAccommodationArea.isBlank()) {
            return null;
        }
        return lastAccommodationArea.trim();
    }

    private void validateTripOwner(Trip trip, Long ownerId) {
        if (ownerId == null) {
            return;
        }
        if (!ownerId.equals(trip.getOwnerId())) {
            throw new IllegalArgumentException("Trip owner does not match. tripId=" + trip.getTripId());
        }
    }

    private void validateCanUpdateVisibility(Trip trip, TripVisibility visibility) {
        if (visibility != TripVisibility.PUBLIC) {
            return;
        }
        if (itineraryRepository.existsByTrip_TripId(trip.getTripId())) {
            List<ItineraryResponse> itineraries = itineraryRepository
                    .findByTrip_TripIdOrderByDayNoAscOrderNoAsc(trip.getTripId())
                    .stream()
                    .map(ItineraryResponse::from)
                    .toList();
            validatePublishableItineraries(trip, itineraries);
            return;
        }

        throw new IllegalArgumentException("Trip must have at least one itinerary before publishing.");
    }

    private void validatePublishableItineraries(Trip trip, List<ItineraryResponse> itineraries) {
        int tripDays = Math.toIntExact(ChronoUnit.DAYS.between(trip.getStartDate(), trip.getEndDate()) + 1);
        List<Integer> missingDayNos = new ArrayList<>();

        for (int dayNo = 1; dayNo <= tripDays; dayNo++) {
            Integer currentDayNo = dayNo;
            List<ItineraryResponse> dayItineraries = itineraries.stream()
                    .filter(itinerary -> currentDayNo.equals(itinerary.dayNo()))
                    .toList();
            if (dayItineraries.isEmpty()) {
                missingDayNos.add(dayNo);
                continue;
            }

        }

        if (!missingDayNos.isEmpty()) {
            throw new IllegalArgumentException(
                    "Trip itinerary must include every trip day before publishing. missingDayNos=" + missingDayNos
            );
        }
    }

    private Trip findPublicTrip(Long tripId) {
        return tripRepository.findByTripIdAndVisibility(tripId, TripVisibility.PUBLIC)
                .orElseThrow(() -> new NoSuchElementException("Public trip not found. tripId=" + tripId));
    }

    private String createCopiedTripTitle(String sourceTitle) {
        String suffix = " 복사본";
        int maxSourceTitleLength = 100 - suffix.length();
        String normalizedSourceTitle = sourceTitle.length() > maxSourceTitleLength
                ? sourceTitle.substring(0, maxSourceTitleLength).stripTrailing()
                : sourceTitle;
        return normalizedSourceTitle + suffix;
    }

    private void validateLikeUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Trip like userId is required.");
        }
    }

    private Set<Long> findLikedTripIds(Long userId, List<Trip> trips) {
        if (userId == null || trips.isEmpty()) {
            return Set.of();
        }

        List<Long> tripIds = trips.stream()
                .map(Trip::getTripId)
                .toList();

        return tripLikeRepository.findByUserIdAndTrip_TripIdIn(userId, tripIds)
                .stream()
                .map(tripLike -> tripLike.getTrip().getTripId())
                .collect(Collectors.toCollection(HashSet::new));
    }

    private void recordPublicTripViewIfNeeded(Trip trip, Long userId) {
        if (userId == null) {
            return;
        }

        LocalDate today = LocalDate.now();
        if (tripViewRepository.existsByTrip_TripIdAndUserIdAndViewDate(trip.getTripId(), userId, today)) {
            return;
        }

        tripViewRepository.save(TripView.create(trip, userId, today));
        trip.increaseViewCount();
    }

    private Map<Long, TripAuthorResponse> findAuthorsByOwnerId(List<Trip> trips) {
        List<Long> ownerIds = trips.stream()
                .map(Trip::getOwnerId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (ownerIds.isEmpty()) {
            return Map.of();
        }

        return memberRepository.findAllById(ownerIds)
                .stream()
                .collect(Collectors.toMap(
                        Member::getMemberId,
                        member -> TripAuthorResponse.from(member)
                ));
    }

    private Map<Long, List<TripPlaceSummaryResponse>> findRepresentativePlacesByTripId(List<Trip> trips) {
        if (trips.isEmpty()) {
            return Map.of();
        }

        List<Long> tripIds = trips.stream()
                .map(Trip::getTripId)
                .toList();

        Map<Long, List<Itinerary>> itinerariesByTripId = itineraryRepository
                .findByTrip_TripIdInOrderByTrip_TripIdAscDayNoAscOrderNoAsc(tripIds)
                .stream()
                .collect(Collectors.groupingBy(itinerary -> itinerary.getTrip().getTripId()));

        return trips.stream()
                .collect(Collectors.toMap(
                        Trip::getTripId,
                        trip -> itinerariesByTripId.getOrDefault(trip.getTripId(), List.<Itinerary>of())
                                .stream()
                                .limit(REPRESENTATIVE_PLACE_LIMIT)
                                .map(itinerary -> TripPlaceSummaryResponse.from(itinerary.getPlace()))
                                .toList()
                ));
    }

    private TripAuthorResponse findAuthorByOwnerId(Long ownerId) {
        if (ownerId == null) {
            return null;
        }

        return memberRepository.findById(ownerId)
                .map(TripAuthorResponse::from)
                .orElse(null);
    }

    private TripAuthorResponse getAuthor(Map<Long, TripAuthorResponse> authorsByMemberId, Long ownerId) {
        if (ownerId == null) {
            return null;
        }

        return authorsByMemberId.get(ownerId);
    }

    private void validateDateRange(String fieldName, LocalDate from, LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException(fieldName + "From must be less than or equal to " + fieldName + "To.");
        }
    }

    private void validateNights(Integer nights) {
        if (nights == null) {
            return;
        }
        if (nights < MIN_NIGHTS || nights > MAX_NIGHTS) {
            throw new IllegalArgumentException("Trip nights must be between 1 and 3.");
        }
    }

    private Sort publicTripSort(PublicTripSort publicTripSort) {
        PublicTripSort normalizedSort = publicTripSort == null ? PublicTripSort.LATEST : publicTripSort;
        return switch (normalizedSort) {
            case LATEST -> Sort.by(Sort.Direction.DESC, "tripId");
            case POPULAR -> Sort.by(
                    Sort.Order.desc("likeCount"),
                    Sort.Order.desc("viewCount"),
                    Sort.Order.desc("tripId")
            );
        };
    }

    private int normalizedPublicTripPage(Integer page) {
        if (page == null) {
            return DEFAULT_PUBLIC_TRIP_PAGE;
        }
        if (page < 0) {
            throw new IllegalArgumentException("Page must be greater than or equal to 0.");
        }
        return page;
    }

    private int normalizedPublicTripPageSize(Integer size) {
        if (size == null) {
            return DEFAULT_PUBLIC_TRIP_PAGE_SIZE;
        }
        if (size < 1 || size > MAX_PUBLIC_TRIP_PAGE_SIZE) {
            throw new IllegalArgumentException("Page size must be between 1 and 50.");
        }
        return size;
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return keyword.trim().toLowerCase(Locale.ROOT);
    }

    private boolean isJejuDestination(String destination) {
        if (destination == null || destination.isBlank()) {
            return false;
        }

        String normalizedDestination = destination.trim();
        return normalizedDestination.equals("제주") || normalizedDestination.equalsIgnoreCase("JEJU");
    }
}
