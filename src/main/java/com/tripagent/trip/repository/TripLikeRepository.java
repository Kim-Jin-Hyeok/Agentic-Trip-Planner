package com.tripagent.trip.repository;

import com.tripagent.trip.domain.TripLike;
import com.tripagent.trip.domain.TripVisibility;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripLikeRepository extends JpaRepository<TripLike, Long> {

    boolean existsByTrip_TripIdAndUserId(Long tripId, Long userId);

    Optional<TripLike> findByTrip_TripIdAndUserId(Long tripId, Long userId);

    Page<TripLike> findByUserIdAndTrip_VisibilityOrderByTrip_TripIdDesc(
            Long userId,
            TripVisibility visibility,
            Pageable pageable
    );

    List<TripLike> findByUserIdAndTrip_TripIdIn(Long userId, List<Long> tripIds);

    long countByTrip_TripId(Long tripId);
}
