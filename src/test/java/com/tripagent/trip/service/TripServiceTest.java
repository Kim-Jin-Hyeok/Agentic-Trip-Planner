package com.tripagent.trip.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tripagent.itinerary.domain.Itinerary;
import com.tripagent.itinerary.repository.ItineraryRepository;
import com.tripagent.place.domain.Place;
import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.dto.TripCreateRequest;
import com.tripagent.trip.dto.TripDetailResponse;
import com.tripagent.trip.dto.TripResponse;
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
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private ItineraryRepository itineraryRepository;

    @InjectMocks
    private TripService tripService;

    @Test
    void createTripSavesValidJejuTrip() {
        TripCreateRequest request = new TripCreateRequest(
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "SEOGWIPO"
        );
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TripResponse response = tripService.createTrip(request);

        assertThat(response.destination()).isEqualTo("JEJU");
        assertThat(response.dailyStartTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(response.dailyEndTime()).isEqualTo(LocalTime.of(18, 0));
        assertThat(response.concept()).isEqualTo(TripConcept.HEALING);
        assertThat(response.transportation()).isEqualTo(Transportation.RENT_CAR);
        verify(tripRepository).save(any(Trip.class));
    }

    @Test
    void createTripRejectsUnsupportedDestination() {
        TripCreateRequest request = new TripCreateRequest(
                "BUSAN",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "SEOGWIPO"
        );

        assertThatThrownBy(() -> tripService.createTrip(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Only Jeju trips are supported in MVP.");
        verify(tripRepository, never()).save(any(Trip.class));
    }

    @Test
    void createTripRejectsTooLongDuration() {
        TripCreateRequest request = new TripCreateRequest(
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 5),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "SEOGWIPO"
        );

        assertThatThrownBy(() -> tripService.createTrip(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip duration must be 1 night 2 days to 3 nights 4 days.");
        verify(tripRepository, never()).save(any(Trip.class));
    }

    @Test
    void createTripRejectsDailyStartTimeNotBeforeDailyEndTime() {
        TripCreateRequest request = new TripCreateRequest(
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(18, 0),
                LocalTime.of(18, 0),
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "SEOGWIPO"
        );

        assertThatThrownBy(() -> tripService.createTrip(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip dailyStartTime must be before dailyEndTime.");
        verify(tripRepository, never()).save(any(Trip.class));
    }

    @Test
    void getTripRejectsUnknownTripId() {
        when(tripRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tripService.getTrip(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Trip not found. tripId=1");
    }

    @Test
    void getTripReturnsTripDetailWithItineraries() {
        Trip trip = trip(1L);
        Place firstPlace = place(10L, "First Place");
        Place secondPlace = place(20L, "Second Place");
        Itinerary firstItinerary = Itinerary.create(
                trip,
                firstPlace,
                1,
                1,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                0,
                "First reason"
        );
        Itinerary secondItinerary = Itinerary.create(
                trip,
                secondPlace,
                1,
                2,
                LocalTime.of(10, 30),
                LocalTime.of(11, 30),
                30,
                "Second reason"
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L))
                .thenReturn(List.of(firstItinerary, secondItinerary));

        TripDetailResponse response = tripService.getTrip(1L);

        assertThat(response.tripId()).isEqualTo(1L);
        assertThat(response.destination()).isEqualTo("JEJU");
        assertThat(response.dailyEndTime()).isEqualTo(LocalTime.of(18, 0));
        assertThat(response.itineraries()).hasSize(2);
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.placeId())
                .containsExactly(10L, 20L);
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.place().name())
                .containsExactly("First Place", "Second Place");
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.place().placeId())
                .containsExactly(10L, 20L);
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.place().category())
                .containsExactly("NATURE", "NATURE");
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.place().address())
                .containsExactly("JEJU", "JEJU");
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.place().description())
                .containsExactly("description", "description");
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.dayNo())
                .containsExactly(1, 1);
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.orderNo())
                .containsExactly(1, 2);
        verify(itineraryRepository).findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L);
    }

    private Trip trip(Long tripId) {
        Trip trip = Trip.create(
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "SEOGWIPO"
        );
        setId(trip, "tripId", tripId);
        return trip;
    }

    private Place place(Long placeId, String name) {
        Place place = Place.create(
                name,
                "NATURE",
                "EAST",
                "JEJU",
                33.0,
                126.0,
                60,
                false,
                true,
                1,
                2,
                3,
                4,
                5,
                4,
                3,
                "description",
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
