package com.tripagent.itinerary.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.tripagent.accommodation.domain.Accommodation;
import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripAccommodation;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.repository.TripAccommodationRepository;
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
    private TripAccommodationRepository tripAccommodationRepository;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private RouteCalculationAdapter routeCalculationAdapter;

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
        assertThat(response.place().placeId()).isEqualTo(10L);
        assertThat(response.place().name()).isEqualTo("Test Place");
        assertThat(response.place().category()).isEqualTo("NATURE");
        assertThat(response.place().region()).isEqualTo("EAST");
        assertThat(response.place().address()).isEqualTo("Test Address");
        assertThat(response.place().latitude()).isEqualTo(33.458056);
        assertThat(response.place().longitude()).isEqualTo(126.942500);
        assertThat(response.place().description()).isEqualTo("Test description");
        assertThat(response.dayNo()).isEqualTo(1);
        assertThat(response.orderNo()).isEqualTo(1);
        assertThat(response.generationSource()).isEqualTo(ItineraryGenerationSource.MANUAL);
        verify(itineraryRepository).save(any(Itinerary.class));
    }

    @Test
    void createItineraryAdjustsFirstVisitTimeFromTripStartPlace() {
        Trip trip = tripWithStartPlace(1L, 50L, LocalTime.of(18, 0));
        Place startPlace = place(50L, "제주국제공항");
        Place place = place(10L);
        ItineraryCreateRequest request = request(10L, 1, 1);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeRepository.findById(10L)).thenReturn(Optional.of(place));
        when(placeRepository.findById(50L)).thenReturn(Optional.of(startPlace));
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 1)).thenReturn(List.of());
        when(routeCalculationAdapter.calculateTravelMinutes(any(), any(), any(), any())).thenReturn(40);
        when(itineraryRepository.save(any(Itinerary.class))).thenAnswer(invocation -> {
            Itinerary itinerary = invocation.getArgument(0);
            setId(itinerary, "itineraryId", 100L);
            return itinerary;
        });

        ItineraryResponse response = itineraryService.createItinerary(1L, request);

        assertThat(response.startTime()).isEqualTo(LocalTime.of(9, 40));
        assertThat(response.endTime()).isEqualTo(LocalTime.of(11, 10));
        assertThat(response.travelMinutesFromPrevious()).isZero();
    }

    @Test
    void createItineraryKeepsLaterFirstVisitTimeSelectedByUser() {
        Trip trip = tripWithStartPlace(1L, 50L, LocalTime.of(18, 0));
        Place startPlace = place(50L, "제주국제공항");
        Place place = place(10L);
        ItineraryCreateRequest request = new ItineraryCreateRequest(
                10L,
                1,
                1,
                LocalTime.of(10, 0),
                LocalTime.of(11, 30),
                0,
                "Later first visit"
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeRepository.findById(10L)).thenReturn(Optional.of(place));
        when(placeRepository.findById(50L)).thenReturn(Optional.of(startPlace));
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 1)).thenReturn(List.of());
        when(routeCalculationAdapter.calculateTravelMinutes(any(), any(), any(), any())).thenReturn(40);
        when(itineraryRepository.save(any(Itinerary.class))).thenAnswer(invocation -> {
            Itinerary itinerary = invocation.getArgument(0);
            setId(itinerary, "itineraryId", 100L);
            return itinerary;
        });

        ItineraryResponse response = itineraryService.createItinerary(1L, request);

        assertThat(response.startTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(response.endTime()).isEqualTo(LocalTime.of(11, 30));
        assertThat(response.travelMinutesFromPrevious()).isZero();
    }

    @Test
    void createItineraryAdjustsFirstVisitTimeFromPreviousAccommodation() {
        Trip trip = trip(1L);
        Place place = place(10L);
        ItineraryCreateRequest request = request(10L, 2, 1);
        TripAccommodation tripAccommodation = org.mockito.Mockito.mock(TripAccommodation.class);
        Accommodation accommodation = org.mockito.Mockito.mock(Accommodation.class);
        when(tripAccommodation.getStayDate()).thenReturn(LocalDate.of(2026, 7, 1));
        when(tripAccommodation.getAccommodation()).thenReturn(accommodation);
        when(accommodation.getLatitude()).thenReturn(33.4);
        when(accommodation.getLongitude()).thenReturn(126.5);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeRepository.findById(10L)).thenReturn(Optional.of(place));
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 2)).thenReturn(List.of());
        when(tripAccommodationRepository.findByTripIdOrderByStayDate(1L))
                .thenReturn(List.of(tripAccommodation));
        when(routeCalculationAdapter.calculateTravelMinutes(any(), any(), any(), any())).thenReturn(30);
        when(itineraryRepository.save(any(Itinerary.class))).thenAnswer(invocation -> {
            Itinerary itinerary = invocation.getArgument(0);
            setId(itinerary, "itineraryId", 100L);
            return itinerary;
        });

        ItineraryResponse response = itineraryService.createItinerary(1L, request);

        assertThat(response.startTime()).isEqualTo(LocalTime.of(9, 30));
        assertThat(response.endTime()).isEqualTo(LocalTime.of(11, 0));
        assertThat(response.travelMinutesFromPrevious()).isZero();
    }

    @Test
    void createItineraryRejectsFirstVisitAdjustedAfterDailyEndTime() {
        Trip trip = tripWithStartPlace(1L, 50L, LocalTime.of(10, 30));
        Place startPlace = place(50L, "제주국제공항");
        Place place = place(10L);
        ItineraryCreateRequest request = request(10L, 1, 1);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeRepository.findById(10L)).thenReturn(Optional.of(place));
        when(placeRepository.findById(50L)).thenReturn(Optional.of(startPlace));
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 1)).thenReturn(List.of());
        when(routeCalculationAdapter.calculateTravelMinutes(any(), any(), any(), any())).thenReturn(40);

        assertThatThrownBy(() -> itineraryService.createItinerary(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary endTime must be at or before trip dailyEndTime.");
        verify(itineraryRepository, never()).save(any(Itinerary.class));
    }

    @Test
    void createItineraryCalculatesTravelAndTimesWhenAppendingPlace() {
        Trip trip = trip(1L);
        Place previousPlace = place(10L, "Previous Place");
        Place newPlace = place(20L, "New Place");
        Itinerary previousItinerary = itinerary(
                100L,
                trip,
                previousPlace,
                1,
                1,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                0
        );
        ItineraryCreateRequest request = new ItineraryCreateRequest(
                20L,
                1,
                2,
                LocalTime.of(10, 20),
                LocalTime.of(11, 50),
                20,
                "Append itinerary"
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeRepository.findById(20L)).thenReturn(Optional.of(newPlace));
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 1)).thenReturn(List.of(previousItinerary));
        when(routeCalculationAdapter.calculateTravelMinutes(any(), any(), any(), any())).thenReturn(35);
        when(itineraryRepository.save(any(Itinerary.class))).thenAnswer(invocation -> {
            Itinerary itinerary = invocation.getArgument(0);
            setId(itinerary, "itineraryId", 200L);
            return itinerary;
        });

        ItineraryResponse response = itineraryService.createItinerary(1L, request);

        assertThat(response.startTime()).isEqualTo(LocalTime.of(10, 35));
        assertThat(response.endTime()).isEqualTo(LocalTime.of(12, 5));
        assertThat(response.travelMinutesFromPrevious()).isEqualTo(35);
        verify(routeCalculationAdapter).calculateTravelMinutes(any(), any(), any(), any());
    }

    @Test
    void createItineraryRejectsAutomaticallyCalculatedEndTimeAfterDailyEndTime() {
        Trip trip = trip(1L, LocalTime.of(9, 0), LocalTime.of(11, 0));
        Place previousPlace = place(10L, "Previous Place");
        Place newPlace = place(20L, "New Place");
        Itinerary previousItinerary = itinerary(
                100L,
                trip,
                previousPlace,
                1,
                1,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                0
        );
        ItineraryCreateRequest request = new ItineraryCreateRequest(
                20L,
                1,
                2,
                LocalTime.of(10, 20),
                LocalTime.of(10, 50),
                20,
                "Append itinerary"
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeRepository.findById(20L)).thenReturn(Optional.of(newPlace));
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 1)).thenReturn(List.of(previousItinerary));
        when(routeCalculationAdapter.calculateTravelMinutes(any(), any(), any(), any())).thenReturn(45);

        assertThatThrownBy(() -> itineraryService.createItinerary(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary endTime must be at or before trip dailyEndTime.");
        verify(itineraryRepository, never()).save(any(Itinerary.class));
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
    void createItineraryWithOwnerIdRejectsDifferentOwner() {
        Trip trip = trip(1L, 100L);
        ItineraryCreateRequest request = request(10L, 1, 1);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        assertThatThrownBy(() -> itineraryService.createItinerary(1L, request, 200L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip owner does not match. tripId=1");
        verify(placeRepository, never()).findById(10L);
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
        ItineraryCreateRequest request = request(10L, 4, 1);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        assertThatThrownBy(() -> itineraryService.createItinerary(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary dayNo must be within trip period. maxDayNo=3");
        verify(itineraryRepository, never()).save(any(Itinerary.class));
    }

    @Test
    void createItineraryRejectsDuplicatedDayNoAndOrderNoInTrip() {
        Trip trip = trip(1L);
        ItineraryCreateRequest request = request(10L, 1, 1);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
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
    void createItineraryRejectsFirstStartTimeBeforeDailyStartTime() {
        Trip trip = trip(1L, LocalTime.of(10, 30));
        ItineraryCreateRequest request = new ItineraryCreateRequest(
                10L,
                1,
                1,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                0,
                "First start time is too early."
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        assertThatThrownBy(() -> itineraryService.createItinerary(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("First itinerary item of each day must start at or after trip dailyStartTime.");
        verify(placeRepository, never()).findById(10L);
        verify(itineraryRepository, never()).save(any(Itinerary.class));
    }

    @Test
    void createItineraryRejectsEndTimeAfterDailyEndTime() {
        Trip trip = trip(1L, LocalTime.of(9, 0), LocalTime.of(10, 0));
        ItineraryCreateRequest request = new ItineraryCreateRequest(
                10L,
                1,
                1,
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                0,
                "End time is too late."
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        assertThatThrownBy(() -> itineraryService.createItinerary(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary endTime must be at or before trip dailyEndTime.");
        verify(placeRepository, never()).findById(10L);
        verify(itineraryRepository, never()).save(any(Itinerary.class));
    }

    @Test
    void createGeneratedItineraryAllowsStartBeforeTripDailyStartTimeWhenInsideDayTimeWindow() {
        Trip trip = trip(1L, LocalTime.of(11, 0));
        Place place = place(10L);
        ItineraryCreateRequest request = new ItineraryCreateRequest(
                10L,
                2,
                1,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                0,
                "Generated itinerary with day time window."
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeRepository.findById(10L)).thenReturn(Optional.of(place));
        when(itineraryRepository.existsByTrip_TripIdAndDayNoAndOrderNo(1L, 2, 1)).thenReturn(false);
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 2)).thenReturn(List.of());
        when(itineraryRepository.save(any(Itinerary.class))).thenAnswer(invocation -> {
            Itinerary itinerary = invocation.getArgument(0);
            setId(itinerary, "itineraryId", 100L);
            return itinerary;
        });

        ItineraryResponse response = itineraryService.createGeneratedItinerary(
                1L,
                request,
                LocalTime.of(10, 0),
                LocalTime.of(18, 0)
        );

        assertThat(response.startTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(response.endTime()).isEqualTo(LocalTime.of(11, 0));
        assertThat(response.generationSource()).isEqualTo(ItineraryGenerationSource.LLM);
        verify(itineraryRepository).save(any(Itinerary.class));
    }

    @Test
    void createGeneratedItineraryRejectsStartTimeBeforeDayTimeWindow() {
        Trip trip = trip(1L, LocalTime.of(11, 0));
        ItineraryCreateRequest request = new ItineraryCreateRequest(
                10L,
                2,
                1,
                LocalTime.of(9, 30),
                LocalTime.of(10, 30),
                0,
                "Generated itinerary before day time window."
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        assertThatThrownBy(() -> itineraryService.createGeneratedItinerary(
                1L,
                request,
                LocalTime.of(10, 0),
                LocalTime.of(18, 0)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("First itinerary item of each day must start at or after the day time window startTime.");
        verify(placeRepository, never()).findById(10L);
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
    void updateItineraryUpdatesPartialFields() {
        Trip trip = trip(1L);
        Place oldPlace = place(10L, "Old Place");
        Place newPlace = place(20L, "New Place");
        Itinerary itinerary = itinerary(100L, trip, oldPlace, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        ItineraryUpdateRequest request = new ItineraryUpdateRequest(
                20L,
                null,
                null,
                LocalTime.of(9, 30),
                LocalTime.of(10, 30),
                null,
                "Updated reason"
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(itinerary));
        when(placeRepository.findById(20L)).thenReturn(Optional.of(newPlace));
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 1)).thenReturn(List.of(itinerary));

        ItineraryResponse response = itineraryService.updateItinerary(1L, 100L, request);

        assertThat(response.itineraryId()).isEqualTo(100L);
        assertThat(response.placeId()).isEqualTo(20L);
        assertThat(response.place().placeId()).isEqualTo(20L);
        assertThat(response.place().name()).isEqualTo("New Place");
        assertThat(response.startTime()).isEqualTo(LocalTime.of(9, 30));
        assertThat(response.endTime()).isEqualTo(LocalTime.of(10, 30));
        assertThat(response.reason()).isEqualTo("Updated reason");
        assertThat(response.generationSource()).isEqualTo(ItineraryGenerationSource.USER_ADJUSTED);
    }

    @Test
    void updateItineraryRecalculatesFirstVisitFromTripStartPlace() {
        Trip trip = tripWithStartPlace(1L, 50L, LocalTime.of(18, 0));
        Place startPlace = place(50L, "제주국제공항");
        Place oldPlace = place(10L, "Old First Place");
        Place newPlace = place(20L, "New First Place");
        Place secondPlace = place(30L, "Second Place");
        Itinerary first = itinerary(100L, trip, oldPlace, 1, 1, LocalTime.of(9, 40), LocalTime.of(10, 40), 0);
        Itinerary second = itinerary(200L, trip, secondPlace, 1, 2, LocalTime.of(11, 10), LocalTime.of(12, 10), 30);
        ItineraryUpdateRequest request = new ItineraryUpdateRequest(
                20L,
                1,
                1,
                LocalTime.of(9, 40),
                LocalTime.of(10, 40),
                0,
                "Changed first place"
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(first));
        when(placeRepository.findById(20L)).thenReturn(Optional.of(newPlace));
        when(placeRepository.findById(50L)).thenReturn(Optional.of(startPlace));
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 1)).thenReturn(List.of(second, first));
        when(routeCalculationAdapter.calculateTravelMinutes(any(), any(), any(), any()))
                .thenReturn(60, 20);

        ItineraryResponse response = itineraryService.updateItinerary(1L, 100L, request);

        assertThat(response.placeId()).isEqualTo(20L);
        assertThat(response.startTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(response.endTime()).isEqualTo(LocalTime.of(11, 0));
        assertThat(response.travelMinutesFromPrevious()).isZero();
        assertThat(second.getStartTime()).isEqualTo(LocalTime.of(11, 20));
        assertThat(second.getEndTime()).isEqualTo(LocalTime.of(12, 20));
        assertThat(second.getTravelMinutesFromPrevious()).isEqualTo(20);
        verify(routeCalculationAdapter, times(2)).calculateTravelMinutes(any(), any(), any(), any());
    }

    @Test
    void updateItineraryKeepsLaterFirstVisitTimeSelectedByUser() {
        Trip trip = tripWithStartPlace(1L, 50L, LocalTime.of(18, 0));
        Place startPlace = place(50L, "제주국제공항");
        Place oldPlace = place(10L, "Old First Place");
        Place newPlace = place(20L, "New First Place");
        Itinerary first = itinerary(100L, trip, oldPlace, 1, 1, LocalTime.of(10, 0), LocalTime.of(11, 0), 0);
        ItineraryUpdateRequest request = new ItineraryUpdateRequest(
                20L,
                1,
                1,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                0,
                "Changed first place"
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(first));
        when(placeRepository.findById(20L)).thenReturn(Optional.of(newPlace));
        when(placeRepository.findById(50L)).thenReturn(Optional.of(startPlace));
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 1)).thenReturn(List.of(first));
        when(routeCalculationAdapter.calculateTravelMinutes(any(), any(), any(), any())).thenReturn(40);

        ItineraryResponse response = itineraryService.updateItinerary(1L, 100L, request);

        assertThat(response.startTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(response.endTime()).isEqualTo(LocalTime.of(11, 0));
    }

    @Test
    void updateItineraryRecalculatesFirstVisitFromPreviousAccommodation() {
        Trip trip = trip(1L);
        Place oldPlace = place(10L, "Old First Place");
        Place newPlace = place(20L, "New First Place");
        Itinerary first = itinerary(100L, trip, oldPlace, 2, 1, LocalTime.of(9, 30), LocalTime.of(10, 30), 0);
        ItineraryUpdateRequest request = new ItineraryUpdateRequest(
                20L,
                2,
                1,
                LocalTime.of(9, 30),
                LocalTime.of(10, 30),
                0,
                "Changed first place"
        );
        TripAccommodation tripAccommodation = org.mockito.Mockito.mock(TripAccommodation.class);
        Accommodation accommodation = org.mockito.Mockito.mock(Accommodation.class);
        when(tripAccommodation.getStayDate()).thenReturn(LocalDate.of(2026, 7, 1));
        when(tripAccommodation.getAccommodation()).thenReturn(accommodation);
        when(accommodation.getLatitude()).thenReturn(33.4);
        when(accommodation.getLongitude()).thenReturn(126.5);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(first));
        when(placeRepository.findById(20L)).thenReturn(Optional.of(newPlace));
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 2)).thenReturn(List.of(first));
        when(tripAccommodationRepository.findByTripIdOrderByStayDate(1L))
                .thenReturn(List.of(tripAccommodation));
        when(routeCalculationAdapter.calculateTravelMinutes(any(), any(), any(), any())).thenReturn(45);

        ItineraryResponse response = itineraryService.updateItinerary(1L, 100L, request);

        assertThat(response.placeId()).isEqualTo(20L);
        assertThat(response.startTime()).isEqualTo(LocalTime.of(9, 45));
        assertThat(response.endTime()).isEqualTo(LocalTime.of(10, 45));
        assertThat(response.travelMinutesFromPrevious()).isZero();
    }

    @Test
    void updateItineraryRejectsFirstPlaceChangeWhenFollowingScheduleExceedsDailyEndTime() {
        Trip trip = tripWithStartPlace(1L, 50L, LocalTime.of(12, 0));
        Place startPlace = place(50L, "제주국제공항");
        Place oldPlace = place(10L, "Old First Place");
        Place newPlace = place(20L, "New First Place");
        Place secondPlace = place(30L, "Second Place");
        Itinerary first = itinerary(100L, trip, oldPlace, 1, 1, LocalTime.of(9, 20), LocalTime.of(10, 20), 0);
        Itinerary second = itinerary(200L, trip, secondPlace, 1, 2, LocalTime.of(10, 40), LocalTime.of(11, 40), 20);
        ItineraryUpdateRequest request = new ItineraryUpdateRequest(
                20L,
                1,
                1,
                LocalTime.of(9, 20),
                LocalTime.of(10, 20),
                0,
                "Changed first place"
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(first));
        when(placeRepository.findById(20L)).thenReturn(Optional.of(newPlace));
        when(placeRepository.findById(50L)).thenReturn(Optional.of(startPlace));
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 1)).thenReturn(List.of(first, second));
        when(routeCalculationAdapter.calculateTravelMinutes(any(), any(), any(), any()))
                .thenReturn(90, 30);

        assertThatThrownBy(() -> itineraryService.updateItinerary(1L, 100L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary endTime must be at or before trip dailyEndTime.");
        assertThat(first.getPlace().getPlaceId()).isEqualTo(10L);
        assertThat(first.getStartTime()).isEqualTo(LocalTime.of(9, 20));
        assertThat(second.getStartTime()).isEqualTo(LocalTime.of(10, 40));
    }

    @Test
    void updateItineraryRecalculatesFollowingScheduleWhenPlaceChanges() {
        Trip trip = trip(1L);
        Place firstPlace = place(10L, "First Place");
        Place oldPlace = place(20L, "Old Place");
        Place newPlace = place(30L, "New Place");
        Place thirdPlace = place(40L, "Third Place");
        Itinerary first = itinerary(100L, trip, firstPlace, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        Itinerary target = itinerary(200L, trip, oldPlace, 1, 2, LocalTime.of(10, 30), LocalTime.of(11, 30), 30);
        Itinerary third = itinerary(300L, trip, thirdPlace, 1, 3, LocalTime.of(12, 0), LocalTime.of(13, 0), 30);
        ItineraryUpdateRequest request = new ItineraryUpdateRequest(
                30L,
                1,
                2,
                LocalTime.of(10, 30),
                LocalTime.of(11, 30),
                30,
                "Changed place"
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(200L)).thenReturn(Optional.of(target));
        when(placeRepository.findById(30L)).thenReturn(Optional.of(newPlace));
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 1))
                .thenReturn(List.of(third, target, first));
        when(routeCalculationAdapter.calculateTravelMinutes(any(), any(), any(), any()))
                .thenReturn(20, 25);

        ItineraryResponse response = itineraryService.updateItinerary(1L, 200L, request);

        assertThat(response.placeId()).isEqualTo(30L);
        assertThat(response.startTime()).isEqualTo(LocalTime.of(10, 20));
        assertThat(response.endTime()).isEqualTo(LocalTime.of(11, 20));
        assertThat(response.travelMinutesFromPrevious()).isEqualTo(20);
        assertThat(response.reason()).isEqualTo("Changed place");
        assertThat(third.getStartTime()).isEqualTo(LocalTime.of(11, 45));
        assertThat(third.getEndTime()).isEqualTo(LocalTime.of(12, 45));
        assertThat(third.getTravelMinutesFromPrevious()).isEqualTo(25);
        assertThat(third.getGenerationSource()).isEqualTo(ItineraryGenerationSource.USER_ADJUSTED);
        assertThat(first.getGenerationSource()).isEqualTo(ItineraryGenerationSource.MANUAL);
        verify(routeCalculationAdapter, times(2)).calculateTravelMinutes(any(), any(), any(), any());
    }

    @Test
    void updateItineraryRejectsPlaceChangeWhenFollowingScheduleExceedsDailyEndTime() {
        Trip trip = trip(1L, LocalTime.of(9, 0), LocalTime.of(12, 30));
        Place firstPlace = place(10L, "First Place");
        Place oldPlace = place(20L, "Old Place");
        Place newPlace = place(30L, "New Place");
        Place thirdPlace = place(40L, "Third Place");
        Itinerary first = itinerary(100L, trip, firstPlace, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        Itinerary target = itinerary(200L, trip, oldPlace, 1, 2, LocalTime.of(10, 20), LocalTime.of(11, 20), 20);
        Itinerary third = itinerary(300L, trip, thirdPlace, 1, 3, LocalTime.of(11, 30), LocalTime.of(12, 30), 10);
        ItineraryUpdateRequest request = new ItineraryUpdateRequest(
                30L,
                1,
                2,
                LocalTime.of(10, 20),
                LocalTime.of(11, 20),
                20,
                "Changed place"
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(200L)).thenReturn(Optional.of(target));
        when(placeRepository.findById(30L)).thenReturn(Optional.of(newPlace));
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 1))
                .thenReturn(List.of(first, target, third));
        when(routeCalculationAdapter.calculateTravelMinutes(any(), any(), any(), any()))
                .thenReturn(20, 30);

        assertThatThrownBy(() -> itineraryService.updateItinerary(1L, 200L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary endTime must be at or before trip dailyEndTime.");
        assertThat(target.getPlace().getPlaceId()).isEqualTo(20L);
        assertThat(third.getStartTime()).isEqualTo(LocalTime.of(11, 30));
        assertThat(third.getEndTime()).isEqualTo(LocalTime.of(12, 30));
    }

    @Test
    void updateItineraryRejectsUnknownTrip() {
        ItineraryUpdateRequest request = new ItineraryUpdateRequest(null, null, null, null, null, null, null);
        when(tripRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itineraryService.updateItinerary(1L, 100L, request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Trip not found. tripId=1");
        verify(itineraryRepository, never()).findById(100L);
    }

    @Test
    void updateItineraryRejectsUnknownItinerary() {
        Trip trip = trip(1L);
        ItineraryUpdateRequest request = new ItineraryUpdateRequest(null, null, null, null, null, null, null);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itineraryService.updateItinerary(1L, 100L, request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Itinerary not found. itineraryId=100");
    }

    @Test
    void updateItineraryRejectsItineraryInOtherTrip() {
        Trip trip = trip(1L);
        Trip otherTrip = trip(2L);
        Place place = place(10L);
        Itinerary itinerary = itinerary(100L, otherTrip, place, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        ItineraryUpdateRequest request = new ItineraryUpdateRequest(null, null, null, null, null, null, null);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(itinerary));

        assertThatThrownBy(() -> itineraryService.updateItinerary(1L, 100L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary does not belong to trip. itineraryId=100");
    }

    @Test
    void updateItineraryRejectsDuplicatedDayNoAndOrderNo() {
        Trip trip = trip(1L);
        Place place = place(10L);
        Itinerary target = itinerary(100L, trip, place, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        Itinerary existing = itinerary(200L, trip, place, 1, 2, LocalTime.of(10, 30), LocalTime.of(11, 30), 30);
        ItineraryUpdateRequest request = new ItineraryUpdateRequest(null, 1, 2, null, null, null, null);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(target));
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 1)).thenReturn(List.of(target, existing));

        assertThatThrownBy(() -> itineraryService.updateItinerary(1L, 100L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary dayNo and orderNo already exist in this trip.");
    }

    @Test
    void updateItineraryRejectsTimeOverlap() {
        Trip trip = trip(1L);
        Place place = place(10L);
        Itinerary target = itinerary(100L, trip, place, 1, 2, LocalTime.of(11, 30), LocalTime.of(12, 30), 30);
        Itinerary existing = itinerary(200L, trip, place, 1, 1, LocalTime.of(10, 0), LocalTime.of(11, 0), 0);
        ItineraryUpdateRequest request = new ItineraryUpdateRequest(
                null,
                null,
                null,
                LocalTime.of(10, 30),
                LocalTime.of(11, 30),
                null,
                null
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(target));
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 1)).thenReturn(List.of(target, existing));

        assertThatThrownBy(() -> itineraryService.updateItinerary(1L, 100L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary time overlaps with existing itinerary.");
    }

    @Test
    void updateItineraryAllowsAdjacentTime() {
        Trip trip = trip(1L);
        Place place = place(10L);
        Itinerary target = itinerary(100L, trip, place, 1, 2, LocalTime.of(11, 30), LocalTime.of(12, 30), 30);
        Itinerary existing = itinerary(200L, trip, place, 1, 1, LocalTime.of(9, 30), LocalTime.of(10, 30), 0);
        ItineraryUpdateRequest request = new ItineraryUpdateRequest(
                null,
                null,
                null,
                LocalTime.of(10, 30),
                LocalTime.of(11, 30),
                null,
                null
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(target));
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 1)).thenReturn(List.of(target, existing));

        ItineraryResponse response = itineraryService.updateItinerary(1L, 100L, request);

        assertThat(response.startTime()).isEqualTo(LocalTime.of(10, 30));
        assertThat(response.endTime()).isEqualTo(LocalTime.of(11, 30));
    }

    @Test
    void updateItineraryRejectsFirstOrderWithTravelMinutes() {
        Trip trip = trip(1L);
        Place place = place(10L);
        Itinerary itinerary = itinerary(100L, trip, place, 1, 2, LocalTime.of(10, 30), LocalTime.of(11, 30), 30);
        ItineraryUpdateRequest request = new ItineraryUpdateRequest(null, null, 1, null, null, 15, null);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(itinerary));

        assertThatThrownBy(() -> itineraryService.updateItinerary(1L, 100L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("First itinerary item of each day must have travelMinutesFromPrevious 0.");
    }

    @Test
    void updateItineraryRejectsFirstStartTimeBeforeDailyStartTime() {
        Trip trip = trip(1L, LocalTime.of(10, 30));
        Place place = place(10L);
        Itinerary itinerary = itinerary(100L, trip, place, 1, 2, LocalTime.of(11, 0), LocalTime.of(12, 0), 30);
        ItineraryUpdateRequest request = new ItineraryUpdateRequest(
                null,
                null,
                1,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                0,
                null
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(itinerary));

        assertThatThrownBy(() -> itineraryService.updateItinerary(1L, 100L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("First itinerary item of each day must start at or after trip dailyStartTime.");
    }

    @Test
    void updateItineraryRejectsEndTimeAfterDailyEndTime() {
        Trip trip = trip(1L, LocalTime.of(9, 0), LocalTime.of(10, 0));
        Place place = place(10L);
        Itinerary itinerary = itinerary(100L, trip, place, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        ItineraryUpdateRequest request = new ItineraryUpdateRequest(
                null,
                null,
                null,
                null,
                LocalTime.of(10, 30),
                null,
                null
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(itinerary));

        assertThatThrownBy(() -> itineraryService.updateItinerary(1L, 100L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary endTime must be at or before trip dailyEndTime.");
    }

    @Test
    void reorderItinerariesUpdatesDayNoAndOrderNoAndReturnsSortedResponses() {
        Trip trip = trip(1L);
        Place place = place(10L);
        Itinerary first = itinerary(100L, trip, place, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        Itinerary second = itinerary(200L, trip, place, 1, 3, LocalTime.of(10, 30), LocalTime.of(11, 30), 30);
        ItineraryReorderRequest request = reorderRequest(item(200L, 1, 2));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(200L)).thenReturn(Optional.of(second));
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L))
                .thenReturn(List.of(second, first));

        List<ItineraryResponse> responses = itineraryService.reorderItineraries(1L, request);

        assertThat(second.getDayNo()).isEqualTo(1);
        assertThat(second.getOrderNo()).isEqualTo(2);
        assertThat(responses).extracting(ItineraryResponse::itineraryId)
                .containsExactly(100L, 200L);
        assertThat(responses).extracting(ItineraryResponse::orderNo)
                .containsExactly(1, 2);
        assertThat(responses.get(1).generationSource()).isEqualTo(ItineraryGenerationSource.USER_ADJUSTED);
    }

    @Test
    void reorderItinerariesRecalculatesTravelAndTimesWhenOrderIsSwapped() {
        Trip trip = trip(1L);
        Place firstPlace = place(10L, "First Place");
        Place secondPlace = place(20L, "Second Place");
        Itinerary first = itinerary(100L, trip, firstPlace, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        Itinerary second = itinerary(200L, trip, secondPlace, 1, 2, LocalTime.of(10, 30), LocalTime.of(11, 30), 30);
        ItineraryReorderRequest request = reorderRequest(
                item(100L, 1, 2),
                item(200L, 1, 1)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(first));
        when(itineraryRepository.findById(200L)).thenReturn(Optional.of(second));
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L))
                .thenReturn(List.of(first, second));
        when(routeCalculationAdapter.calculateTravelMinutes(any(), any(), any(), any())).thenReturn(20);

        List<ItineraryResponse> responses = itineraryService.reorderItineraries(1L, request);

        assertThat(responses).extracting(ItineraryResponse::itineraryId)
                .containsExactly(200L, 100L);
        assertThat(second.getStartTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(second.getEndTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(second.getTravelMinutesFromPrevious()).isZero();
        assertThat(first.getStartTime()).isEqualTo(LocalTime.of(10, 20));
        assertThat(first.getEndTime()).isEqualTo(LocalTime.of(11, 20));
        assertThat(first.getTravelMinutesFromPrevious()).isEqualTo(20);
    }

    @Test
    void reorderItinerariesRecalculatesUnmovedFollowingItinerary() {
        Trip trip = trip(1L);
        Place firstPlace = place(10L, "First Place");
        Place secondPlace = place(20L, "Second Place");
        Place thirdPlace = place(30L, "Third Place");
        Itinerary first = itinerary(100L, trip, firstPlace, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        Itinerary second = itinerary(200L, trip, secondPlace, 1, 2, LocalTime.of(10, 30), LocalTime.of(11, 30), 30);
        Itinerary third = itinerary(300L, trip, thirdPlace, 1, 3, LocalTime.of(12, 0), LocalTime.of(13, 0), 30);
        ItineraryReorderRequest request = reorderRequest(
                item(100L, 1, 2),
                item(200L, 1, 1)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(first));
        when(itineraryRepository.findById(200L)).thenReturn(Optional.of(second));
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L))
                .thenReturn(List.of(first, second, third));
        when(routeCalculationAdapter.calculateTravelMinutes(any(), any(), any(), any()))
                .thenReturn(15, 25);

        itineraryService.reorderItineraries(1L, request);

        assertThat(third.getStartTime()).isEqualTo(LocalTime.of(11, 40));
        assertThat(third.getEndTime()).isEqualTo(LocalTime.of(12, 40));
        assertThat(third.getTravelMinutesFromPrevious()).isEqualTo(25);
        assertThat(third.getGenerationSource()).isEqualTo(ItineraryGenerationSource.USER_ADJUSTED);
        verify(routeCalculationAdapter, times(2)).calculateTravelMinutes(any(), any(), any(), any());
    }

    @Test
    void reorderItinerariesRejectsRecalculatedScheduleAfterDailyEndTime() {
        Trip trip = trip(1L, LocalTime.of(9, 0), LocalTime.of(11, 0));
        Place firstPlace = place(10L, "First Place");
        Place secondPlace = place(20L, "Second Place");
        Itinerary first = itinerary(100L, trip, firstPlace, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        Itinerary second = itinerary(200L, trip, secondPlace, 1, 2, LocalTime.of(10, 0), LocalTime.of(11, 0), 0);
        ItineraryReorderRequest request = reorderRequest(
                item(100L, 1, 2),
                item(200L, 1, 1)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(first));
        when(itineraryRepository.findById(200L)).thenReturn(Optional.of(second));
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L))
                .thenReturn(List.of(first, second));
        when(routeCalculationAdapter.calculateTravelMinutes(any(), any(), any(), any())).thenReturn(30);

        assertThatThrownBy(() -> itineraryService.reorderItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary endTime must be at or before trip dailyEndTime.");
    }

    @Test
    void reorderItinerariesRejectsEmptyItems() {
        ItineraryReorderRequest request = reorderRequest();

        assertThatThrownBy(() -> itineraryService.reorderItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary reorder items must not be empty.");
        verify(tripRepository, never()).findById(1L);
    }

    @Test
    void reorderItinerariesRejectsDuplicatedItineraryId() {
        Trip trip = trip(1L);
        ItineraryReorderRequest request = reorderRequest(
                item(100L, 1, 1),
                item(100L, 1, 2)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        assertThatThrownBy(() -> itineraryService.reorderItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary reorder items must not contain duplicated itineraryId. itineraryId=100");
    }

    @Test
    void reorderItinerariesRejectsItineraryInOtherTrip() {
        Trip trip = trip(1L);
        Trip otherTrip = trip(2L);
        Place place = place(10L);
        Itinerary otherTripItinerary = itinerary(
                100L,
                otherTrip,
                place,
                1,
                1,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                0
        );
        ItineraryReorderRequest request = reorderRequest(item(100L, 1, 1));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(otherTripItinerary));

        assertThatThrownBy(() -> itineraryService.reorderItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary does not belong to trip. itineraryId=100");
        verify(itineraryRepository, never()).findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L);
    }

    @Test
    void reorderItinerariesRejectsDayNoOutsideTripPeriod() {
        Trip trip = trip(1L);
        Place place = place(10L);
        Itinerary itinerary = itinerary(100L, trip, place, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        ItineraryReorderRequest request = reorderRequest(item(100L, 4, 1));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(itinerary));
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L)).thenReturn(List.of(itinerary));

        assertThatThrownBy(() -> itineraryService.reorderItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary dayNo must be within trip period. maxDayNo=3");
    }

    @Test
    void reorderItinerariesRejectsFinalDuplicatedOrderNoInSameDay() {
        Trip trip = trip(1L);
        Place place = place(10L);
        Itinerary first = itinerary(100L, trip, place, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        Itinerary second = itinerary(200L, trip, place, 1, 2, LocalTime.of(10, 30), LocalTime.of(11, 30), 0);
        ItineraryReorderRequest request = reorderRequest(item(200L, 1, 1));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(200L)).thenReturn(Optional.of(second));
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L))
                .thenReturn(List.of(first, second));

        assertThatThrownBy(() -> itineraryService.reorderItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary dayNo and orderNo already exist in this trip.");
    }

    @Test
    void reorderItinerariesRejectsTimeOverlapAfterDayNoChange() {
        Trip trip = trip(1L);
        Place place = place(10L);
        Itinerary first = itinerary(100L, trip, place, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        Itinerary second = itinerary(200L, trip, place, 2, 1, LocalTime.of(9, 30), LocalTime.of(10, 30), 0);
        ItineraryReorderRequest request = reorderRequest(item(200L, 1, 2));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(200L)).thenReturn(Optional.of(second));
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L))
                .thenReturn(List.of(first, second));

        assertThatThrownBy(() -> itineraryService.reorderItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary time overlaps with existing itinerary.");
    }

    @Test
    void reorderItinerariesRejectsOrderNoNotFollowingTimeOrder() {
        Trip trip = trip(1L);
        Place place = place(10L);
        Itinerary first = itinerary(100L, trip, place, 1, 1, LocalTime.of(15, 0), LocalTime.of(16, 0), 0);
        Itinerary second = itinerary(200L, trip, place, 2, 1, LocalTime.of(10, 0), LocalTime.of(11, 0), 30);
        ItineraryReorderRequest request = reorderRequest(item(200L, 1, 2));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(200L)).thenReturn(Optional.of(second));
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L))
                .thenReturn(List.of(first, second));

        assertThatThrownBy(() -> itineraryService.reorderItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary orderNo must follow time order within each day. dayNo=1");
    }

    @Test
    void reorderItinerariesAllowsAdjacentTimeAfterDayNoChange() {
        Trip trip = trip(1L);
        Place place = place(10L);
        Itinerary first = itinerary(100L, trip, place, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        Itinerary second = itinerary(200L, trip, place, 2, 1, LocalTime.of(10, 0), LocalTime.of(11, 0), 30);
        ItineraryReorderRequest request = reorderRequest(item(200L, 1, 2));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(200L)).thenReturn(Optional.of(second));
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L))
                .thenReturn(List.of(first, second));

        List<ItineraryResponse> responses = itineraryService.reorderItineraries(1L, request);

        assertThat(second.getDayNo()).isEqualTo(1);
        assertThat(second.getOrderNo()).isEqualTo(2);
        assertThat(responses).extracting(ItineraryResponse::itineraryId)
                .containsExactly(100L, 200L);
    }

    @Test
    void reorderItinerariesResetsFirstTravelMinutesWhenMovedToFirstOrder() {
        Trip trip = trip(1L);
        Place place = place(10L);
        Itinerary itinerary = itinerary(100L, trip, place, 1, 2, LocalTime.of(10, 30), LocalTime.of(11, 30), 30);
        ItineraryReorderRequest request = reorderRequest(item(100L, 1, 1));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(itinerary));
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L)).thenReturn(List.of(itinerary));

        List<ItineraryResponse> responses = itineraryService.reorderItineraries(1L, request);

        assertThat(responses.getFirst().travelMinutesFromPrevious()).isZero();
    }

    @Test
    void reorderItinerariesRejectsFirstStartTimeBeforeDailyStartTime() {
        Trip trip = trip(1L, LocalTime.of(10, 30));
        Place place = place(10L);
        Itinerary itinerary = itinerary(100L, trip, place, 1, 2, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        ItineraryReorderRequest request = reorderRequest(item(100L, 1, 1));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(itinerary));
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L)).thenReturn(List.of(itinerary));

        assertThatThrownBy(() -> itineraryService.reorderItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("First itinerary item of each day must start at or after trip dailyStartTime.");
    }

    @Test
    void deleteItineraryDeletesExistingItinerary() {
        Trip trip = trip(1L);
        Place place = place(10L);
        Itinerary itinerary = itinerary(100L, trip, place, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        when(tripRepository.existsById(1L)).thenReturn(true);
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(itinerary));

        itineraryService.deleteItinerary(1L, 100L);

        verify(itineraryRepository).delete(itinerary);
    }

    @Test
    void deleteFirstItineraryRecalculatesRemainingDaySchedule() {
        Trip trip = trip(1L);
        Place firstPlace = place(10L, "First Place");
        Place secondPlace = place(20L, "Second Place");
        Place thirdPlace = place(30L, "Third Place");
        Itinerary first = itinerary(100L, trip, firstPlace, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        Itinerary second = itinerary(200L, trip, secondPlace, 1, 2, LocalTime.of(10, 30), LocalTime.of(11, 30), 30);
        Itinerary third = itinerary(300L, trip, thirdPlace, 1, 3, LocalTime.of(12, 0), LocalTime.of(13, 0), 30);
        when(tripRepository.existsById(1L)).thenReturn(true);
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(first));
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 1))
                .thenReturn(List.of(third, first, second));
        when(routeCalculationAdapter.calculateTravelMinutes(
                secondPlace.getLatitude(),
                secondPlace.getLongitude(),
                thirdPlace.getLatitude(),
                thirdPlace.getLongitude()
        )).thenReturn(25);

        itineraryService.deleteItinerary(1L, 100L);

        assertThat(second.getOrderNo()).isEqualTo(1);
        assertThat(second.getStartTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(second.getEndTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(second.getTravelMinutesFromPrevious()).isZero();
        assertThat(second.getGenerationSource()).isEqualTo(ItineraryGenerationSource.USER_ADJUSTED);
        assertThat(third.getOrderNo()).isEqualTo(2);
        assertThat(third.getStartTime()).isEqualTo(LocalTime.of(10, 25));
        assertThat(third.getEndTime()).isEqualTo(LocalTime.of(11, 25));
        assertThat(third.getTravelMinutesFromPrevious()).isEqualTo(25);
        assertThat(third.getGenerationSource()).isEqualTo(ItineraryGenerationSource.USER_ADJUSTED);
        verify(routeCalculationAdapter).calculateTravelMinutes(
                secondPlace.getLatitude(),
                secondPlace.getLongitude(),
                thirdPlace.getLatitude(),
                thirdPlace.getLongitude()
        );
        verify(itineraryRepository).delete(first);
    }

    @Test
    void deleteItineraryRecalculatesTravelMinutesForImmediateSuccessor() {
        Trip trip = trip(1L);
        Place firstPlace = place(10L);
        Place deletedPlace = place(20L);
        Place thirdPlace = place(30L);
        Itinerary first = itinerary(100L, trip, firstPlace, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        Itinerary deleted = itinerary(200L, trip, deletedPlace, 1, 2, LocalTime.of(10, 30), LocalTime.of(11, 30), 30);
        Itinerary third = itinerary(300L, trip, thirdPlace, 1, 3, LocalTime.of(12, 0), LocalTime.of(13, 0), 20);
        when(tripRepository.existsById(1L)).thenReturn(true);
        when(itineraryRepository.findById(200L)).thenReturn(Optional.of(deleted));
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 1))
                .thenReturn(List.of(third, deleted, first));
        when(routeCalculationAdapter.calculateTravelMinutes(
                firstPlace.getLatitude(),
                firstPlace.getLongitude(),
                thirdPlace.getLatitude(),
                thirdPlace.getLongitude()
        )).thenReturn(47);

        itineraryService.deleteItinerary(1L, 200L);

        assertThat(first.getOrderNo()).isEqualTo(1);
        assertThat(first.getGenerationSource()).isEqualTo(ItineraryGenerationSource.MANUAL);
        assertThat(third.getOrderNo()).isEqualTo(2);
        assertThat(third.getStartTime()).isEqualTo(LocalTime.of(10, 47));
        assertThat(third.getEndTime()).isEqualTo(LocalTime.of(11, 47));
        assertThat(third.getTravelMinutesFromPrevious()).isEqualTo(47);
        assertThat(third.getGenerationSource()).isEqualTo(ItineraryGenerationSource.USER_ADJUSTED);
        verify(routeCalculationAdapter).calculateTravelMinutes(
                firstPlace.getLatitude(),
                firstPlace.getLongitude(),
                thirdPlace.getLatitude(),
                thirdPlace.getLongitude()
        );
        verify(itineraryRepository).delete(deleted);
    }

    @Test
    void deleteLastItineraryDoesNotRecalculateTravelMinutes() {
        Trip trip = trip(1L);
        Place place = place(10L);
        Itinerary first = itinerary(100L, trip, place, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        Itinerary last = itinerary(200L, trip, place, 1, 2, LocalTime.of(10, 30), LocalTime.of(11, 30), 30);
        when(tripRepository.existsById(1L)).thenReturn(true);
        when(itineraryRepository.findById(200L)).thenReturn(Optional.of(last));
        when(itineraryRepository.findByTrip_TripIdAndDayNo(1L, 1)).thenReturn(List.of(first, last));

        itineraryService.deleteItinerary(1L, 200L);

        assertThat(first.getOrderNo()).isEqualTo(1);
        assertThat(first.getStartTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(first.getEndTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(first.getTravelMinutesFromPrevious()).isZero();
        assertThat(first.getGenerationSource()).isEqualTo(ItineraryGenerationSource.MANUAL);
        verify(routeCalculationAdapter, never()).calculateTravelMinutes(any(), any(), any(), any());
        verify(itineraryRepository).delete(last);
    }

    @Test
    void deleteItineraryRejectsUnknownTrip() {
        when(tripRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> itineraryService.deleteItinerary(1L, 100L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Trip not found. tripId=1");
        verify(itineraryRepository, never()).findById(100L);
    }

    @Test
    void deleteItineraryRejectsUnknownItinerary() {
        when(tripRepository.existsById(1L)).thenReturn(true);
        when(itineraryRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itineraryService.deleteItinerary(1L, 100L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Itinerary not found. itineraryId=100");
    }

    @Test
    void deleteItineraryRejectsItineraryInOtherTrip() {
        Trip otherTrip = trip(2L);
        Place place = place(10L);
        Itinerary itinerary = itinerary(100L, otherTrip, place, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        when(tripRepository.existsById(1L)).thenReturn(true);
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(itinerary));

        assertThatThrownBy(() -> itineraryService.deleteItinerary(1L, 100L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary does not belong to trip. itineraryId=100");
        verify(itineraryRepository, never()).delete(itinerary);
    }

    @Test
    void deletedItineraryIsExcludedFromLookupResult() {
        Trip trip = trip(1L);
        Place place = place(10L);
        Itinerary itinerary = itinerary(100L, trip, place, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0);
        when(tripRepository.existsById(1L)).thenReturn(true);
        when(itineraryRepository.findById(100L)).thenReturn(Optional.of(itinerary));
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L)).thenReturn(List.of());

        itineraryService.deleteItinerary(1L, 100L);
        List<ItineraryResponse> responses = itineraryService.getItineraries(1L);

        assertThat(responses).isEmpty();
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
                "First itinerary"
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
        assertThat(responses.get(0).place().placeId()).isEqualTo(10L);
        assertThat(responses.get(0).place().name()).isEqualTo("Test Place");
    }

    @Test
    void getItinerariesRejectsUnknownTrip() {
        when(tripRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> itineraryService.getItineraries(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Trip not found. tripId=1");
    }

    @Test
    void getItinerariesWithOwnerIdRejectsDifferentOwner() {
        Trip trip = trip(1L, 100L);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        assertThatThrownBy(() -> itineraryService.getItineraries(1L, 200L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip owner does not match. tripId=1");
        verify(itineraryRepository, never()).findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L);
    }

    private ItineraryReorderRequest reorderRequest(ItineraryReorderRequestItem... items) {
        return new ItineraryReorderRequest(List.of(items));
    }

    private ItineraryReorderRequestItem item(Long itineraryId, Integer dayNo, Integer orderNo) {
        return new ItineraryReorderRequestItem(itineraryId, dayNo, orderNo);
    }

    private ItineraryCreateRequest request(Long placeId, Integer dayNo, Integer orderNo) {
        return new ItineraryCreateRequest(
                placeId,
                dayNo,
                orderNo,
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                0,
                "First itinerary"
        );
    }

    private Trip trip(Long tripId) {
        return trip(tripId, LocalTime.of(9, 0));
    }

    private Trip trip(Long tripId, Long ownerId) {
        Trip trip = Trip.create(
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "SEOGWIPO",
                ownerId
        );
        setId(trip, "tripId", tripId);
        return trip;
    }

    private Trip trip(Long tripId, LocalTime dailyStartTime) {
        return trip(tripId, dailyStartTime, LocalTime.of(18, 0));
    }

    private Trip trip(Long tripId, LocalTime dailyStartTime, LocalTime dailyEndTime) {
        Trip trip = Trip.create(
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                dailyStartTime,
                dailyEndTime,
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "SEOGWIPO"
        );
        setId(trip, "tripId", tripId);
        return trip;
    }

    private Trip tripWithStartPlace(Long tripId, Long startPlaceId, LocalTime dailyEndTime) {
        Trip trip = Trip.create(
                "제주 여행",
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                dailyEndTime,
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "SEOGWIPO",
                startPlaceId,
                startPlaceId,
                null
        );
        setId(trip, "tripId", tripId);
        return trip;
    }

    private Place place(Long placeId) {
        return place(placeId, "Test Place");
    }

    private Place place(Long placeId, String name) {
        Place place = Place.create(
                name,
                "NATURE",
                "EAST",
                "Test Address",
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
                "Test description",
                true
        );
        setId(place, "placeId", placeId);
        return place;
    }

    private Itinerary itinerary(
            Long itineraryId,
            Trip trip,
            Place place,
            Integer dayNo,
            Integer orderNo,
            LocalTime startTime,
            LocalTime endTime,
            Integer travelMinutesFromPrevious
    ) {
        Itinerary itinerary = Itinerary.create(
                trip,
                place,
                dayNo,
                orderNo,
                startTime,
                endTime,
                travelMinutesFromPrevious,
                "Test reason"
        );
        setId(itinerary, "itineraryId", itineraryId);
        return itinerary;
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
