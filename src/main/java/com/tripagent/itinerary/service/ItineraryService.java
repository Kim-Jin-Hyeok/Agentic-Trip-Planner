package com.tripagent.itinerary.service;

import com.tripagent.itinerary.domain.Itinerary;
import com.tripagent.itinerary.domain.ItineraryGenerationSource;
import com.tripagent.itinerary.dto.ItineraryCreateRequest;
import com.tripagent.itinerary.dto.ItineraryReorderRequest;
import com.tripagent.itinerary.dto.ItineraryReorderRequestItem;
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.itinerary.dto.ItineraryUpdateRequest;
import com.tripagent.itinerary.repository.ItineraryRepository;
import com.tripagent.place.domain.Place;
import com.tripagent.place.repository.PlaceRepository;
import com.tripagent.route.RouteCalculationAdapter;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.repository.TripRepository;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final TripRepository tripRepository;
    private final PlaceRepository placeRepository;
    private final RouteCalculationAdapter routeCalculationAdapter;

    public ItineraryService(
            ItineraryRepository itineraryRepository,
            TripRepository tripRepository,
            PlaceRepository placeRepository,
            RouteCalculationAdapter routeCalculationAdapter
    ) {
        this.itineraryRepository = itineraryRepository;
        this.tripRepository = tripRepository;
        this.placeRepository = placeRepository;
        this.routeCalculationAdapter = routeCalculationAdapter;
    }

    @Transactional
    public ItineraryResponse createItinerary(Long tripId, ItineraryCreateRequest request) {
        return createItinerary(tripId, request, null);
    }

    @Transactional
    public ItineraryResponse createItinerary(Long tripId, ItineraryCreateRequest request, Long ownerId) {
        return createItinerary(tripId, request, ownerId, ItineraryGenerationSource.MANUAL);
    }

    @Transactional
    public ItineraryResponse createGeneratedItinerary(
            Long tripId,
            ItineraryCreateRequest request,
            ItineraryGenerationSource generationSource
    ) {
        return createItinerary(tripId, request, null, generationSource);
    }

    private ItineraryResponse createItinerary(
            Long tripId,
            ItineraryCreateRequest request,
            Long ownerId,
            ItineraryGenerationSource generationSource
    ) {
        validateRequest(tripId, request);

        Trip trip = findTripAndValidateOwner(tripId, ownerId);
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
        ItineraryCreateValues createValues = resolveCreateValues(
                trip.getTripId(),
                request,
                place,
                generationSource
        );

        if (!createValues.matches(request)) {
            validateItineraryValues(
                    trip,
                    null,
                    request.dayNo(),
                    request.orderNo(),
                    createValues.startTime(),
                    createValues.endTime(),
                    createValues.travelMinutesFromPrevious()
            );
        }

        Itinerary itinerary = Itinerary.create(
                trip,
                place,
                request.dayNo(),
                request.orderNo(),
                createValues.startTime(),
                createValues.endTime(),
                createValues.travelMinutesFromPrevious(),
                request.reason(),
                generationSource
        );

        return ItineraryResponse.from(itineraryRepository.save(itinerary));
    }

    private ItineraryCreateValues resolveCreateValues(
            Long tripId,
            ItineraryCreateRequest request,
            Place place,
            ItineraryGenerationSource generationSource
    ) {
        ItineraryCreateValues requestedValues = new ItineraryCreateValues(
                request.startTime(),
                request.endTime(),
                request.travelMinutesFromPrevious()
        );
        if (generationSource != ItineraryGenerationSource.MANUAL) {
            return requestedValues;
        }

        Itinerary lastItinerary = itineraryRepository.findByTrip_TripIdAndDayNo(tripId, request.dayNo())
                .stream()
                .max(Comparator.comparing(Itinerary::getOrderNo))
                .orElse(null);
        if (lastItinerary == null || request.orderNo() != lastItinerary.getOrderNo() + 1) {
            return requestedValues;
        }

        int stayMinutes = (int) ChronoUnit.MINUTES.between(request.startTime(), request.endTime());
        Place previousPlace = lastItinerary.getPlace();
        int travelMinutes = routeCalculationAdapter.calculateTravelMinutes(
                previousPlace.getLatitude(),
                previousPlace.getLongitude(),
                place.getLatitude(),
                place.getLongitude()
        );
        LocalTime startTime = lastItinerary.getEndTime().plusMinutes(travelMinutes);

        return new ItineraryCreateValues(
                startTime,
                startTime.plusMinutes(stayMinutes),
                travelMinutes
        );
    }

    @Transactional
    public ItineraryResponse createGeneratedItinerary(
            Long tripId,
            ItineraryCreateRequest request,
            LocalTime dayTimeWindowStartTime,
            LocalTime dayTimeWindowEndTime
    ) {
        return createGeneratedItinerary(
                tripId, request, dayTimeWindowStartTime, dayTimeWindowEndTime,
                ItineraryGenerationSource.LLM
        );
    }

    @Transactional
    public ItineraryResponse createGeneratedItinerary(
            Long tripId,
            ItineraryCreateRequest request,
            LocalTime dayTimeWindowStartTime,
            LocalTime dayTimeWindowEndTime,
            ItineraryGenerationSource generationSource
    ) {
        validateRequest(tripId, request);
        validateDayTimeWindow(dayTimeWindowStartTime, dayTimeWindowEndTime);

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));

        validateGeneratedItineraryValues(
                trip,
                null,
                request.dayNo(),
                request.orderNo(),
                request.startTime(),
                request.endTime(),
                request.travelMinutesFromPrevious(),
                dayTimeWindowStartTime,
                dayTimeWindowEndTime
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
                request.reason(),
                generationSource
        );

        return ItineraryResponse.from(itineraryRepository.save(itinerary));
    }

    @Transactional
    public ItineraryResponse updateItinerary(Long tripId, Long itineraryId, ItineraryUpdateRequest request) {
        return updateItinerary(tripId, itineraryId, request, null);
    }

    @Transactional
    public ItineraryResponse updateItinerary(
            Long tripId,
            Long itineraryId,
            ItineraryUpdateRequest request,
            Long ownerId
    ) {
        validateUpdateRequest(tripId, itineraryId, request);

        Trip trip = findTripAndValidateOwner(tripId, ownerId);
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

        boolean changesPlaceInSamePosition = !updatedPlaceId.equals(itinerary.getPlace().getPlaceId())
                && updatedDayNo.equals(itinerary.getDayNo())
                && updatedOrderNo.equals(itinerary.getOrderNo());
        if (changesPlaceInSamePosition) {
            return updatePlaceAndRecalculateDay(
                    trip,
                    itinerary,
                    updatedPlace,
                    updatedStartTime,
                    updatedEndTime,
                    updatedReason
            );
        }

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
        itinerary.markAsUserAdjusted();

        return ItineraryResponse.from(itinerary);
    }

    private ItineraryResponse updatePlaceAndRecalculateDay(
            Trip trip,
            Itinerary targetItinerary,
            Place updatedPlace,
            LocalTime requestedStartTime,
            LocalTime requestedEndTime,
            String updatedReason
    ) {
        List<Itinerary> dayItineraries = itineraryRepository.findByTrip_TripIdAndDayNo(
                        trip.getTripId(),
                        targetItinerary.getDayNo()
                ).stream()
                .sorted(Comparator.comparing(Itinerary::getOrderNo))
                .toList();
        int targetIndex = findItineraryIndex(dayItineraries, targetItinerary.getItineraryId());
        List<ReorderState> recalculatedStates = buildPlaceUpdateStates(
                dayItineraries,
                targetIndex,
                updatedPlace,
                requestedStartTime,
                requestedEndTime
        );
        validateFinalReorderStates(trip, recalculatedStates);

        Map<Long, ReorderState> recalculatedStateById = recalculatedStates.stream()
                .collect(java.util.stream.Collectors.toMap(ReorderState::itineraryId, state -> state));
        for (Itinerary dayItinerary : dayItineraries) {
            ReorderState state = recalculatedStateById.get(dayItinerary.getItineraryId());
            Place place = dayItinerary.getItineraryId().equals(targetItinerary.getItineraryId())
                    ? updatedPlace
                    : dayItinerary.getPlace();
            String reason = dayItinerary.getItineraryId().equals(targetItinerary.getItineraryId())
                    ? updatedReason
                    : dayItinerary.getReason();
            if (hasSameState(dayItinerary, state) && dayItinerary.getPlace().getPlaceId().equals(place.getPlaceId())) {
                continue;
            }

            dayItinerary.update(
                    place,
                    state.dayNo(),
                    state.orderNo(),
                    state.startTime(),
                    state.endTime(),
                    state.travelMinutesFromPrevious(),
                    reason
            );
            dayItinerary.markAsUserAdjusted();
        }

        return ItineraryResponse.from(targetItinerary);
    }

    private int findItineraryIndex(List<Itinerary> itineraries, Long itineraryId) {
        for (int index = 0; index < itineraries.size(); index++) {
            if (itineraries.get(index).getItineraryId().equals(itineraryId)) {
                return index;
            }
        }
        throw new IllegalArgumentException("Itinerary must exist in its trip day. itineraryId=" + itineraryId);
    }

    private List<ReorderState> buildPlaceUpdateStates(
            List<Itinerary> dayItineraries,
            int targetIndex,
            Place updatedPlace,
            LocalTime requestedStartTime,
            LocalTime requestedEndTime
    ) {
        List<ReorderState> states = new java.util.ArrayList<>();
        for (int index = 0; index < dayItineraries.size(); index++) {
            Itinerary currentItinerary = dayItineraries.get(index);
            LocalTime startTime = currentItinerary.getStartTime();
            LocalTime endTime = currentItinerary.getEndTime();
            int travelMinutes = currentItinerary.getTravelMinutesFromPrevious();

            if (index >= targetIndex) {
                int stayMinutes = index == targetIndex
                        ? (int) ChronoUnit.MINUTES.between(requestedStartTime, requestedEndTime)
                        : (int) ChronoUnit.MINUTES.between(
                                currentItinerary.getStartTime(),
                                currentItinerary.getEndTime()
                        );
                if (index == 0) {
                    startTime = requestedStartTime;
                    travelMinutes = 0;
                } else {
                    Itinerary previousItinerary = dayItineraries.get(index - 1);
                    Place previousPlace = index - 1 == targetIndex
                            ? updatedPlace
                            : previousItinerary.getPlace();
                    Place currentPlace = index == targetIndex
                            ? updatedPlace
                            : currentItinerary.getPlace();
                    travelMinutes = routeCalculationAdapter.calculateTravelMinutes(
                            previousPlace.getLatitude(),
                            previousPlace.getLongitude(),
                            currentPlace.getLatitude(),
                            currentPlace.getLongitude()
                    );
                    startTime = states.get(index - 1).endTime().plusMinutes(travelMinutes);
                }
                endTime = startTime.plusMinutes(stayMinutes);
            }

            states.add(new ReorderState(
                    currentItinerary.getItineraryId(),
                    currentItinerary.getDayNo(),
                    currentItinerary.getOrderNo(),
                    startTime,
                    endTime,
                    travelMinutes
            ));
        }
        return states;
    }

    @Transactional
    public List<ItineraryResponse> reorderItineraries(Long tripId, ItineraryReorderRequest request) {
        return reorderItineraries(tripId, request, null);
    }

    @Transactional
    public List<ItineraryResponse> reorderItineraries(
            Long tripId,
            ItineraryReorderRequest request,
            Long ownerId
    ) {
        validateReorderRequest(tripId, request);

        Trip trip = findTripAndValidateOwner(tripId, ownerId);
        Map<Long, ItineraryReorderRequestItem> reorderItems = validateAndMapReorderItems(tripId, request);
        List<Itinerary> tripItineraries = itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(tripId);
        List<ReorderState> finalStates = recalculateSameDayReorderStates(
                tripItineraries,
                reorderItems,
                buildReorderStates(tripItineraries, reorderItems)
        );

        validateFinalReorderStates(trip, finalStates);
        Map<Long, ReorderState> finalStateByItineraryId = finalStates.stream()
                .collect(java.util.stream.Collectors.toMap(ReorderState::itineraryId, state -> state));

        for (Itinerary itinerary : tripItineraries) {
            ReorderState finalState = finalStateByItineraryId.get(itinerary.getItineraryId());
            if (hasSameState(itinerary, finalState)) {
                continue;
            }
            itinerary.update(
                    itinerary.getPlace(),
                    finalState.dayNo(),
                    finalState.orderNo(),
                    finalState.startTime(),
                    finalState.endTime(),
                    finalState.travelMinutesFromPrevious(),
                    itinerary.getReason()
            );
            itinerary.markAsUserAdjusted();
        }

        return tripItineraries.stream()
                .sorted(Comparator.comparing(Itinerary::getDayNo).thenComparing(Itinerary::getOrderNo))
                .map(ItineraryResponse::from)
                .toList();
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

        deleteAndNormalizeDay(itinerary);
    }

    @Transactional
    public void deleteItinerary(Long tripId, Long itineraryId, Long ownerId) {
        validateTripAndItineraryIds(tripId, itineraryId);
        findTripAndValidateOwner(tripId, ownerId);

        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new NoSuchElementException("Itinerary not found. itineraryId=" + itineraryId));
        validateItineraryBelongsToTrip(itinerary, tripId);

        deleteAndNormalizeDay(itinerary);
    }

    private void deleteAndNormalizeDay(Itinerary itinerary) {
        List<Itinerary> dayItineraries = itineraryRepository.findByTrip_TripIdAndDayNo(
                itinerary.getTrip().getTripId(),
                itinerary.getDayNo()
        );
        List<Itinerary> remainingItineraries = dayItineraries.stream()
                .filter(dayItinerary -> !dayItinerary.getItineraryId().equals(itinerary.getItineraryId()))
                .sorted(Comparator.comparing(Itinerary::getOrderNo))
                .toList();
        List<ReorderState> normalizedStates = buildDeleteStates(itinerary, remainingItineraries);
        for (ReorderState state : normalizedStates) {
            validateTimeRange(state.startTime(), state.endTime());
            validateFirstStartTime(itinerary.getTrip(), state.orderNo(), state.startTime());
            validateDailyEndTime(itinerary.getTrip(), state.endTime());
        }

        itineraryRepository.delete(itinerary);
        Map<Long, ReorderState> normalizedStateById = normalizedStates.stream()
                .collect(java.util.stream.Collectors.toMap(ReorderState::itineraryId, state -> state));
        for (Itinerary remainingItinerary : remainingItineraries) {
            ReorderState state = normalizedStateById.get(remainingItinerary.getItineraryId());
            if (hasSameState(remainingItinerary, state)) {
                continue;
            }

            remainingItinerary.update(
                    remainingItinerary.getPlace(),
                    state.dayNo(),
                    state.orderNo(),
                    state.startTime(),
                    state.endTime(),
                    state.travelMinutesFromPrevious(),
                    remainingItinerary.getReason()
            );
            remainingItinerary.markAsUserAdjusted();
        }
    }

    private List<ReorderState> buildDeleteStates(
            Itinerary deletedItinerary,
            List<Itinerary> remainingItineraries
    ) {
        int successorIndex = -1;
        for (int index = 0; index < remainingItineraries.size(); index++) {
            if (remainingItineraries.get(index).getOrderNo() > deletedItinerary.getOrderNo()) {
                successorIndex = index;
                break;
            }
        }

        List<ReorderState> states = new java.util.ArrayList<>();
        for (int index = 0; index < remainingItineraries.size(); index++) {
            Itinerary currentItinerary = remainingItineraries.get(index);
            int orderNo = index + 1;
            LocalTime startTime = currentItinerary.getStartTime();
            LocalTime endTime = currentItinerary.getEndTime();
            int travelMinutes = orderNo == 1 ? 0 : currentItinerary.getTravelMinutesFromPrevious();

            if (successorIndex >= 0 && index >= successorIndex) {
                int stayMinutes = (int) ChronoUnit.MINUTES.between(
                        currentItinerary.getStartTime(),
                        currentItinerary.getEndTime()
                );
                if (index == 0) {
                    startTime = deletedItinerary.getStartTime();
                    travelMinutes = 0;
                } else {
                    Itinerary previousItinerary = remainingItineraries.get(index - 1);
                    Place previousPlace = previousItinerary.getPlace();
                    Place currentPlace = currentItinerary.getPlace();
                    travelMinutes = routeCalculationAdapter.calculateTravelMinutes(
                            previousPlace.getLatitude(),
                            previousPlace.getLongitude(),
                            currentPlace.getLatitude(),
                            currentPlace.getLongitude()
                    );
                    startTime = states.get(index - 1).endTime().plusMinutes(travelMinutes);
                }
                endTime = startTime.plusMinutes(stayMinutes);
            }

            states.add(new ReorderState(
                    currentItinerary.getItineraryId(),
                    currentItinerary.getDayNo(),
                    orderNo,
                    startTime,
                    endTime,
                    travelMinutes
            ));
        }
        return states;
    }

    public List<ItineraryResponse> getItineraries(Long tripId) {
        if (!tripRepository.existsById(tripId)) {
            throw new NoSuchElementException("Trip not found. tripId=" + tripId);
        }

        return itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(tripId).stream()
                .map(ItineraryResponse::from)
                .toList();
    }

    public List<ItineraryResponse> getItineraries(Long tripId, Long ownerId) {
        findTripAndValidateOwner(tripId, ownerId);

        return itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(tripId).stream()
                .map(ItineraryResponse::from)
                .toList();
    }

    private boolean hasSameState(Itinerary itinerary, ReorderState state) {
        return itinerary.getDayNo().equals(state.dayNo())
                && itinerary.getOrderNo().equals(state.orderNo())
                && itinerary.getStartTime().equals(state.startTime())
                && itinerary.getEndTime().equals(state.endTime())
                && itinerary.getTravelMinutesFromPrevious().equals(state.travelMinutesFromPrevious());
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

    private void validateGeneratedItineraryValues(
            Trip trip,
            Long excludedItineraryId,
            Integer dayNo,
            Integer orderNo,
            LocalTime startTime,
            LocalTime endTime,
            Integer travelMinutesFromPrevious,
            LocalTime dayTimeWindowStartTime,
            LocalTime dayTimeWindowEndTime
    ) {
        validateTimeRange(startTime, endTime);
        validateDayNoInTripPeriod(trip, dayNo);
        validateFirstTravelMinutes(orderNo, travelMinutesFromPrevious);
        validateDayTimeWindowStartTime(startTime, dayTimeWindowStartTime);
        validateDayTimeWindowEndTime(endTime, dayTimeWindowEndTime);
        validateNoDuplicatedOrder(trip.getTripId(), excludedItineraryId, dayNo, orderNo);
        validateNoTimeOverlap(trip.getTripId(), excludedItineraryId, dayNo, startTime, endTime);
    }

    private void validateDayTimeWindow(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Day time window startTime and endTime are required.");
        }
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Day time window startTime must be before endTime.");
        }
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

    private void validateDayTimeWindowStartTime(LocalTime startTime, LocalTime dayTimeWindowStartTime) {
        if (startTime.isBefore(dayTimeWindowStartTime)) {
            throw new IllegalArgumentException(
                    "First itinerary item of each day must start at or after the day time window startTime."
            );
        }
    }

    private void validateDayTimeWindowEndTime(LocalTime endTime, LocalTime dayTimeWindowEndTime) {
        if (endTime.isAfter(dayTimeWindowEndTime)) {
            throw new IllegalArgumentException(
                    "Itinerary endTime must be at or before the day time window endTime."
            );
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

    private void validateReorderRequest(Long tripId, ItineraryReorderRequest request) {
        if (tripId == null) {
            throw new IllegalArgumentException("Trip id is required.");
        }
        if (request == null) {
            throw new IllegalArgumentException("Itinerary reorder request is required.");
        }
        if (request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("Itinerary reorder items must not be empty.");
        }
    }

    private Map<Long, ItineraryReorderRequestItem> validateAndMapReorderItems(
            Long tripId,
            ItineraryReorderRequest request
    ) {
        Map<Long, ItineraryReorderRequestItem> reorderItems = new HashMap<>();

        for (ItineraryReorderRequestItem item : request.items()) {
            validateReorderRequestItem(item);
            if (reorderItems.putIfAbsent(item.itineraryId(), item) != null) {
                throw new IllegalArgumentException(
                        "Itinerary reorder items must not contain duplicated itineraryId. itineraryId="
                                + item.itineraryId()
                );
            }
        }

        for (ItineraryReorderRequestItem item : reorderItems.values()) {
            Itinerary itinerary = itineraryRepository.findById(item.itineraryId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Itinerary not found. itineraryId=" + item.itineraryId()
                    ));
            validateItineraryBelongsToTrip(itinerary, tripId);
        }

        return reorderItems;
    }

    private void validateReorderRequestItem(ItineraryReorderRequestItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Itinerary reorder item must not be null.");
        }
        if (item.itineraryId() == null) {
            throw new IllegalArgumentException("Itinerary id is required.");
        }
        if (item.dayNo() == null || item.dayNo() < 1) {
            throw new IllegalArgumentException("Itinerary dayNo must be greater than or equal to 1.");
        }
        if (item.orderNo() == null || item.orderNo() < 1) {
            throw new IllegalArgumentException("Itinerary orderNo must be greater than or equal to 1.");
        }
    }

    private List<ReorderState> buildReorderStates(
            List<Itinerary> tripItineraries,
            Map<Long, ItineraryReorderRequestItem> reorderItems
    ) {
        Map<String, Itinerary> itineraryByDayAndOrder = tripItineraries.stream()
                .collect(java.util.stream.Collectors.toMap(
                        itinerary -> itinerary.getDayNo() + ":" + itinerary.getOrderNo(),
                        itinerary -> itinerary
                ));

        return tripItineraries.stream()
                .map(itinerary -> {
                    ItineraryReorderRequestItem item = reorderItems.get(itinerary.getItineraryId());
                    Integer dayNo = item == null ? itinerary.getDayNo() : item.dayNo();
                    Integer orderNo = item == null ? itinerary.getOrderNo() : item.orderNo();
                    Itinerary timeSlot = item == null
                            ? itinerary
                            : itineraryByDayAndOrder.getOrDefault(dayNo + ":" + orderNo, itinerary);

                    return new ReorderState(
                            itinerary.getItineraryId(),
                            dayNo,
                            orderNo,
                            timeSlot.getStartTime(),
                            timeSlot.getEndTime(),
                            timeSlot.getTravelMinutesFromPrevious()
                    );
                })
                .toList();
    }

    private List<ReorderState> recalculateSameDayReorderStates(
            List<Itinerary> tripItineraries,
            Map<Long, ItineraryReorderRequestItem> reorderItems,
            List<ReorderState> reorderStates
    ) {
        Map<Long, Itinerary> itineraryById = tripItineraries.stream()
                .collect(java.util.stream.Collectors.toMap(Itinerary::getItineraryId, itinerary -> itinerary));
        Set<Integer> affectedDayNos = new HashSet<>();

        for (ItineraryReorderRequestItem item : reorderItems.values()) {
            Itinerary itinerary = itineraryById.get(item.itineraryId());
            if (!itinerary.getDayNo().equals(item.dayNo())) {
                return reorderStates;
            }
            if (!itinerary.getOrderNo().equals(item.orderNo())) {
                affectedDayNos.add(item.dayNo());
            }
        }
        if (affectedDayNos.isEmpty()) {
            return reorderStates;
        }

        Map<Long, ReorderState> recalculatedStateById = new HashMap<>();
        for (Integer dayNo : affectedDayNos) {
            List<ReorderState> dayStates = reorderStates.stream()
                    .filter(state -> state.dayNo().equals(dayNo))
                    .sorted(Comparator.comparing(ReorderState::orderNo))
                    .toList();
            LocalTime cursor = dayStates.stream()
                    .map(ReorderState::startTime)
                    .min(LocalTime::compareTo)
                    .orElseThrow();

            Itinerary previousItinerary = null;
            for (ReorderState state : dayStates) {
                Itinerary currentItinerary = itineraryById.get(state.itineraryId());
                int stayMinutes = (int) ChronoUnit.MINUTES.between(
                        currentItinerary.getStartTime(),
                        currentItinerary.getEndTime()
                );
                int travelMinutes = 0;
                if (previousItinerary != null) {
                    Place previousPlace = previousItinerary.getPlace();
                    Place currentPlace = currentItinerary.getPlace();
                    travelMinutes = routeCalculationAdapter.calculateTravelMinutes(
                            previousPlace.getLatitude(),
                            previousPlace.getLongitude(),
                            currentPlace.getLatitude(),
                            currentPlace.getLongitude()
                    );
                    cursor = cursor.plusMinutes(travelMinutes);
                }
                LocalTime endTime = cursor.plusMinutes(stayMinutes);
                recalculatedStateById.put(state.itineraryId(), new ReorderState(
                        state.itineraryId(),
                        state.dayNo(),
                        state.orderNo(),
                        cursor,
                        endTime,
                        travelMinutes
                ));
                cursor = endTime;
                previousItinerary = currentItinerary;
            }
        }

        return reorderStates.stream()
                .map(state -> recalculatedStateById.getOrDefault(state.itineraryId(), state))
                .toList();
    }

    private void validateFinalReorderStates(Trip trip, List<ReorderState> finalStates) {
        Set<String> dayOrderKeys = new HashSet<>();

        for (ReorderState state : finalStates) {
            validateTimeRange(state.startTime(), state.endTime());
            validateDayNoInTripPeriod(trip, state.dayNo());
            validateFirstTravelMinutes(state.orderNo(), state.travelMinutesFromPrevious());
            validateFirstStartTime(trip, state.orderNo(), state.startTime());
            validateDailyEndTime(trip, state.endTime());

            String dayOrderKey = state.dayNo() + ":" + state.orderNo();
            if (!dayOrderKeys.add(dayOrderKey)) {
                throw new IllegalArgumentException(
                        "Itinerary dayNo and orderNo already exist in this trip."
                );
            }
        }

        validateNoFinalTimeOverlap(finalStates);
        validateFinalOrderTimeSequence(finalStates);
    }

    private void validateNoFinalTimeOverlap(List<ReorderState> finalStates) {
        List<ReorderState> sortedStates = finalStates.stream()
                .sorted(Comparator.comparing(ReorderState::dayNo).thenComparing(ReorderState::startTime))
                .toList();

        for (int index = 1; index < sortedStates.size(); index++) {
            ReorderState previous = sortedStates.get(index - 1);
            ReorderState current = sortedStates.get(index);

            if (previous.dayNo().equals(current.dayNo()) && current.startTime().isBefore(previous.endTime())) {
                throw new IllegalArgumentException("Itinerary time overlaps with existing itinerary.");
            }
        }
    }

    private void validateFinalOrderTimeSequence(List<ReorderState> finalStates) {
        List<ReorderState> sortedStates = finalStates.stream()
                .sorted(Comparator.comparing(ReorderState::dayNo).thenComparing(ReorderState::orderNo))
                .toList();

        for (int index = 1; index < sortedStates.size(); index++) {
            ReorderState previous = sortedStates.get(index - 1);
            ReorderState current = sortedStates.get(index);

            if (previous.dayNo().equals(current.dayNo()) && current.startTime().isBefore(previous.endTime())) {
                throw new IllegalArgumentException(
                        "Itinerary orderNo must follow time order within each day. dayNo=" + current.dayNo()
                );
            }
        }
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

    private Trip findTripAndValidateOwner(Long tripId, Long ownerId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NoSuchElementException("Trip not found. tripId=" + tripId));
        validateTripOwner(trip, ownerId);
        return trip;
    }

    private void validateTripOwner(Trip trip, Long ownerId) {
        if (ownerId == null) {
            return;
        }
        if (!ownerId.equals(trip.getOwnerId())) {
            throw new IllegalArgumentException("Trip owner does not match. tripId=" + trip.getTripId());
        }
    }

    private void validateItineraryBelongsToTrip(Itinerary itinerary, Long tripId) {
        if (!itinerary.getTrip().getTripId().equals(tripId)) {
            throw new IllegalArgumentException("Itinerary does not belong to trip. itineraryId=" + itinerary.getItineraryId());
        }
    }

    private record ReorderState(
            Long itineraryId,
            Integer dayNo,
            Integer orderNo,
            LocalTime startTime,
            LocalTime endTime,
            Integer travelMinutesFromPrevious
    ) {
    }

    private record ItineraryCreateValues(
            LocalTime startTime,
            LocalTime endTime,
            Integer travelMinutesFromPrevious
    ) {

        private boolean matches(ItineraryCreateRequest request) {
            return startTime.equals(request.startTime())
                    && endTime.equals(request.endTime())
                    && travelMinutesFromPrevious.equals(request.travelMinutesFromPrevious());
        }
    }
}
