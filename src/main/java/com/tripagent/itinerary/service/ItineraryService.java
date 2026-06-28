package com.tripagent.itinerary.service;

import com.tripagent.itinerary.domain.Itinerary;
import com.tripagent.itinerary.dto.ItineraryCreateRequest;
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.itinerary.dto.ItineraryUpdateRequest;
import com.tripagent.itinerary.repository.ItineraryRepository;
import com.tripagent.place.domain.Place;
import com.tripagent.place.repository.PlaceRepository;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.repository.TripRepository;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final TripRepository tripRepository;
    private final PlaceRepository placeRepository;

    public ItineraryService(
            ItineraryRepository itineraryRepository,
            TripRepository tripRepository,
            PlaceRepository placeRepository
    ) {
        this.itineraryRepository = itineraryRepository;
        this.tripRepository = tripRepository;
        this.placeRepository = placeRepository;
    }

    @Transactional
    public ItineraryResponse createItinerary(Long tripId, ItineraryCreateRequest request) {
        validateRequest(tripId, request);

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));

        validateItineraryValues(
                trip,
                null,
                request.dayNo(),
                request.orderNo(),
                request.startTime(),
                request.endTime(),
                request.travelMinutesFromPrevious()
        );
        Place place = placeRepository.findById(request.placeId())
                .orElseThrow(() -> new NoSuchElementException("Place not found. placeId=" + request.placeId()));

        Itinerary itinerary = Itinerary.create(
                trip,
                place,
                request.dayNo(),
                request.orderNo(),
                request.startTime(),
                request.endTime(),
                request.travelMinutesFromPrevious(),
                request.reason()
        );

        return ItineraryResponse.from(itineraryRepository.save(itinerary));
    }

    @Transactional
    public ItineraryResponse updateItinerary(Long tripId, Long itineraryId, ItineraryUpdateRequest request) {
        validateUpdateRequest(tripId, itineraryId, request);

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));
        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new NoSuchElementException("Itinerary not found. itineraryId=" + itineraryId));
        validateItineraryBelongsToTrip(itinerary, tripId);

        Long updatedPlaceId = request.placeId() == null ? itinerary.getPlace().getPlaceId() : request.placeId();
        Place updatedPlace = request.placeId() == null
                ? itinerary.getPlace()
                : placeRepository.findById(updatedPlaceId)
                .orElseThrow(() -> new NoSuchElementException("Place not found. placeId=" + updatedPlaceId));
        Integer updatedDayNo = request.dayNo() == null ? itinerary.getDayNo() : request.dayNo();
        Integer updatedOrderNo = request.orderNo() == null ? itinerary.getOrderNo() : request.orderNo();
        LocalTime updatedStartTime = request.startTime() == null
                ? itinerary.getStartTime()
                : request.startTime();
        LocalTime updatedEndTime = request.endTime() == null ? itinerary.getEndTime() : request.endTime();
        Integer updatedTravelMinutesFromPrevious = request.travelMinutesFromPrevious() == null
                ? itinerary.getTravelMinutesFromPrevious()
                : request.travelMinutesFromPrevious();
        String updatedReason = request.reason() == null ? itinerary.getReason() : request.reason();

        validateItineraryValues(
                trip,
                itineraryId,
                updatedDayNo,
                updatedOrderNo,
                updatedStartTime,
                updatedEndTime,
                updatedTravelMinutesFromPrevious
        );

        itinerary.update(
                updatedPlace,
                updatedDayNo,
                updatedOrderNo,
                updatedStartTime,
                updatedEndTime,
                updatedTravelMinutesFromPrevious,
                updatedReason
        );

        return ItineraryResponse.from(itinerary);
    }

    @Transactional
    public void deleteItinerary(Long tripId, Long itineraryId) {
        validateTripAndItineraryIds(tripId, itineraryId);

        if (!tripRepository.existsById(tripId)) {
            throw new NoSuchElementException("Trip not found. tripId=" + tripId);
        }

        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new NoSuchElementException("Itinerary not found. itineraryId=" + itineraryId));
        validateItineraryBelongsToTrip(itinerary, tripId);

        itineraryRepository.delete(itinerary);
    }

    public List<ItineraryResponse> getItineraries(Long tripId) {
        if (!tripRepository.existsById(tripId)) {
            throw new NoSuchElementException("Trip not found. tripId=" + tripId);
        }

        return itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(tripId).stream()
                .map(ItineraryResponse::from)
                .toList();
    }

    private void validateRequest(Long tripId, ItineraryCreateRequest request) {
        if (tripId == null) {
            throw new IllegalArgumentException("Trip id is required.");
        }
        if (request == null) {
            throw new IllegalArgumentException("Itinerary request is required.");
        }
        if (request.placeId() == null) {
            throw new IllegalArgumentException("Place id is required.");
        }
        if (request.dayNo() == null || request.dayNo() < 1) {
            throw new IllegalArgumentException("Itinerary dayNo must be greater than or equal to 1.");
        }
        if (request.orderNo() == null || request.orderNo() < 1) {
            throw new IllegalArgumentException("Itinerary orderNo must be greater than or equal to 1.");
        }
        if (request.startTime() == null || request.endTime() == null) {
            throw new IllegalArgumentException("Itinerary startTime and endTime are required.");
        }
        if (!request.startTime().isBefore(request.endTime())) {
            throw new IllegalArgumentException("Itinerary startTime must be before endTime.");
        }
        if (request.travelMinutesFromPrevious() == null || request.travelMinutesFromPrevious() < 0) {
            throw new IllegalArgumentException("Itinerary travelMinutesFromPrevious must be greater than or equal to 0.");
        }
        if (Integer.valueOf(1).equals(request.orderNo()) && request.travelMinutesFromPrevious() != 0) {
            throw new IllegalArgumentException("First itinerary item of each day must have travelMinutesFromPrevious 0.");
        }
    }

    private void validateItineraryValues(
            Trip trip,
            Long excludedItineraryId,
            Integer dayNo,
            Integer orderNo,
            LocalTime startTime,
            LocalTime endTime,
            Integer travelMinutesFromPrevious
    ) {
        validateTimeRange(startTime, endTime);
        validateDayNoInTripPeriod(trip, dayNo);
        validateFirstTravelMinutes(orderNo, travelMinutesFromPrevious);
        validateFirstStartTime(trip, orderNo, startTime);
        validateDailyEndTime(trip, endTime);
        validateNoDuplicatedOrder(trip.getTripId(), excludedItineraryId, dayNo, orderNo);
        validateNoTimeOverlap(trip.getTripId(), excludedItineraryId, dayNo, startTime, endTime);
    }

    private void validateTimeRange(LocalTime startTime, LocalTime endTime) {
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Itinerary startTime must be before endTime.");
        }
    }

    private void validateFirstTravelMinutes(Integer orderNo, Integer travelMinutesFromPrevious) {
        if (Integer.valueOf(1).equals(orderNo) && travelMinutesFromPrevious != 0) {
            throw new IllegalArgumentException("First itinerary item of each day must have travelMinutesFromPrevious 0.");
        }
    }

    private void validateFirstStartTime(Trip trip, Integer orderNo, LocalTime startTime) {
        if (Integer.valueOf(1).equals(orderNo) && startTime.isBefore(trip.getDailyStartTime())) {
            throw new IllegalArgumentException(
                    "First itinerary item of each day must start at or after trip dailyStartTime."
            );
        }
    }

    private void validateDailyEndTime(Trip trip, LocalTime endTime) {
        if (endTime.isAfter(trip.getDailyEndTime())) {
            throw new IllegalArgumentException("Itinerary endTime must be at or before trip dailyEndTime.");
        }
    }

    private void validateDayNoInTripPeriod(Trip trip, Integer dayNo) {
        long tripDays = ChronoUnit.DAYS.between(trip.getStartDate(), trip.getEndDate()) + 1;
        if (dayNo > tripDays) {
            throw new IllegalArgumentException(
                    "Itinerary dayNo must be within trip period. maxDayNo=" + tripDays
            );
        }
    }

    private void validateNoDuplicatedOrder(
            Long tripId,
            Long excludedItineraryId,
            Integer dayNo,
            Integer orderNo
    ) {
        if (excludedItineraryId == null && itineraryRepository.existsByTrip_TripIdAndDayNoAndOrderNo(
                tripId,
                dayNo,
                orderNo
        )) {
            throw new IllegalArgumentException("Itinerary dayNo and orderNo already exist in this trip.");
        }

        List<Itinerary> sameDayItineraries = itineraryRepository.findByTrip_TripIdAndDayNo(tripId, dayNo);

        for (Itinerary itinerary : sameDayItineraries) {
            if (isExcludedItinerary(itinerary, excludedItineraryId)) {
                continue;
            }
            if (itinerary.getOrderNo().equals(orderNo)) {
                throw new IllegalArgumentException("Itinerary dayNo and orderNo already exist in this trip.");
            }
        }
    }

    private void validateNoTimeOverlap(
            Long tripId,
            Long excludedItineraryId,
            Integer dayNo,
            LocalTime startTime,
            LocalTime endTime
    ) {
        List<Itinerary> sameDayItineraries = itineraryRepository.findByTrip_TripIdAndDayNo(
                tripId,
                dayNo
        );

        for (Itinerary itinerary : sameDayItineraries) {
            if (isExcludedItinerary(itinerary, excludedItineraryId)) {
                continue;
            }
            boolean overlaps = startTime.isBefore(itinerary.getEndTime())
                    && itinerary.getStartTime().isBefore(endTime);
            if (overlaps) {
                throw new IllegalArgumentException("Itinerary time overlaps with existing itinerary.");
            }
        }
    }

    private boolean isExcludedItinerary(Itinerary itinerary, Long excludedItineraryId) {
        return excludedItineraryId != null && excludedItineraryId.equals(itinerary.getItineraryId());
    }

    private void validateUpdateRequest(Long tripId, Long itineraryId, ItineraryUpdateRequest request) {
        validateTripAndItineraryIds(tripId, itineraryId);
        if (request == null) {
            throw new IllegalArgumentException("Itinerary update request is required.");
        }
        if (request.dayNo() != null && request.dayNo() < 1) {
            throw new IllegalArgumentException("Itinerary dayNo must be greater than or equal to 1.");
        }
        if (request.orderNo() != null && request.orderNo() < 1) {
            throw new IllegalArgumentException("Itinerary orderNo must be greater than or equal to 1.");
        }
        if (request.travelMinutesFromPrevious() != null && request.travelMinutesFromPrevious() < 0) {
            throw new IllegalArgumentException("Itinerary travelMinutesFromPrevious must be greater than or equal to 0.");
        }
    }

    private void validateTripAndItineraryIds(Long tripId, Long itineraryId) {
        if (tripId == null) {
            throw new IllegalArgumentException("Trip id is required.");
        }
        if (itineraryId == null) {
            throw new IllegalArgumentException("Itinerary id is required.");
        }
    }

    private void validateItineraryBelongsToTrip(Itinerary itinerary, Long tripId) {
        if (!itinerary.getTrip().getTripId().equals(tripId)) {
            throw new IllegalArgumentException("Itinerary does not belong to trip. itineraryId=" + itinerary.getItineraryId());
        }
    }
}
