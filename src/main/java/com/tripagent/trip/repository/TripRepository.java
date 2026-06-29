package com.tripagent.trip.repository;

import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripConcept;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query("""
            select t
            from Trip t
            where (:destination is null or lower(t.destination) like concat('%', :destination, '%'))
              and (:concept is null or t.concept = :concept)
              and (:startDateFrom is null or t.startDate >= :startDateFrom)
              and (:startDateTo is null or t.startDate <= :startDateTo)
              and (:endDateFrom is null or t.endDate >= :endDateFrom)
              and (:endDateTo is null or t.endDate <= :endDateTo)
            """)
    List<Trip> searchTrips(
            @Param("destination") String destination,
            @Param("concept") TripConcept concept,
            @Param("startDateFrom") LocalDate startDateFrom,
            @Param("startDateTo") LocalDate startDateTo,
            @Param("endDateFrom") LocalDate endDateFrom,
            @Param("endDateTo") LocalDate endDateTo,
            Sort sort
    );
}
