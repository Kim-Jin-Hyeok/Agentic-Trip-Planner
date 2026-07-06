package com.tripagent.trip.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tripagent.common.response.PageResponse;
import com.tripagent.itinerary.domain.Itinerary;
import com.tripagent.itinerary.repository.ItineraryRepository;
import com.tripagent.place.domain.Place;
import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.domain.TripLike;
import com.tripagent.trip.domain.TripVisibility;
import com.tripagent.trip.dto.PublicTripSort;
import com.tripagent.trip.dto.TripCreateRequest;
import com.tripagent.trip.dto.TripDetailResponse;
import com.tripagent.trip.dto.TripLikeResponse;
import com.tripagent.trip.dto.TripResponse;
import com.tripagent.trip.repository.TripLikeRepository;
import com.tripagent.trip.repository.TripRepository;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
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
    private TripLikeRepository tripLikeRepository;

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
        assertThat(response.nights()).isEqualTo(2);
        assertThat(response.concept()).isEqualTo(TripConcept.HEALING);
        assertThat(response.transportation()).isEqualTo(Transportation.RENT_CAR);
        assertThat(response.visibility()).isEqualTo(TripVisibility.PRIVATE);
        assertThat(response.likeCount()).isZero();
        verify(tripRepository).save(any(Trip.class));
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
    void searchPublicTripsSortsByLikeCountWhenPopularSortIsRequested() {
        Trip popularTrip = trip(3L, "JEJU", TripConcept.HEALING, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3));
        popularTrip.changeVisibility(TripVisibility.PUBLIC);
        popularTrip.increaseLikeCount();
        Sort popularSort = Sort.by(Sort.Order.desc("likeCount"), Sort.Order.desc("tripId"));
        when(tripRepository.searchTripsByVisibility(
                eq(TripVisibility.PUBLIC),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                eq(popularSort)
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
    }

    @Test
    void searchPublicTripsFiltersByNights() {
        Trip twoNightTrip = trip(4L, "JEJU", TripConcept.FOOD, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3));
        twoNightTrip.changeVisibility(TripVisibility.PUBLIC);
        when(tripRepository.searchTripsByVisibility(
                eq(TripVisibility.PUBLIC),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                eq(2),
                eq(Sort.by(Sort.Direction.DESC, "tripId"))
        )).thenReturn(List.of(twoNightTrip));

        List<TripResponse> responses = tripService.searchPublicTrips(
                null,
                null,
                null,
                null,
                null,
                null,
                2,
                PublicTripSort.LATEST
        );

        assertThat(responses).extracting(TripResponse::nights)
                .containsExactly(2);
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
                4,
                PublicTripSort.LATEST
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip nights must be between 1 and 3.");
        verify(tripRepository, never()).searchTripsByVisibility(any(), any(), any(), any(), any(), any(), any(), any(), any(Sort.class));
    }

    @Test
    void searchPublicTripPageReturnsPagedPublicTrips() {
        Trip publicTrip = trip(2L, "JEJU", TripConcept.HEALING, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 3));
        publicTrip.changeVisibility(TripVisibility.PUBLIC);
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "tripId"));
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

        PageResponse<TripResponse> response = tripService.searchPublicTripPage(
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

        assertThat(response.content()).extracting(TripResponse::tripId)
                .containsExactly(2L);
        assertThat(response.page()).isZero();
        assertThat(response.size()).isEqualTo(20);
        assertThat(response.totalElements()).isEqualTo(1L);
        assertThat(response.totalPages()).isEqualTo(1);
        assertThat(response.first()).isTrue();
        assertThat(response.last()).isTrue();
    }

    @Test
    void searchPublicTripPageUsesRequestedPageSizeAndPopularSort() {
        Sort popularSort = Sort.by(Sort.Order.desc("likeCount"), Sort.Order.desc("tripId"));
        PageRequest pageRequest = PageRequest.of(1, 10, popularSort);
        when(tripRepository.searchTripsByVisibility(
                eq(TripVisibility.PUBLIC),
                eq("jeju"),
                eq(TripConcept.FOOD),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                eq(2),
                eq(pageRequest)
        )).thenReturn(Page.empty(pageRequest));

        PageResponse<TripResponse> response = tripService.searchPublicTripPage(
                " JeJu ",
                TripConcept.FOOD,
                null,
                null,
                null,
                null,
                2,
                PublicTripSort.POPULAR,
                1,
                10
        );

        assertThat(response.page()).isEqualTo(1);
        assertThat(response.size()).isEqualTo(10);
        assertThat(response.totalElements()).isZero();
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

        TripDetailResponse response = tripService.getPublicTrip(1L);

        assertThat(response.tripId()).isEqualTo(1L);
        assertThat(response.visibility()).isEqualTo(TripVisibility.PUBLIC);
        assertThat(response.itineraries()).hasSize(3);
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.placeId())
                .containsExactly(10L, 20L, 30L);
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.place().name())
                .containsExactly("Place 10", "Place 20", "Place 30");
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.dayNo())
                .containsExactly(1, 1, 2);
        assertThat(response.itineraries()).extracting(itinerary -> itinerary.orderNo())
                .containsExactly(1, 2, 1);
        verify(itineraryRepository).findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L);
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
    void searchLikedPublicTripsReturnsUserLikedPublicTripsByTripIdDesc() {
        Trip firstTrip = trip(3L);
        firstTrip.changeVisibility(TripVisibility.PUBLIC);
        firstTrip.increaseLikeCount();
        Trip secondTrip = trip(2L);
        secondTrip.changeVisibility(TripVisibility.PUBLIC);
        when(tripLikeRepository.findByUserIdOrderByTrip_TripIdDesc(100L))
                .thenReturn(List.of(
                        TripLike.create(firstTrip, 100L),
                        TripLike.create(secondTrip, 100L)
                ));

        List<TripResponse> responses = tripService.searchLikedPublicTrips(100L);

        assertThat(responses).extracting(TripResponse::tripId)
                .containsExactly(3L, 2L);
        assertThat(responses).extracting(TripResponse::visibility)
                .containsExactly(TripVisibility.PUBLIC, TripVisibility.PUBLIC);
        assertThat(responses).extracting(TripResponse::likeCount)
                .containsExactly(1L, 0L);
    }

    @Test
    void searchLikedPublicTripsExcludesPrivateTripsDefensively() {
        Trip publicTrip = trip(3L);
        publicTrip.changeVisibility(TripVisibility.PUBLIC);
        Trip privateTrip = trip(2L);
        when(tripLikeRepository.findByUserIdOrderByTrip_TripIdDesc(100L))
                .thenReturn(List.of(
                        TripLike.create(publicTrip, 100L),
                        TripLike.create(privateTrip, 100L)
                ));

        List<TripResponse> responses = tripService.searchLikedPublicTrips(100L);

        assertThat(responses).extracting(TripResponse::tripId)
                .containsExactly(3L);
    }

    @Test
    void searchLikedPublicTripsRejectsNullUserId() {
        assertThatThrownBy(() -> tripService.searchLikedPublicTrips(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip like userId is required.");
        verify(tripLikeRepository, never()).findByUserIdOrderByTrip_TripIdDesc(any());
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
    void updateTripVisibilityRejectsPublicVisibilityWhenDayOrderDoesNotStartFromOne() {
        Trip trip = trip(1L);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(true);
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(1L))
                .thenReturn(List.of(
                        itinerary(trip, 10L, 1, 2),
                        itinerary(trip, 20L, 2, 1),
                        itinerary(trip, 30L, 3, 1)
                ));

        assertThatThrownBy(() -> tripService.updateTripVisibility(1L, TripVisibility.PUBLIC))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip itinerary orderNo must be consecutive from 1 before publishing. "
                        + "dayNo=1, expectedOrderNo=1, actualOrderNo=2");
        assertThat(trip.getVisibility()).isEqualTo(TripVisibility.PRIVATE);
    }

    @Test
    void updateTripVisibilityRejectsPublicVisibilityWhenDayOrderHasGap() {
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

        assertThatThrownBy(() -> tripService.updateTripVisibility(1L, TripVisibility.PUBLIC))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip itinerary orderNo must be consecutive from 1 before publishing. "
                        + "dayNo=1, expectedOrderNo=2, actualOrderNo=3");
        assertThat(trip.getVisibility()).isEqualTo(TripVisibility.PRIVATE);
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

        InOrder inOrder = inOrder(itineraryRepository, tripRepository);
        inOrder.verify(itineraryRepository).deleteByTrip_TripId(1L);
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
