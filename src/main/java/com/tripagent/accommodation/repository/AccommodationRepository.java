package com.tripagent.accommodation.repository;

import com.tripagent.accommodation.domain.Accommodation;
import com.tripagent.accommodation.domain.AccommodationType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    Optional<Accommodation> findFirstByExternalProviderAndExternalPlaceId(
            String externalProvider,
            String externalPlaceId
    );

    Optional<Accommodation> findFirstByNameIgnoreCaseAndAddressIgnoreCaseOrderByAccommodationIdDesc(
            String name,
            String address
    );

    List<Accommodation> findByLatitudeBetweenAndLongitudeBetween(
            Double minimumLatitude,
            Double maximumLatitude,
            Double minimumLongitude,
            Double maximumLongitude
    );

    @Query("""
            select a
            from Accommodation a
            where a.useYn = :useYn
              and (:accommodationType is null or a.accommodationType = :accommodationType)
              and (:region is null or a.region = :region)
              and (
                    :keyword is null
                    or lower(a.name) like concat('%', :keyword, '%')
                    or lower(a.address) like concat('%', :keyword, '%')
                    or lower(coalesce(a.description, '')) like concat('%', :keyword, '%')
              )
            """)
    Page<Accommodation> searchAccommodations(
            @Param("useYn") Boolean useYn,
            @Param("accommodationType") AccommodationType accommodationType,
            @Param("region") String region,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
            select a
            from Accommodation a
            where (:useYn is null or a.useYn = :useYn)
              and (:accommodationType is null or a.accommodationType = :accommodationType)
              and (:region is null or a.region = :region)
              and (
                    :keyword is null
                    or lower(a.name) like concat('%', :keyword, '%')
                    or lower(a.address) like concat('%', :keyword, '%')
                    or lower(coalesce(a.description, '')) like concat('%', :keyword, '%')
              )
            order by a.accommodationId desc
            """)
    Page<Accommodation> searchAdminAccommodations(
            @Param("useYn") Boolean useYn,
            @Param("accommodationType") AccommodationType accommodationType,
            @Param("region") String region,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
