package com.tripagent.itinerary.service;

import com.tripagent.ai.validator.CandidatePlaceValidator;
import com.tripagent.itinerary.dto.ItineraryCreateRequest;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.service.PlaceService;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.repository.TripRepository;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ItineraryGenerateService {

    private static final int MOCK_PLACE_LIMIT = 3;
    private static final int DEFAULT_TRAVEL_MINUTES = 30;

    private final TripRepository tripRepository;
    private final PlaceService placeService;
    private final CandidatePlaceValidator candidatePlaceValidator;

    public ItineraryGenerateService(
            TripRepository tripRepository,
            PlaceService placeService,
            CandidatePlaceValidator candidatePlaceValidator
    ) {
        this.tripRepository = tripRepository;
        this.placeService = placeService;
        this.candidatePlaceValidator = candidatePlaceValidator;
    }

    public List<ItineraryCreateRequest> generateDraftItineraries(Long tripId) {
        if (tripId == null) {
            throw new IllegalArgumentException("Trip id is required.");
        }

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));
        List<PlaceResponse> candidatePlaces = placeService.findCandidatePlaces(trip.getConcept());
        List<PlaceResponse> selectedPlaces = selectMockPlaces(candidatePlaces);
        List<Long> selectedPlaceIds = selectedPlaces.stream()
                .map(PlaceResponse::placeId)
                .toList();

        candidatePlaceValidator.validatePlaceIds(candidatePlaces, selectedPlaceIds);

        return createDraftRequests(trip.getDailyStartTime(), selectedPlaces);
    }

    private List<PlaceResponse> selectMockPlaces(List<PlaceResponse> candidatePlaces) {
        return candidatePlaces.stream()
                .limit(MOCK_PLACE_LIMIT)
                .toList();
    }

    private List<ItineraryCreateRequest> createDraftRequests(
            LocalTime dailyStartTime,
            List<PlaceResponse> selectedPlaces
    ) {
        LocalTime nextStartTime = dailyStartTime;
        java.util.ArrayList<ItineraryCreateRequest> draftRequests = new java.util.ArrayList<>();

        for (int index = 0; index < selectedPlaces.size(); index++) {
            PlaceResponse place = selectedPlaces.get(index);
            int travelMinutesFromPrevious = index == 0 ? 0 : DEFAULT_TRAVEL_MINUTES;
            LocalTime startTime = index == 0
                    ? nextStartTime
                    : nextStartTime.plusMinutes(DEFAULT_TRAVEL_MINUTES);
            LocalTime endTime = startTime.plusMinutes(place.avgStayMinutes());

            draftRequests.add(new ItineraryCreateRequest(
                    place.placeId(),
                    1,
                    index + 1,
                    startTime,
                    endTime,
                    travelMinutesFromPrevious,
                    "Mock itinerary item selected from candidate places."
            ));

            nextStartTime = endTime;
        }

        return draftRequests;
    }
}
