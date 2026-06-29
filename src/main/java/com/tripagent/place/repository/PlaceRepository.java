package com.tripagent.place.repository;

import com.tripagent.place.domain.Place;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByUseYnTrueOrderByHealingScoreDesc();

    List<Place> findByUseYnTrueOrderByFoodScoreDesc();

    List<Place> findByUseYnTrueOrderByCafeScoreDesc();

    List<Place> findByUseYnTrueOrderByPhotoScoreDesc();

    List<Place> findByUseYnTrueOrderByCoupleScoreDesc();

    List<Place> findByUseYnTrueOrderByFamilyScoreDesc();

    @Query("""
            select p
            from Place p
            where p.useYn = :useYn
              and (:region is null or p.region = :region)
              and (:categoryNamesEmpty = true or p.category in :categoryNames)
              and (
                    :keyword is null
                    or lower(p.name) like concat('%', :keyword, '%')
                    or lower(p.address) like concat('%', :keyword, '%')
                    or lower(coalesce(p.description, '')) like concat('%', :keyword, '%')
              )
              and (
                    :applyBounds = false
                    or (
                        p.latitude between :minLat and :maxLat
                        and p.longitude between :minLng and :maxLng
                    )
              )
            """)
    List<Place> searchPlaces(
            @Param("useYn") Boolean useYn,
            @Param("region") String region,
            @Param("categoryNames") List<String> categoryNames,
            @Param("categoryNamesEmpty") boolean categoryNamesEmpty,
            @Param("keyword") String keyword,
            @Param("applyBounds") boolean applyBounds,
            @Param("minLat") Double minLat,
            @Param("maxLat") Double maxLat,
            @Param("minLng") Double minLng,
            @Param("maxLng") Double maxLng,
            Sort sort
    );

    boolean existsByNameAndAddress(String name, String address);
}
