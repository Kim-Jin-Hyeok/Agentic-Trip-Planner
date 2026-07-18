package com.tripagent.trip.repository;

import com.tripagent.trip.domain.TripView;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TripViewRepository extends JpaRepository<TripView, Long> {

    boolean existsByTrip_TripIdAndUserIdAndViewDate(Long tripId, Long userId, LocalDate viewDate);

    @Modifying
    @Query("delete from TripView tv where tv.trip.tripId = :tripId")
    void deleteByTripId(@Param("tripId") Long tripId);
}
