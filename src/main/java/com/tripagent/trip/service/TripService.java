package com.tripagent.trip.service;

import com.tripagent.itinerary.repository.ItineraryRepository;
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.dto.TripCreateRequest;
import com.tripagent.trip.dto.TripDetailResponse;
import com.tripagent.trip.dto.TripResponse;
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

    public TripService(
            TripRepository tripRepository,
            ItineraryRepository itineraryRepository
    ) {
        this.tripRepository = tripRepository;
        this.itineraryRepository = itineraryRepository;
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
                request.lastAccommodationArea()
        );

        return TripResponse.from(tripRepository.save(trip));
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

        return tripRepository.findAll(Sort.by(Sort.Direction.DESC, "tripId"))
                .stream()
                .filter(trip -> normalizedDestination == null
                        || trip.getDestination().toLowerCase(Locale.ROOT).contains(normalizedDestination))
                .filter(trip -> concept == null || trip.getConcept() == concept)
                .filter(trip -> startDateFrom == null || !trip.getStartDate().isBefore(startDateFrom))
                .filter(trip -> startDateTo == null || !trip.getStartDate().isAfter(startDateTo))
                .filter(trip -> endDateFrom == null || !trip.getEndDate().isBefore(endDateFrom))
                .filter(trip -> endDateTo == null || !trip.getEndDate().isAfter(endDateTo))
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

    private void validateDateRange(String fieldName, LocalDate from, LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException(fieldName + "From must be less than or equal to " + fieldName + "To.");
        }
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
