package com.tripagent.place.repository;

import com.tripagent.place.domain.Place;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("""
            select p
            from Place p
            where (:useYn is null or p.useYn = :useYn)
              and (:region is null or p.region = :region)
              and (:category is null or p.category = :category)
              and (
                    :keyword is null
                    or lower(p.name) like concat('%', :keyword, '%')
                    or lower(p.address) like concat('%', :keyword, '%')
              )
            order by p.placeId desc
            """)
    Page<Place> searchAdminPlaces(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("region") String region,
            @Param("useYn") Boolean useYn,
            Pageable pageable
    );

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

    Optional<Place> findFirstByNameAndUseYnTrueOrderByPlaceIdDesc(String name);

    Optional<Place> findFirstByExternalProviderAndExternalPlaceId(
            String externalProvider,
            String externalPlaceId
    );

    Optional<Place> findFirstByNameIgnoreCaseAndAddressIgnoreCaseOrderByPlaceIdDesc(
            String name,
            String address
    );

    List<Place> findByLatitudeBetweenAndLongitudeBetween(
            Double minimumLatitude,
            Double maximumLatitude,
            Double minimumLongitude,
            Double maximumLongitude
    );
}
