package com.tripagent.itinerary.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tripagent.itinerary.domain.Itinerary;
import com.tripagent.itinerary.dto.ItineraryCreateRequest;
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.itinerary.repository.ItineraryRepository;
import com.tripagent.place.domain.Place;
import com.tripagent.place.repository.PlaceRepository;
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
class ItineraryServiceTest {

    @Mock
    private ItineraryRepository itineraryRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private PlaceRepository placeRepository;

    @InjectMocks
    private ItineraryService itineraryService;

    @Test
    void createItinerarySavesValidRequest() {
        Trip trip = trip(1L);
        Place place = place(10L);
        ItineraryCreateRequest request = request(10L, 1, 1);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeRepository.findById(10L)).thenReturn(Optional.of(place));
        when(itineraryRepository.existsByTrip_TripIdAndDayNoAndOrderNo(1L, 1, 1)).thenReturn(false);
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 1)).thenReturn(List.of());
        when(itineraryRepository.save(any(Itinerary.class))).thenAnswer(invocation -> {
            Itinerary itinerary = invocation.getArgument(0);
            setId(itinerary, "itineraryId", 100L);
            return itinerary;
        });

        ItineraryResponse response = itineraryService.createItinerary(1L, request);

        assertThat(response.itineraryId()).isEqualTo(100L);
        assertThat(response.tripId()).isEqualTo(1L);
        assertThat(response.placeId()).isEqualTo(10L);
        assertThat(response.dayNo()).isEqualTo(1);
        assertThat(response.orderNo()).isEqualTo(1);
        verify(itineraryRepository).save(any(Itinerary.class));
    }

    @Test
    void createItineraryRejectsUnknownTrip() {
        ItineraryCreateRequest request = request(10L, 1, 1);
        when(tripRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itineraryService.createItinerary(1L, request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Trip not found. tripId=1");
        verify(itineraryRepository, never()).save(any(Itinerary.class));
    }

    @Test
    void createItineraryRejectsUnknownPlace() {
        Trip trip = trip(1L);
        ItineraryCreateRequest request = request(10L, 1, 1);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itineraryService.createItinerary(1L, request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Place not found. placeId=10");
        verify(itineraryRepository, never()).save(any(Itinerary.class));
    }

    @Test
    void createItineraryRejectsDayNoOutsideTripPeriod() {
        Trip trip = trip(1L);
        Place place = place(10L);
        ItineraryCreateRequest request = request(10L, 4, 1);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeRepository.findById(10L)).thenReturn(Optional.of(place));

        assertThatThrownBy(() -> itineraryService.createItinerary(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary dayNo must be within trip period. maxDayNo=3");
        verify(itineraryRepository, never()).save(any(Itinerary.class));
    }

    @Test
    void createItineraryRejectsDuplicatedDayNoAndOrderNoInTrip() {
        Trip trip = trip(1L);
        Place place = place(10L);
        ItineraryCreateRequest request = request(10L, 1, 1);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeRepository.findById(10L)).thenReturn(Optional.of(place));
        when(itineraryRepository.existsByTrip_TripIdAndDayNoAndOrderNo(1L, 1, 1)).thenReturn(true);

        assertThatThrownBy(() -> itineraryService.createItinerary(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary dayNo and orderNo already exist in this trip.");
        verify(itineraryRepository, never()).save(any(Itinerary.class));
    }

    @Test
    void createItineraryRejectsTimeOverlapInSameTripDay() {
        Trip trip = trip(1L);
        Place place = place(10L);
        ItineraryCreateRequest request = new ItineraryCreateRequest(
                10L,
                1,
                2,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                30,
                "Overlap request"
        );
        Itinerary existingItinerary = Itinerary.create(
                trip,
                place,
                1,
                1,
                LocalTime.of(9, 30),
                LocalTime.of(10, 30),
                0,
                "Existing itinerary"
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeRepository.findById(10L)).thenReturn(Optional.of(place));
        when(itineraryRepository.existsByTrip_TripIdAndDayNoAndOrderNo(1L, 1, 2)).thenReturn(false);
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 1)).thenReturn(List.of(existingItinerary));

        assertThatThrownBy(() -> itineraryService.createItinerary(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary time overlaps with existing itinerary.");
        verify(itineraryRepository, never()).save(any(Itinerary.class));
    }

    @Test
    void createItineraryRejectsFirstOrderWithTravelMinutes() {
        ItineraryCreateRequest request = new ItineraryCreateRequest(
                10L,
                1,
                1,
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                15,
                "First order must not have previous travel minutes."
        );

        assertThatThrownBy(() -> itineraryService.createItinerary(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("First itinerary item of each day must have travelMinutesFromPrevious 0.");
        verify(tripRepository, never()).findById(1L);
        verify(itineraryRepository, never()).save(any(Itinerary.class));
    }

    @Test
    void createItineraryAllowsAdjacentTimeInSameTripDay() {
        Trip trip = trip(1L);
        Place place = place(10L);
        ItineraryCreateRequest request = new ItineraryCreateRequest(
                10L,
                1,
                2,
                LocalTime.of(10, 30),
                LocalTime.of(11, 30),
                30,
                "Adjacent request"
        );
        Itinerary existingItinerary = Itinerary.create(
                trip,
                place,
                1,
                1,
                LocalTime.of(9, 30),
                LocalTime.of(10, 30),
                0,
                "Existing itinerary"
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeRepository.findById(10L)).thenReturn(Optional.of(place));
        when(itineraryRepository.existsByTrip_TripIdAndDayNoAndOrderNo(1L, 1, 2)).thenReturn(false);
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 1)).thenReturn(List.of(existingItinerary));
        when(itineraryRepository.save(any(Itinerary.class))).thenAnswer(invocation -> {
            Itinerary itinerary = invocation.getArgument(0);
            setId(itinerary, "itineraryId", 101L);
            return itinerary;
        });

        ItineraryResponse response = itineraryService.createItinerary(1L, request);

        assertThat(response.itineraryId()).isEqualTo(101L);
        assertThat(response.orderNo()).isEqualTo(2);
        verify(itineraryRepository).save(any(Itinerary.class));
    }

    @Test
    void getItinerariesReturnsTripItineraries() {
        Trip trip = trip(1L);
        Place place = place(10L);
        Itinerary itinerary = Itinerary.create(
                trip,
                place,
                1,
                1,
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                0,
                "첫 일정으로 방문하기 좋습니다."
        );
        setId(itinerary, "itineraryId", 100L);
        when(tripRepository.existsById(1L)).thenReturn(true);
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L))
                .thenReturn(List.of(itinerary));

        List<ItineraryResponse> responses = itineraryService.getItineraries(1L);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).itineraryId()).isEqualTo(100L);
        assertThat(responses.get(0).tripId()).isEqualTo(1L);
        assertThat(responses.get(0).placeId()).isEqualTo(10L);
    }

    @Test
    void getItinerariesRejectsUnknownTrip() {
        when(tripRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> itineraryService.getItineraries(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Trip not found. tripId=1");
    }

    private ItineraryCreateRequest request(Long placeId, Integer dayNo, Integer orderNo) {
        return new ItineraryCreateRequest(
                placeId,
                dayNo,
                orderNo,
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                0,
                "첫 일정으로 방문하기 좋습니다."
        );
    }

    private Trip trip(Long tripId) {
        Trip trip = Trip.create(
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "SEOGWIPO"
        );
        setId(trip, "tripId", tripId);
        return trip;
    }

    private Place place(Long placeId) {
        Place place = Place.create(
                "성산일출봉",
                "NATURE",
                "EAST",
                "제주특별자치도 서귀포시 성산읍 성산리 1",
                33.458056,
                126.942500,
                90,
                false,
                true,
                2,
                5,
                1,
                1,
                5,
                4,
                4,
                "제주 동쪽을 대표하는 일출 명소입니다.",
                true
        );
        setId(place, "placeId", placeId);
        return place;
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
