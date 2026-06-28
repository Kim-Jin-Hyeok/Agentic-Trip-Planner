package com.tripagent.itinerary.service;

import com.tripagent.ai.llm.LlmClient;
import com.tripagent.ai.llm.LlmItineraryResponseConverter;
import com.tripagent.ai.llm.dto.LlmItineraryItemResponse;
import com.tripagent.ai.llm.parser.LlmItineraryJsonParser;
import com.tripagent.ai.prompt.ItineraryPromptGenerator;
import com.tripagent.ai.validator.CandidatePlaceValidator;
import com.tripagent.itinerary.dto.ItineraryCreateRequest;
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.itinerary.repository.ItineraryRepository;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.service.PlaceService;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.repository.TripRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ItineraryGenerateService {

    private final TripRepository tripRepository;
    private final PlaceService placeService;
    private final ItineraryPromptGenerator itineraryPromptGenerator;
    private final LlmClient llmClient;
    private final LlmItineraryJsonParser llmItineraryJsonParser;
    private final LlmItineraryResponseConverter llmItineraryResponseConverter;
    private final CandidatePlaceValidator candidatePlaceValidator;
    private final ItineraryService itineraryService;
    private final ItineraryRepository itineraryRepository;

    public ItineraryGenerateService(
            TripRepository tripRepository,
            PlaceService placeService,
            ItineraryPromptGenerator itineraryPromptGenerator,
            LlmClient llmClient,
            LlmItineraryJsonParser llmItineraryJsonParser,
            LlmItineraryResponseConverter llmItineraryResponseConverter,
            CandidatePlaceValidator candidatePlaceValidator,
            ItineraryService itineraryService,
            ItineraryRepository itineraryRepository
    ) {
        this.tripRepository = tripRepository;
        this.placeService = placeService;
        this.itineraryPromptGenerator = itineraryPromptGenerator;
        this.llmClient = llmClient;
        this.llmItineraryJsonParser = llmItineraryJsonParser;
        this.llmItineraryResponseConverter = llmItineraryResponseConverter;
        this.candidatePlaceValidator = candidatePlaceValidator;
        this.itineraryService = itineraryService;
        this.itineraryRepository = itineraryRepository;
    }

    @Transactional
    public List<ItineraryResponse> generateItineraries(Long tripId) {
        validateTripId(tripId);
        if (itineraryRepository.existsByTrip_TripId(tripId)) {
            throw new IllegalArgumentException("Itinerary already exists for this trip.");
        }

        return generateDraftItineraries(tripId).stream()
                .map(request -> itineraryService.createItinerary(tripId, request))
                .toList();
    }

    @Transactional
    public List<ItineraryResponse> regenerateItineraries(Long tripId) {
        validateTripId(tripId);

        List<ItineraryCreateRequest> createRequests = generateDraftItineraries(tripId);
        itineraryRepository.deleteByTrip_TripId(tripId);

        return createRequests.stream()
                .map(request -> itineraryService.createItinerary(tripId, request))
                .toList();
    }

    public List<ItineraryCreateRequest> generateDraftItineraries(Long tripId) {
        validateTripId(tripId);

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));
        List<PlaceResponse> candidatePlaces = placeService.findCandidatePlaces(trip.getConcept());
        String prompt = itineraryPromptGenerator.generate(trip, candidatePlaces);
        String rawResponse = llmClient.generate(prompt);
        List<LlmItineraryItemResponse> parsedItems = llmItineraryJsonParser.parse(rawResponse);
        List<ItineraryCreateRequest> createRequests = llmItineraryResponseConverter.toCreateRequests(parsedItems);
        List<Long> selectedPlaceIds = createRequests.stream()
                .map(ItineraryCreateRequest::placeId)
                .toList();

        candidatePlaceValidator.validatePlaceIds(candidatePlaces, selectedPlaceIds);
        validateFirstTravelMinutes(createRequests);
        validateFirstStartTimes(trip, createRequests);

        return createRequests;
    }

    private void validateFirstTravelMinutes(List<ItineraryCreateRequest> createRequests) {
        for (ItineraryCreateRequest request : createRequests) {
            if (Integer.valueOf(1).equals(request.orderNo())
                    && !Integer.valueOf(0).equals(request.travelMinutesFromPrevious())) {
                throw new IllegalArgumentException(
                        "First itinerary item of each day must have travelMinutesFromPrevious 0. dayNo="
                                + request.dayNo()
                );
            }
        }
    }

    private void validateFirstStartTimes(Trip trip, List<ItineraryCreateRequest> createRequests) {
        for (ItineraryCreateRequest request : createRequests) {
            if (Integer.valueOf(1).equals(request.orderNo())
                    && request.startTime().isBefore(trip.getDailyStartTime())) {
                throw new IllegalArgumentException(
                        "First itinerary item of each day must start at or after trip dailyStartTime. dayNo="
                                + request.dayNo()
                );
            }
        }
    }

    private void validateTripId(Long tripId) {
        if (tripId == null) {
            throw new IllegalArgumentException("Trip id is required.");
        }
    }
}
