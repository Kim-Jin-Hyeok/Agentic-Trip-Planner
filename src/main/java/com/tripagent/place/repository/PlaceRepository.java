package com.tripagent.place.repository;

import com.tripagent.place.domain.Place;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByUseYnTrueOrderByHealingScoreDesc();
}
