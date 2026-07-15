package com.tripagent.trip.service;

import com.tripagent.accommodation.domain.Accommodation;
import com.tripagent.accommodation.repository.AccommodationRepository;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripAccommodation;
import com.tripagent.trip.dto.TripAccommodationItemRequest;
import com.tripagent.trip.dto.TripAccommodationReplaceRequest;
import com.tripagent.trip.dto.TripAccommodationResponse;
import com.tripagent.trip.repository.TripAccommodationRepository;
import com.tripagent.trip.repository.TripRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TripAccommodationService {

    private final TripRepository tripRepository;
    private final AccommodationRepository accommodationRepository;
    private final TripAccommodationRepository tripAccommodationRepository;

    public TripAccommodationService(
            TripRepository tripRepository,
            AccommodationRepository accommodationRepository,
            TripAccommodationRepository tripAccommodationRepository
    ) {
        this.tripRepository = tripRepository;
        this.accommodationRepository = accommodationRepository;
        this.tripAccommodationRepository = tripAccommodationRepository;
    }

    public List<TripAccommodationResponse> getTripAccommodations(Long tripId, Long ownerId) {
        Trip trip = getOwnedTrip(tripId, ownerId);
        return tripAccommodationRepository.findByTripIdOrderByStayDate(trip.getTripId()).stream()
                .map(TripAccommodationResponse::from)
                .toList();
    }

    @Transactional
    public List<TripAccommodationResponse> replaceTripAccommodations(
            Long tripId,
            Long ownerId,
            TripAccommodationReplaceRequest request
    ) {
        Trip trip = getOwnedTrip(tripId, ownerId);
        List<TripAccommodationItemRequest> items = validateAndSortItems(trip, request);
        Map<Long, Accommodation> accommodationsById = findActiveAccommodations(items);

        List<TripAccommodation> tripAccommodations = items.stream()
                .map(item -> TripAccommodation.create(
                        trip,
                        accommodationsById.get(item.accommodationId()),
                        item.stayDate()
                ))
                .toList();

        tripAccommodationRepository.deleteByTripId(tripId);
        return tripAccommodationRepository.saveAll(tripAccommodations).stream()
                .sorted(Comparator.comparing(TripAccommodation::getStayDate))
                .map(TripAccommodationResponse::from)
                .toList();
    }

    private Trip getOwnedTrip(Long tripId, Long ownerId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));
        if (ownerId != null && !ownerId.equals(trip.getOwnerId())) {
            throw new IllegalArgumentException("Trip owner does not match. tripId=" + tripId);
        }
        return trip;
    }

    private List<TripAccommodationItemRequest> validateAndSortItems(
            Trip trip,
            TripAccommodationReplaceRequest request
    ) {
        if (request == null || request.accommodations() == null) {
            throw new IllegalArgumentException("Trip accommodations are required.");
        }

        Set<LocalDate> stayDates = new HashSet<>();
        List<TripAccommodationItemRequest> items = new ArrayList<>(request.accommodations());
        for (TripAccommodationItemRequest item : items) {
            if (item == null || item.stayDate() == null || item.accommodationId() == null) {
                throw new IllegalArgumentException("Trip accommodation stayDate and accommodationId are required.");
            }
            if (item.stayDate().isBefore(trip.getStartDate()) || !item.stayDate().isBefore(trip.getEndDate())) {
                throw new IllegalArgumentException(
                        "Trip accommodation stayDate must be within trip nights. stayDate=" + item.stayDate()
                );
            }
            if (!stayDates.add(item.stayDate())) {
                throw new IllegalArgumentException(
                        "Trip accommodations must not contain duplicated stayDate. stayDate=" + item.stayDate()
                );
            }
        }
        items.sort(Comparator.comparing(TripAccommodationItemRequest::stayDate));
        return items;
    }

    private Map<Long, Accommodation> findActiveAccommodations(List<TripAccommodationItemRequest> items) {
        Set<Long> accommodationIds = items.stream()
                .map(TripAccommodationItemRequest::accommodationId)
                .collect(java.util.stream.Collectors.toSet());
        Map<Long, Accommodation> accommodationsById = new HashMap<>();
        accommodationRepository.findAllById(accommodationIds).forEach(accommodation ->
                accommodationsById.put(accommodation.getAccommodationId(), accommodation)
        );

        for (Long accommodationId : accommodationIds) {
            Accommodation accommodation = accommodationsById.get(accommodationId);
            if (accommodation == null || !Boolean.TRUE.equals(accommodation.getUseYn())) {
                throw new NoSuchElementException(
                        "Active accommodation not found. accommodationId=" + accommodationId
                );
            }
        }
        return accommodationsById;
    }
}
