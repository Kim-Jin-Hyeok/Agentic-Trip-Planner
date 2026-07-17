package com.tripagent.place.repository;

import com.tripagent.place.domain.PlaceSuggestion;
import com.tripagent.place.domain.PlaceSuggestionStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceSuggestionRepository extends JpaRepository<PlaceSuggestion, Long> {

    List<PlaceSuggestion> findByMember_MemberIdOrderByPlaceSuggestionIdDesc(Long memberId);

    Page<PlaceSuggestion> findByStatusOrderByPlaceSuggestionIdDesc(
            PlaceSuggestionStatus status,
            Pageable pageable
    );
}
