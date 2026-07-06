package com.tripagent.trip.repository;

import com.tripagent.trip.domain.TripLike;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripLikeRepository extends JpaRepository<TripLike, Long> {

    boolean existsByTrip_TripIdAndUserId(Long tripId, Long userId);

    Optional<TripLike> findByTrip_TripIdAndUserId(Long tripId, Long userId);

    List<TripLike> findByUserIdOrderByTrip_TripIdDesc(Long userId);

    long countByTrip_TripId(Long tripId);
}
