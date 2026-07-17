package com.tripagent.place.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tripagent.member.domain.Member;
import com.tripagent.member.repository.MemberRepository;
import com.tripagent.place.domain.PlaceSuggestion;
import com.tripagent.place.domain.PlaceSuggestionStatus;
import com.tripagent.place.dto.PlaceSuggestionCreateRequest;
import com.tripagent.place.dto.PlaceSuggestionResponse;
import com.tripagent.place.repository.PlaceSuggestionRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PlaceSuggestionServiceTest {

    private PlaceSuggestionRepository placeSuggestionRepository;
    private MemberRepository memberRepository;
    private PlaceSuggestionService placeSuggestionService;

    @BeforeEach
    void setUp() {
        placeSuggestionRepository = org.mockito.Mockito.mock(PlaceSuggestionRepository.class);
        memberRepository = org.mockito.Mockito.mock(MemberRepository.class);
        placeSuggestionService = new PlaceSuggestionService(placeSuggestionRepository, memberRepository);
    }

    @Test
    void createSuggestionStoresNormalizedPendingSuggestion() {
        Member member = member(100L);
        when(memberRepository.findById(100L)).thenReturn(Optional.of(member));
        when(placeSuggestionRepository.save(any(PlaceSuggestion.class))).thenAnswer(invocation -> {
            PlaceSuggestion suggestion = invocation.getArgument(0);
            ReflectionTestUtils.setField(suggestion, "placeSuggestionId", 10L);
            return suggestion;
        });

        PlaceSuggestionResponse response = placeSuggestionService.createSuggestion(
                100L,
                new PlaceSuggestionCreateRequest(
                        "  새별오름  ",
                        "  제주특별자치도 제주시 애월읍 봉성리  ",
                        "  노을을 보기 좋은 장소  "
                )
        );

        assertThat(response.placeSuggestionId()).isEqualTo(10L);
        assertThat(response.name()).isEqualTo("새별오름");
        assertThat(response.address()).isEqualTo("제주특별자치도 제주시 애월읍 봉성리");
        assertThat(response.description()).isEqualTo("노을을 보기 좋은 장소");
        assertThat(response.status()).isEqualTo(PlaceSuggestionStatus.PENDING);
        assertThat(response.createdAt()).isNotNull();
    }

    @Test
    void createSuggestionNormalizesBlankDescriptionToNull() {
        Member member = member(100L);
        when(memberRepository.findById(100L)).thenReturn(Optional.of(member));
        when(placeSuggestionRepository.save(any(PlaceSuggestion.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PlaceSuggestionResponse response = placeSuggestionService.createSuggestion(
                100L,
                new PlaceSuggestionCreateRequest("새별오름", "제주특별자치도 제주시", "  ")
        );

        assertThat(response.description()).isNull();
    }

    @Test
    void createSuggestionRejectsUnknownMember() {
        when(memberRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> placeSuggestionService.createSuggestion(
                100L,
                new PlaceSuggestionCreateRequest("새별오름", "제주특별자치도 제주시", null)
        ))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Member not found. memberId=100");
    }

    @Test
    void getMySuggestionsReturnsOnlyAuthenticatedMembersSuggestionsInRepositoryOrder() {
        PlaceSuggestion newerSuggestion = suggestion(member(100L), 20L, "새별오름");
        PlaceSuggestion olderSuggestion = suggestion(member(100L), 10L, "금오름");
        when(placeSuggestionRepository.findByMember_MemberIdOrderByPlaceSuggestionIdDesc(100L))
                .thenReturn(List.of(newerSuggestion, olderSuggestion));

        List<PlaceSuggestionResponse> responses = placeSuggestionService.getMySuggestions(100L);

        assertThat(responses).extracting(PlaceSuggestionResponse::placeSuggestionId)
                .containsExactly(20L, 10L);
        verify(placeSuggestionRepository).findByMember_MemberIdOrderByPlaceSuggestionIdDesc(100L);
    }

    private Member member(Long memberId) {
        Member member = Member.create("member@example.com", "여행자", "password-hash");
        ReflectionTestUtils.setField(member, "memberId", memberId);
        return member;
    }

    private PlaceSuggestion suggestion(Member member, Long suggestionId, String name) {
        PlaceSuggestion suggestion = PlaceSuggestion.create(
                member,
                name,
                "제주특별자치도 제주시",
                null
        );
        ReflectionTestUtils.setField(suggestion, "placeSuggestionId", suggestionId);
        return suggestion;
    }
}
