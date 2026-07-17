package com.tripagent.trip.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tripagent.common.response.PageResponse;
import com.tripagent.accommodation.domain.Accommodation;
import com.tripagent.itinerary.domain.Itinerary;
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.itinerary.repository.ItineraryRepository;
import com.tripagent.member.domain.Member;
import com.tripagent.member.repository.MemberRepository;
import com.tripagent.place.domain.Place;
import com.tripagent.place.repository.PlaceRepository;
import com.tripagent.route.RouteCalculationAdapter;
import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripAccommodation;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.domain.TripLike;
import com.tripagent.trip.domain.TripView;
import com.tripagent.trip.domain.TripVisibility;
import com.tripagent.trip.dto.PublicTripDetailResponse;
import com.tripagent.trip.dto.PublicTripResponse;
import com.tripagent.trip.dto.PublicTripSort;
import com.tripagent.trip.dto.TripConditionUpdateRequest;
import com.tripagent.trip.dto.TripCreateRequest;
import com.tripagent.trip.dto.TripDetailResponse;
import com.tripagent.trip.dto.TripLikeResponse;
import com.tripagent.trip.dto.TripPlaceSummaryResponse;
import com.tripagent.trip.dto.TripResponse;
import com.tripagent.trip.repository.TripLikeRepository;
import com.tripagent.trip.repository.TripAccommodationRepository;
import com.tripagent.trip.repository.TripRepository;
import com.tripagent.trip.repository.TripViewRepository;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.mockito.ArgumentCaptor;
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

    @Mock
    private TripAccommodationRepository tripAccommodationRepository;

    @Mock
    private TripLikeRepository tripLikeRepository;

    @Mock
    private TripViewRepository tripViewRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private RouteCalculationAdapter routeCalculationAdapter;

    @InjectMocks
    private TripService tripService;

    @BeforeEach
    void setUpDefaultTripEndpoint() {
        Place airport = org.mockito.Mockito.mock(Place.class);
        lenient().when(airport.getPlaceId()).thenReturn(1550L);
        lenient().when(airport.getUseYn()).thenReturn(true);
        lenient().when(placeRepository.findFirstByNameAndUseYnTrueOrderByPlaceIdDesc("제주국제공항"))
                .thenReturn(Optional.of(airport));
    }

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
        assertThat(response.nights()).isEqualTo(2);
        assertThat(response.concept()).isEqualTo(TripConcept.HEALING);
        assertThat(response.transportation()).isEqualTo(Transportation.RENT_CAR);
        assertThat(response.visibility()).isEqualTo(TripVisibility.PRIVATE);
        assertThat(response.likeCount()).isZero();
        assertThat(response.viewCount()).isZero();
        assertThat(response.title()).isEqualTo("JEJU 여행");
        assertThat(response.startPlaceId()).isEqualTo(1550L);
        assertThat(response.endPlaceId()).isEqualTo(1550L);
        verify(tripRepository).save(any(Trip.class));
    }

    @Test
    void createTripAllowsSixNightsSevenDays() {
        TripCreateRequest request = new TripCreateRequest(
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 7),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "SEOGWIPO"
        );
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TripResponse response = tripService.createTrip(request);

        assertThat(response.nights()).isEqualTo(6);
        verify(tripRepository).save(any(Trip.class));
    }

    @Test
    void createTripUsesRequestedTitle() {
        TripCreateRequest request = new TripCreateRequest(
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "SEOGWIPO",
                null,
                "부모님과 제주 여행"
        );
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TripResponse response = tripService.createTrip(request);

        assertThat(response.title()).isEqualTo("부모님과 제주 여행");
    }

    @Test
    void createTripSavesValidKoreanJejuTrip() {
        TripCreateRequest request = new TripCreateRequest(
                "제주",
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

        assertThat(response.destination()).isEqualTo("제주");
        assertThat(response.nights()).isEqualTo(2);
        assertThat(response.transportation()).isEqualTo(Transportation.RENT_CAR);
        verify(tripRepository).save(any(Trip.class));
    }

    @Test
    void createTripSavesOwnerIdWhenProvided() {
        TripCreateRequest request = new TripCreateRequest(
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "SEOGWIPO",
                100L
        );
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> invocation.getArgument(0));

        tripService.createTrip(request);

        ArgumentCaptor<Trip> tripCaptor = ArgumentCaptor.forClass(Trip.class);
        verify(tripRepository).save(tripCaptor.capture());
        assertThat(tripCaptor.getValue().getOwnerId()).isEqualTo(100L);
        assertThat(tripCaptor.getValue().getVisibility()).isEqualTo(TripVisibility.PRIVATE);
    }

    @Test
    void createTripWithAuthenticatedOwnerIdIgnoresRequestOwnerId() {
        TripCreateRequest request = new TripCreateRequest(
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "SEOGWIPO",
                999L
        );
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> invocation.getArgument(0));

        tripService.createTrip(request, 100L);

        ArgumentCaptor<Trip> tripCaptor = ArgumentCaptor.forClass(Trip.class);
        verify(tripRepository).save(tripCaptor.capture());
        assertThat(tripCaptor.getValue().getOwnerId()).isEqualTo(100L);
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
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "SEOGWIPO"
        );

        assertThatThrownBy(() -> tripService.createTrip(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip duration must be 1 night 2 days to 6 nights 7 days.");
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
        stubSearchTrips(List.of(firstTrip, secondTrip));

        List<TripResponse> responses = tripService.searchTrips(null, null, null, null, null, null);

        assertThat(responses).extracting(TripResponse::tripId)
                .containsExactly(3L, 2L);
        assertThat(responses).extracting(TripResponse::destination)
                .containsExactly("JEJU", "JEJU");
        verify(tripRepository).searchTrips(
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                eq(Sort.by(Sort.Direction.DESC, "tripId"))
        );
    }

    @Test
    void searchTripsFiltersByDestinationKeyword() {
        Trip jejuTrip = trip(1L, "JEJU EAST", TripConcept.FOOD, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3));
        stubSearchTrips(List.of(jejuTrip));

        List<TripResponse> responses = tripService.searchTrips(" JeJu ", null, null, null, null, null);

        assertThat(responses).extracting(TripResponse::tripId)
                .containsExactly(1L);
        verify(tripRepository).searchTrips(
                eq("jeju"),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                eq(Sort.by(Sort.Direction.DESC, "tripId"))
        );
    }

    @Test
    void searchTripsFiltersByConcept() {
        Trip healingTrip = trip(2L, "JEJU", TripConcept.HEALING, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3));
        stubSearchTrips(List.of(healingTrip));

        List<TripResponse> responses = tripService.searchTrips(null, TripConcept.HEALING, null, null, null, null);

        assertThat(responses).extracting(TripResponse::tripId)
                .containsExactly(2L);
        verify(tripRepository).searchTrips(
                isNull(),
                eq(TripConcept.HEALING),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                eq(Sort.by(Sort.Direction.DESC, "tripId"))
        );
    }

    @Test
    void searchTripsFiltersByStartDateRange() {
        Trip inRangeTrip = trip(1L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 5), LocalDate.of(2026, 7, 7));
        stubSearchTrips(List.of(inRangeTrip));

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
        verify(tripRepository).searchTrips(
                isNull(),
                isNull(),
                eq(LocalDate.of(2026, 7, 4)),
                eq(LocalDate.of(2026, 7, 8)),
                isNull(),
                isNull(),
                eq(Sort.by(Sort.Direction.DESC, "tripId"))
        );
    }

    @Test
    void searchTripsFiltersByEndDateRange() {
        Trip inRangeTrip = trip(1L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 5));
        stubSearchTrips(List.of(inRangeTrip));

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
        verify(tripRepository).searchTrips(
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                eq(LocalDate.of(2026, 7, 4)),
                eq(LocalDate.of(2026, 7, 8)),
                eq(Sort.by(Sort.Direction.DESC, "tripId"))
        );
    }

    @Test
    void searchTripsFiltersByStartDateFromOnly() {
        Trip inRangeTrip = trip(1L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 5), LocalDate.of(2026, 7, 7));
        stubSearchTrips(List.of(inRangeTrip));

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
        verify(tripRepository).searchTrips(
                isNull(),
                isNull(),
                eq(LocalDate.of(2026, 7, 4)),
                isNull(),
                isNull(),
                isNull(),
                eq(Sort.by(Sort.Direction.DESC, "tripId"))
        );
    }

    @Test
    void searchTripsFiltersByEndDateToOnly() {
        Trip inRangeTrip = trip(1L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 5));
        stubSearchTrips(List.of(inRangeTrip));

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
        verify(tripRepository).searchTrips(
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                eq(LocalDate.of(2026, 7, 6)),
                eq(Sort.by(Sort.Direction.DESC, "tripId"))
        );
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
        verify(tripRepository, never()).searchTrips(any(), any(), any(), any(), any(), any(), any(Sort.class));
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
        verify(tripRepository, never()).searchTrips(any(), any(), any(), any(), any(), any(), any(Sort.class));
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
    void getTripReturnsAccommodationAndTripEndArrivalSummaries() {
        Trip trip = Trip.create(
                "제주 여행",
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "SEOGWIPO",
                10L,
                50L,
                null
        );
        setId(trip, "tripId", 1L);
        Place dayOnePlace = place(20L, "Day One Last Place");
        Place dayThreePlace = place(30L, "Day Three Last Place");
        Place airport = place(50L, "제주국제공항");
        Itinerary dayOneItinerary = Itinerary.create(
                trip,
                dayOnePlace,
                1,
                1,
                LocalTime.of(16, 0),
                LocalTime.of(17, 0),
                0,
                "Day one"
        );
        Itinerary dayThreeItinerary = Itinerary.create(
                trip,
                dayThreePlace,
                3,
                1,
                LocalTime.of(16, 30),
                LocalTime.of(17, 30),
                0,
                "Day three"
        );
        TripAccommodation tripAccommodation = org.mockito.Mockito.mock(TripAccommodation.class);
        Accommodation accommodation = org.mockito.Mockito.mock(Accommodation.class);
        when(tripAccommodation.getStayDate()).thenReturn(LocalDate.of(2026, 7, 1));
        when(tripAccommodation.getAccommodation()).thenReturn(accommodation);
        when(accommodation.getName()).thenReturn("첫날 숙소");
        when(accommodation.getLatitude()).thenReturn(33.4);
        when(accommodation.getLongitude()).thenReturn(126.5);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L))
                .thenReturn(List.of(dayOneItinerary, dayThreeItinerary));
        when(tripAccommodationRepository.findByTripIdOrderByStayDate(1L))
                .thenReturn(List.of(tripAccommodation));
        when(placeRepository.findById(50L)).thenReturn(Optional.of(airport));
        when(routeCalculationAdapter.calculateTravelMinutes(any(), any(), any(), any()))
                .thenReturn(25, 45);

        TripDetailResponse response = tripService.getTrip(1L);

        assertThat(response.dayEndRoutes()).hasSize(2);
        assertThat(response.dayEndRoutes().get(0).dayNo()).isEqualTo(1);
        assertThat(response.dayEndRoutes().get(0).destinationType()).isEqualTo("ACCOMMODATION");
        assertThat(response.dayEndRoutes().get(0).destinationName()).isEqualTo("첫날 숙소");
        assertThat(response.dayEndRoutes().get(0).travelMinutes()).isEqualTo(25);
        assertThat(response.dayEndRoutes().get(0).estimatedArrivalTime()).isEqualTo(LocalTime.of(17, 25));
        assertThat(response.dayEndRoutes().get(0).arrivalAfterDailyEndTime()).isFalse();
        assertThat(response.dayEndRoutes().get(1).dayNo()).isEqualTo(3);
        assertThat(response.dayEndRoutes().get(1).destinationType()).isEqualTo("TRIP_END");
        assertThat(response.dayEndRoutes().get(1).destinationName()).isEqualTo("제주국제공항");
        assertThat(response.dayEndRoutes().get(1).travelMinutes()).isEqualTo(45);
        assertThat(response.dayEndRoutes().get(1).estimatedArrivalTime()).isEqualTo(LocalTime.of(18, 15));
        assertThat(response.dayEndRoutes().get(1).arrivalAfterDailyEndTime()).isTrue();
    }

    @Test
    void getTripReturnsTripStartAndAccommodationDepartureSummaries() {
        Trip trip = Trip.create(
                "제주 여행",
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 2),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "SEOGWIPO",
                50L,
                50L,
                10L
        );
        setId(trip, "tripId", 1L);
        Place dayOnePlace = place(20L, "Day One First Place");
        Place dayTwoPlace = place(30L, "Day Two First Place");
        Place airport = place(50L, "제주국제공항");
        Itinerary dayOneItinerary = Itinerary.create(
                trip,
                dayOnePlace,
                1,
                1,
                LocalTime.of(9, 40),
                LocalTime.of(10, 40),
                0,
                "Day one"
        );
        Itinerary dayTwoItinerary = Itinerary.create(
                trip,
                dayTwoPlace,
                2,
                1,
                LocalTime.of(9, 20),
                LocalTime.of(10, 20),
                0,
                "Day two"
        );
        TripAccommodation tripAccommodation = org.mockito.Mockito.mock(TripAccommodation.class);
        Accommodation accommodation = org.mockito.Mockito.mock(Accommodation.class);
        when(tripAccommodation.getStayDate()).thenReturn(LocalDate.of(2026, 7, 1));
        when(tripAccommodation.getAccommodation()).thenReturn(accommodation);
        when(accommodation.getName()).thenReturn("첫날 숙소");
        when(accommodation.getLatitude()).thenReturn(33.4);
        when(accommodation.getLongitude()).thenReturn(126.5);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L))
                .thenReturn(List.of(dayOneItinerary, dayTwoItinerary));
        when(tripAccommodationRepository.findByTripIdOrderByStayDate(1L))
                .thenReturn(List.of(tripAccommodation));
        when(placeRepository.findById(50L)).thenReturn(Optional.of(airport));
        when(routeCalculationAdapter.calculateTravelMinutes(any(), any(), any(), any()))
                .thenReturn(20, 30, 40, 50);

        TripDetailResponse response = tripService.getTrip(1L);

        assertThat(response.dayStartRoutes()).hasSize(2);
        assertThat(response.dayStartRoutes().get(0).dayNo()).isEqualTo(1);
        assertThat(response.dayStartRoutes().get(0).originType()).isEqualTo("TRIP_START");
        assertThat(response.dayStartRoutes().get(0).originName()).isEqualTo("제주국제공항");
        assertThat(response.dayStartRoutes().get(0).travelMinutes()).isEqualTo(40);
        assertThat(response.dayStartRoutes().get(0).estimatedDepartureTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(response.dayStartRoutes().get(0).departureBeforeDailyStartTime()).isFalse();
        assertThat(response.dayStartRoutes().get(1).dayNo()).isEqualTo(2);
        assertThat(response.dayStartRoutes().get(1).originType()).isEqualTo("ACCOMMODATION");
        assertThat(response.dayStartRoutes().get(1).originName()).isEqualTo("첫날 숙소");
        assertThat(response.dayStartRoutes().get(1).travelMinutes()).isEqualTo(50);
        assertThat(response.dayStartRoutes().get(1).estimatedDepartureTime()).isEqualTo(LocalTime.of(8, 30));
        assertThat(response.dayStartRoutes().get(1).departureBeforeDailyStartTime()).isTrue();
    }

    @Test
    void getTripWithOwnerIdRejectsDifferentOwner() {
        Trip trip = trip(
                1L,
                "JEJU",
                TripConcept.HEALING,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                100L
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        assertThatThrownBy(() -> tripService.getTrip(1L, 200L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip owner does not match. tripId=1");
        verify(itineraryRepository, never()).findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L);
    }

    @Test
    void searchPublicTripsReturnsOnlyPublicTripsByTripIdDescendingOrder() {
        Trip publicTrip = trip(2L, "JEJU", TripConcept.HEALING, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3));
        publicTrip.changeVisibility(TripVisibility.PUBLIC);
        when(tripRepository.searchTripsByVisibility(
                eq(TripVisibility.PUBLIC),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                eq(Sort.by(Sort.Direction.DESC, "tripId"))
        )).thenReturn(List.of(publicTrip));

        List<TripResponse> responses = tripService.searchPublicTrips(null, null, null, null, null, null);

        assertThat(responses).extracting(TripResponse::tripId)
                .containsExactly(2L);
        assertThat(responses).extracting(TripResponse::visibility)
                .containsExactly(TripVisibility.PUBLIC);
    }

    @Test
    void searchPublicTripsUsesLikeCountViewCountAndTripIdForPopularSort() {
        Trip popularTrip = trip(3L, "JEJU", TripConcept.HEALING, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3));
        popularTrip.changeVisibility(TripVisibility.PUBLIC);
        popularTrip.increaseLikeCount();
        popularTrip.increaseViewCount();
        when(tripRepository.searchTripsByVisibility(
                eq(TripVisibility.PUBLIC),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                any(Sort.class)
        )).thenReturn(List.of(popularTrip));

        List<TripResponse> responses = tripService.searchPublicTrips(
                null,
                null,
                null,
                null,
                null,
                null,
                PublicTripSort.POPULAR
        );

        assertThat(responses).extracting(TripResponse::tripId)
                .containsExactly(3L);
        assertThat(responses).extracting(TripResponse::likeCount)
                .containsExactly(1L);
        assertThat(responses).extracting(TripResponse::viewCount)
                .containsExactly(1L);

        ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
        verify(tripRepository).searchTripsByVisibility(
                eq(TripVisibility.PUBLIC),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                sortCaptor.capture()
        );
        assertPopularSort(sortCaptor.getValue());
    }

    @Test
    void searchPublicTripsFiltersByNights() {
        Trip sixNightTrip = trip(
                4L,
                "JEJU",
                TripConcept.FOOD,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 7)
        );
        sixNightTrip.changeVisibility(TripVisibility.PUBLIC);
        when(tripRepository.searchTripsByVisibility(
                eq(TripVisibility.PUBLIC),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                eq(6),
                eq(Sort.by(Sort.Direction.DESC, "tripId"))
        )).thenReturn(List.of(sixNightTrip));

        List<TripResponse> responses = tripService.searchPublicTrips(
                null,
                null,
                null,
                null,
                null,
                null,
                6,
                PublicTripSort.LATEST
        );

        assertThat(responses).extracting(TripResponse::nights)
                .containsExactly(6);
    }

    @Test
    void searchPublicTripsRejectsInvalidNights() {
        assertThatThrownBy(() -> tripService.searchPublicTrips(
                null,
                null,
                null,
                null,
                null,
                null,
                7,
                PublicTripSort.LATEST
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip nights must be between 1 and 6.");
        verify(tripRepository, never()).searchTripsByVisibility(any(), any(), any(), any(), any(), any(), any(), any(), any(Sort.class));
    }

    @Test
    void searchPublicTripPageReturnsPagedPublicTrips() {
        Trip publicTrip = trip(2L, "JEJU", TripConcept.HEALING, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3));
        publicTrip.changeVisibility(TripVisibility.PUBLIC);
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "tripId"));
        List<Itinerary> itineraries = List.of(
                itinerary(publicTrip, 10L, 1, 1),
                itinerary(publicTrip, 20L, 1, 2),
                itinerary(publicTrip, 30L, 2, 1),
                itinerary(publicTrip, 40L, 2, 2)
        );
        Page<Trip> tripPage = new PageImpl<>(List.of(publicTrip), pageRequest, 1);
        when(tripRepository.searchTripsByVisibility(
                eq(TripVisibility.PUBLIC),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                eq(pageRequest)
        )).thenReturn(tripPage);
        when(itineraryRepository.findByTrip_TripIdInOrderByTrip_TripIdAscDayNoAscOrderNoAsc(List.of(2L)))
                .thenReturn(itineraries);

        PageResponse<PublicTripResponse> response = tripService.searchPublicTripPage(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                PublicTripSort.LATEST,
                null,
                null
        );

        assertThat(response.content()).extracting(PublicTripResponse::tripId)
                .containsExactly(2L);
        assertThat(response.content()).extracting(PublicTripResponse::liked)
                .containsExactly(false);
        assertThat(response.content()).extracting(PublicTripResponse::viewCount)
                .containsExactly(0L);
        assertThat(response.content().get(0).representativePlaces()).hasSize(3);
        assertThat(response.content().get(0).representativePlaces()).extracting(TripPlaceSummaryResponse::placeId)
                .containsExactly(10L, 20L, 30L);
        assertThat(response.content().get(0).representativePlaces()).extracting(TripPlaceSummaryResponse::name)
                .containsExactly("Place 10", "Place 20", "Place 30");
        assertThat(publicTrip.getViewCount()).isZero();
        assertThat(response.page()).isZero();
        assertThat(response.size()).isEqualTo(20);
        assertThat(response.totalElements()).isEqualTo(1L);
        assertThat(response.totalPages()).isEqualTo(1);
        assertThat(response.first()).isTrue();
        assertThat(response.last()).isTrue();
    }

    @Test
    void searchPublicTripPageMarksLikedTripsForCurrentUser() {
        Trip likedTrip = trip(3L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3), 100L);
        likedTrip.changeVisibility(TripVisibility.PUBLIC);
        Trip notLikedTrip = trip(2L, "JEJU", TripConcept.HEALING, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3));
        notLikedTrip.changeVisibility(TripVisibility.PUBLIC);
        Member author = member(100L, "jeju-maker");
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "tripId"));
        when(tripRepository.searchTripsByVisibility(
                eq(TripVisibility.PUBLIC),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                eq(pageRequest)
        )).thenReturn(new PageImpl<>(List.of(likedTrip, notLikedTrip), pageRequest, 2));
        when(tripLikeRepository.findByUserIdAndTrip_TripIdIn(100L, List.of(3L, 2L)))
                .thenReturn(List.of(TripLike.create(likedTrip, 100L)));
        when(memberRepository.findAllById(List.of(100L))).thenReturn(List.of(author));
        when(itineraryRepository.findByTrip_TripIdInOrderByTrip_TripIdAscDayNoAscOrderNoAsc(List.of(3L, 2L)))
                .thenReturn(List.of(itinerary(likedTrip, 30L, 1, 1)));

        PageResponse<PublicTripResponse> response = tripService.searchPublicTripPage(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                PublicTripSort.LATEST,
                null,
                null,
                100L
        );

        assertThat(response.content()).extracting(PublicTripResponse::tripId)
                .containsExactly(3L, 2L);
        assertThat(response.content()).extracting(PublicTripResponse::liked)
                .containsExactly(true, false);
        assertThat(response.content()).extracting(responseTrip -> responseTrip.author() == null ? null : responseTrip.author().memberId())
                .containsExactly(100L, null);
        assertThat(response.content()).extracting(responseTrip -> responseTrip.author() == null ? null : responseTrip.author().nickname())
                .containsExactly("jeju-maker", null);
        assertThat(response.content().get(0).representativePlaces()).extracting(TripPlaceSummaryResponse::placeId)
                .containsExactly(30L);
        assertThat(response.content().get(1).representativePlaces()).isEmpty();
    }

    @Test
    void searchPublicTripPageMapsRepresentativePlacesByTripAndLimitsToThree() {
        Trip firstTrip = trip(3L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3));
        firstTrip.changeVisibility(TripVisibility.PUBLIC);
        Trip secondTrip = trip(2L, "JEJU", TripConcept.HEALING, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3));
        secondTrip.changeVisibility(TripVisibility.PUBLIC);
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "tripId"));
        when(tripRepository.searchTripsByVisibility(
                eq(TripVisibility.PUBLIC),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                eq(pageRequest)
        )).thenReturn(new PageImpl<>(List.of(firstTrip, secondTrip), pageRequest, 2));
        when(itineraryRepository.findByTrip_TripIdInOrderByTrip_TripIdAscDayNoAscOrderNoAsc(List.of(3L, 2L)))
                .thenReturn(List.of(
                        itinerary(firstTrip, 31L, 1, 1),
                        itinerary(firstTrip, 32L, 1, 2),
                        itinerary(firstTrip, 33L, 2, 1),
                        itinerary(firstTrip, 34L, 2, 2),
                        itinerary(secondTrip, 21L, 1, 1),
                        itinerary(secondTrip, 22L, 2, 1)
                ));

        PageResponse<PublicTripResponse> response = tripService.searchPublicTripPage(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                PublicTripSort.LATEST,
                null,
                null
        );

        assertThat(response.content()).extracting(PublicTripResponse::tripId)
                .containsExactly(3L, 2L);
        assertThat(response.content().get(0).representativePlaces()).extracting(TripPlaceSummaryResponse::placeId)
                .containsExactly(31L, 32L, 33L);
        assertThat(response.content().get(1).representativePlaces()).extracting(TripPlaceSummaryResponse::placeId)
                .containsExactly(21L, 22L);
    }

    @Test
    void searchPublicTripPageUsesLikeCountViewCountAndTripIdForPopularSort() {
        when(tripRepository.searchTripsByVisibility(
                eq(TripVisibility.PUBLIC),
                eq("jeju"),
                eq(TripConcept.FOOD),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                eq(2),
                any(Pageable.class)
        )).thenAnswer(invocation -> Page.empty(invocation.getArgument(8)));

        PageResponse<PublicTripResponse> response = tripService.searchPublicTripPage(
                " JeJu ",
                TripConcept.FOOD,
                null,
                null,
                null,
                null,
                2,
                PublicTripSort.POPULAR,
                1,
                10,
                100L
        );

        assertThat(response.page()).isEqualTo(1);
        assertThat(response.size()).isEqualTo(10);
        assertThat(response.totalElements()).isZero();
        verify(tripLikeRepository, never()).findByUserIdAndTrip_TripIdIn(any(), any());
        verify(memberRepository, never()).findAllById(any());
        verify(itineraryRepository, never()).findByTrip_TripIdInOrderByTrip_TripIdAscDayNoAscOrderNoAsc(any());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(tripRepository).searchTripsByVisibility(
                eq(TripVisibility.PUBLIC),
                eq("jeju"),
                eq(TripConcept.FOOD),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                eq(2),
                pageableCaptor.capture()
        );
        assertPopularSort(pageableCaptor.getValue().getSort());
    }

    @Test
    void searchPublicTripPageRejectsNegativePage() {
        assertThatThrownBy(() -> tripService.searchPublicTripPage(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                PublicTripSort.LATEST,
                -1,
                20
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Page must be greater than or equal to 0.");
        verify(tripRepository, never()).searchTripsByVisibility(any(), any(), any(), any(), any(), any(), any(), any(), any(Pageable.class));
    }

    @Test
    void searchPublicTripPageRejectsTooLargeSize() {
        assertThatThrownBy(() -> tripService.searchPublicTripPage(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                PublicTripSort.LATEST,
                0,
                51
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Page size must be between 1 and 50.");
        verify(tripRepository, never()).searchTripsByVisibility(any(), any(), any(), any(), any(), any(), any(), any(), any(Pageable.class));
    }

    @Test
    void searchTripsByOwnerIdReturnsOwnerTripsByTripIdDescendingOrder() {
        Trip firstTrip = trip(3L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 12), 100L);
        Trip secondTrip = trip(2L, "JEJU", TripConcept.HEALING, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3), 100L);
        when(tripRepository.findByOwnerIdOrderByTripIdDesc(100L)).thenReturn(List.of(firstTrip, secondTrip));

        List<TripResponse> responses = tripService.searchTripsByOwnerId(100L);

        assertThat(responses).extracting(TripResponse::tripId)
                .containsExactly(3L, 2L);
        verify(tripRepository).findByOwnerIdOrderByTripIdDesc(100L);
    }

    @Test
    void searchTripsByOwnerIdRejectsNullOwnerId() {
        assertThatThrownBy(() -> tripService.searchTripsByOwnerId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip ownerId is required.");
        verify(tripRepository, never()).findByOwnerIdOrderByTripIdDesc(any());
    }

    @Test
    void getPublicTripReturnsPublicTripDetailWithItineraries() {
        Trip trip = trip(1L);
        trip.changeVisibility(TripVisibility.PUBLIC);
        List<Itinerary> itineraries = List.of(
                itinerary(trip, 10L, 1, 1),
                itinerary(trip, 20L, 1, 2),
                itinerary(trip, 30L, 2, 1)
        );
        when(tripRepository.findByTripIdAndVisibility(1L, TripVisibility.PUBLIC)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L)).thenReturn(itineraries);

        PublicTripDetailResponse response = tripService.getPublicTrip(1L);

        assertThat(response.tripId()).isEqualTo(1L);
        assertThat(response.visibility()).isEqualTo(TripVisibility.PUBLIC);
        assertThat(response.viewCount()).isZero();
        assertThat(trip.getViewCount()).isZero();
        assertThat(response.itineraries()).hasSize(3);
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.placeId())
                .containsExactly(10L, 20L, 30L);
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.place().name())
                .containsExactly("Place 10", "Place 20", "Place 30");
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.dayNo())
                .containsExactly(1, 1, 2);
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.orderNo())
                .containsExactly(1, 2, 1);
        assertThat(response.liked()).isFalse();
        assertThat(response.author()).isNull();
        verify(itineraryRepository).findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L);
        verify(tripViewRepository, never()).save(any(TripView.class));
        verify(tripLikeRepository, never()).existsByTrip_TripIdAndUserId(any(), any());
        verify(memberRepository, never()).findById(any());
    }

    @Test
    void getPublicTripReturnsAuthorForGuestWhenTripHasOwner() {
        Trip trip = trip(1L, "JEJU", TripConcept.HEALING, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3), 100L);
        trip.changeVisibility(TripVisibility.PUBLIC);
        Member author = member(100L, "guest-visible-author");
        when(tripRepository.findByTripIdAndVisibility(1L, TripVisibility.PUBLIC)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L)).thenReturn(List.of());
        when(memberRepository.findById(100L)).thenReturn(Optional.of(author));

        PublicTripDetailResponse response = tripService.getPublicTrip(1L);

        assertThat(response.liked()).isFalse();
        assertThat(response.author().memberId()).isEqualTo(100L);
        assertThat(response.author().nickname()).isEqualTo("guest-visible-author");
        assertThat(trip.getViewCount()).isZero();
        verify(tripViewRepository, never()).save(any(TripView.class));
        verify(tripLikeRepository, never()).existsByTrip_TripIdAndUserId(any(), any());
        verify(memberRepository).findById(100L);
    }

    @Test
    void getPublicTripMarksLikedWhenCurrentUserLikedTrip() {
        Trip trip = trip(1L, "JEJU", TripConcept.HEALING, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3), 100L);
        trip.changeVisibility(TripVisibility.PUBLIC);
        Member author = member(100L, "trip-author");
        when(tripRepository.findByTripIdAndVisibility(1L, TripVisibility.PUBLIC)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L)).thenReturn(List.of());
        when(tripLikeRepository.existsByTrip_TripIdAndUserId(1L, 100L)).thenReturn(true);
        when(tripViewRepository.existsByTrip_TripIdAndUserIdAndViewDate(1L, 100L, LocalDate.now()))
                .thenReturn(false);
        when(memberRepository.findById(100L)).thenReturn(Optional.of(author));

        PublicTripDetailResponse response = tripService.getPublicTrip(1L, 100L);

        assertThat(response.tripId()).isEqualTo(1L);
        assertThat(response.liked()).isTrue();
        assertThat(response.viewCount()).isEqualTo(1L);
        assertThat(trip.getViewCount()).isEqualTo(1L);
        assertThat(response.author().memberId()).isEqualTo(100L);
        assertThat(response.author().nickname()).isEqualTo("trip-author");
        verify(tripViewRepository).save(any(TripView.class));
    }

    @Test
    void getPublicTripReturnsNullAuthorWhenAuthorMemberIsMissing() {
        Trip trip = trip(1L, "JEJU", TripConcept.HEALING, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3), 100L);
        trip.changeVisibility(TripVisibility.PUBLIC);
        when(tripRepository.findByTripIdAndVisibility(1L, TripVisibility.PUBLIC)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L)).thenReturn(List.of());
        when(memberRepository.findById(100L)).thenReturn(Optional.empty());

        PublicTripDetailResponse response = tripService.getPublicTrip(1L);

        assertThat(response.author()).isNull();
        verify(memberRepository).findById(100L);
    }

    @Test
    void getPublicTripDoesNotIncreaseViewCountWhenUserAlreadyViewedToday() {
        Trip trip = trip(1L);
        trip.changeVisibility(TripVisibility.PUBLIC);
        when(tripRepository.findByTripIdAndVisibility(1L, TripVisibility.PUBLIC)).thenReturn(Optional.of(trip));
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L)).thenReturn(List.of());
        when(tripViewRepository.existsByTrip_TripIdAndUserIdAndViewDate(1L, 100L, LocalDate.now()))
                .thenReturn(true);

        PublicTripDetailResponse response = tripService.getPublicTrip(1L, 100L);

        assertThat(response.viewCount()).isZero();
        assertThat(trip.getViewCount()).isZero();
        verify(tripViewRepository, never()).save(any(TripView.class));
    }

    @Test
    void getPublicTripRejectsPrivateTrip() {
        when(tripRepository.findByTripIdAndVisibility(1L, TripVisibility.PUBLIC)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tripService.getPublicTrip(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Public trip not found. tripId=1");
        verify(itineraryRepository, never()).findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L);
    }

    @Test
    void searchLikedPublicTripPageReturnsUserLikedPublicTripsByTripIdDesc() {
        Trip firstTrip = trip(
                3L,
                "JEJU",
                TripConcept.HEALING,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                200L
        );
        firstTrip.changeVisibility(TripVisibility.PUBLIC);
        firstTrip.increaseLikeCount();
        Trip secondTrip = trip(2L);
        secondTrip.changeVisibility(TripVisibility.PUBLIC);
        Member author = member(200L, "liked-author");
        PageRequest pageRequest = PageRequest.of(0, 20);
        when(tripLikeRepository.findByUserIdAndTrip_VisibilityOrderByTrip_TripIdDesc(
                100L,
                TripVisibility.PUBLIC,
                pageRequest
        ))
                .thenReturn(new PageImpl<>(List.of(
                        TripLike.create(firstTrip, 100L),
                        TripLike.create(secondTrip, 100L)
                ), pageRequest, 2));
        when(itineraryRepository.findByTrip_TripIdInOrderByTrip_TripIdAscDayNoAscOrderNoAsc(List.of(3L, 2L)))
                .thenReturn(List.of(
                        itinerary(firstTrip, 31L, 1, 1),
                        itinerary(firstTrip, 32L, 1, 2),
                        itinerary(firstTrip, 33L, 2, 1),
                        itinerary(firstTrip, 34L, 2, 2),
                        itinerary(secondTrip, 21L, 1, 1),
                        itinerary(secondTrip, 22L, 2, 1)
                ));
        when(memberRepository.findAllById(List.of(200L))).thenReturn(List.of(author));

        PageResponse<PublicTripResponse> response = tripService.searchLikedPublicTripPage(100L, null, null);

        assertThat(response.content()).extracting(PublicTripResponse::tripId)
                .containsExactly(3L, 2L);
        assertThat(response.content()).extracting(PublicTripResponse::visibility)
                .containsExactly(TripVisibility.PUBLIC, TripVisibility.PUBLIC);
        assertThat(response.content()).extracting(PublicTripResponse::likeCount)
                .containsExactly(1L, 0L);
        assertThat(response.content()).extracting(PublicTripResponse::liked)
                .containsExactly(true, true);
        assertThat(response.content()).extracting(responseTrip -> responseTrip.author() == null ? null : responseTrip.author().memberId())
                .containsExactly(200L, null);
        assertThat(response.content()).extracting(responseTrip -> responseTrip.author() == null ? null : responseTrip.author().nickname())
                .containsExactly("liked-author", null);
        assertThat(response.content().get(0).representativePlaces()).extracting(TripPlaceSummaryResponse::placeId)
                .containsExactly(31L, 32L, 33L);
        assertThat(response.content().get(1).representativePlaces()).extracting(TripPlaceSummaryResponse::placeId)
                .containsExactly(21L, 22L);
        assertThat(response.page()).isZero();
        assertThat(response.size()).isEqualTo(20);
        assertThat(response.totalElements()).isEqualTo(2L);
        assertThat(response.totalPages()).isEqualTo(1);
        assertThat(response.first()).isTrue();
        assertThat(response.last()).isTrue();
    }

    @Test
    void searchLikedPublicTripPageUsesRequestedPageSizeAndPublicVisibility() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        when(tripLikeRepository.findByUserIdAndTrip_VisibilityOrderByTrip_TripIdDesc(
                100L,
                TripVisibility.PUBLIC,
                pageRequest
        )).thenReturn(Page.empty(pageRequest));

        PageResponse<PublicTripResponse> response = tripService.searchLikedPublicTripPage(100L, 1, 10);

        assertThat(response.content()).isEmpty();
        assertThat(response.page()).isEqualTo(1);
        assertThat(response.size()).isEqualTo(10);
        assertThat(response.totalElements()).isZero();
        verify(memberRepository, never()).findAllById(any());
        verify(itineraryRepository, never()).findByTrip_TripIdInOrderByTrip_TripIdAscDayNoAscOrderNoAsc(any());
    }

    @Test
    void searchLikedPublicTripPageRejectsNullUserId() {
        assertThatThrownBy(() -> tripService.searchLikedPublicTripPage(null, 0, 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip like userId is required.");
        verify(tripLikeRepository, never()).findByUserIdAndTrip_VisibilityOrderByTrip_TripIdDesc(
                any(),
                any(),
                any(Pageable.class)
        );
    }

    @Test
    void searchLikedPublicTripPageRejectsInvalidSize() {
        assertThatThrownBy(() -> tripService.searchLikedPublicTripPage(100L, 0, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Page size must be between 1 and 50.");
        verify(tripLikeRepository, never()).findByUserIdAndTrip_VisibilityOrderByTrip_TripIdDesc(
                any(),
                any(),
                any(Pageable.class)
        );
    }

    @Test
    void updateTripVisibilityChangesVisibility() {
        Trip trip = trip(1L);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(true);
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L))
                .thenReturn(publishableItineraries(trip));

        TripResponse response = tripService.updateTripVisibility(1L, TripVisibility.PUBLIC);

        assertThat(response.visibility()).isEqualTo(TripVisibility.PUBLIC);
        assertThat(trip.getVisibility()).isEqualTo(TripVisibility.PUBLIC);
    }

    @Test
    void updateTripVisibilityWithOwnerIdChangesVisibilityWhenOwnerMatches() {
        Trip trip = trip(
                1L,
                "JEJU",
                TripConcept.HEALING,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                100L
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(true);
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L))
                .thenReturn(publishableItineraries(trip));

        TripResponse response = tripService.updateTripVisibility(1L, 100L, TripVisibility.PUBLIC);

        assertThat(response.visibility()).isEqualTo(TripVisibility.PUBLIC);
        assertThat(trip.getVisibility()).isEqualTo(TripVisibility.PUBLIC);
    }

    @Test
    void updateTripVisibilityRejectsPublicVisibilityWhenTripHasNoItinerary() {
        Trip trip = trip(1L);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);

        assertThatThrownBy(() -> tripService.updateTripVisibility(1L, TripVisibility.PUBLIC))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip must have at least one itinerary before publishing.");
        assertThat(trip.getVisibility()).isEqualTo(TripVisibility.PRIVATE);
    }

    @Test
    void updateTripVisibilityRejectsPublicVisibilityWhenTripDayIsMissing() {
        Trip trip = trip(1L);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(true);
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L))
                .thenReturn(List.of(
                        itinerary(trip, 10L, 1, 1),
                        itinerary(trip, 30L, 3, 1)
                ));

        assertThatThrownBy(() -> tripService.updateTripVisibility(1L, TripVisibility.PUBLIC))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip itinerary must include every trip day before publishing. missingDayNos=[2]");
        assertThat(trip.getVisibility()).isEqualTo(TripVisibility.PRIVATE);
    }

    @Test
    void updateTripVisibilityAllowsPublicVisibilityWhenDayOrderDoesNotStartFromOne() {
        Trip trip = trip(1L);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(true);
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L))
                .thenReturn(List.of(
                        itinerary(trip, 10L, 1, 2),
                        itinerary(trip, 20L, 2, 1),
                        itinerary(trip, 30L, 3, 1)
                ));

        TripResponse response = tripService.updateTripVisibility(1L, TripVisibility.PUBLIC);

        assertThat(response.visibility()).isEqualTo(TripVisibility.PUBLIC);
    }

    @Test
    void updateTripVisibilityAllowsPublicVisibilityWhenDayOrderHasGap() {
        Trip trip = trip(1L);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(true);
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L))
                .thenReturn(List.of(
                        itinerary(trip, 10L, 1, 1),
                        itinerary(trip, 11L, 1, 3),
                        itinerary(trip, 20L, 2, 1),
                        itinerary(trip, 30L, 3, 1)
                ));

        TripResponse response = tripService.updateTripVisibility(1L, TripVisibility.PUBLIC);

        assertThat(response.visibility()).isEqualTo(TripVisibility.PUBLIC);
    }

    @Test
    void updateTripVisibilityAllowsPrivateVisibilityWithoutItinerary() {
        Trip trip = trip(1L);
        trip.changeVisibility(TripVisibility.PUBLIC);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        TripResponse response = tripService.updateTripVisibility(1L, TripVisibility.PRIVATE);

        assertThat(response.visibility()).isEqualTo(TripVisibility.PRIVATE);
        assertThat(trip.getVisibility()).isEqualTo(TripVisibility.PRIVATE);
        verify(itineraryRepository, never()).existsByTrip_TripId(1L);
    }

    @Test
    void updateTripVisibilityWithOwnerIdRejectsDifferentOwner() {
        Trip trip = trip(
                1L,
                "JEJU",
                TripConcept.HEALING,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                100L
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        assertThatThrownBy(() -> tripService.updateTripVisibility(1L, 200L, TripVisibility.PUBLIC))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip owner does not match. tripId=1");
        assertThat(trip.getVisibility()).isEqualTo(TripVisibility.PRIVATE);
    }

    @Test
    void updateTripVisibilityRejectsNullVisibility() {
        assertThatThrownBy(() -> tripService.updateTripVisibility(1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip visibility is required.");
        verify(tripRepository, never()).findById(1L);
    }

    @Test
    void updateTripTitleTrimsAndReturnsTitle() {
        Trip trip = trip(1L);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        TripResponse response = tripService.updateTripTitle(1L, null, "  친구들과 제주 여행  ");

        assertThat(response.title()).isEqualTo("친구들과 제주 여행");
        assertThat(trip.getTitle()).isEqualTo("친구들과 제주 여행");
    }

    @Test
    void updateTripTitleRejectsBlankTitle() {
        Trip trip = trip(1L);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        assertThatThrownBy(() -> tripService.updateTripTitle(1L, null, "   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip title is required.");
    }

    @Test
    void updateTripTitleRejectsDifferentOwner() {
        Trip trip = trip(
                1L,
                "JEJU",
                TripConcept.HEALING,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                100L
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        assertThatThrownBy(() -> tripService.updateTripTitle(1L, 200L, "새 제목"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip owner does not match. tripId=1");
    }

    @Test
    void updateTripConditionsUpdatesTripAndRemovesExistingItinerary() {
        Trip trip = trip(
                1L,
                "JEJU",
                TripConcept.HEALING,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                100L
        );
        trip.changeVisibility(TripVisibility.PUBLIC);
        TripConditionUpdateRequest request = new TripConditionUpdateRequest(
                LocalDate.of(2026, 7, 2),
                LocalDate.of(2026, 7, 5),
                LocalTime.of(8, 30),
                LocalTime.of(19, 30),
                TripConcept.FOOD,
                "  JEJU_CITY  "
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(true);

        TripResponse response = tripService.updateTripConditions(1L, 100L, request);

        assertThat(response.startDate()).isEqualTo(LocalDate.of(2026, 7, 2));
        assertThat(response.endDate()).isEqualTo(LocalDate.of(2026, 7, 5));
        assertThat(response.nights()).isEqualTo(3);
        assertThat(response.dailyStartTime()).isEqualTo(LocalTime.of(8, 30));
        assertThat(response.dailyEndTime()).isEqualTo(LocalTime.of(19, 30));
        assertThat(response.concept()).isEqualTo(TripConcept.FOOD);
        assertThat(response.lastAccommodationArea()).isEqualTo("JEJU_CITY");
        assertThat(response.visibility()).isEqualTo(TripVisibility.PRIVATE);
        verify(itineraryRepository).deleteByTrip_TripId(1L);
        verify(tripAccommodationRepository).deleteOutsideStayRange(
                1L,
                LocalDate.of(2026, 7, 2),
                LocalDate.of(2026, 7, 5)
        );
    }

    @Test
    void updateTripConditionsAllowsSixNightsSevenDays() {
        Trip trip = trip(1L);
        TripConditionUpdateRequest request = new TripConditionUpdateRequest(
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 7),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.HEALING,
                "SEOGWIPO"
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);

        TripResponse response = tripService.updateTripConditions(1L, null, request);

        assertThat(response.nights()).isEqualTo(6);
    }

    @Test
    void updateTripConditionsDoesNotRemoveItineraryWhenConditionsAreUnchanged() {
        Trip trip = trip(1L);
        TripConditionUpdateRequest request = new TripConditionUpdateRequest(
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.HEALING,
                "SEOGWIPO"
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        TripResponse response = tripService.updateTripConditions(1L, null, request);

        assertThat(response.nights()).isEqualTo(2);
        verify(itineraryRepository, never()).existsByTrip_TripId(1L);
        verify(itineraryRepository, never()).deleteByTrip_TripId(1L);
        verify(tripAccommodationRepository, never()).deleteOutsideStayRange(any(), any(), any());
    }

    @Test
    void updateTripConditionsRejectsUnsupportedDurationBeforeLookup() {
        TripConditionUpdateRequest request = new TripConditionUpdateRequest(
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 8),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.HEALING,
                "SEOGWIPO"
        );

        assertThatThrownBy(() -> tripService.updateTripConditions(1L, 100L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip duration must be 1 night 2 days to 6 nights 7 days.");
        verify(tripRepository, never()).findById(1L);
    }

    @Test
    void copyPublicTripCreatesPrivateTripWithCopiedItineraries() {
        Trip sourceTrip = trip(
                1L,
                "JEJU",
                TripConcept.HEALING,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                200L
        );
        sourceTrip.changeTitle("부모님과 제주 여행");
        sourceTrip.changeVisibility(TripVisibility.PUBLIC);
        List<Itinerary> sourceItineraries = publishableItineraries(sourceTrip);
        when(tripRepository.findByTripIdAndVisibility(1L, TripVisibility.PUBLIC)).thenReturn(Optional.of(sourceTrip));
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> {
            Trip copiedTrip = invocation.getArgument(0);
            setId(copiedTrip, "tripId", 99L);
            return copiedTrip;
        });
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L))
                .thenReturn(sourceItineraries);
        when(itineraryRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        TripDetailResponse response = tripService.copyPublicTrip(1L, 100L);

        assertThat(response.tripId()).isEqualTo(99L);
        assertThat(response.title()).isEqualTo("부모님과 제주 여행 복사본");
        assertThat(response.visibility()).isEqualTo(TripVisibility.PRIVATE);
        assertThat(response.likeCount()).isZero();
        assertThat(response.viewCount()).isZero();
        assertThat(response.itineraries()).hasSize(3);
        assertThat(response.itineraries()).extracting(ItineraryResponse::placeId)
                .containsExactly(10L, 20L, 30L);

        ArgumentCaptor<Trip> tripCaptor = ArgumentCaptor.forClass(Trip.class);
        verify(tripRepository).save(tripCaptor.capture());
        assertThat(tripCaptor.getValue().getOwnerId()).isEqualTo(100L);
        assertThat(tripCaptor.getValue().getVisibility()).isEqualTo(TripVisibility.PRIVATE);
    }

    @Test
    void copyPublicTripRejectsPrivateOrUnknownSource() {
        when(tripRepository.findByTripIdAndVisibility(1L, TripVisibility.PUBLIC)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tripService.copyPublicTrip(1L, 100L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Public trip not found. tripId=1");
        verify(tripRepository, never()).save(any());
        verify(itineraryRepository, never()).saveAll(any());
    }

    @Test
    void copyPublicTripRejectsMissingOwnerBeforeLookup() {
        assertThatThrownBy(() -> tripService.copyPublicTrip(1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip copy ownerId is required.");
        verify(tripRepository, never()).findByTripIdAndVisibility(1L, TripVisibility.PUBLIC);
    }

    @Test
    void likePublicTripCreatesLikeAndIncreasesCount() {
        Trip trip = trip(1L);
        trip.changeVisibility(TripVisibility.PUBLIC);
        when(tripRepository.findByTripIdAndVisibility(1L, TripVisibility.PUBLIC)).thenReturn(Optional.of(trip));
        when(tripLikeRepository.existsByTrip_TripIdAndUserId(1L, 100L)).thenReturn(false);
        when(tripLikeRepository.save(any(TripLike.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TripLikeResponse response = tripService.likePublicTrip(1L, 100L);

        assertThat(response.tripId()).isEqualTo(1L);
        assertThat(response.userId()).isEqualTo(100L);
        assertThat(response.likeCount()).isEqualTo(1L);
        assertThat(response.liked()).isTrue();
        assertThat(trip.getLikeCount()).isEqualTo(1L);
        verify(tripLikeRepository).save(any(TripLike.class));
    }

    @Test
    void likePublicTripRejectsPrivateTrip() {
        when(tripRepository.findByTripIdAndVisibility(1L, TripVisibility.PUBLIC)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tripService.likePublicTrip(1L, 100L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Public trip not found. tripId=1");
        verify(tripLikeRepository, never()).save(any());
    }

    @Test
    void likePublicTripRejectsDuplicatedLike() {
        Trip trip = trip(1L);
        trip.changeVisibility(TripVisibility.PUBLIC);
        when(tripRepository.findByTripIdAndVisibility(1L, TripVisibility.PUBLIC)).thenReturn(Optional.of(trip));
        when(tripLikeRepository.existsByTrip_TripIdAndUserId(1L, 100L)).thenReturn(true);

        assertThatThrownBy(() -> tripService.likePublicTrip(1L, 100L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip like already exists. tripId=1, userId=100");
        assertThat(trip.getLikeCount()).isZero();
        verify(tripLikeRepository, never()).save(any());
    }

    @Test
    void likePublicTripRejectsNullUserId() {
        assertThatThrownBy(() -> tripService.likePublicTrip(1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip like userId is required.");
        verify(tripRepository, never()).findByTripIdAndVisibility(any(), any());
    }

    @Test
    void unlikePublicTripDeletesLikeAndDecreasesCount() {
        Trip trip = trip(1L);
        trip.changeVisibility(TripVisibility.PUBLIC);
        trip.increaseLikeCount();
        TripLike tripLike = TripLike.create(trip, 100L);
        when(tripRepository.findByTripIdAndVisibility(1L, TripVisibility.PUBLIC)).thenReturn(Optional.of(trip));
        when(tripLikeRepository.findByTrip_TripIdAndUserId(1L, 100L)).thenReturn(Optional.of(tripLike));

        TripLikeResponse response = tripService.unlikePublicTrip(1L, 100L);

        assertThat(response.tripId()).isEqualTo(1L);
        assertThat(response.userId()).isEqualTo(100L);
        assertThat(response.likeCount()).isZero();
        assertThat(response.liked()).isFalse();
        assertThat(trip.getLikeCount()).isZero();
        verify(tripLikeRepository).delete(tripLike);
    }

    @Test
    void unlikePublicTripRejectsMissingLike() {
        Trip trip = trip(1L);
        trip.changeVisibility(TripVisibility.PUBLIC);
        when(tripRepository.findByTripIdAndVisibility(1L, TripVisibility.PUBLIC)).thenReturn(Optional.of(trip));
        when(tripLikeRepository.findByTrip_TripIdAndUserId(1L, 100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tripService.unlikePublicTrip(1L, 100L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Trip like not found. tripId=1, userId=100");
        verify(tripLikeRepository, never()).delete(any());
    }

    @Test
    void deleteTripDeletesTripAndItsItineraries() {
        Trip trip = trip(1L);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        tripService.deleteTrip(1L);

        InOrder inOrder = inOrder(itineraryRepository, tripAccommodationRepository, tripRepository);
        inOrder.verify(itineraryRepository).deleteByTrip_TripId(1L);
        inOrder.verify(tripAccommodationRepository).deleteByTripId(1L);
        inOrder.verify(tripRepository).delete(trip);
    }

    @Test
    void deleteTripWithOwnerIdRejectsDifferentOwner() {
        Trip trip = trip(
                1L,
                "JEJU",
                TripConcept.HEALING,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                100L
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        assertThatThrownBy(() -> tripService.deleteTrip(1L, 200L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip owner does not match. tripId=1");
        verify(itineraryRepository, never()).deleteByTrip_TripId(1L);
        verify(tripAccommodationRepository, never()).deleteByTripId(1L);
        verify(tripRepository, never()).delete(trip);
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
        stubSearchTrips(List.of(remainingTrip));

        tripService.deleteTrip(1L);
        List<TripResponse> responses = tripService.searchTrips(null, null, null, null, null, null);

        assertThat(responses).extracting(TripResponse::tripId)
                .containsExactly(2L);
    }

    private void stubSearchTrips(List<Trip> trips) {
        when(tripRepository.searchTrips(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(Sort.class)
        )).thenReturn(trips);
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
        return trip(tripId, destination, concept, startDate, endDate, null);
    }

    private Trip trip(
            Long tripId,
            String destination,
            TripConcept concept,
            LocalDate startDate,
            LocalDate endDate,
            Long ownerId
    ) {
        Trip trip = Trip.create(
                destination,
                startDate,
                endDate,
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                concept,
                Transportation.RENT_CAR,
                "SEOGWIPO",
                ownerId
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

    private Member member(Long memberId, String nickname) {
        Member member = Member.create("member" + memberId + "@example.com", nickname, "password-hash");
        setId(member, "memberId", memberId);
        return member;
    }

    private List<Itinerary> publishableItineraries(Trip trip) {
        return List.of(
                itinerary(trip, 10L, 1, 1),
                itinerary(trip, 20L, 2, 1),
                itinerary(trip, 30L, 3, 1)
        );
    }

    private Itinerary itinerary(Trip trip, Long placeId, Integer dayNo, Integer orderNo) {
        return Itinerary.create(
                trip,
                place(placeId, "Place " + placeId),
                dayNo,
                orderNo,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                orderNo == 1 ? 0 : 30,
                "reason"
        );
    }

    private void assertPopularSort(Sort sort) {
        assertThat(sort.stream().map(Sort.Order::getProperty).toList())
                .containsExactly("likeCount", "viewCount", "tripId");
        assertThat(sort.stream().map(Sort.Order::getDirection).toList())
                .containsExactly(Sort.Direction.DESC, Sort.Direction.DESC, Sort.Direction.DESC);
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
