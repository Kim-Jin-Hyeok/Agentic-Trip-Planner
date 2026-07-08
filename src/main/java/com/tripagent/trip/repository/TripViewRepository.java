package com.tripagent.trip.repository;

import com.tripagent.trip.domain.TripView;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripViewRepository extends JpaRepository<TripView, Long> {

    boolean existsByTrip_TripIdAndUserIdAndViewDate(Long tripId, Long userId, LocalDate viewDate);
}
