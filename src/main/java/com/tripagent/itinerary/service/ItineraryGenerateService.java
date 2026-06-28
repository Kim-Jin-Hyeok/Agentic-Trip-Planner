package com.tripagent.itinerary.service;

import com.tripagent.ai.llm.LlmClient;
import com.tripagent.ai.llm.LlmItineraryResponseConverter;
import com.tripagent.ai.llm.dto.LlmItineraryItemResponse;
import com.tripagent.ai.llm.parser.LlmItineraryJsonParser;
import com.tripagent.ai.prompt.ItineraryPromptGenerator;
import com.tripagent.ai.validator.CandidatePlaceValidator;
import com.tripagent.itinerary.dto.ItineraryCreateRequest;
import com.tripagent.itinerary.dto.ItineraryGenerateRequest;
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.place.dto.PlaceCategory;
import com.tripagent.itinerary.repository.ItineraryRepository;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.service.PlaceService;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.repository.TripRepository;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ItineraryGenerateService {

    private static final int MAX_LLM_VALIDATION_RETRY_COUNT = 2;

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
        return generateItineraries(tripId, null);
    }

    @Transactional
    public List<ItineraryResponse> generateItineraries(Long tripId, ItineraryGenerateRequest request) {
        validateTripId(tripId);
        if (itineraryRepository.existsByTrip_TripId(tripId)) {
            throw new IllegalArgumentException("Itinerary already exists for this trip.");
        }

        return generateDraftItineraries(tripId, request).stream()
                .map(createRequest -> itineraryService.createItinerary(tripId, createRequest))
                .toList();
    }

    @Transactional
    public List<ItineraryResponse> regenerateItineraries(Long tripId) {
        return regenerateItineraries(tripId, null);
    }

    @Transactional
    public List<ItineraryResponse> regenerateItineraries(Long tripId, ItineraryGenerateRequest request) {
        validateTripId(tripId);

        List<ItineraryCreateRequest> createRequests = generateDraftItineraries(tripId, request);
        itineraryRepository.deleteByTrip_TripId(tripId);

        return createRequests.stream()
                .map(createRequest -> itineraryService.createItinerary(tripId, createRequest))
                .toList();
    }

    public List<ItineraryCreateRequest> generateDraftItineraries(Long tripId) {
        return generateDraftItineraries(tripId, null);
    }

    public List<ItineraryCreateRequest> generateDraftItineraries(Long tripId, ItineraryGenerateRequest request) {
        validateTripId(tripId);

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));
        List<PlaceResponse> candidatePlaces = placeService.findCandidatePlaces(trip.getConcept());
        validateGenerateRequest(candidatePlaces, request);
        List<PlaceResponse> filteredCandidatePlaces = filterExcludedCandidatePlaces(candidatePlaces, request);
        String prompt = request == null
                ? itineraryPromptGenerator.generate(trip, filteredCandidatePlaces)
                : itineraryPromptGenerator.generate(trip, filteredCandidatePlaces, request);

        return generateValidatedDraftItineraries(trip, filteredCandidatePlaces, request, prompt);
    }

    private List<ItineraryCreateRequest> generateValidatedDraftItineraries(
            Trip trip,
            List<PlaceResponse> candidatePlaces,
            ItineraryGenerateRequest request,
            String prompt
    ) {
        IllegalArgumentException lastValidationException = null;

        for (int attempt = 0; attempt <= MAX_LLM_VALIDATION_RETRY_COUNT; attempt++) {
            try {
                return generateValidatedDraftItinerary(trip, candidatePlaces, request, prompt);
            } catch (IllegalArgumentException exception) {
                lastValidationException = exception;
            }
        }

        throw lastValidationException;
    }

    private List<ItineraryCreateRequest> generateValidatedDraftItinerary(
            Trip trip,
            List<PlaceResponse> candidatePlaces,
            ItineraryGenerateRequest request,
            String prompt
    ) {
        String rawResponse = llmClient.generate(prompt);
        List<LlmItineraryItemResponse> parsedItems = llmItineraryJsonParser.parse(rawResponse);
        List<ItineraryCreateRequest> createRequests = llmItineraryResponseConverter.toCreateRequests(parsedItems);
        List<Long> selectedPlaceIds = createRequests.stream()
                .map(ItineraryCreateRequest::placeId)
                .toList();

        validateGeneratedPlaceControls(request, selectedPlaceIds);
        candidatePlaceValidator.validatePlaceIds(candidatePlaces, selectedPlaceIds);
        validateDraftItineraries(trip, createRequests);

        return createRequests;
    }

    private void validateDraftItineraries(Trip trip, List<ItineraryCreateRequest> createRequests) {
        validateDayAndOrderPolicies(trip, createRequests);
        validateFirstTravelMinutes(createRequests);
        validateFirstStartTimes(trip, createRequests);
        validateDailyEndTimes(trip, createRequests);
        validateNoDraftTimeOverlap(createRequests);
    }

    private void validateDayAndOrderPolicies(Trip trip, List<ItineraryCreateRequest> createRequests) {
        long tripDays = ChronoUnit.DAYS.between(trip.getStartDate(), trip.getEndDate()) + 1;
        Set<String> dayOrderKeys = new HashSet<>();

        for (ItineraryCreateRequest request : createRequests) {
            if (request.dayNo() == null || request.dayNo() < 1 || request.dayNo() > tripDays) {
                throw new IllegalArgumentException(
                        "Itinerary dayNo must be within trip period. maxDayNo=" + tripDays
                );
            }
            if (request.orderNo() == null || request.orderNo() < 1) {
                throw new IllegalArgumentException("Itinerary orderNo must be greater than or equal to 1.");
            }

            String dayOrderKey = request.dayNo() + ":" + request.orderNo();
            if (!dayOrderKeys.add(dayOrderKey)) {
                throw new IllegalArgumentException(
                        "Itinerary dayNo and orderNo must not be duplicated in generated itinerary. dayNo="
                                + request.dayNo()
                                + ", orderNo="
                                + request.orderNo()
                );
            }
        }
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

    private void validateDailyEndTimes(Trip trip, List<ItineraryCreateRequest> createRequests) {
        for (ItineraryCreateRequest request : createRequests) {
            if (request.endTime().isAfter(trip.getDailyEndTime())) {
                throw new IllegalArgumentException(
                        "Itinerary endTime must be at or before trip dailyEndTime. dayNo="
                                + request.dayNo()
                );
            }
        }
    }

    private void validateNoDraftTimeOverlap(List<ItineraryCreateRequest> createRequests) {
        List<ItineraryCreateRequest> sortedRequests = createRequests.stream()
                .sorted(Comparator.comparing(ItineraryCreateRequest::dayNo)
                        .thenComparing(ItineraryCreateRequest::startTime))
                .toList();

        for (int index = 1; index < sortedRequests.size(); index++) {
            ItineraryCreateRequest previous = sortedRequests.get(index - 1);
            ItineraryCreateRequest current = sortedRequests.get(index);

            if (previous.dayNo().equals(current.dayNo()) && current.startTime().isBefore(previous.endTime())) {
                throw new IllegalArgumentException(
                        "Itinerary time overlaps in generated itinerary. dayNo=" + current.dayNo()
                );
            }
        }
    }

    private void validateTripId(Long tripId) {
        if (tripId == null) {
            throw new IllegalArgumentException("Trip id is required.");
        }
    }

    private void validateGenerateRequest(List<PlaceResponse> candidatePlaces, ItineraryGenerateRequest request) {
        if (request == null) {
            return;
        }

        Set<Long> candidatePlaceIds = candidatePlaces.stream()
                .map(PlaceResponse::placeId)
                .collect(Collectors.toSet());
        List<Long> mustVisitPlaceIds = request.normalizedMustVisitPlaceIds();
        List<Long> excludedPlaceIds = request.normalizedExcludedPlaceIds();

        validateControlPlaceIds("mustVisitPlaceIds", mustVisitPlaceIds, candidatePlaceIds);
        validateControlPlaceIds("excludedPlaceIds", excludedPlaceIds, candidatePlaceIds);
        validatePreferredCategories(request.normalizedPreferredCategories());

        Set<Long> excludedPlaceIdSet = new HashSet<>(excludedPlaceIds);
        for (Long mustVisitPlaceId : mustVisitPlaceIds) {
            if (excludedPlaceIdSet.contains(mustVisitPlaceId)) {
                throw new IllegalArgumentException(
                        "mustVisitPlaceIds and excludedPlaceIds must not contain the same placeId. placeId="
                                + mustVisitPlaceId
                );
            }
        }
    }

    private void validatePreferredCategories(List<PlaceCategory> preferredCategories) {
        Set<PlaceCategory> seenCategories = new HashSet<>();

        for (PlaceCategory preferredCategory : preferredCategories) {
            if (preferredCategory == null) {
                throw new IllegalArgumentException("preferredCategories must not contain null.");
            }
            if (!seenCategories.add(preferredCategory)) {
                throw new IllegalArgumentException(
                        "preferredCategories must not contain duplicated category. category=" + preferredCategory
                );
            }
        }
    }

    private void validateControlPlaceIds(
            String fieldName,
            List<Long> placeIds,
            Set<Long> candidatePlaceIds
    ) {
        Set<Long> seenPlaceIds = new HashSet<>();

        for (Long placeId : placeIds) {
            if (placeId == null) {
                throw new IllegalArgumentException(fieldName + " must not contain null.");
            }
            if (!seenPlaceIds.add(placeId)) {
                throw new IllegalArgumentException(fieldName + " must not contain duplicated placeId. placeId=" + placeId);
            }
            if (!candidatePlaceIds.contains(placeId)) {
                throw new IllegalArgumentException(fieldName + " must be included in candidate places. placeId=" + placeId);
            }
        }
    }

    private List<PlaceResponse> filterExcludedCandidatePlaces(
            List<PlaceResponse> candidatePlaces,
            ItineraryGenerateRequest request
    ) {
        if (request == null || request.normalizedExcludedPlaceIds().isEmpty()) {
            return candidatePlaces;
        }

        Set<Long> excludedPlaceIds = new HashSet<>(request.normalizedExcludedPlaceIds());
        return candidatePlaces.stream()
                .filter(candidatePlace -> !excludedPlaceIds.contains(candidatePlace.placeId()))
                .toList();
    }

    private void validateGeneratedPlaceControls(ItineraryGenerateRequest request, List<Long> selectedPlaceIds) {
        if (!hasPlaceControls(request)) {
            return;
        }

        Set<Long> selectedPlaceIdSet = new HashSet<>(selectedPlaceIds);
        for (Long mustVisitPlaceId : request.normalizedMustVisitPlaceIds()) {
            if (!selectedPlaceIdSet.contains(mustVisitPlaceId)) {
                throw new IllegalArgumentException(
                        "Generated itinerary must include mustVisitPlaceId. placeId=" + mustVisitPlaceId
                );
            }
        }

        for (Long excludedPlaceId : request.normalizedExcludedPlaceIds()) {
            if (selectedPlaceIdSet.contains(excludedPlaceId)) {
                throw new IllegalArgumentException(
                        "Generated itinerary must not include excludedPlaceId. placeId=" + excludedPlaceId
                );
            }
        }
    }

    private boolean hasPlaceControls(ItineraryGenerateRequest request) {
        return request != null
                && (!request.normalizedMustVisitPlaceIds().isEmpty()
                || !request.normalizedExcludedPlaceIds().isEmpty());
    }
}
