package com.tripagent.trip.repository;

import com.tripagent.trip.domain.TripAccommodation;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TripAccommodationRepository extends JpaRepository<TripAccommodation, Long> {

    @Query("""
            select ta
            from TripAccommodation ta
            join fetch ta.accommodation
            where ta.trip.tripId = :tripId
            order by ta.stayDate asc
            """)
    List<TripAccommodation> findByTripIdOrderByStayDate(@Param("tripId") Long tripId);

    @Modifying
    @Query("delete from TripAccommodation ta where ta.trip.tripId = :tripId")
    void deleteByTripId(@Param("tripId") Long tripId);

    @Modifying
    @Query("""
            delete from TripAccommodation ta
            where ta.trip.tripId = :tripId
              and (ta.stayDate < :startDate or ta.stayDate >= :endDate)
            """)
    void deleteOutsideStayRange(
            @Param("tripId") Long tripId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
