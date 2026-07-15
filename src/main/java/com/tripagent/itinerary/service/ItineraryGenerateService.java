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
import com.tripagent.itinerary.dto.ItineraryPace;
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.itinerary.domain.Itinerary;
import com.tripagent.itinerary.policy.AccommodationAreaRegionMapper;
import com.tripagent.itinerary.policy.PaceItineraryPolicy;
import com.tripagent.place.dto.PlaceCategory;
import com.tripagent.itinerary.repository.ItineraryRepository;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.service.PlaceService;
import com.tripagent.route.RouteCalculationAdapter;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripAccommodation;
import com.tripagent.trip.domain.TripConcept;
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
    private static final int DEFAULT_LLM_CANDIDATE_PLACE_COUNT = 30;
    private static final int MAX_BALANCED_REGION_COUNT = 3;
    private static final int MAX_TRAVEL_MINUTES_BETWEEN_PLACES = 90;
    private static final int MIN_FALLBACK_STAY_MINUTES = 30;
    private static final Set<String> MEAL_AND_REST_CATEGORY_NAMES = Set.of("FOOD", "CAFE");
    private static final Set<String> TOUR_CATEGORY_NAMES = Set.of("NATURE", "BEACH", "GARDEN", "MUSEUM");
    private static final String OPERATION_GENERATE = "generate";
    private static final String OPERATION_REGENERATE = "regenerate";
    private static final String OPERATION_REGENERATE_DAY = "regenerateDay";
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
    private final TripAccommodationRepository tripAccommodationRepository;
    private final RouteCalculationAdapter routeCalculationAdapter;
    private final AccommodationAreaRegionMapper accommodationAreaRegionMapper;

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
            TripAccommodationRepository tripAccommodationRepository,
            RouteCalculationAdapter routeCalculationAdapter,
            AccommodationAreaRegionMapper accommodationAreaRegionMapper
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
        this.tripAccommodationRepository = tripAccommodationRepository;
        this.routeCalculationAdapter = routeCalculationAdapter;
        this.accommodationAreaRegionMapper = accommodationAreaRegionMapper;
    }

    @Transactional
    public List<ItineraryResponse> generateItineraries(Long tripId) {
        return generateItineraries(tripId, null);
    }

    @Transactional
    public List<ItineraryResponse> generateItineraries(Long tripId, ItineraryGenerateRequest request) {
        return generateItineraries(tripId, request, null);
    }

    @Transactional
    public List<ItineraryResponse> generateItineraries(
            Long tripId,
            ItineraryGenerateRequest request,
            Long ownerId
    ) {
        validateTripId(tripId);
        validateTripOwner(tripId, ownerId);
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
        return regenerateItineraries(tripId, request, null);
    }

    @Transactional
    public List<ItineraryResponse> regenerateItineraries(
            Long tripId,
            ItineraryGenerateRequest request,
            Long ownerId
    ) {
        validateTripId(tripId);
        validateTripOwner(tripId, ownerId);

        List<ItineraryCreateRequest> createRequests = generateDraftItineraries(
                tripId,
                request,
                OPERATION_REGENERATE
        );
        itineraryRepository.deleteByTrip_TripId(tripId);

        return saveGeneratedItineraries(tripId, request, createRequests);
    }

    @Transactional
    public List<ItineraryResponse> regenerateDayItineraries(
            Long tripId,
            Integer dayNo,
            ItineraryGenerateRequest request,
            Long ownerId
    ) {
        validateTripId(tripId);
        validateTripOwner(tripId, ownerId);

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));
        validateTargetDayNo(trip, dayNo);

        List<Itinerary> dayItineraries = itineraryRepository.findByTrip_TripIdAndDayNo(tripId, dayNo);
        if (dayItineraries.isEmpty()) {
            throw new NoSuchElementException(
                    "Itinerary day not found. tripId=" + tripId + ", dayNo=" + dayNo
            );
        }

        Set<Long> unavailablePlaceIds = itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(tripId)
                .stream()
                .filter(itinerary -> !dayNo.equals(itinerary.getDayNo()))
                .map(itinerary -> itinerary.getPlace().getPlaceId())
                .collect(Collectors.toSet());
        ItineraryGenerateRequest dayRequest = requestForDay(request, unavailablePlaceIds, dayNo);
        List<ItineraryCreateRequest> createRequests = generateDraftItineraries(
                tripId,
                dayRequest,
                OPERATION_REGENERATE_DAY,
                dayNo,
                unavailablePlaceIds
        );

        itineraryRepository.deleteByTrip_TripIdAndDayNo(tripId, dayNo);
        saveGeneratedItineraries(tripId, dayRequest, createRequests);
        return itineraryService.getItineraries(tripId, ownerId);
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
        return generateDraftItineraries(tripId, request, operation, null, Set.of());
    }

    private List<ItineraryCreateRequest> generateDraftItineraries(
            Long tripId,
            ItineraryGenerateRequest request,
            String operation,
            Integer targetDayNo,
            Set<Long> unavailablePlaceIds
    ) {
        validateTripId(tripId);

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));
        Map<Integer, String> accommodationRegionByDayNo = findAccommodationRegionsByDayNo(trip);
        List<PlaceResponse> allCandidatePlaces = placeService.findCandidatePlaces(trip.getConcept());
        validateGenerateRequest(trip, allCandidatePlaces, request);
        List<PlaceResponse> availableCandidatePlaces = allCandidatePlaces.stream()
                .filter(place -> !unavailablePlaceIds.contains(place.placeId()))
                .toList();
        List<PlaceResponse> selectedCandidatePlaces = selectLlmCandidatePlaces(
                trip,
                availableCandidatePlaces,
                request,
                targetDayNo,
                accommodationRegionByDayNo
        );
        validateCandidatePlacesEnoughForTripDays(trip, selectedCandidatePlaces, targetDayNo);
        validateCandidatePlacesEnoughForPace(trip, request, selectedCandidatePlaces, targetDayNo);
        validateMustVisitPlacesFitPace(trip, request, targetDayNo);
        String prompt;
        if (targetDayNo != null) {
            prompt = itineraryPromptGenerator.generate(trip, selectedCandidatePlaces, request, targetDayNo);
        } else if (request == null) {
            prompt = itineraryPromptGenerator.generate(trip, selectedCandidatePlaces);
        } else {
            prompt = itineraryPromptGenerator.generate(trip, selectedCandidatePlaces, request);
        }
        if (!accommodationRegionByDayNo.isEmpty()) {
            prompt = itineraryPromptGenerator.appendAccommodationRoutePreferences(
                    prompt,
                    trip,
                    accommodationRegionByDayNo,
                    targetDayNo
            );
        }

        return generateValidatedDraftItineraries(
                trip,
                selectedCandidatePlaces,
                request,
                prompt,
                operation,
                targetDayNo,
                accommodationRegionByDayNo
        );
    }

    private List<ItineraryCreateRequest> generateValidatedDraftItineraries(
            Trip trip,
            List<PlaceResponse> candidatePlaces,
            ItineraryGenerateRequest request,
            String prompt,
            String operation,
            Integer targetDayNo,
            Map<Integer, String> accommodationRegionByDayNo
    ) {
        IllegalArgumentException lastValidationException = null;
        int maxAttemptCount = MAX_LLM_VALIDATION_RETRY_COUNT + 1;

        for (int attempt = 0; attempt <= MAX_LLM_VALIDATION_RETRY_COUNT; attempt++) {
            int attemptNumber = attempt + 1;
            String attemptPrompt = promptForValidationAttempt(prompt, lastValidationException);
            try {
                List<ItineraryCreateRequest> createRequests = generateValidatedDraftItinerary(
                        trip,
                        candidatePlaces,
                        request,
                        attemptPrompt,
                        targetDayNo
                );
                logDraftGenerationSuccess(trip, operation, attemptNumber, maxAttemptCount, request, candidatePlaces);
                return createRequests;
            } catch (LlmException exception) {
                logLlmFailure(trip, operation, attemptNumber, maxAttemptCount, request, candidatePlaces, exception);
                return generateFallbackDraftItinerariesOrThrow(
                        trip, candidatePlaces, request, operation, exception, targetDayNo,
                        accommodationRegionByDayNo
                );
            } catch (IllegalArgumentException exception) {
                boolean repeatedValidationFailure = lastValidationException != null
                        && Objects.equals(lastValidationException.getMessage(), exception.getMessage())
                        && exception.getMessage().startsWith(
                                "Itinerary travelMinutesFromPrevious must be less than or equal to "
                        );
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
                if (repeatedValidationFailure) {
                    logFinalValidationFailure(
                            trip,
                            operation,
                            attemptNumber,
                            request,
                            candidatePlaces,
                            lastValidationException
                    );
                    return generateFallbackDraftItinerariesOrThrow(
                            trip,
                            candidatePlaces,
                            request,
                            operation,
                            lastValidationException,
                            targetDayNo,
                            accommodationRegionByDayNo
                    );
                }
            }
        }

        logFinalValidationFailure(trip, operation, maxAttemptCount, request, candidatePlaces, lastValidationException);
        return generateFallbackDraftItinerariesOrThrow(
                trip,
                candidatePlaces,
                request,
                operation,
                lastValidationException,
                targetDayNo,
                accommodationRegionByDayNo
        );
    }

    private String promptForValidationAttempt(String prompt, IllegalArgumentException lastValidationException) {
        if (lastValidationException == null) {
            return prompt;
        }

        return prompt
                + "\nPrevious validation failure:\n"
                + "- "
                + lastValidationException.getMessage()
                + "\nCorrect the itinerary so this validation failure is not repeated.\n"
                + "Return JSON only. Do not include markdown or explanation outside JSON.\n";
    }

    private List<ItineraryCreateRequest> generateFallbackDraftItinerariesOrThrow(
            Trip trip,
            List<PlaceResponse> candidatePlaces,
            ItineraryGenerateRequest request,
            String operation,
            RuntimeException originalException,
            Integer targetDayNo,
            Map<Integer, String> accommodationRegionByDayNo
    ) {
        if (!supportsFallback(operation)) {
            throw originalException;
        }

        try {
            List<ItineraryCreateRequest> fallbackCreateRequests = generateFallbackDraftItineraries(
                    trip,
                    candidatePlaces,
                    request,
                    targetDayNo,
                    accommodationRegionByDayNo
            );
            logFallbackDraftGenerationSuccess(trip, operation, request, candidatePlaces, originalException);
            return fallbackCreateRequests;
        } catch (RuntimeException fallbackException) {
            logFallbackDraftGenerationFailure(
                    trip,
                    operation,
                    request,
                    candidatePlaces,
                    originalException,
                    fallbackException
            );
            throw originalException;
        }
    }

    private boolean supportsFallback(String operation) {
        return OPERATION_GENERATE.equals(operation)
                || OPERATION_REGENERATE.equals(operation)
                || OPERATION_REGENERATE_DAY.equals(operation);
    }

    private List<ItineraryCreateRequest> generateFallbackDraftItineraries(
            Trip trip,
            List<PlaceResponse> candidatePlaces,
            ItineraryGenerateRequest request,
            Integer targetDayNo,
            Map<Integer, String> accommodationRegionByDayNo
    ) {
        int tripDays = generationDayCount(trip, targetDayNo);
        PaceItineraryPolicy pacePolicy = request == null || request.pace() == null ? null : pacePolicy(request);
        int minItemsPerDay = pacePolicy == null ? 1 : pacePolicy.minItemsPerDay();
        int maxItemsPerDay = pacePolicy == null ? candidatePlaces.size() : pacePolicy.maxItemsPerDay();
        int targetItemCount = targetFallbackItemCount(tripDays, minItemsPerDay, request, candidatePlaces);

        List<PlaceResponse> selectedPlaces = selectFallbackPlacesByRoute(
                trip,
                request,
                candidatePlaces,
                targetItemCount,
                accommodationRegionByDayNo
        );
        List<ItineraryCreateRequest> fallbackCreateRequests = new ArrayList<>();
        boolean routeFlexibleSelection = hasNoFallbackPlaceControls(request);
        List<PlaceResponse> remainingSelectedPlaces = routeFlexibleSelection
                ? new ArrayList<>(candidatePlaces)
                : new ArrayList<>(selectedPlaces);

        int firstDayNo = targetDayNo == null ? 1 : targetDayNo;
        int lastDayNo = targetDayNo == null ? tripDays : targetDayNo;
        for (int dayNo = firstDayNo; dayNo <= lastDayNo; dayNo++) {
            int generationDayNo = dayNo - firstDayNo + 1;
            int generatedItemCount = fallbackCreateRequests.size();
            int dayItemCount = fallbackDayItemCount(
                    generationDayNo,
                    tripDays,
                    targetItemCount - generatedItemCount,
                    minItemsPerDay,
                    maxItemsPerDay
            );
            List<PlaceResponse> dayPlaces;
            String preferredStartRegion = preferredStartRegion(trip, accommodationRegionByDayNo, dayNo);
            String preferredEndRegion = accommodationRegionByDayNo.get(dayNo);
            if (routeFlexibleSelection) {
                dayPlaces = selectReachableFallbackDayPlaces(
                        trip,
                        remainingSelectedPlaces,
                        dayItemCount,
                        preferredStartRegion,
                        preferredEndRegion
                );
            } else {
                List<PlaceResponse> selectedDayPlaces = selectFallbackDayPlaces(
                        request,
                        dayNo,
                        remainingSelectedPlaces,
                        dayItemCount
                );
                dayPlaces = orderFallbackDayPlacesByRoute(
                        trip,
                        selectedDayPlaces,
                        preferredStartRegion,
                        preferredEndRegion
                );
            }
            fallbackCreateRequests.addAll(createFallbackDayItineraries(trip, request, dayNo, dayPlaces));
            remainingSelectedPlaces.removeAll(dayPlaces);
        }

        List<Long> selectedPlaceIds = fallbackCreateRequests.stream()
                .map(ItineraryCreateRequest::placeId)
                .toList();
        validateGeneratedPlaceControls(request, selectedPlaceIds);
        candidatePlaceValidator.validatePlaceIds(candidatePlaces, selectedPlaceIds);
        validateDraftItineraries(trip, request, fallbackCreateRequests, targetDayNo);

        return fallbackCreateRequests;
    }

    private boolean hasNoFallbackPlaceControls(ItineraryGenerateRequest request) {
        return request == null
                || (request.normalizedMustVisitPlaceIds().isEmpty()
                && !request.normalizedRainyDayMode()
                && request.normalizedRainyDayNos().isEmpty());
    }

    private List<PlaceResponse> selectReachableFallbackDayPlaces(
            Trip trip,
            List<PlaceResponse> candidatePlaces,
            int dayItemCount,
            String preferredStartRegion,
            String preferredEndRegion
    ) {
        List<PlaceResponse> startCandidates = new ArrayList<>(candidatePlaces);
        PlaceResponse preferredStart = findFallbackStartPlace(trip, candidatePlaces, preferredStartRegion);
        if (dayItemCount == 1 && preferredEndRegion != null) {
            preferredStart = findFallbackStartPlace(trip, candidatePlaces, preferredEndRegion);
        }
        startCandidates.remove(preferredStart);
        startCandidates.addFirst(preferredStart);

        for (PlaceResponse startCandidate : startCandidates) {
            List<PlaceResponse> route = new ArrayList<>();
            List<PlaceResponse> remainingPlaces = new ArrayList<>(candidatePlaces);
            route.add(startCandidate);
            remainingPlaces.remove(startCandidate);

            while (route.size() < dayItemCount) {
                PlaceResponse currentPlace = route.getLast();
                boolean finalPlace = route.size() == dayItemCount - 1;
                PlaceResponse nextPlace = findReachableFallbackPlace(
                        currentPlace,
                        remainingPlaces,
                        finalPlace ? preferredEndRegion : null
                );
                if (nextPlace == null) {
                    break;
                }
                route.add(nextPlace);
                remainingPlaces.remove(nextPlace);
            }

            if (route.size() == dayItemCount) {
                return route;
            }
        }

        throw new IllegalArgumentException(
                "Fallback itinerary cannot find enough places within maximum travel time."
        );
    }

    private PlaceResponse findReachableFallbackPlace(
            PlaceResponse currentPlace,
            List<PlaceResponse> candidatePlaces,
            String preferredRegion
    ) {
        return candidatePlaces.stream()
                .filter(place -> routeCalculationAdapter.calculateTravelMinutes(currentPlace, place)
                        <= MAX_TRAVEL_MINUTES_BETWEEN_PLACES)
                .min(Comparator.comparing((PlaceResponse place) -> isSameArea(preferredRegion, place.region()))
                        .reversed()
                        .thenComparingInt(place ->
                                routeCalculationAdapter.calculateTravelMinutes(currentPlace, place))
                        .thenComparing(place -> !isSameRegion(currentPlace, place))
                        .thenComparing(PlaceResponse::placeId))
                .orElse(null);
    }

    private List<PlaceResponse> selectFallbackPlacesByRoute(
            Trip trip,
            ItineraryGenerateRequest request,
            List<PlaceResponse> candidatePlaces,
            int targetItemCount,
            Map<Integer, String> accommodationRegionByDayNo
    ) {
        Set<Long> mustVisitPlaceIds = request == null
                ? Set.of()
                : new HashSet<>(request.normalizedMustVisitPlaceIds());
        List<PlaceResponse> remainingPlaces = new ArrayList<>(candidatePlaces);
        List<PlaceResponse> selectedPlaces = new ArrayList<>();
        PlaceResponse currentPlace = null;
        Set<String> accommodationRegions = new HashSet<>(accommodationRegionByDayNo.values());

        while (selectedPlaces.size() < targetItemCount && !remainingPlaces.isEmpty()) {
            List<PlaceResponse> selectablePlaces = remainingPlaces;
            int remainingSelectionCount = targetItemCount - selectedPlaces.size();
            long remainingMustVisitCount = remainingPlaces.stream()
                    .filter(place -> mustVisitPlaceIds.contains(place.placeId()))
                    .count();
            if (remainingMustVisitCount >= remainingSelectionCount) {
                selectablePlaces = remainingPlaces.stream()
                        .filter(place -> mustVisitPlaceIds.contains(place.placeId()))
                        .toList();
            }

            PlaceResponse nextPlace = findNextFallbackPlace(
                    trip,
                    currentPlace,
                    selectablePlaces,
                    accommodationRegions
            );
            selectedPlaces.add(nextPlace);
            remainingPlaces.remove(nextPlace);
            currentPlace = nextPlace;
        }

        return selectedPlaces;
    }

    private List<PlaceResponse> selectFallbackDayPlaces(
            ItineraryGenerateRequest request,
            int dayNo,
            List<PlaceResponse> remainingPlaces,
            int dayItemCount
    ) {
        if (!isRainyDay(request, dayNo) && !hasRainyDayAfter(request, dayNo)) {
            return new ArrayList<>(remainingPlaces.subList(0, dayItemCount));
        }

        Comparator<PlaceResponse> rainyDayComparator = Comparator
                .comparingInt((PlaceResponse place) -> rainyDayScore(place))
                .thenComparing(PlaceResponse::placeId);
        if (isRainyDay(request, dayNo)) {
            rainyDayComparator = rainyDayComparator.reversed();
        }

        return remainingPlaces.stream()
                .sorted(rainyDayComparator)
                .limit(dayItemCount)
                .toList();
    }

    private int targetFallbackItemCount(
            int tripDays,
            int minItemsPerDay,
            ItineraryGenerateRequest request,
            List<PlaceResponse> candidatePlaces
    ) {
        int mustVisitPlaceCount = request == null ? 0 : request.normalizedMustVisitPlaceIds().size();
        int requiredItemCount = Math.max(tripDays * minItemsPerDay, mustVisitPlaceCount);
        return Math.min(candidatePlaces.size(), requiredItemCount);
    }

    private int fallbackDayItemCount(
            int dayNo,
            int tripDays,
            int remainingItemCount,
            int minItemsPerDay,
            int maxItemsPerDay
    ) {
        int remainingDaysAfterToday = tripDays - dayNo;
        int minimumItemsForLaterDays = remainingDaysAfterToday * minItemsPerDay;
        int itemCount = remainingItemCount - minimumItemsForLaterDays;
        return Math.min(maxItemsPerDay, Math.max(minItemsPerDay, itemCount));
    }

    private List<PlaceResponse> orderFallbackDayPlacesByRoute(
            Trip trip,
            List<PlaceResponse> dayPlaces,
            String preferredStartRegion,
            String preferredEndRegion
    ) {
        List<PlaceResponse> remainingPlaces = new ArrayList<>(dayPlaces);
        List<PlaceResponse> orderedPlaces = new ArrayList<>();
        PlaceResponse currentPlace = null;
        PlaceResponse preferredEndPlace = null;
        if (remainingPlaces.size() > 1 && preferredEndRegion != null) {
            preferredEndPlace = remainingPlaces.stream()
                    .filter(place -> isSameArea(preferredEndRegion, place.region()))
                    .findFirst()
                    .orElse(null);
            remainingPlaces.remove(preferredEndPlace);
        }

        while (!remainingPlaces.isEmpty()) {
            PlaceResponse nextPlace = currentPlace == null
                    ? findFallbackStartPlace(trip, remainingPlaces, preferredStartRegion)
                    : findNextFallbackPlace(trip, currentPlace, remainingPlaces);
            orderedPlaces.add(nextPlace);
            remainingPlaces.remove(nextPlace);
            currentPlace = nextPlace;
        }
        if (preferredEndPlace != null) {
            orderedPlaces.add(preferredEndPlace);
        }

        return orderedPlaces;
    }

    private PlaceResponse findNextFallbackPlace(
            Trip trip,
            PlaceResponse currentPlace,
            List<PlaceResponse> candidatePlaces
    ) {
        return findNextFallbackPlace(trip, currentPlace, candidatePlaces, Set.of());
    }

    private PlaceResponse findNextFallbackPlace(
            Trip trip,
            PlaceResponse currentPlace,
            List<PlaceResponse> candidatePlaces,
            Set<String> preferredRegions
    ) {
        if (currentPlace == null) {
            return findFallbackStartPlace(trip, candidatePlaces, null);
        }

        return candidatePlaces.stream()
                .min(Comparator.comparing((PlaceResponse place) -> preferredRegions.stream()
                                .anyMatch(region -> isSameArea(region, place.region())))
                        .reversed()
                        .thenComparingInt(place ->
                                routeCalculationAdapter.calculateTravelMinutes(currentPlace, place))
                        .thenComparing(place -> !isSameRegion(currentPlace, place))
                        .thenComparing(PlaceResponse::placeId))
                .orElseThrow();
    }

    private PlaceResponse findFallbackStartPlace(Trip trip, List<PlaceResponse> candidatePlaces) {
        return findFallbackStartPlace(trip, candidatePlaces, null);
    }

    private PlaceResponse findFallbackStartPlace(
            Trip trip,
            List<PlaceResponse> candidatePlaces,
            String preferredRegion
    ) {
        if (preferredRegion != null) {
            PlaceResponse preferredRegionPlace = candidatePlaces.stream()
                    .filter(place -> isSameArea(preferredRegion, place.region()))
                    .findFirst()
                    .orElse(null);
            if (preferredRegionPlace != null) {
                return preferredRegionPlace;
            }
        }
        PlaceResponse sameAreaPlace = candidatePlaces.stream()
                .filter(place -> isSameArea(trip.getLastAccommodationArea(), place.region()))
                .findFirst()
                .orElse(null);
        if (sameAreaPlace != null) {
            return sameAreaPlace;
        }

        String mappedRegion = accommodationAreaRegionMapper.toPlaceRegion(trip.getLastAccommodationArea());
        if (mappedRegion != null) {
            return candidatePlaces.stream()
                    .filter(place -> isSameArea(mappedRegion, place.region()))
                    .findFirst()
                    .orElse(candidatePlaces.getFirst());
        }

        return candidatePlaces.getFirst();
    }

    private Map<Integer, String> findAccommodationRegionsByDayNo(Trip trip) {
        Map<Integer, String> accommodationRegionByDayNo = new HashMap<>();
        List<TripAccommodation> tripAccommodations =
                tripAccommodationRepository.findByTripIdOrderByStayDate(trip.getTripId());

        for (TripAccommodation tripAccommodation : tripAccommodations) {
            LocalDate stayDate = tripAccommodation.getStayDate();
            if (stayDate.isBefore(trip.getStartDate()) || !stayDate.isBefore(trip.getEndDate())) {
                continue;
            }
            String region = normalizeArea(tripAccommodation.getAccommodation().getRegion());
            if (region == null) {
                continue;
            }
            int dayNo = Math.toIntExact(ChronoUnit.DAYS.between(trip.getStartDate(), stayDate) + 1);
            accommodationRegionByDayNo.put(dayNo, region);
        }

        return accommodationRegionByDayNo;
    }

    private String preferredStartRegion(
            Trip trip,
            Map<Integer, String> accommodationRegionByDayNo,
            int dayNo
    ) {
        if (dayNo > 1) {
            String previousAccommodationRegion = accommodationRegionByDayNo.get(dayNo - 1);
            if (previousAccommodationRegion != null) {
                return previousAccommodationRegion;
            }
        }
        return accommodationAreaRegionMapper.toPlaceRegion(trip.getLastAccommodationArea());
    }

    private String normalizeArea(String area) {
        if (area == null || area.isBlank()) {
            return null;
        }

        return area.trim().toUpperCase();
    }

    private boolean isSameRegion(PlaceResponse previousPlace, PlaceResponse currentPlace) {
        return isSameArea(previousPlace.region(), currentPlace.region());
    }

    private boolean isSameArea(String firstArea, String secondArea) {
        String normalizedFirstArea = normalizeArea(firstArea);
        String normalizedSecondArea = normalizeArea(secondArea);
        return normalizedFirstArea != null && normalizedFirstArea.equals(normalizedSecondArea);
    }

    private List<ItineraryCreateRequest> createFallbackDayItineraries(
            Trip trip,
            ItineraryGenerateRequest request,
            int dayNo,
            List<PlaceResponse> dayPlaces
    ) {
        DayTimeWindow dayTimeWindow = createDayTimeWindows(trip, request).get(dayNo);
        List<Integer> travelMinutes = fallbackTravelMinutes(dayPlaces);
        int totalTravelMinutes = travelMinutes.stream()
                .mapToInt(Integer::intValue)
                .sum();
        int availableStayMinutes = Math.toIntExact(
                ChronoUnit.MINUTES.between(dayTimeWindow.startTime(), dayTimeWindow.endTime())
        ) - totalTravelMinutes;
        if (availableStayMinutes < dayPlaces.size() * MIN_FALLBACK_STAY_MINUTES) {
            throw new IllegalArgumentException("Fallback itinerary cannot fit within day time window. dayNo=" + dayNo);
        }

        int maxStayMinutesPerPlace = availableStayMinutes / dayPlaces.size();
        LocalTime cursor = dayTimeWindow.startTime();
        List<ItineraryCreateRequest> createRequests = new ArrayList<>();

        for (int index = 0; index < dayPlaces.size(); index++) {
            PlaceResponse place = dayPlaces.get(index);
            int travelMinutesFromPrevious = travelMinutes.get(index);
            LocalTime startTime = cursor.plusMinutes(travelMinutesFromPrevious);
            int stayMinutes = fallbackStayMinutes(place, maxStayMinutesPerPlace);
            LocalTime endTime = startTime.plusMinutes(stayMinutes);

            createRequests.add(new ItineraryCreateRequest(
                    place.placeId(),
                    dayNo,
                    index + 1,
                    startTime,
                    endTime,
                    travelMinutesFromPrevious,
                    "Fallback itinerary item generated from candidate places."
            ));

            cursor = endTime;
        }

        return createRequests;
    }

    private List<Integer> fallbackTravelMinutes(List<PlaceResponse> dayPlaces) {
        List<Integer> travelMinutes = new ArrayList<>();
        PlaceResponse previousPlace = null;

        for (PlaceResponse place : dayPlaces) {
            if (previousPlace == null) {
                travelMinutes.add(0);
            } else {
                travelMinutes.add(routeCalculationAdapter.calculateTravelMinutes(previousPlace, place));
            }
            previousPlace = place;
        }

        return travelMinutes;
    }

    private int fallbackStayMinutes(PlaceResponse place, int maxStayMinutesPerPlace) {
        int preferredStayMinutes = place.avgStayMinutes() == null
                ? MIN_FALLBACK_STAY_MINUTES
                : place.avgStayMinutes();
        int stayMinutes = Math.min(preferredStayMinutes, maxStayMinutesPerPlace);
        return Math.max(MIN_FALLBACK_STAY_MINUTES, stayMinutes);
    }

    private List<ItineraryCreateRequest> generateValidatedDraftItinerary(
            Trip trip,
            List<PlaceResponse> candidatePlaces,
            ItineraryGenerateRequest request,
            String prompt,
            Integer targetDayNo
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
        validateDraftItineraries(trip, request, createRequests, targetDayNo);

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
        List<ItineraryCreateRequest> requestsWithCalculatedTravelMinutes = new ArrayList<>();
        PlaceResponse previousPlace = null;
        ItineraryCreateRequest previousRequest = null;

        for (ItineraryCreateRequest request : sortedRequests) {
            PlaceResponse currentPlace = candidatePlaceById.get(request.placeId());
            if (currentPlace == null) {
                throw new IllegalArgumentException("Itinerary placeId must be included in candidate places. placeId="
                        + request.placeId());
            }

            PlaceResponse previousPlaceForCalculation = previousPlace;
            if (Integer.valueOf(1).equals(request.orderNo())) {
                previousPlaceForCalculation = null;
            }

            int travelMinutesFromPrevious = routeCalculationAdapter.calculateTravelMinutes(
                    previousPlaceForCalculation,
                    currentPlace
            );

            LocalTime startTime = request.startTime();
            LocalTime endTime = request.endTime();
            if (previousRequest != null && previousRequest.dayNo().equals(request.dayNo())) {
                LocalTime earliestStartTime = previousRequest.endTime().plusMinutes(travelMinutesFromPrevious);
                if (!startTime.isBefore(previousRequest.endTime()) && startTime.isBefore(earliestStartTime)) {
                    long stayMinutes = ChronoUnit.MINUTES.between(startTime, endTime);
                    startTime = earliestStartTime;
                    endTime = startTime.plusMinutes(stayMinutes);
                }
            }

            ItineraryCreateRequest adjustedRequest = new ItineraryCreateRequest(
                    request.placeId(),
                    request.dayNo(),
                    request.orderNo(),
                    startTime,
                    endTime,
                    travelMinutesFromPrevious,
                    request.reason()
            );
            requestsWithCalculatedTravelMinutes.add(adjustedRequest);
            previousPlace = currentPlace;
            previousRequest = adjustedRequest;
        }

        return requestsWithCalculatedTravelMinutes;
    }

    private void validateDraftItineraries(
            Trip trip,
            ItineraryGenerateRequest request,
            List<ItineraryCreateRequest> createRequests,
            Integer targetDayNo
    ) {
        validateDayAndOrderPolicies(trip, createRequests);
        validateGeneratedDays(trip, createRequests, targetDayNo);
        validatePaceItemsPerDay(trip, request, createRequests, targetDayNo);
        validateNoDuplicatedPlace(createRequests);
        validateFirstTravelMinutes(createRequests);
        validateTravelMinutesBetweenPlaces(createRequests);
        validateDayTimeWindowBounds(trip, request, createRequests);
        validateDraftOrderTimeSequence(createRequests);
        validateTravelTimeFitsSchedule(createRequests);
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

    private void validateGeneratedDays(
            Trip trip,
            List<ItineraryCreateRequest> createRequests,
            Integer targetDayNo
    ) {
        if (targetDayNo != null) {
            if (createRequests.isEmpty() || createRequests.stream()
                    .anyMatch(request -> !targetDayNo.equals(request.dayNo()))) {
                throw new IllegalArgumentException(
                        "Generated itinerary must contain only target day items. targetDayNo=" + targetDayNo
                );
            }
            return;
        }

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

    private void validatePaceItemsPerDay(
            Trip trip,
            ItineraryGenerateRequest request,
            List<ItineraryCreateRequest> createRequests,
            Integer targetDayNo
    ) {
        if (request == null || request.pace() == null) {
            return;
        }

        PaceItineraryPolicy pacePolicy = pacePolicy(request);
        long tripDays = ChronoUnit.DAYS.between(trip.getStartDate(), trip.getEndDate()) + 1;
        Map<Integer, Long> itemCountByDayNo = createRequests.stream()
                .collect(Collectors.groupingBy(ItineraryCreateRequest::dayNo, Collectors.counting()));

        int firstDayNo = targetDayNo == null ? 1 : targetDayNo;
        int lastDayNo = targetDayNo == null ? Math.toIntExact(tripDays) : targetDayNo;
        for (int dayNo = firstDayNo; dayNo <= lastDayNo; dayNo++) {
            long itemCount = itemCountByDayNo.getOrDefault(dayNo, 0L);
            if (itemCount < pacePolicy.minItemsPerDay() || itemCount > pacePolicy.maxItemsPerDay()) {
                throw new IllegalArgumentException(
                        "Generated itinerary item count per day does not match pace policy. pace="
                                + pacePolicy.pace()
                                + ", dayNo="
                                + dayNo
                                + ", itemCount="
                                + itemCount
                                + ", minItemsPerDay="
                                + pacePolicy.minItemsPerDay()
                                + ", maxItemsPerDay="
                                + pacePolicy.maxItemsPerDay()
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

    private void validateNoDuplicatedPlace(List<ItineraryCreateRequest> createRequests) {
        Set<Long> placeIds = new HashSet<>();

        for (ItineraryCreateRequest request : createRequests) {
            if (!placeIds.add(request.placeId())) {
                throw new IllegalArgumentException(
                        "Place id must not be duplicated in generated itinerary. placeId=" + request.placeId()
                );
            }
        }
    }

    private void validateTravelMinutesBetweenPlaces(List<ItineraryCreateRequest> createRequests) {
        for (ItineraryCreateRequest request : createRequests) {
            if (Integer.valueOf(1).equals(request.orderNo())) {
                continue;
            }
            if (request.travelMinutesFromPrevious() > MAX_TRAVEL_MINUTES_BETWEEN_PLACES) {
                throw new IllegalArgumentException(
                        "Itinerary travelMinutesFromPrevious must be less than or equal to "
                                + MAX_TRAVEL_MINUTES_BETWEEN_PLACES
                                + ". dayNo="
                                + request.dayNo()
                                + ", orderNo="
                                + request.orderNo()
                                + ", travelMinutesFromPrevious="
                                + request.travelMinutesFromPrevious()
                );
            }
        }
    }

    private void validateDraftOrderTimeSequence(List<ItineraryCreateRequest> createRequests) {
        List<ItineraryCreateRequest> sortedRequests = createRequests.stream()
                .sorted(Comparator.comparing(ItineraryCreateRequest::dayNo)
                        .thenComparing(ItineraryCreateRequest::orderNo))
                .toList();

        for (int index = 1; index < sortedRequests.size(); index++) {
            ItineraryCreateRequest previous = sortedRequests.get(index - 1);
            ItineraryCreateRequest current = sortedRequests.get(index);

            if (previous.dayNo().equals(current.dayNo()) && current.startTime().isBefore(previous.endTime())) {
                throw new IllegalArgumentException(
                        "Itinerary orderNo must follow time order in generated itinerary. dayNo="
                                + current.dayNo()
                                + ", orderNo="
                                + current.orderNo()
                );
            }
        }
    }

    private void validateTravelTimeFitsSchedule(List<ItineraryCreateRequest> createRequests) {
        List<ItineraryCreateRequest> sortedRequests = createRequests.stream()
                .sorted(Comparator.comparing(ItineraryCreateRequest::dayNo)
                        .thenComparing(ItineraryCreateRequest::orderNo))
                .toList();

        for (int index = 1; index < sortedRequests.size(); index++) {
            ItineraryCreateRequest previous = sortedRequests.get(index - 1);
            ItineraryCreateRequest current = sortedRequests.get(index);

            if (!previous.dayNo().equals(current.dayNo())) {
                continue;
            }

            LocalTime earliestStartTime = previous.endTime().plusMinutes(current.travelMinutesFromPrevious());
            if (current.startTime().isBefore(earliestStartTime)) {
                throw new IllegalArgumentException(
                        "Itinerary startTime must allow travelMinutesFromPrevious in generated itinerary. dayNo="
                                + current.dayNo()
                                + ", orderNo="
                                + current.orderNo()
                                + ", earliestStartTime="
                                + earliestStartTime
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

    private void validateTargetDayNo(Trip trip, Integer dayNo) {
        long tripDays = ChronoUnit.DAYS.between(trip.getStartDate(), trip.getEndDate()) + 1;
        if (dayNo == null || dayNo < 1 || dayNo > tripDays) {
            throw new IllegalArgumentException(
                    "dayNo must be within trip period. dayNo=" + dayNo + ", maxDayNo=" + tripDays
            );
        }
    }

    private ItineraryGenerateRequest requestForDay(
            ItineraryGenerateRequest request,
            Set<Long> unavailablePlaceIds,
            Integer dayNo
    ) {
        if (request == null) {
            return null;
        }

        List<Long> mustVisitPlaceIds = request.normalizedMustVisitPlaceIds().stream()
                .filter(placeId -> !unavailablePlaceIds.contains(placeId))
                .toList();
        List<ItineraryDayTimeWindowRequest> dayTimeWindows = request.normalizedDayTimeWindows().stream()
                .filter(dayTimeWindow -> dayTimeWindow != null && dayNo.equals(dayTimeWindow.dayNo()))
                .toList();
        List<Integer> rainyDayNos = request.normalizedRainyDayNos().stream()
                .filter(dayNo::equals)
                .toList();
        return new ItineraryGenerateRequest(
                mustVisitPlaceIds,
                request.excludedPlaceIds(),
                request.pace(),
                request.preferredCategories(),
                dayTimeWindows,
                request.rainyDayMode(),
                rainyDayNos
        );
    }

    private void validateTripOwner(Long tripId, Long ownerId) {
        if (ownerId == null) {
            return;
        }

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));
        if (!ownerId.equals(trip.getOwnerId())) {
            throw new IllegalArgumentException("Trip owner does not match. tripId=" + tripId);
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
        validateRainyDayNos(trip, request);

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

    private void validateRainyDayNos(Trip trip, ItineraryGenerateRequest request) {
        long tripDays = ChronoUnit.DAYS.between(trip.getStartDate(), trip.getEndDate()) + 1;
        Set<Integer> seenDayNos = new HashSet<>();

        for (Integer dayNo : request.normalizedRainyDayNos()) {
            if (dayNo == null) {
                throw new IllegalArgumentException("rainyDayNos must not contain null.");
            }
            if (dayNo < 1 || dayNo > tripDays) {
                throw new IllegalArgumentException(
                        "rainyDayNos must be within trip period. dayNo=" + dayNo + ", maxDayNo=" + tripDays
                );
            }
            if (!seenDayNos.add(dayNo)) {
                throw new IllegalArgumentException(
                        "rainyDayNos must not contain duplicated dayNo. dayNo=" + dayNo
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
            ItineraryGenerateRequest request,
            Integer targetDayNo,
            Map<Integer, String> accommodationRegionByDayNo
    ) {
        List<PlaceResponse> filteredCandidatePlaces = filterExcludedCandidatePlaces(candidatePlaces, request);
        Set<Long> mustVisitPlaceIds = request == null
                ? Set.of()
                : new HashSet<>(request.normalizedMustVisitPlaceIds());

        List<PlaceResponse> mustVisitPlaces = filteredCandidatePlaces.stream()
                .filter(candidatePlace -> mustVisitPlaceIds.contains(candidatePlace.placeId()))
                .sorted(candidatePlaceComparator(trip, request, accommodationRegionByDayNo))
                .toList();
        List<PlaceResponse> ordinaryPlaces = filteredCandidatePlaces.stream()
                .filter(candidatePlace -> !mustVisitPlaceIds.contains(candidatePlace.placeId()))
                .sorted(candidatePlaceComparator(trip, request, accommodationRegionByDayNo))
                .toList();

        int candidatePlaceLimit = calculateLlmCandidatePlaceLimit(trip, request, targetDayNo);
        int ordinaryPlaceLimit = Math.max(0, candidatePlaceLimit - mustVisitPlaces.size());

        List<PlaceResponse> selectedCandidatePlaces = java.util.stream.Stream.concat(
                        mustVisitPlaces.stream(),
                        ordinaryPlaces.stream().limit(ordinaryPlaceLimit)
                )
                .toList();
        List<PlaceResponse> categoryBalancedCandidatePlaces = includeCategoryBalanceCandidates(
                selectedCandidatePlaces,
                filteredCandidatePlaces,
                mustVisitPlaceIds,
                candidatePlaceComparator(trip, request, accommodationRegionByDayNo)
        );
        return includeRegionBalanceCandidates(
                categoryBalancedCandidatePlaces,
                filteredCandidatePlaces,
                mustVisitPlaceIds,
                candidatePlaceComparator(trip, request, accommodationRegionByDayNo)
        );
    }

    private int calculateLlmCandidatePlaceLimit(
            Trip trip,
            ItineraryGenerateRequest request,
            Integer targetDayNo
    ) {
        ItineraryPace pace = request == null ? ItineraryPace.NORMAL : request.normalizedPace();
        PaceItineraryPolicy policy = PaceItineraryPolicy.findByPace(pace)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Pace itinerary policy is not defined. pace=" + pace
                ));
        int generationDaysWithBuffer = generationDayCount(trip, targetDayNo) + 1;
        int requiredCandidateCount = generationDaysWithBuffer * policy.maxItemsPerDay();
        return Math.max(DEFAULT_LLM_CANDIDATE_PLACE_COUNT, requiredCandidateCount);
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

    private List<PlaceResponse> includeCategoryBalanceCandidates(
            List<PlaceResponse> selectedCandidatePlaces,
            List<PlaceResponse> candidatePlaces,
            Set<Long> mustVisitPlaceIds,
            Comparator<PlaceResponse> comparator
    ) {
        if (selectedCandidatePlaces.size() >= candidatePlaces.size()) {
            return selectedCandidatePlaces;
        }

        List<PlaceResponse> balancedCandidatePlaces = new ArrayList<>(selectedCandidatePlaces);
        includeCategoryGroupCandidate(
                balancedCandidatePlaces,
                candidatePlaces,
                mustVisitPlaceIds,
                comparator,
                Set.of("FOOD")
        );
        includeCategoryGroupCandidate(
                balancedCandidatePlaces,
                candidatePlaces,
                mustVisitPlaceIds,
                comparator,
                Set.of("CAFE")
        );
        includeCategoryGroupCandidate(
                balancedCandidatePlaces,
                candidatePlaces,
                mustVisitPlaceIds,
                comparator,
                TOUR_CATEGORY_NAMES
        );
        return balancedCandidatePlaces;
    }

    private void includeCategoryGroupCandidate(
            List<PlaceResponse> selectedCandidatePlaces,
            List<PlaceResponse> candidatePlaces,
            Set<Long> mustVisitPlaceIds,
            Comparator<PlaceResponse> comparator,
            Set<String> categoryNames
    ) {
        if (containsCategory(selectedCandidatePlaces, categoryNames)) {
            return;
        }

        Set<Long> selectedPlaceIds = selectedCandidatePlaces.stream()
                .map(PlaceResponse::placeId)
                .collect(Collectors.toSet());

        PlaceResponse categoryCandidate = candidatePlaces.stream()
                .filter(candidatePlace -> !selectedPlaceIds.contains(candidatePlace.placeId()))
                .filter(candidatePlace -> categoryNames.contains(candidatePlace.category()))
                .min(comparator)
                .orElse(null);
        if (categoryCandidate == null) {
            return;
        }

        int replacementIndex = lastReplaceableCandidateIndex(selectedCandidatePlaces, mustVisitPlaceIds);
        if (replacementIndex < 0) {
            return;
        }

        selectedCandidatePlaces.set(replacementIndex, categoryCandidate);
    }

    private boolean containsCategory(List<PlaceResponse> places, Set<String> categoryNames) {
        return places.stream()
                .anyMatch(place -> categoryNames.contains(place.category()));
    }

    private int lastReplaceableCandidateIndex(List<PlaceResponse> selectedCandidatePlaces, Set<Long> mustVisitPlaceIds) {
        for (int index = selectedCandidatePlaces.size() - 1; index >= 0; index--) {
            PlaceResponse candidatePlace = selectedCandidatePlaces.get(index);
            if (!mustVisitPlaceIds.contains(candidatePlace.placeId())
                    && !MEAL_AND_REST_CATEGORY_NAMES.contains(candidatePlace.category())
                    && !TOUR_CATEGORY_NAMES.contains(candidatePlace.category())) {
                return index;
            }
        }

        for (int index = selectedCandidatePlaces.size() - 1; index >= 0; index--) {
            PlaceResponse candidatePlace = selectedCandidatePlaces.get(index);
            if (!mustVisitPlaceIds.contains(candidatePlace.placeId())
                    && keepsExistingBalanceCategory(selectedCandidatePlaces, candidatePlace)) {
                return index;
            }
        }

        return -1;
    }

    private boolean keepsExistingBalanceCategory(
            List<PlaceResponse> selectedCandidatePlaces,
            PlaceResponse candidatePlace
    ) {
        if ("FOOD".equals(candidatePlace.category())) {
            return countCategory(selectedCandidatePlaces, Set.of("FOOD")) > 1;
        }
        if ("CAFE".equals(candidatePlace.category())) {
            return countCategory(selectedCandidatePlaces, Set.of("CAFE")) > 1;
        }
        if (TOUR_CATEGORY_NAMES.contains(candidatePlace.category())) {
            return countCategory(selectedCandidatePlaces, TOUR_CATEGORY_NAMES) > 1;
        }
        return true;
    }

    private long countCategory(List<PlaceResponse> places, Set<String> categoryNames) {
        return places.stream()
                .filter(place -> categoryNames.contains(place.category()))
                .count();
    }

    private List<PlaceResponse> includeRegionBalanceCandidates(
            List<PlaceResponse> selectedCandidatePlaces,
            List<PlaceResponse> candidatePlaces,
            Set<Long> mustVisitPlaceIds,
            Comparator<PlaceResponse> comparator
    ) {
        if (selectedCandidatePlaces.size() >= candidatePlaces.size()) {
            return selectedCandidatePlaces;
        }

        int targetRegionCount = Math.min(
                Math.min(MAX_BALANCED_REGION_COUNT, countDistinctRegions(candidatePlaces)),
                selectedCandidatePlaces.size()
        );
        if (countDistinctRegions(selectedCandidatePlaces) >= targetRegionCount) {
            return selectedCandidatePlaces;
        }

        List<PlaceResponse> regionBalancedCandidatePlaces = new ArrayList<>(selectedCandidatePlaces);
        while (countDistinctRegions(regionBalancedCandidatePlaces) < targetRegionCount) {
            Set<Long> selectedPlaceIds = regionBalancedCandidatePlaces.stream()
                    .map(PlaceResponse::placeId)
                    .collect(Collectors.toSet());
            Set<String> selectedRegions = regionBalancedCandidatePlaces.stream()
                    .map(PlaceResponse::region)
                    .filter(this::hasRegion)
                    .collect(Collectors.toSet());

            PlaceResponse regionCandidate = candidatePlaces.stream()
                    .filter(candidatePlace -> !selectedPlaceIds.contains(candidatePlace.placeId()))
                    .filter(candidatePlace -> hasRegion(candidatePlace.region()))
                    .filter(candidatePlace -> !selectedRegions.contains(candidatePlace.region()))
                    .min(comparator)
                    .orElse(null);
            if (regionCandidate == null) {
                return regionBalancedCandidatePlaces;
            }

            int replacementIndex = lastReplaceableRegionCandidateIndex(
                    regionBalancedCandidatePlaces,
                    mustVisitPlaceIds
            );
            if (replacementIndex < 0) {
                return regionBalancedCandidatePlaces;
            }

            regionBalancedCandidatePlaces.set(replacementIndex, regionCandidate);
        }

        return regionBalancedCandidatePlaces;
    }

    private int lastReplaceableRegionCandidateIndex(
            List<PlaceResponse> selectedCandidatePlaces,
            Set<Long> mustVisitPlaceIds
    ) {
        for (int index = selectedCandidatePlaces.size() - 1; index >= 0; index--) {
            PlaceResponse candidatePlace = selectedCandidatePlaces.get(index);
            if (!mustVisitPlaceIds.contains(candidatePlace.placeId())
                    && countRegion(selectedCandidatePlaces, candidatePlace.region()) > 1
                    && !MEAL_AND_REST_CATEGORY_NAMES.contains(candidatePlace.category())
                    && !TOUR_CATEGORY_NAMES.contains(candidatePlace.category())) {
                return index;
            }
        }

        for (int index = selectedCandidatePlaces.size() - 1; index >= 0; index--) {
            PlaceResponse candidatePlace = selectedCandidatePlaces.get(index);
            if (!mustVisitPlaceIds.contains(candidatePlace.placeId())
                    && countRegion(selectedCandidatePlaces, candidatePlace.region()) > 1
                    && keepsExistingBalanceCategory(selectedCandidatePlaces, candidatePlace)) {
                return index;
            }
        }

        return -1;
    }

    private int countDistinctRegions(List<PlaceResponse> places) {
        return (int) places.stream()
                .map(PlaceResponse::region)
                .filter(this::hasRegion)
                .distinct()
                .count();
    }

    private long countRegion(List<PlaceResponse> places, String region) {
        if (!hasRegion(region)) {
            return 0;
        }

        return places.stream()
                .filter(place -> region.equals(place.region()))
                .count();
    }

    private boolean hasRegion(String region) {
        return region != null && !region.isBlank();
    }

    private void validateCandidatePlacesEnoughForTripDays(
            Trip trip,
            List<PlaceResponse> selectedCandidatePlaces,
            Integer targetDayNo
    ) {
        long tripDays = generationDayCount(trip, targetDayNo);
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

    private void validateCandidatePlacesEnoughForPace(
            Trip trip,
            ItineraryGenerateRequest request,
            List<PlaceResponse> selectedCandidatePlaces,
            Integer targetDayNo
    ) {
        if (request == null || request.pace() == null) {
            return;
        }

        PaceItineraryPolicy pacePolicy = pacePolicy(request);
        long tripDays = generationDayCount(trip, targetDayNo);
        long requiredMinItems = tripDays * pacePolicy.minItemsPerDay();
        int candidateCount = selectedCandidatePlaces.size();

        if (candidateCount < requiredMinItems) {
            throw new IllegalArgumentException(
                    "Candidate places are not enough to satisfy pace policy. pace="
                            + pacePolicy.pace()
                            + ", tripDays="
                            + tripDays
                            + ", requiredMinItems="
                            + requiredMinItems
                            + ", candidateCount="
                            + candidateCount
                            + ". Add more candidate places or choose a slower pace."
            );
        }
    }

    private void validateMustVisitPlacesFitPace(
            Trip trip,
            ItineraryGenerateRequest request,
            Integer targetDayNo
    ) {
        if (request == null || request.pace() == null) {
            return;
        }

        PaceItineraryPolicy pacePolicy = pacePolicy(request);
        long tripDays = generationDayCount(trip, targetDayNo);
        long allowedMaxItems = tripDays * pacePolicy.maxItemsPerDay();
        int mustVisitPlaceCount = request.normalizedMustVisitPlaceIds().size();

        if (mustVisitPlaceCount > allowedMaxItems) {
            throw new IllegalArgumentException(
                    "mustVisitPlaceIds are too many to satisfy pace policy. pace="
                            + pacePolicy.pace()
                            + ", tripDays="
                            + tripDays
                            + ", allowedMaxItems="
                            + allowedMaxItems
                            + ", mustVisitPlaceCount="
                            + mustVisitPlaceCount
                            + ". Remove must-visit places or choose a busier pace."
            );
        }
    }

    private PaceItineraryPolicy pacePolicy(ItineraryGenerateRequest request) {
        return PaceItineraryPolicy.findByPace(request.normalizedPace())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Pace itinerary policy is not defined. pace=" + request.normalizedPace()
                ));
    }

    private int generationDayCount(Trip trip, Integer targetDayNo) {
        if (targetDayNo != null) {
            return 1;
        }
        return Math.toIntExact(ChronoUnit.DAYS.between(trip.getStartDate(), trip.getEndDate()) + 1);
    }

    private Comparator<PlaceResponse> candidatePlaceComparator(
            Trip trip,
            ItineraryGenerateRequest request,
            Map<Integer, String> accommodationRegionByDayNo
    ) {
        return Comparator
                .comparing((PlaceResponse place) -> isPreferredCategory(place, request))
                .reversed()
                .thenComparing(Comparator.comparing((PlaceResponse place) -> rainyDayScore(place, request)).reversed())
                .thenComparing(Comparator.comparing((PlaceResponse place) -> conceptScore(place, trip.getConcept())).reversed())
                .thenComparing(Comparator.comparing((PlaceResponse place) ->
                        isDailyAccommodationRegion(place, accommodationRegionByDayNo)).reversed())
                .thenComparing(Comparator.comparing((PlaceResponse place) ->
                        isLastAccommodationRegion(place, trip)).reversed())
                .thenComparing(PlaceResponse::placeId);
    }

    private int rainyDayScore(PlaceResponse place, ItineraryGenerateRequest request) {
        if (request == null
                || (!request.normalizedRainyDayMode() && request.normalizedRainyDayNos().isEmpty())) {
            return 0;
        }

        return rainyDayScore(place);
    }

    private int rainyDayScore(PlaceResponse place) {
        int indoorScore = Boolean.TRUE.equals(place.indoorYn()) ? 1_000 : 0;
        int rainyDayScore = place.rainyDayScore() == null ? 0 : place.rainyDayScore();
        return indoorScore + rainyDayScore;
    }

    private boolean isRainyDay(ItineraryGenerateRequest request, int dayNo) {
        return request != null
                && (request.normalizedRainyDayMode() || request.normalizedRainyDayNos().contains(dayNo));
    }

    private boolean hasRainyDayAfter(ItineraryGenerateRequest request, int dayNo) {
        return request != null
                && !request.normalizedRainyDayMode()
                && request.normalizedRainyDayNos().stream().anyMatch(rainyDayNo -> rainyDayNo > dayNo);
    }

    private boolean isDailyAccommodationRegion(
            PlaceResponse place,
            Map<Integer, String> accommodationRegionByDayNo
    ) {
        return accommodationRegionByDayNo.values().stream()
                .anyMatch(region -> isSameArea(region, place.region()));
    }

    private boolean isLastAccommodationRegion(PlaceResponse place, Trip trip) {
        String accommodationRegion = accommodationAreaRegionMapper.toPlaceRegion(trip.getLastAccommodationArea());
        return accommodationRegion != null && accommodationRegion.equals(place.region());
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

    private void logFallbackDraftGenerationSuccess(
            Trip trip,
            String operation,
            ItineraryGenerateRequest request,
            List<PlaceResponse> selectedCandidatePlaces,
            RuntimeException originalException
    ) {
        log.warn(
                "Fallback itinerary draft generation succeeded. tripId={}, operation={}, originalExceptionClass={}, "
                        + "originalReason={}, mustVisitPlaceIdsCount={}, excludedPlaceIdsCount={}, "
                        + "preferredCategoriesCount={}, selectedCandidatePlacesCount={}, fallbackSuccess={}",
                trip.getTripId(),
                operation,
                originalException.getClass().getSimpleName(),
                originalException.getMessage(),
                mustVisitPlaceIdsCount(request),
                excludedPlaceIdsCount(request),
                preferredCategoriesCount(request),
                selectedCandidatePlaces.size(),
                true
        );
    }

    private void logFallbackDraftGenerationFailure(
            Trip trip,
            String operation,
            ItineraryGenerateRequest request,
            List<PlaceResponse> selectedCandidatePlaces,
            RuntimeException originalException,
            RuntimeException fallbackException
    ) {
        log.warn(
                "Fallback itinerary draft generation failed. tripId={}, operation={}, originalExceptionClass={}, "
                        + "originalReason={}, fallbackExceptionClass={}, fallbackReason={}, "
                        + "mustVisitPlaceIdsCount={}, excludedPlaceIdsCount={}, preferredCategoriesCount={}, "
                        + "selectedCandidatePlacesCount={}, fallbackSuccess={}",
                trip.getTripId(),
                operation,
                originalException.getClass().getSimpleName(),
                originalException.getMessage(),
                fallbackException.getClass().getSimpleName(),
                fallbackException.getMessage(),
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
