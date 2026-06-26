package com.tripagent.trip.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.dto.TripCreateRequest;
import com.tripagent.trip.dto.TripResponse;
import com.tripagent.trip.repository.TripRepository;
import java.time.LocalDate;
import java.time.LocalTime;
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

    @InjectMocks
    private TripService tripService;

    @Test
    void createTripSavesValidJejuTrip() {
        TripCreateRequest request = new TripCreateRequest(
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "SEOGWIPO"
        );
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TripResponse response = tripService.createTrip(request);

        assertThat(response.destination()).isEqualTo("JEJU");
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
    void getTripRejectsUnknownTripId() {
        when(tripRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tripService.getTrip(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Trip not found. tripId=1");
    }
}
