package com.tripagent.itinerary.repository;

import com.tripagent.itinerary.domain.ItineraryGenerationPreference;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItineraryGenerationPreferenceRepository
        extends JpaRepository<ItineraryGenerationPreference, Long> {

    Optional<ItineraryGenerationPreference> findByTrip_TripId(Long tripId);

    void deleteByTrip_TripId(Long tripId);
}
