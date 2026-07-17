package com.tripagent.trip.service;

import com.tripagent.accommodation.domain.Accommodation;
import com.tripagent.accommodation.repository.AccommodationRepository;
import com.tripagent.itinerary.domain.Itinerary;
import com.tripagent.itinerary.repository.ItineraryRepository;
import com.tripagent.place.domain.Place;
import com.tripagent.route.RouteCalculationAdapter;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripAccommodation;
import com.tripagent.trip.dto.TripAccommodationItemRequest;
import com.tripagent.trip.dto.TripAccommodationReplaceRequest;
import com.tripagent.trip.dto.TripAccommodationResponse;
import com.tripagent.trip.repository.TripAccommodationRepository;
import com.tripagent.trip.repository.TripRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TripAccommodationService {

    private final TripRepository tripRepository;
    private final AccommodationRepository accommodationRepository;
    private final TripAccommodationRepository tripAccommodationRepository;
    private final ItineraryRepository itineraryRepository;
    private final RouteCalculationAdapter routeCalculationAdapter;

    public TripAccommodationService(
            TripRepository tripRepository,
            AccommodationRepository accommodationRepository,
            TripAccommodationRepository tripAccommodationRepository,
            ItineraryRepository itineraryRepository,
            RouteCalculationAdapter routeCalculationAdapter
    ) {
        this.tripRepository = tripRepository;
        this.accommodationRepository = accommodationRepository;
        this.tripAccommodationRepository = tripAccommodationRepository;
        this.itineraryRepository = itineraryRepository;
        this.routeCalculationAdapter = routeCalculationAdapter;
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
        List<TripAccommodation> existingTripAccommodations =
                tripAccommodationRepository.findByTripIdOrderByStayDate(tripId);
        List<ItineraryScheduleUpdate> scheduleUpdates = buildScheduleUpdates(
                trip,
                items,
                accommodationsById,
                existingTripAccommodations
        );

        List<TripAccommodation> tripAccommodations = items.stream()
                .map(item -> TripAccommodation.create(
                        trip,
                        accommodationsById.get(item.accommodationId()),
                        item.stayDate()
                ))
                .toList();

        applyScheduleUpdates(scheduleUpdates);
        tripAccommodationRepository.deleteByTripId(tripId);
        return tripAccommodationRepository.saveAll(tripAccommodations).stream()
                .sorted(Comparator.comparing(TripAccommodation::getStayDate))
                .map(TripAccommodationResponse::from)
                .toList();
    }

    private List<ItineraryScheduleUpdate> buildScheduleUpdates(
            Trip trip,
            List<TripAccommodationItemRequest> items,
            Map<Long, Accommodation> accommodationsById,
            List<TripAccommodation> existingTripAccommodations
    ) {
        Map<LocalDate, Long> existingAccommodationIdByDate = existingTripAccommodations.stream()
                .collect(java.util.stream.Collectors.toMap(
                        TripAccommodation::getStayDate,
                        tripAccommodation -> tripAccommodation.getAccommodation().getAccommodationId()
                ));
        List<ItineraryScheduleUpdate> scheduleUpdates = new ArrayList<>();

        for (TripAccommodationItemRequest item : items) {
            Long existingAccommodationId = existingAccommodationIdByDate.get(item.stayDate());
            if (Objects.equals(existingAccommodationId, item.accommodationId())) {
                continue;
            }

            Accommodation accommodation = accommodationsById.get(item.accommodationId());
            if (accommodation.getLatitude() == null || accommodation.getLongitude() == null) {
                continue;
            }

            int dayNo = (int) ChronoUnit.DAYS.between(trip.getStartDate(), item.stayDate()) + 2;
            List<Itinerary> dayItineraries = itineraryRepository.findByTrip_TripIdAndDayNo(
                            trip.getTripId(),
                            dayNo
                    ).stream()
                    .sorted(Comparator.comparing(Itinerary::getOrderNo))
                    .toList();
            scheduleUpdates.addAll(calculateScheduleUpdates(trip, accommodation, dayItineraries));
        }
        return scheduleUpdates;
    }

    private List<ItineraryScheduleUpdate> calculateScheduleUpdates(
            Trip trip,
            Accommodation accommodation,
            List<Itinerary> dayItineraries
    ) {
        if (dayItineraries.isEmpty()) {
            return List.of();
        }

        Itinerary firstItinerary = dayItineraries.getFirst();
        Place firstPlace = firstItinerary.getPlace();
        int firstTravelMinutes = routeCalculationAdapter.calculateTravelMinutes(
                accommodation.getLatitude(),
                accommodation.getLongitude(),
                firstPlace.getLatitude(),
                firstPlace.getLongitude()
        );
        LocalTime cursor = trip.getDailyStartTime().plusMinutes(firstTravelMinutes);
        List<ItineraryScheduleUpdate> scheduleUpdates = new ArrayList<>();

        Itinerary previousItinerary = null;
        for (Itinerary itinerary : dayItineraries) {
            int travelMinutes = 0;
            if (previousItinerary != null) {
                travelMinutes = routeCalculationAdapter.calculateTravelMinutes(
                        previousItinerary.getPlace().getLatitude(),
                        previousItinerary.getPlace().getLongitude(),
                        itinerary.getPlace().getLatitude(),
                        itinerary.getPlace().getLongitude()
                );
                cursor = cursor.plusMinutes(travelMinutes);
            }

            int stayMinutes = (int) ChronoUnit.MINUTES.between(
                    itinerary.getStartTime(),
                    itinerary.getEndTime()
            );
            LocalTime endTime = cursor.plusMinutes(stayMinutes);
            if (endTime.isAfter(trip.getDailyEndTime())) {
                throw new IllegalArgumentException(
                        "Itinerary endTime must be at or before trip dailyEndTime. dayNo="
                                + itinerary.getDayNo()
                );
            }
            scheduleUpdates.add(new ItineraryScheduleUpdate(itinerary, cursor, endTime, travelMinutes));
            cursor = endTime;
            previousItinerary = itinerary;
        }
        return scheduleUpdates;
    }

    private void applyScheduleUpdates(List<ItineraryScheduleUpdate> scheduleUpdates) {
        for (ItineraryScheduleUpdate scheduleUpdate : scheduleUpdates) {
            Itinerary itinerary = scheduleUpdate.itinerary();
            itinerary.update(
                    itinerary.getPlace(),
                    itinerary.getDayNo(),
                    itinerary.getOrderNo(),
                    scheduleUpdate.startTime(),
                    scheduleUpdate.endTime(),
                    scheduleUpdate.travelMinutesFromPrevious(),
                    itinerary.getReason()
            );
            itinerary.markAsUserAdjusted();
        }
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

    private record ItineraryScheduleUpdate(
            Itinerary itinerary,
            LocalTime startTime,
            LocalTime endTime,
            Integer travelMinutesFromPrevious
    ) {
    }
}
