package com.tripagent.itinerary.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tripagent.ai.validator.CandidatePlaceValidator;
import com.tripagent.itinerary.dto.ItineraryCreateRequest;
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.service.PlaceService;
import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.repository.TripRepository;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItineraryGenerateServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private PlaceService placeService;

    @Mock
    private CandidatePlaceValidator candidatePlaceValidator;

    @Mock
    private ItineraryService itineraryService;

    @InjectMocks
    private ItineraryGenerateService itineraryGenerateService;

    @Test
    void generateDraftItinerariesCreatesDraftsFromCandidatePlaces() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90),
                place(30L, 120),
                place(40L, 60)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);

        List<ItineraryCreateRequest> drafts = itineraryGenerateService.generateDraftItineraries(1L);

        assertThat(drafts).hasSize(3);
        assertThat(drafts).extracting(ItineraryCreateRequest::placeId)
                .containsExactly(10L, 20L, 30L);
        assertThat(drafts).extracting(ItineraryCreateRequest::dayNo)
                .containsExactly(1, 1, 1);
        assertThat(drafts).extracting(ItineraryCreateRequest::orderNo)
                .containsExactly(1, 2, 3);
        assertThat(drafts.get(0).startTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(drafts.get(0).endTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(drafts.get(1).startTime()).isEqualTo(LocalTime.of(10, 30));
        assertThat(drafts.get(1).endTime()).isEqualTo(LocalTime.of(12, 0));
        assertThat(drafts.get(2).startTime()).isEqualTo(LocalTime.of(12, 30));
        assertThat(drafts.get(2).endTime()).isEqualTo(LocalTime.of(14, 30));
        assertThat(drafts).extracting(ItineraryCreateRequest::travelMinutesFromPrevious)
                .containsExactly(0, 30, 30);
        verify(placeService).findCandidatePlaces(TripConcept.FOOD);
        verify(candidatePlaceValidator).validatePlaceIds(candidatePlaces, List.of(10L, 20L, 30L));
    }

    @Test
    void generateDraftItinerariesRejectsUnknownTrip() {
        when(tripRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itineraryGenerateService.generateDraftItineraries(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Trip not found. tripId=1");
    }

    @Test
    void generateDraftItinerariesRejectsNullTripId() {
        assertThatThrownBy(() -> itineraryGenerateService.generateDraftItineraries(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip id is required.");
    }

    @Test
    void generateItinerariesSavesDraftsAndReturnsResponses() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryService.createItinerary(1L, request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)))
                .thenReturn(response(100L, 1L, 10L, 1));
        when(itineraryService.createItinerary(1L, request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)))
                .thenReturn(response(200L, 1L, 20L, 2));

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L);

        assertThat(responses).extracting(ItineraryResponse::itineraryId)
                .containsExactly(100L, 200L);
        assertThat(responses).extracting(ItineraryResponse::placeId)
                .containsExactly(10L, 20L);
        verify(candidatePlaceValidator).validatePlaceIds(candidatePlaces, List.of(10L, 20L));
        verify(itineraryService).createItinerary(
                1L,
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        verify(itineraryService).createItinerary(
                1L,
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
    }

    private Trip trip(Long tripId, TripConcept concept) {
        Trip trip = Trip.create(
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                concept,
                Transportation.RENT_CAR,
                "SEOGWIPO"
        );
        setId(trip, "tripId", tripId);
        return trip;
    }

    private PlaceResponse place(Long placeId, Integer avgStayMinutes) {
        return new PlaceResponse(
                placeId,
                "Place " + placeId,
                "NATURE",
                "EAST",
                "JEJU",
                33.0,
                126.0,
                avgStayMinutes,
                false,
                true,
                1,
                2,
                3,
                4,
                5,
                4,
                3,
                "description"
        );
    }

    private ItineraryCreateRequest request(
            Long placeId,
            Integer orderNo,
            LocalTime startTime,
            LocalTime endTime,
            Integer travelMinutesFromPrevious
    ) {
        return new ItineraryCreateRequest(
                placeId,
                1,
                orderNo,
                startTime,
                endTime,
                travelMinutesFromPrevious,
                "Mock itinerary item selected from candidate places."
        );
    }

    private ItineraryResponse response(Long itineraryId, Long tripId, Long placeId, Integer orderNo) {
        return new ItineraryResponse(
                itineraryId,
                tripId,
                placeId,
                1,
                orderNo,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                0,
                "Mock itinerary item selected from candidate places."
        );
    }

    private void setId(Object target, String fieldName, Long id) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, id);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
