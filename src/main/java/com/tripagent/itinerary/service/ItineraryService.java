package com.tripagent.itinerary.service;

import com.tripagent.itinerary.domain.Itinerary;
import com.tripagent.itinerary.dto.ItineraryCreateRequest;
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.itinerary.repository.ItineraryRepository;
import com.tripagent.place.domain.Place;
import com.tripagent.place.repository.PlaceRepository;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.repository.TripRepository;
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
        Place place = placeRepository.findById(request.placeId())
                .orElseThrow(() -> new NoSuchElementException("Place not found. placeId=" + request.placeId()));

        if (itineraryRepository.existsByTrip_TripIdAndDayNoAndOrderNo(
                tripId,
                request.dayNo(),
                request.orderNo()
        )) {
            throw new IllegalArgumentException("Itinerary dayNo and orderNo already exist in this trip.");
        }

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
    }
}
