package com.tripagent.place.repository;

import com.tripagent.place.domain.Place;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByUseYnTrueOrderByHealingScoreDesc();

    List<Place> findByUseYnTrueOrderByFoodScoreDesc();

    List<Place> findByUseYnTrueOrderByCafeScoreDesc();

    List<Place> findByUseYnTrueOrderByPhotoScoreDesc();

    List<Place> findByUseYnTrueOrderByCoupleScoreDesc();

    List<Place> findByUseYnTrueOrderByFamilyScoreDesc();

    boolean existsByNameAndAddress(String name, String address);
}
