package com.tripagent.trip.service;

import com.tripagent.itinerary.repository.ItineraryRepository;
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.domain.TripLike;
import com.tripagent.trip.domain.TripVisibility;
import com.tripagent.trip.dto.PublicTripSort;
import com.tripagent.trip.dto.TripCreateRequest;
import com.tripagent.trip.dto.TripDetailResponse;
import com.tripagent.trip.dto.TripLikeResponse;
import com.tripagent.trip.dto.TripResponse;
import com.tripagent.trip.repository.TripLikeRepository;
import com.tripagent.trip.repository.TripRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TripService {

    private static final long MIN_NIGHTS = 1;
    private static final long MAX_NIGHTS = 3;

    private final TripRepository tripRepository;
    private final ItineraryRepository itineraryRepository;
    private final TripLikeRepository tripLikeRepository;

    public TripService(
            TripRepository tripRepository,
            ItineraryRepository itineraryRepository,
            TripLikeRepository tripLikeRepository
    ) {
        this.tripRepository = tripRepository;
        this.itineraryRepository = itineraryRepository;
        this.tripLikeRepository = tripLikeRepository;
    }

    @Transactional
    public TripResponse createTrip(TripCreateRequest request) {
        validateCreateRequest(request);

        Trip trip = Trip.create(
                request.destination(),
                request.startDate(),
                request.endDate(),
                request.dailyStartTime(),
                request.dailyEndTime(),
                request.concept(),
                request.transportation(),
                request.lastAccommodationArea(),
                request.ownerId()
        );

        return TripResponse.from(tripRepository.save(trip));
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
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));
        List<ItineraryResponse> itineraries = itineraryRepository
                .findByTrip_TripIdOrderByDayNoAscOrderNoAsc(tripId)
                .stream()
                .map(ItineraryResponse::from)
                .toList();

        return TripDetailResponse.from(trip, itineraries);
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

    public TripDetailResponse getPublicTrip(Long tripId) {
        Trip trip = tripRepository.findByTripIdAndVisibility(tripId, TripVisibility.PUBLIC)
                .orElseThrow(() -> new NoSuchElementException("Public trip not found. tripId=" + tripId));
        List<ItineraryResponse> itineraries = itineraryRepository
                .findByTrip_TripIdOrderByDayNoAscOrderNoAsc(tripId)
                .stream()
                .map(ItineraryResponse::from)
                .toList();

        return TripDetailResponse.from(trip, itineraries);
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
    public TripResponse updateTripVisibility(Long tripId, Long ownerId, TripVisibility visibility) {
        if (visibility == null) {
            throw new IllegalArgumentException("Trip visibility is required.");
        }

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));
        validateTripOwner(trip, ownerId);
        trip.changeVisibility(visibility);
        return TripResponse.from(trip);
    }

    @Transactional
    public void deleteTrip(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));

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

    private void validateTripOwner(Trip trip, Long ownerId) {
        if (ownerId == null) {
            return;
        }
        if (!ownerId.equals(trip.getOwnerId())) {
            throw new IllegalArgumentException("Trip owner does not match. tripId=" + trip.getTripId());
        }
    }

    private Trip findPublicTrip(Long tripId) {
        return tripRepository.findByTripIdAndVisibility(tripId, TripVisibility.PUBLIC)
                .orElseThrow(() -> new NoSuchElementException("Public trip not found. tripId=" + tripId));
    }

    private void validateLikeUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Trip like userId is required.");
        }
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
            case POPULAR -> Sort.by(Sort.Order.desc("likeCount"), Sort.Order.desc("tripId"));
        };
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
