package com.tripagent.trip.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
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
import org.springframework.data.domain.Sort;
import org.mockito.InOrder;
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
    void searchTripsReturnsAllTripsByTripIdDescendingOrder() {
        Trip firstTrip = trip(3L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 12));
        Trip secondTrip = trip(2L, "JEJU", TripConcept.HEALING, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3));
        when(tripRepository.findAll(Sort.by(Sort.Direction.DESC, "tripId")))
                .thenReturn(List.of(firstTrip, secondTrip));

        List<TripResponse> responses = tripService.searchTrips(null, null, null, null, null, null);

        assertThat(responses).extracting(TripResponse::tripId)
                .containsExactly(3L, 2L);
        assertThat(responses).extracting(TripResponse::destination)
                .containsExactly("JEJU", "JEJU");
    }

    @Test
    void searchTripsFiltersByDestinationKeyword() {
        Trip jejuTrip = trip(1L, "JEJU EAST", TripConcept.FOOD, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3));
        Trip busanTrip = trip(2L, "BUSAN", TripConcept.FOOD, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3));
        when(tripRepository.findAll(Sort.by(Sort.Direction.DESC, "tripId")))
                .thenReturn(List.of(jejuTrip, busanTrip));

        List<TripResponse> responses = tripService.searchTrips("jeju", null, null, null, null, null);

        assertThat(responses).extracting(TripResponse::tripId)
                .containsExactly(1L);
    }

    @Test
    void searchTripsFiltersByConcept() {
        Trip foodTrip = trip(1L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3));
        Trip healingTrip = trip(2L, "JEJU", TripConcept.HEALING, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3));
        when(tripRepository.findAll(Sort.by(Sort.Direction.DESC, "tripId")))
                .thenReturn(List.of(foodTrip, healingTrip));

        List<TripResponse> responses = tripService.searchTrips(null, TripConcept.HEALING, null, null, null, null);

        assertThat(responses).extracting(TripResponse::tripId)
                .containsExactly(2L);
    }

    @Test
    void searchTripsFiltersByStartDateRange() {
        Trip inRangeTrip = trip(1L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 5), LocalDate.of(2026, 7, 7));
        Trip beforeRangeTrip = trip(2L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3));
        Trip afterRangeTrip = trip(3L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 12));
        when(tripRepository.findAll(Sort.by(Sort.Direction.DESC, "tripId")))
                .thenReturn(List.of(inRangeTrip, beforeRangeTrip, afterRangeTrip));

        List<TripResponse> responses = tripService.searchTrips(
                null,
                null,
                LocalDate.of(2026, 7, 4),
                LocalDate.of(2026, 7, 8),
                null,
                null
        );

        assertThat(responses).extracting(TripResponse::tripId)
                .containsExactly(1L);
    }

    @Test
    void searchTripsFiltersByEndDateRange() {
        Trip inRangeTrip = trip(1L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 5));
        Trip beforeRangeTrip = trip(2L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3));
        Trip afterRangeTrip = trip(3L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 10));
        when(tripRepository.findAll(Sort.by(Sort.Direction.DESC, "tripId")))
                .thenReturn(List.of(inRangeTrip, beforeRangeTrip, afterRangeTrip));

        List<TripResponse> responses = tripService.searchTrips(
                null,
                null,
                null,
                null,
                LocalDate.of(2026, 7, 4),
                LocalDate.of(2026, 7, 8)
        );

        assertThat(responses).extracting(TripResponse::tripId)
                .containsExactly(1L);
    }

    @Test
    void searchTripsFiltersByStartDateFromOnly() {
        Trip inRangeTrip = trip(1L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 5), LocalDate.of(2026, 7, 7));
        Trip beforeRangeTrip = trip(2L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3));
        when(tripRepository.findAll(Sort.by(Sort.Direction.DESC, "tripId")))
                .thenReturn(List.of(inRangeTrip, beforeRangeTrip));

        List<TripResponse> responses = tripService.searchTrips(
                null,
                null,
                LocalDate.of(2026, 7, 4),
                null,
                null,
                null
        );

        assertThat(responses).extracting(TripResponse::tripId)
                .containsExactly(1L);
    }

    @Test
    void searchTripsFiltersByEndDateToOnly() {
        Trip inRangeTrip = trip(1L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 5));
        Trip afterRangeTrip = trip(2L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 10));
        when(tripRepository.findAll(Sort.by(Sort.Direction.DESC, "tripId")))
                .thenReturn(List.of(inRangeTrip, afterRangeTrip));

        List<TripResponse> responses = tripService.searchTrips(
                null,
                null,
                null,
                null,
                null,
                LocalDate.of(2026, 7, 6)
        );

        assertThat(responses).extracting(TripResponse::tripId)
                .containsExactly(1L);
    }

    @Test
    void searchTripsRejectsStartDateFromAfterStartDateTo() {
        assertThatThrownBy(() -> tripService.searchTrips(
                null,
                null,
                LocalDate.of(2026, 7, 10),
                LocalDate.of(2026, 7, 1),
                null,
                null
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("startDateFrom must be less than or equal to startDateTo.");
        verify(tripRepository, never()).findAll(any(Sort.class));
    }

    @Test
    void searchTripsRejectsEndDateFromAfterEndDateTo() {
        assertThatThrownBy(() -> tripService.searchTrips(
                null,
                null,
                null,
                null,
                LocalDate.of(2026, 7, 10),
                LocalDate.of(2026, 7, 1)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("endDateFrom must be less than or equal to endDateTo.");
        verify(tripRepository, never()).findAll(any(Sort.class));
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
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.place().region())
                .containsExactly("EAST", "EAST");
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.place().address())
                .containsExactly("JEJU", "JEJU");
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.place().latitude())
                .containsExactly(33.0, 33.0);
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.place().longitude())
                .containsExactly(126.0, 126.0);
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.place().description())
                .containsExactly("description", "description");
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.dayNo())
                .containsExactly(1, 1);
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.orderNo())
                .containsExactly(1, 2);
        verify(itineraryRepository).findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L);
    }

    @Test
    void deleteTripDeletesTripAndItsItineraries() {
        Trip trip = trip(1L);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        tripService.deleteTrip(1L);

        InOrder inOrder = inOrder(itineraryRepository, tripRepository);
        inOrder.verify(itineraryRepository).deleteByTrip_TripId(1L);
        inOrder.verify(tripRepository).delete(trip);
    }

    @Test
    void deleteTripRejectsUnknownTripId() {
        when(tripRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tripService.deleteTrip(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Trip not found. tripId=1");
        verify(itineraryRepository, never()).deleteByTrip_TripId(1L);
    }

    @Test
    void deletedTripCannotBeLookedUp() {
        Trip trip = trip(1L);
        when(tripRepository.findById(1L))
                .thenReturn(Optional.of(trip))
                .thenReturn(Optional.empty());

        tripService.deleteTrip(1L);

        assertThatThrownBy(() -> tripService.getTrip(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Trip not found. tripId=1");
    }

    @Test
    void deletedTripIsExcludedFromSearchResult() {
        Trip deletedTrip = trip(1L);
        Trip remainingTrip = trip(2L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 5), LocalDate.of(2026, 7, 7));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(deletedTrip));
        when(tripRepository.findAll(Sort.by(Sort.Direction.DESC, "tripId")))
                .thenReturn(List.of(remainingTrip));

        tripService.deleteTrip(1L);
        List<TripResponse> responses = tripService.searchTrips(null, null, null, null, null, null);

        assertThat(responses).extracting(TripResponse::tripId)
                .containsExactly(2L);
    }

    private Trip trip(Long tripId) {
        return trip(tripId, "JEJU", TripConcept.HEALING, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3));
    }

    private Trip trip(
            Long tripId,
            String destination,
            TripConcept concept,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Trip trip = Trip.create(
                destination,
                startDate,
                endDate,
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                concept,
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
