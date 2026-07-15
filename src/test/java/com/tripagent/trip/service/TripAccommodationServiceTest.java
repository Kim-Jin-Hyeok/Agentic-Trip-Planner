package com.tripagent.trip.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tripagent.accommodation.domain.Accommodation;
import com.tripagent.accommodation.domain.AccommodationType;
import com.tripagent.accommodation.repository.AccommodationRepository;
import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripAccommodation;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.dto.TripAccommodationItemRequest;
import com.tripagent.trip.dto.TripAccommodationReplaceRequest;
import com.tripagent.trip.dto.TripAccommodationResponse;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TripAccommodationServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private TripAccommodationRepository tripAccommodationRepository;

    @InjectMocks
    private TripAccommodationService tripAccommodationService;

    @Test
    void replaceTripAccommodationsSavesByStayDateAndAllowsConsecutiveStay() {
        Trip trip = trip(1L, 100L);
        Accommodation accommodation = accommodation(10L, true);
        TripAccommodationReplaceRequest request = new TripAccommodationReplaceRequest(List.of(
                new TripAccommodationItemRequest(LocalDate.of(2026, 7, 2), 10L),
                new TripAccommodationItemRequest(LocalDate.of(2026, 7, 1), 10L)
        ));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(accommodationRepository.findAllById(any())).thenReturn(List.of(accommodation));
        when(tripAccommodationRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<TripAccommodationResponse> responses = tripAccommodationService.replaceTripAccommodations(
                1L,
                100L,
                request
        );

        assertThat(responses).extracting(TripAccommodationResponse::stayDate)
                .containsExactly(LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 2));
        assertThat(responses).extracting(response -> response.accommodation().accommodationId())
                .containsExactly(10L, 10L);
        verify(tripAccommodationRepository).deleteByTripId(1L);
        ArgumentCaptor<List<TripAccommodation>> captor = ArgumentCaptor.forClass(List.class);
        verify(tripAccommodationRepository).saveAll(captor.capture());
        assertThat(captor.getValue()).extracting(TripAccommodation::getStayDate)
                .containsExactly(LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 2));
    }

    @Test
    void replaceTripAccommodationsWithEmptyListClearsExistingItems() {
        Trip trip = trip(1L, 100L);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(tripAccommodationRepository.saveAll(List.of())).thenReturn(List.of());

        List<TripAccommodationResponse> responses = tripAccommodationService.replaceTripAccommodations(
                1L,
                100L,
                new TripAccommodationReplaceRequest(List.of())
        );

        assertThat(responses).isEmpty();
        verify(tripAccommodationRepository).deleteByTripId(1L);
    }

    @Test
    void replaceTripAccommodationsRejectsDuplicatedStayDate() {
        Trip trip = trip(1L, 100L);
        LocalDate stayDate = LocalDate.of(2026, 7, 1);
        TripAccommodationReplaceRequest request = new TripAccommodationReplaceRequest(List.of(
                new TripAccommodationItemRequest(stayDate, 10L),
                new TripAccommodationItemRequest(stayDate, 20L)
        ));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        assertThatThrownBy(() -> tripAccommodationService.replaceTripAccommodations(1L, 100L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip accommodations must not contain duplicated stayDate. stayDate=2026-07-01");
        verify(tripAccommodationRepository, never()).deleteByTripId(1L);
    }

    @Test
    void replaceTripAccommodationsRejectsCheckoutDate() {
        Trip trip = trip(1L, 100L);
        TripAccommodationReplaceRequest request = new TripAccommodationReplaceRequest(List.of(
                new TripAccommodationItemRequest(LocalDate.of(2026, 7, 3), 10L)
        ));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        assertThatThrownBy(() -> tripAccommodationService.replaceTripAccommodations(1L, 100L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip accommodation stayDate must be within trip nights. stayDate=2026-07-03");
        verify(tripAccommodationRepository, never()).deleteByTripId(1L);
    }

    @Test
    void replaceTripAccommodationsRejectsInactiveAccommodation() {
        Trip trip = trip(1L, 100L);
        Accommodation accommodation = accommodation(10L, false);
        TripAccommodationReplaceRequest request = new TripAccommodationReplaceRequest(List.of(
                new TripAccommodationItemRequest(LocalDate.of(2026, 7, 1), 10L)
        ));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(accommodationRepository.findAllById(any())).thenReturn(List.of(accommodation));

        assertThatThrownBy(() -> tripAccommodationService.replaceTripAccommodations(1L, 100L, request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Active accommodation not found. accommodationId=10");
        verify(tripAccommodationRepository, never()).deleteByTripId(1L);
    }

    @Test
    void getTripAccommodationsRejectsDifferentOwner() {
        Trip trip = trip(1L, 100L);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        assertThatThrownBy(() -> tripAccommodationService.getTripAccommodations(1L, 200L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip owner does not match. tripId=1");
        verify(tripAccommodationRepository, never()).findByTripIdOrderByStayDate(1L);
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

    private Accommodation accommodation(Long accommodationId, boolean useYn) {
        Accommodation accommodation = Accommodation.create(
                "Test Hotel",
                AccommodationType.HOTEL,
                "SOUTH",
                "Jeju",
                33.25,
                126.55,
                "description",
                "https://example.com/image.jpg",
                true,
                useYn
        );
        setId(accommodation, "accommodationId", accommodationId);
        return accommodation;
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
