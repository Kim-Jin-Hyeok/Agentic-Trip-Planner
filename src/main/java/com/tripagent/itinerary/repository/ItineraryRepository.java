package com.tripagent.itinerary.repository;

import com.tripagent.itinerary.domain.Itinerary;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {

    boolean existsByTrip_TripIdAndDayNoAndOrderNo(Long tripId, Integer dayNo, Integer orderNo);

    boolean existsByTrip_TripId(Long tripId);

    List<Itinerary> findByTrip_TripIdAndDayNo(Long tripId, Integer dayNo);

    List<Itinerary> findByTrip_TripIdOrderByDayNoAscOrderNoAsc(Long tripId);
}
