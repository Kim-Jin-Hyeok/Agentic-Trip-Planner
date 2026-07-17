package com.tripagent.place.repository;

import com.tripagent.place.domain.PlaceSuggestion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceSuggestionRepository extends JpaRepository<PlaceSuggestion, Long> {

    List<PlaceSuggestion> findByMember_MemberIdOrderByPlaceSuggestionIdDesc(Long memberId);
}
