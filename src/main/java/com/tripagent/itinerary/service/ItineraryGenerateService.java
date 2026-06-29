package com.tripagent.itinerary.service;

import com.tripagent.ai.llm.LlmClient;
import com.tripagent.ai.llm.LlmException;
import com.tripagent.ai.llm.LlmItineraryResponseConverter;
import com.tripagent.ai.llm.dto.LlmItineraryItemResponse;
import com.tripagent.ai.llm.parser.LlmItineraryJsonParser;
import com.tripagent.ai.prompt.ItineraryPromptGenerator;
import com.tripagent.ai.validator.CandidatePlaceValidator;
import com.tripagent.itinerary.dto.ItineraryCreateRequest;
import com.tripagent.itinerary.dto.ItineraryDayTimeWindowRequest;
import com.tripagent.itinerary.dto.ItineraryGenerateRequest;
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.place.dto.PlaceCategory;
import com.tripagent.itinerary.repository.ItineraryRepository;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.service.PlaceService;
import com.tripagent.route.RouteCalculationAdapter;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.repository.TripRepository;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ItineraryGenerateService {

    private static final Logger log = LoggerFactory.getLogger(ItineraryGenerateService.class);

    private static final int MAX_LLM_VALIDATION_RETRY_COUNT = 2;
    private static final int MAX_LLM_CANDIDATE_PLACE_COUNT = 30;
    private static final String OPERATION_GENERATE = "generate";
    private static final String OPERATION_REGENERATE = "regenerate";
    private static final String OPERATION_GENERATE_DRAFT = "generateDraft";

    private final TripRepository tripRepository;
    private final PlaceService placeService;
    private final ItineraryPromptGenerator itineraryPromptGenerator;
    private final LlmClient llmClient;
    private final LlmItineraryJsonParser llmItineraryJsonParser;
    private final LlmItineraryResponseConverter llmItineraryResponseConverter;
    private final CandidatePlaceValidator candidatePlaceValidator;
    private final ItineraryService itineraryService;
    private final ItineraryRepository itineraryRepository;
    private final RouteCalculationAdapter routeCalculationAdapter;

    public ItineraryGenerateService(
            TripRepository tripRepository,
            PlaceService placeService,
            ItineraryPromptGenerator itineraryPromptGenerator,
            LlmClient llmClient,
            LlmItineraryJsonParser llmItineraryJsonParser,
            LlmItineraryResponseConverter llmItineraryResponseConverter,
            CandidatePlaceValidator candidatePlaceValidator,
            ItineraryService itineraryService,
            ItineraryRepository itineraryRepository,
            RouteCalculationAdapter routeCalculationAdapter
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
        this.routeCalculationAdapter = routeCalculationAdapter;
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

        List<ItineraryCreateRequest> createRequests = generateDraftItineraries(tripId, request, OPERATION_GENERATE);
        return saveGeneratedItineraries(tripId, request, createRequests);
    }

    @Transactional
    public List<ItineraryResponse> regenerateItineraries(Long tripId) {
        return regenerateItineraries(tripId, null);
    }

    @Transactional
    public List<ItineraryResponse> regenerateItineraries(Long tripId, ItineraryGenerateRequest request) {
        validateTripId(tripId);

        List<ItineraryCreateRequest> createRequests = generateDraftItineraries(
                tripId,
                request,
                OPERATION_REGENERATE
        );
        itineraryRepository.deleteByTrip_TripId(tripId);

        return saveGeneratedItineraries(tripId, request, createRequests);
    }

    private List<ItineraryResponse> saveGeneratedItineraries(
            Long tripId,
            ItineraryGenerateRequest request,
            List<ItineraryCreateRequest> createRequests
    ) {
        if (!hasDayTimeWindowOverrides(request)) {
            return createRequests.stream()
                    .map(createRequest -> itineraryService.createItinerary(tripId, createRequest))
                    .toList();
        }

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));
        Map<Integer, DayTimeWindow> dayTimeWindowByDayNo = createDayTimeWindows(trip, request);

        return createRequests.stream()
                .map(createRequest -> {
                    DayTimeWindow dayTimeWindow = dayTimeWindowByDayNo.get(createRequest.dayNo());
                    return itineraryService.createGeneratedItinerary(
                            tripId,
                            createRequest,
                            dayTimeWindow.startTime(),
                            dayTimeWindow.endTime()
                    );
                })
                .toList();
    }

    private boolean hasDayTimeWindowOverrides(ItineraryGenerateRequest request) {
        return request != null && !request.normalizedDayTimeWindows().isEmpty();
    }

    public List<ItineraryCreateRequest> generateDraftItineraries(Long tripId) {
        return generateDraftItineraries(tripId, null, OPERATION_GENERATE_DRAFT);
    }

    public List<ItineraryCreateRequest> generateDraftItineraries(Long tripId, ItineraryGenerateRequest request) {
        return generateDraftItineraries(tripId, request, OPERATION_GENERATE_DRAFT);
    }

    private List<ItineraryCreateRequest> generateDraftItineraries(
            Long tripId,
            ItineraryGenerateRequest request,
            String operation
    ) {
        validateTripId(tripId);

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));
        List<PlaceResponse> candidatePlaces = placeService.findCandidatePlaces(trip.getConcept());
        validateGenerateRequest(trip, candidatePlaces, request);
        List<PlaceResponse> selectedCandidatePlaces = selectLlmCandidatePlaces(trip, candidatePlaces, request);
        validateCandidatePlacesEnoughForTripDays(trip, selectedCandidatePlaces);
        String prompt = request == null
                ? itineraryPromptGenerator.generate(trip, selectedCandidatePlaces)
                : itineraryPromptGenerator.generate(trip, selectedCandidatePlaces, request);

        return generateValidatedDraftItineraries(trip, selectedCandidatePlaces, request, prompt, operation);
    }

    private List<ItineraryCreateRequest> generateValidatedDraftItineraries(
            Trip trip,
            List<PlaceResponse> candidatePlaces,
            ItineraryGenerateRequest request,
            String prompt,
            String operation
    ) {
        IllegalArgumentException lastValidationException = null;
        int maxAttemptCount = MAX_LLM_VALIDATION_RETRY_COUNT + 1;

        for (int attempt = 0; attempt <= MAX_LLM_VALIDATION_RETRY_COUNT; attempt++) {
            int attemptNumber = attempt + 1;
            try {
                List<ItineraryCreateRequest> createRequests = generateValidatedDraftItinerary(
                        trip,
                        candidatePlaces,
                        request,
                        prompt
                );
                logDraftGenerationSuccess(trip, operation, attemptNumber, maxAttemptCount, request, candidatePlaces);
                return createRequests;
            } catch (LlmException exception) {
                logLlmFailure(trip, operation, attemptNumber, maxAttemptCount, request, candidatePlaces, exception);
                throw exception;
            } catch (IllegalArgumentException exception) {
                lastValidationException = exception;
                logValidationFailure(
                        trip,
                        operation,
                        attemptNumber,
                        maxAttemptCount,
                        request,
                        candidatePlaces,
                        exception
                );
            }
        }

        logFinalValidationFailure(trip, operation, maxAttemptCount, request, candidatePlaces, lastValidationException);
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
        validateDayAndOrderPolicies(trip, createRequests);
        createRequests = applyCalculatedTravelMinutes(candidatePlaces, createRequests);
        validateDraftItineraries(trip, request, createRequests);

        return createRequests;
    }

    private List<ItineraryCreateRequest> applyCalculatedTravelMinutes(
            List<PlaceResponse> candidatePlaces,
            List<ItineraryCreateRequest> createRequests
    ) {
        Map<Long, PlaceResponse> candidatePlaceById = candidatePlaces.stream()
                .collect(Collectors.toMap(PlaceResponse::placeId, Function.identity()));
        List<ItineraryCreateRequest> sortedRequests = createRequests.stream()
                .sorted(Comparator.comparing(ItineraryCreateRequest::dayNo)
                        .thenComparing(ItineraryCreateRequest::orderNo))
                .toList();
        List<ItineraryCreateRequest> correctedRequests = new ArrayList<>();
        Integer previousDayNo = null;
        PlaceResponse previousPlace = null;

        for (ItineraryCreateRequest request : sortedRequests) {
            if (!request.dayNo().equals(previousDayNo)) {
                previousPlace = null;
            }

            PlaceResponse currentPlace = candidatePlaceById.get(request.placeId());
            int travelMinutesFromPrevious = routeCalculationAdapter.calculateTravelMinutes(
                    previousPlace,
                    currentPlace
            );
            correctedRequests.add(new ItineraryCreateRequest(
                    request.placeId(),
                    request.dayNo(),
                    request.orderNo(),
                    request.startTime(),
                    request.endTime(),
                    travelMinutesFromPrevious,
                    request.reason()
            ));

            previousDayNo = request.dayNo();
            previousPlace = currentPlace;
        }

        return correctedRequests;
    }

    private void validateDraftItineraries(
            Trip trip,
            ItineraryGenerateRequest request,
            List<ItineraryCreateRequest> createRequests
    ) {
        validateDayAndOrderPolicies(trip, createRequests);
        validateAllTripDaysCovered(trip, createRequests);
        validateFirstTravelMinutes(createRequests);
        validateDayTimeWindowBounds(trip, request, createRequests);
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

    private void validateAllTripDaysCovered(Trip trip, List<ItineraryCreateRequest> createRequests) {
        long tripDays = ChronoUnit.DAYS.between(trip.getStartDate(), trip.getEndDate()) + 1;
        Set<Integer> includedDayNos = createRequests.stream()
                .map(ItineraryCreateRequest::dayNo)
                .collect(Collectors.toSet());
        List<Integer> missingDayNos = new ArrayList<>();

        for (int dayNo = 1; dayNo <= tripDays; dayNo++) {
            if (!includedDayNos.contains(dayNo)) {
                missingDayNos.add(dayNo);
            }
        }

        if (!missingDayNos.isEmpty()) {
            throw new IllegalArgumentException(
                    "Generated itinerary must include at least one item for every trip day. missingDayNos="
                            + missingDayNos
            );
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

    private void validateDayTimeWindowBounds(
            Trip trip,
            ItineraryGenerateRequest request,
            List<ItineraryCreateRequest> createRequests
    ) {
        Map<Integer, DayTimeWindow> dayTimeWindowByDayNo = createDayTimeWindows(trip, request);

        for (ItineraryCreateRequest createRequest : createRequests) {
            DayTimeWindow dayTimeWindow = dayTimeWindowByDayNo.get(createRequest.dayNo());
            if (createRequest.startTime().isBefore(dayTimeWindow.startTime())) {
                if (hasDayTimeWindowOverride(createRequest.dayNo(), request)) {
                    throw new IllegalArgumentException(
                            "Itinerary startTime must be at or after day time window startTime. dayNo="
                                    + createRequest.dayNo()
                    );
                }
                throw new IllegalArgumentException(
                        "First itinerary item of each day must start at or after trip dailyStartTime. dayNo="
                                + createRequest.dayNo()
                );
            }
            if (createRequest.endTime().isAfter(dayTimeWindow.endTime())) {
                if (hasDayTimeWindowOverride(createRequest.dayNo(), request)) {
                    throw new IllegalArgumentException(
                            "Itinerary endTime must be at or before day time window endTime. dayNo="
                                    + createRequest.dayNo()
                    );
                }
                throw new IllegalArgumentException(
                        "Itinerary endTime must be at or before trip dailyEndTime. dayNo="
                                + createRequest.dayNo()
                );
            }
        }
    }

    private boolean hasDayTimeWindowOverride(Integer dayNo, ItineraryGenerateRequest request) {
        if (request == null || dayNo == null) {
            return false;
        }

        return request.normalizedDayTimeWindows().stream()
                .anyMatch(dayTimeWindow -> dayTimeWindow != null && dayNo.equals(dayTimeWindow.dayNo()));
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

    private void validateGenerateRequest(
            Trip trip,
            List<PlaceResponse> candidatePlaces,
            ItineraryGenerateRequest request
    ) {
        if (request == null) {
            return;
        }

        validateDayTimeWindows(trip, request);

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

    private void validateDayTimeWindows(Trip trip, ItineraryGenerateRequest request) {
        long tripDays = ChronoUnit.DAYS.between(trip.getStartDate(), trip.getEndDate()) + 1;
        Set<Integer> seenDayNos = new HashSet<>();

        for (ItineraryDayTimeWindowRequest dayTimeWindow : request.normalizedDayTimeWindows()) {
            if (dayTimeWindow == null) {
                throw new IllegalArgumentException("dayTimeWindows must not contain null.");
            }
            if (dayTimeWindow.dayNo() == null) {
                throw new IllegalArgumentException("dayTimeWindows.dayNo is required.");
            }
            if (dayTimeWindow.dayNo() < 1 || dayTimeWindow.dayNo() > tripDays) {
                throw new IllegalArgumentException(
                        "dayTimeWindows.dayNo must be within trip period. dayNo="
                                + dayTimeWindow.dayNo()
                                + ", maxDayNo="
                                + tripDays
                );
            }
            if (!seenDayNos.add(dayTimeWindow.dayNo())) {
                throw new IllegalArgumentException(
                        "dayTimeWindows must not contain duplicated dayNo. dayNo=" + dayTimeWindow.dayNo()
                );
            }
            if (dayTimeWindow.startTime() == null) {
                throw new IllegalArgumentException("dayTimeWindows.startTime is required. dayNo=" + dayTimeWindow.dayNo());
            }
            if (dayTimeWindow.endTime() == null) {
                throw new IllegalArgumentException("dayTimeWindows.endTime is required. dayNo=" + dayTimeWindow.dayNo());
            }
            if (!dayTimeWindow.startTime().isBefore(dayTimeWindow.endTime())) {
                throw new IllegalArgumentException(
                        "dayTimeWindows.startTime must be before endTime. dayNo=" + dayTimeWindow.dayNo()
                );
            }
        }
    }

    private Map<Integer, DayTimeWindow> createDayTimeWindows(Trip trip, ItineraryGenerateRequest request) {
        long tripDays = ChronoUnit.DAYS.between(trip.getStartDate(), trip.getEndDate()) + 1;
        Map<Integer, DayTimeWindow> dayTimeWindowByDayNo = new HashMap<>();

        for (int dayNo = 1; dayNo <= tripDays; dayNo++) {
            dayTimeWindowByDayNo.put(dayNo, new DayTimeWindow(
                    dayNo,
                    trip.getDailyStartTime(),
                    trip.getDailyEndTime()
            ));
        }
        if (request == null) {
            return dayTimeWindowByDayNo;
        }

        for (ItineraryDayTimeWindowRequest override : request.normalizedDayTimeWindows()) {
            dayTimeWindowByDayNo.put(override.dayNo(), new DayTimeWindow(
                    override.dayNo(),
                    override.startTime(),
                    override.endTime()
            ));
        }
        return dayTimeWindowByDayNo;
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

    private List<PlaceResponse> selectLlmCandidatePlaces(
            Trip trip,
            List<PlaceResponse> candidatePlaces,
            ItineraryGenerateRequest request
    ) {
        List<PlaceResponse> filteredCandidatePlaces = filterExcludedCandidatePlaces(candidatePlaces, request);
        Set<Long> mustVisitPlaceIds = request == null
                ? Set.of()
                : new HashSet<>(request.normalizedMustVisitPlaceIds());

        List<PlaceResponse> mustVisitPlaces = filteredCandidatePlaces.stream()
                .filter(candidatePlace -> mustVisitPlaceIds.contains(candidatePlace.placeId()))
                .sorted(candidatePlaceComparator(trip.getConcept(), request))
                .toList();
        List<PlaceResponse> ordinaryPlaces = filteredCandidatePlaces.stream()
                .filter(candidatePlace -> !mustVisitPlaceIds.contains(candidatePlace.placeId()))
                .sorted(candidatePlaceComparator(trip.getConcept(), request))
                .toList();

        int ordinaryPlaceLimit = Math.max(0, MAX_LLM_CANDIDATE_PLACE_COUNT - mustVisitPlaces.size());

        return java.util.stream.Stream.concat(
                        mustVisitPlaces.stream(),
                        ordinaryPlaces.stream().limit(ordinaryPlaceLimit)
                )
                .toList();
    }

    private List<PlaceResponse> filterExcludedCandidatePlaces(
            List<PlaceResponse> candidatePlaces,
            ItineraryGenerateRequest request
    ) {
        Set<Long> excludedPlaceIds = request == null
                ? Set.of()
                : new HashSet<>(request.normalizedExcludedPlaceIds());

        return candidatePlaces.stream()
                .filter(candidatePlace -> !excludedPlaceIds.contains(candidatePlace.placeId()))
                .toList();
    }

    private void validateCandidatePlacesEnoughForTripDays(
            Trip trip,
            List<PlaceResponse> selectedCandidatePlaces
    ) {
        long tripDays = ChronoUnit.DAYS.between(trip.getStartDate(), trip.getEndDate()) + 1;
        int candidateCount = selectedCandidatePlaces.size();

        if (candidateCount < tripDays) {
            throw new IllegalArgumentException(
                    "Candidate places are not enough to cover every trip day. requiredDays="
                            + tripDays
                            + ", candidateCount="
                            + candidateCount
            );
        }
    }

    private Comparator<PlaceResponse> candidatePlaceComparator(TripConcept concept, ItineraryGenerateRequest request) {
        return Comparator
                .comparing((PlaceResponse place) -> isPreferredCategory(place, request))
                .reversed()
                .thenComparing(Comparator.comparing((PlaceResponse place) -> conceptScore(place, concept)).reversed())
                .thenComparing(PlaceResponse::placeId);
    }

    private boolean isPreferredCategory(PlaceResponse place, ItineraryGenerateRequest request) {
        if (request == null || request.normalizedPreferredCategories().isEmpty()) {
            return false;
        }

        return request.normalizedPreferredCategories().stream()
                .map(PlaceCategory::name)
                .anyMatch(preferredCategory -> preferredCategory.equals(place.category()));
    }

    private Integer conceptScore(PlaceResponse place, TripConcept concept) {
        return switch (concept) {
            case HEALING -> place.healingScore();
            case FOOD -> place.foodScore();
            case CAFE -> place.cafeScore();
            case PHOTO -> place.photoScore();
            case COUPLE -> place.coupleScore();
            case FAMILY -> place.familyScore();
        };
    }

    private void logDraftGenerationSuccess(
            Trip trip,
            String operation,
            int attempt,
            int maxAttemptCount,
            ItineraryGenerateRequest request,
            List<PlaceResponse> selectedCandidatePlaces
    ) {
        log.debug(
                "LLM itinerary draft validation succeeded. tripId={}, operation={}, attempt={}, maxAttempts={}, "
                        + "mustVisitPlaceIdsCount={}, excludedPlaceIdsCount={}, preferredCategoriesCount={}, "
                        + "selectedCandidatePlacesCount={}, llmSuccess={}",
                trip.getTripId(),
                operation,
                attempt,
                maxAttemptCount,
                mustVisitPlaceIdsCount(request),
                excludedPlaceIdsCount(request),
                preferredCategoriesCount(request),
                selectedCandidatePlaces.size(),
                true
        );
    }

    private void logValidationFailure(
            Trip trip,
            String operation,
            int attempt,
            int maxAttemptCount,
            ItineraryGenerateRequest request,
            List<PlaceResponse> selectedCandidatePlaces,
            IllegalArgumentException exception
    ) {
        log.warn(
                "LLM itinerary draft validation failed. tripId={}, operation={}, attempt={}, maxAttempts={}, "
                        + "reason={}, exceptionClass={}, mustVisitPlaceIdsCount={}, excludedPlaceIdsCount={}, "
                        + "preferredCategoriesCount={}, selectedCandidatePlacesCount={}, llmSuccess={}",
                trip.getTripId(),
                operation,
                attempt,
                maxAttemptCount,
                exception.getMessage(),
                exception.getClass().getSimpleName(),
                mustVisitPlaceIdsCount(request),
                excludedPlaceIdsCount(request),
                preferredCategoriesCount(request),
                selectedCandidatePlaces.size(),
                false
        );
    }

    private void logFinalValidationFailure(
            Trip trip,
            String operation,
            int maxAttemptCount,
            ItineraryGenerateRequest request,
            List<PlaceResponse> selectedCandidatePlaces,
            IllegalArgumentException exception
    ) {
        log.warn(
                "LLM itinerary draft validation finally failed. tripId={}, operation={}, attempt={}, maxAttempts={}, "
                        + "reason={}, exceptionClass={}, mustVisitPlaceIdsCount={}, excludedPlaceIdsCount={}, "
                        + "preferredCategoriesCount={}, selectedCandidatePlacesCount={}, llmSuccess={}",
                trip.getTripId(),
                operation,
                maxAttemptCount,
                maxAttemptCount,
                exception.getMessage(),
                exception.getClass().getSimpleName(),
                mustVisitPlaceIdsCount(request),
                excludedPlaceIdsCount(request),
                preferredCategoriesCount(request),
                selectedCandidatePlaces.size(),
                false
        );
    }

    private void logLlmFailure(
            Trip trip,
            String operation,
            int attempt,
            int maxAttemptCount,
            ItineraryGenerateRequest request,
            List<PlaceResponse> selectedCandidatePlaces,
            LlmException exception
    ) {
        log.warn(
                "LLM itinerary draft generation failed. tripId={}, operation={}, attempt={}, maxAttempts={}, "
                        + "reason={}, exceptionClass={}, llmFailureType={}, providerErrorType={}, providerErrorCode={}, "
                        + "mustVisitPlaceIdsCount={}, excludedPlaceIdsCount={}, preferredCategoriesCount={}, "
                        + "selectedCandidatePlacesCount={}, llmSuccess={}",
                trip.getTripId(),
                operation,
                attempt,
                maxAttemptCount,
                exception.getMessage(),
                exception.getClass().getSimpleName(),
                exception.getFailureType(),
                exception.getProviderErrorType(),
                exception.getProviderErrorCode(),
                mustVisitPlaceIdsCount(request),
                excludedPlaceIdsCount(request),
                preferredCategoriesCount(request),
                selectedCandidatePlaces.size(),
                false
        );
    }

    private int mustVisitPlaceIdsCount(ItineraryGenerateRequest request) {
        return request == null ? 0 : request.normalizedMustVisitPlaceIds().size();
    }

    private int excludedPlaceIdsCount(ItineraryGenerateRequest request) {
        return request == null ? 0 : request.normalizedExcludedPlaceIds().size();
    }

    private int preferredCategoriesCount(ItineraryGenerateRequest request) {
        return request == null ? 0 : request.normalizedPreferredCategories().size();
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

    private record DayTimeWindow(
            Integer dayNo,
            LocalTime startTime,
            LocalTime endTime
    ) {
    }
}
