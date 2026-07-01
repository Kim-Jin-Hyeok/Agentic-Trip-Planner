package com.tripagent.trip.repository;

import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.domain.TripVisibility;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByOwnerIdOrderByTripIdDesc(Long ownerId);

    @Query("""
            select t
            from Trip t
            where t.ownerId = :ownerId
              and (:destination is null or lower(t.destination) like concat('%', :destination, '%'))
              and (:concept is null or t.concept = :concept)
              and (:startDateFrom is null or t.startDate >= :startDateFrom)
              and (:startDateTo is null or t.startDate <= :startDateTo)
              and (:endDateFrom is null or t.endDate >= :endDateFrom)
              and (:endDateTo is null or t.endDate <= :endDateTo)
            """)
    List<Trip> searchTripsByOwnerId(
            @Param("ownerId") Long ownerId,
            @Param("destination") String destination,
            @Param("concept") TripConcept concept,
            @Param("startDateFrom") LocalDate startDateFrom,
            @Param("startDateTo") LocalDate startDateTo,
            @Param("endDateFrom") LocalDate endDateFrom,
            @Param("endDateTo") LocalDate endDateTo,
            Sort sort
    );

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

    @Query("""
            select t
            from Trip t
            where t.visibility = :visibility
              and (:destination is null or lower(t.destination) like concat('%', :destination, '%'))
              and (:concept is null or t.concept = :concept)
              and (:startDateFrom is null or t.startDate >= :startDateFrom)
              and (:startDateTo is null or t.startDate <= :startDateTo)
              and (:endDateFrom is null or t.endDate >= :endDateFrom)
              and (:endDateTo is null or t.endDate <= :endDateTo)
              and (:nights is null or t.nights = :nights)
            """)
    List<Trip> searchTripsByVisibility(
            @Param("visibility") TripVisibility visibility,
            @Param("destination") String destination,
            @Param("concept") TripConcept concept,
            @Param("startDateFrom") LocalDate startDateFrom,
            @Param("startDateTo") LocalDate startDateTo,
            @Param("endDateFrom") LocalDate endDateFrom,
            @Param("endDateTo") LocalDate endDateTo,
            @Param("nights") Integer nights,
            Sort sort
    );

    @Query("""
            select t
            from Trip t
            where t.visibility = :visibility
              and (:destination is null or lower(t.destination) like concat('%', :destination, '%'))
              and (:concept is null or t.concept = :concept)
              and (:startDateFrom is null or t.startDate >= :startDateFrom)
              and (:startDateTo is null or t.startDate <= :startDateTo)
              and (:endDateFrom is null or t.endDate >= :endDateFrom)
              and (:endDateTo is null or t.endDate <= :endDateTo)
              and (:nights is null or t.nights = :nights)
            """)
    Page<Trip> searchTripsByVisibility(
            @Param("visibility") TripVisibility visibility,
            @Param("destination") String destination,
            @Param("concept") TripConcept concept,
            @Param("startDateFrom") LocalDate startDateFrom,
            @Param("startDateTo") LocalDate startDateTo,
            @Param("endDateFrom") LocalDate endDateFrom,
            @Param("endDateTo") LocalDate endDateTo,
            @Param("nights") Integer nights,
            Pageable pageable
    );

    Optional<Trip> findByTripIdAndVisibility(Long tripId, TripVisibility visibility);
}
