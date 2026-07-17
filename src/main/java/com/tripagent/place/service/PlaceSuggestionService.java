package com.tripagent.place.service;

import com.tripagent.member.domain.Member;
import com.tripagent.member.repository.MemberRepository;
import com.tripagent.place.domain.PlaceSuggestion;
import com.tripagent.place.dto.PlaceSuggestionCreateRequest;
import com.tripagent.place.dto.PlaceSuggestionResponse;
import com.tripagent.place.repository.PlaceSuggestionRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PlaceSuggestionService {

    private final PlaceSuggestionRepository placeSuggestionRepository;
    private final MemberRepository memberRepository;

    public PlaceSuggestionService(
            PlaceSuggestionRepository placeSuggestionRepository,
            MemberRepository memberRepository
    ) {
        this.placeSuggestionRepository = placeSuggestionRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public PlaceSuggestionResponse createSuggestion(Long memberId, PlaceSuggestionCreateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Place suggestion request is required.");
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("Member not found. memberId=" + memberId));
        PlaceSuggestion suggestion = PlaceSuggestion.create(
                member,
                request.name(),
                request.address(),
                request.description()
        );
        return PlaceSuggestionResponse.from(placeSuggestionRepository.save(suggestion));
    }

    public List<PlaceSuggestionResponse> getMySuggestions(Long memberId) {
        return placeSuggestionRepository.findByMember_MemberIdOrderByPlaceSuggestionIdDesc(memberId).stream()
                .map(PlaceSuggestionResponse::from)
                .toList();
    }
}
