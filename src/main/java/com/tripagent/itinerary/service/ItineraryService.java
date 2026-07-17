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
        List<ReorderState> finalStates = buildReorderStates(tripItineraries, reorderItems);

        validateFinalReorderStates(trip, finalStates);
        Map<Long, ReorderState> finalStateByItineraryId = finalStates.stream()
                .collect(java.util.stream.Collectors.toMap(ReorderState::itineraryId, state -> state));

        for (Itinerary itinerary : tripItineraries) {
            ItineraryReorderRequestItem item = reorderItems.get(itinerary.getItineraryId());
            if (item == null) {
                continue;
            }
            ReorderState finalState = finalStateByItineraryId.get(itinerary.getItineraryId());
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

        itineraryRepository.delete(itinerary);
    }

    @Transactional
    public void deleteItinerary(Long tripId, Long itineraryId, Long ownerId) {
        validateTripAndItineraryIds(tripId, itineraryId);
        findTripAndValidateOwner(tripId, ownerId);

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

    public List<ItineraryResponse> getItineraries(Long tripId, Long ownerId) {
        findTripAndValidateOwner(tripId, ownerId);

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
}
