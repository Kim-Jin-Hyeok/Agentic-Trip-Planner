package com.tripagent.accommodation.repository;

import com.tripagent.accommodation.domain.Accommodation;
import com.tripagent.accommodation.domain.AccommodationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

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
}
