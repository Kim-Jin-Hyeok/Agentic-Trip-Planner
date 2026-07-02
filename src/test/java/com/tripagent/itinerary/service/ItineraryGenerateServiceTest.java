package com.tripagent.itinerary.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import com.tripagent.ai.llm.LlmClient;
import com.tripagent.ai.llm.LlmException;
import com.tripagent.ai.llm.LlmFailureType;
import com.tripagent.ai.llm.LlmItineraryResponseConverter;
import com.tripagent.ai.llm.dto.LlmItineraryItemResponse;
import com.tripagent.ai.llm.parser.LlmItineraryJsonParser;
import com.tripagent.ai.prompt.ItineraryPromptGenerator;
import com.tripagent.ai.validator.CandidatePlaceValidator;
import com.tripagent.itinerary.dto.ItineraryCreateRequest;
import com.tripagent.itinerary.dto.ItineraryDayTimeWindowRequest;
import com.tripagent.itinerary.dto.ItineraryGenerateRequest;
import com.tripagent.itinerary.dto.ItineraryPace;
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.itinerary.repository.ItineraryRepository;
import com.tripagent.place.dto.PlaceCategory;
import com.tripagent.place.dto.PlaceSummaryResponse;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.service.PlaceService;
import com.tripagent.route.RouteCalculationAdapter;
import com.tripagent.route.SimpleRouteCalculationAdapter;
import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.repository.TripRepository;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItineraryGenerateServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private PlaceService placeService;

    @Mock
    private ItineraryPromptGenerator itineraryPromptGenerator;

    @Mock
    private LlmClient llmClient;

    @Mock
    private LlmItineraryJsonParser llmItineraryJsonParser;

    @Mock
    private LlmItineraryResponseConverter llmItineraryResponseConverter;

    @Mock
    private CandidatePlaceValidator candidatePlaceValidator;

    @Mock
    private ItineraryService itineraryService;

    @Mock
    private ItineraryRepository itineraryRepository;

    @Spy
    private RouteCalculationAdapter routeCalculationAdapter = new SimpleRouteCalculationAdapter();

    @InjectMocks
    private ItineraryGenerateService itineraryGenerateService;

    @Test
    void generateDraftItinerariesCreatesDraftsFromCandidatePlaces() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90),
                place(30L, 120)
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        List<ItineraryCreateRequest> drafts = itineraryGenerateService.generateDraftItineraries(1L);

        assertThat(drafts).hasSize(2);
        assertThat(drafts).extracting(ItineraryCreateRequest::placeId)
                .containsExactly(10L, 20L);
        assertThat(drafts).extracting(ItineraryCreateRequest::dayNo)
                .containsExactly(1, 1);
        assertThat(drafts).extracting(ItineraryCreateRequest::orderNo)
                .containsExactly(1, 2);
        assertThat(drafts.get(0).startTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(drafts.get(0).endTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(drafts.get(1).startTime()).isEqualTo(LocalTime.of(10, 30));
        assertThat(drafts.get(1).endTime()).isEqualTo(LocalTime.of(12, 0));
        assertThat(drafts).extracting(ItineraryCreateRequest::travelMinutesFromPrevious)
                .containsExactly(0, 5);
        verify(placeService).findCandidatePlaces(TripConcept.FOOD);
        verify(itineraryPromptGenerator).generate(trip, candidatePlaces);
        verify(llmClient).generate(prompt);
        verify(llmItineraryJsonParser).parse(rawResponse);
        verify(llmItineraryResponseConverter).toCreateRequests(parsedItems);
        verify(candidatePlaceValidator).validatePlaceIds(candidatePlaces, List.of(10L, 20L));
    }

    @Test
    void generateDraftItinerariesPassesPaceRequestToPromptGenerator() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90),
                place(30L, 60),
                place(40L, 60),
                place(50L, 60)
        );
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(null, null, ItineraryPace.BUSY);
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(20L, 2, LocalTime.of(10, 30), LocalTime.of(11, 30), 30),
                llmItem(30L, 3, LocalTime.of(12, 0), LocalTime.of(13, 0), 30),
                llmItem(40L, 4, LocalTime.of(13, 30), LocalTime.of(14, 30), 30),
                llmItem(50L, 5, LocalTime.of(15, 0), LocalTime.of(16, 0), 30)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(11, 30), 5),
                request(30L, 3, LocalTime.of(12, 0), LocalTime.of(13, 0), 5),
                request(40L, 4, LocalTime.of(13, 30), LocalTime.of(14, 30), 5),
                request(50L, 5, LocalTime.of(15, 0), LocalTime.of(16, 0), 5)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces, request)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        List<ItineraryCreateRequest> drafts = itineraryGenerateService.generateDraftItineraries(1L, request);

        assertThat(drafts).hasSize(5);
        verify(itineraryPromptGenerator).generate(trip, candidatePlaces, request);
        verify(candidatePlaceValidator).validatePlaceIds(candidatePlaces, List.of(10L, 20L, 30L, 40L, 50L));
    }

    @Test
    void generateItinerariesSucceedsWhenRelaxedPaceHasThreeItemsPerDay() {
        assertGenerateItinerariesSucceedsWithPaceItemCount(ItineraryPace.RELAXED, 3);
    }

    @Test
    void generateItinerariesSavesFallbackWhenRelaxedPaceValidationFails() {
        assertGenerateItinerariesSavesFallbackWithPaceItemCount(
                ItineraryPace.RELAXED,
                5,
                3
        );
    }

    @Test
    void generateItinerariesSucceedsWhenNormalPaceHasFourItemsPerDay() {
        assertGenerateItinerariesSucceedsWithPaceItemCount(ItineraryPace.NORMAL, 4);
    }

    @Test
    void generateItinerariesSavesFallbackWhenNormalPaceValidationFails() {
        assertGenerateItinerariesSavesFallbackWithPaceItemCount(
                ItineraryPace.NORMAL,
                6,
                4
        );
    }

    @Test
    void generateItinerariesSucceedsWhenBusyPaceHasFiveItemsPerDay() {
        assertGenerateItinerariesSucceedsWithPaceItemCount(ItineraryPace.BUSY, 5);
    }

    @Test
    void generateItinerariesSavesFallbackWhenBusyPaceValidationFails() {
        assertGenerateItinerariesSavesFallbackWithPaceItemCount(
                ItineraryPace.BUSY,
                4,
                5
        );
    }

    @Test
    void generateItinerariesSavesFallbackWhenPaceRequestHasEmptyTripDay() {
        Trip trip = trip(
                1L,
                TripConcept.FOOD,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 2),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0)
        );
        List<PlaceResponse> candidatePlaces = places(8);
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(null, null, ItineraryPace.NORMAL);
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = llmItems(4);
        List<ItineraryCreateRequest> createRequests = createRequests(4);
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces, request)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L, request);

        assertThat(responses).hasSize(8);
        verify(llmClient, times(3)).generate(anyString());
        verify(itineraryService, times(8)).createItinerary(eq(1L), any());
    }

    @Test
    void generateItinerariesRetriesWhenPaceItemCountFailsAndThenSucceeds() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = places(6);
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(null, null, ItineraryPace.NORMAL);
        String prompt = "prompt";
        String firstRawResponse = "first raw response";
        String secondRawResponse = "second raw response";
        List<LlmItineraryItemResponse> firstParsedItems = llmItems(6);
        List<LlmItineraryItemResponse> secondParsedItems = llmItems(4);
        List<ItineraryCreateRequest> firstCreateRequests = createRequests(6);
        List<ItineraryCreateRequest> secondCreateRequests = createRequests(4);
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces, request)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(firstRawResponse, secondRawResponse);
        when(llmItineraryJsonParser.parse(firstRawResponse)).thenReturn(firstParsedItems);
        when(llmItineraryJsonParser.parse(secondRawResponse)).thenReturn(secondParsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(firstParsedItems)).thenReturn(firstCreateRequests);
        when(llmItineraryResponseConverter.toCreateRequests(secondParsedItems)).thenReturn(secondCreateRequests);
        stubSuccessfulItinerarySaves(4);

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L, request);

        assertThat(responses).hasSize(4);
        ArgumentCaptor<String> promptCaptor = ArgumentCaptor.forClass(String.class);
        verify(llmClient, times(2)).generate(promptCaptor.capture());
        assertThat(promptCaptor.getAllValues().get(0)).isEqualTo(prompt);
        assertThat(promptCaptor.getAllValues().get(1))
                .contains("Previous validation failure:")
                .contains("Generated itinerary item count per day does not match pace policy. pace=NORMAL, "
                        + "dayNo=1, itemCount=6, minItemsPerDay=4, maxItemsPerDay=5")
                .contains("Correct the itinerary so this validation failure is not repeated.")
                .contains("Return JSON only. Do not include markdown or explanation outside JSON.");
        verify(itineraryService, times(4)).createItinerary(eq(1L), any());
    }

    @Test
    void generateDraftItinerariesRejectsUnknownTrip() {
        when(tripRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itineraryGenerateService.generateDraftItineraries(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Trip not found. tripId=1");
    }

    @Test
    void generateDraftItinerariesRejectsNullTripId() {
        assertThatThrownBy(() -> itineraryGenerateService.generateDraftItineraries(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Trip id is required.");
    }

    @Test
    void generateDraftItinerariesRejectsMustVisitPlaceIdOutsideCandidatePlaces() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(place(10L, 60));
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(List.of(999L), null);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);

        assertThatThrownBy(() -> itineraryGenerateService.generateDraftItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("mustVisitPlaceIds must be included in candidate places. placeId=999");
        verify(llmClient, never()).generate("prompt");
    }

    @Test
    void generateDraftItinerariesRejectsExcludedPlaceIdOutsideCandidatePlaces() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(place(10L, 60));
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(null, List.of(999L));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);

        assertThatThrownBy(() -> itineraryGenerateService.generateDraftItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("excludedPlaceIds must be included in candidate places. placeId=999");
        verify(llmClient, never()).generate("prompt");
    }

    @Test
    void generateDraftItinerariesRejectsOverlappedMustVisitAndExcludedPlaceIds() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(place(10L, 60));
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(List.of(10L), List.of(10L));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);

        assertThatThrownBy(() -> itineraryGenerateService.generateDraftItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("mustVisitPlaceIds and excludedPlaceIds must not contain the same placeId. placeId=10");
        verify(llmClient, never()).generate("prompt");
    }

    @Test
    void regenerateItinerariesRejectsOverlappedMustVisitAndExcludedPlaceIdsBeforeDeletingExistingItinerary() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(place(10L, 60));
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(List.of(10L), List.of(10L));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);

        assertThatThrownBy(() -> itineraryGenerateService.regenerateItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("mustVisitPlaceIds and excludedPlaceIds must not contain the same placeId. placeId=10");
        verify(llmClient, never()).generate(anyString());
        verify(itineraryRepository, never()).deleteByTrip_TripId(1L);
    }

    @Test
    void generateDraftItinerariesRejectsDuplicatedMustVisitPlaceIds() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(place(10L, 60));
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(List.of(10L, 10L), null);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);

        assertThatThrownBy(() -> itineraryGenerateService.generateDraftItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("mustVisitPlaceIds must not contain duplicated placeId. placeId=10");
        verify(llmClient, never()).generate("prompt");
    }

    @Test
    void generateDraftItinerariesRejectsDuplicatedExcludedPlaceIds() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(place(10L, 60));
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(null, List.of(10L, 10L));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);

        assertThatThrownBy(() -> itineraryGenerateService.generateDraftItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("excludedPlaceIds must not contain duplicated placeId. placeId=10");
        verify(llmClient, never()).generate("prompt");
    }

    @Test
    void generateDraftItinerariesRejectsDuplicatedPreferredCategories() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(place(10L, 60));
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(
                null,
                null,
                null,
                List.of(PlaceCategory.FOOD, PlaceCategory.FOOD)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);

        assertThatThrownBy(() -> itineraryGenerateService.generateDraftItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("preferredCategories must not contain duplicated category. category=FOOD");
        verify(llmClient, never()).generate("prompt");
    }

    @Test
    void generateDraftItinerariesRejectsDayTimeWindowOutsideTripPeriodBeforeCallingLlm() {
        Trip trip = trip(
                1L,
                TripConcept.FOOD,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0)
        );
        List<PlaceResponse> candidatePlaces = List.of(place(10L, 60), place(20L, 60), place(30L, 60));
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(
                null,
                null,
                null,
                null,
                List.of(new ItineraryDayTimeWindowRequest(4, LocalTime.of(9, 0), LocalTime.of(18, 0)))
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);

        assertThatThrownBy(() -> itineraryGenerateService.generateDraftItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("dayTimeWindows.dayNo must be within trip period. dayNo=4, maxDayNo=3");
        verify(llmClient, never()).generate(anyString());
    }

    @Test
    void generateDraftItinerariesRejectsDuplicatedDayTimeWindowBeforeCallingLlm() {
        Trip trip = trip(
                1L,
                TripConcept.FOOD,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0)
        );
        List<PlaceResponse> candidatePlaces = List.of(place(10L, 60), place(20L, 60), place(30L, 60));
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(
                null,
                null,
                null,
                null,
                List.of(
                        new ItineraryDayTimeWindowRequest(2, LocalTime.of(10, 0), LocalTime.of(18, 0)),
                        new ItineraryDayTimeWindowRequest(2, LocalTime.of(11, 0), LocalTime.of(18, 0))
                )
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);

        assertThatThrownBy(() -> itineraryGenerateService.generateDraftItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("dayTimeWindows must not contain duplicated dayNo. dayNo=2");
        verify(llmClient, never()).generate(anyString());
    }

    @Test
    void generateDraftItinerariesRejectsInvalidDayTimeWindowTimeRangeBeforeCallingLlm() {
        Trip trip = trip(
                1L,
                TripConcept.FOOD,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0)
        );
        List<PlaceResponse> candidatePlaces = List.of(place(10L, 60), place(20L, 60), place(30L, 60));
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(
                null,
                null,
                null,
                null,
                List.of(new ItineraryDayTimeWindowRequest(1, LocalTime.of(18, 0), LocalTime.of(18, 0)))
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);

        assertThatThrownBy(() -> itineraryGenerateService.generateDraftItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("dayTimeWindows.startTime must be before endTime. dayNo=1");
        verify(llmClient, never()).generate(anyString());
    }

    @Test
    void generateDraftItinerariesRejectsTooFewCandidatePlacesBeforeLlmCall() {
        Trip trip = trip(
                1L,
                TripConcept.FOOD,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0)
        );
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);

        assertThatThrownBy(() -> itineraryGenerateService.generateDraftItineraries(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Candidate places are not enough to cover every trip day. requiredDays=3, candidateCount=2");
        verify(itineraryPromptGenerator, never()).generate(eq(trip), anyList());
        verify(llmClient, never()).generate(anyString());
    }

    @Test
    void regenerateItinerariesRejectsTooFewCandidatePlacesAfterExcludedBeforeDeletingExistingItinerary() {
        Trip trip = trip(
                1L,
                TripConcept.FOOD,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0)
        );
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90),
                place(30L, 120)
        );
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(null, List.of(30L));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);

        assertThatThrownBy(() -> itineraryGenerateService.regenerateItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Candidate places are not enough to cover every trip day. requiredDays=3, candidateCount=2");
        verify(itineraryPromptGenerator, never()).generate(eq(trip), anyList(), eq(request));
        verify(llmClient, never()).generate(anyString());
        verify(itineraryRepository, never()).deleteByTrip_TripId(1L);
    }

    @Test
    void generateDraftItinerariesRejectsTooFewCandidatePlacesForPaceBeforeLlmCall() {
        Trip trip = trip(
                1L,
                TripConcept.FOOD,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 2),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0)
        );
        List<PlaceResponse> candidatePlaces = places(9);
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(null, null, ItineraryPace.BUSY);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);

        assertThatThrownBy(() -> itineraryGenerateService.generateDraftItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Candidate places are not enough to satisfy pace policy. pace=BUSY, "
                                + "tripDays=2, requiredMinItems=10, candidateCount=9. "
                                + "Add more candidate places or choose a slower pace."
                );
        verify(itineraryPromptGenerator, never()).generate(eq(trip), anyList(), eq(request));
        verify(llmClient, never()).generate(anyString());
    }

    @Test
    void generateDraftItinerariesRejectsTooManyMustVisitPlacesForPaceBeforeLlmCall() {
        Trip trip = trip(
                1L,
                TripConcept.FOOD,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 2),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0)
        );
        List<PlaceResponse> candidatePlaces = places(9);
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(
                candidatePlaces.stream()
                        .map(PlaceResponse::placeId)
                        .toList(),
                null,
                ItineraryPace.RELAXED
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);

        assertThatThrownBy(() -> itineraryGenerateService.generateDraftItineraries(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "mustVisitPlaceIds are too many to satisfy pace policy. pace=RELAXED, "
                                + "tripDays=2, allowedMaxItems=8, mustVisitPlaceCount=9. "
                                + "Remove must-visit places or choose a busier pace."
                );
        verify(itineraryPromptGenerator, never()).generate(eq(trip), anyList(), eq(request));
        verify(llmClient, never()).generate(anyString());
    }

    @Test
    void generateDraftItinerariesAllowsMustVisitPlaceIdOutsidePreferredCategories() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, "NATURE", 60),
                place(20L, "FOOD", 90)
        );
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(
                List.of(10L),
                null,
                null,
                List.of(PlaceCategory.FOOD)
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces, request)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        List<ItineraryCreateRequest> drafts = itineraryGenerateService.generateDraftItineraries(1L, request);

        assertThat(drafts).extracting(ItineraryCreateRequest::placeId)
                .containsExactly(10L, 20L);
        verify(candidatePlaceValidator).validatePlaceIds(candidatePlaces, List.of(10L, 20L));
    }

    @Test
    void generateDraftItinerariesLimitsPromptCandidatePlacesToThirty() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = java.util.stream.IntStream.rangeClosed(1, 35)
                .mapToObj(placeId -> placeWithFoodScore((long) placeId, "NATURE", placeId))
                .toList();
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(35L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(35L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(eq(trip), anyList())).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        itineraryGenerateService.generateDraftItineraries(1L);

        ArgumentCaptor<List<PlaceResponse>> candidateCaptor = ArgumentCaptor.forClass(List.class);
        verify(itineraryPromptGenerator).generate(eq(trip), candidateCaptor.capture());
        assertThat(candidateCaptor.getValue()).hasSize(30);
        assertThat(candidateCaptor.getValue()).extracting(PlaceResponse::placeId)
                .contains(35L)
                .doesNotContain(1L, 2L, 3L, 4L, 5L);
    }

    @Test
    void generateDraftItinerariesIncludesMealAndRestCandidatesWhenTopThirtyWouldExcludeThem() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = new java.util.ArrayList<>(
                java.util.stream.IntStream.rangeClosed(1, 30)
                        .mapToObj(placeId -> placeWithFoodScore((long) placeId, "NATURE", 1000 - placeId))
                        .toList()
        );
        candidatePlaces.add(placeWithFoodScore(31L, "FOOD", 1));
        candidatePlaces.add(placeWithFoodScore(32L, "CAFE", 1));
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(1L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(1L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(eq(trip), anyList())).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        itineraryGenerateService.generateDraftItineraries(1L);

        ArgumentCaptor<List<PlaceResponse>> candidateCaptor = ArgumentCaptor.forClass(List.class);
        verify(itineraryPromptGenerator).generate(eq(trip), candidateCaptor.capture());
        assertThat(candidateCaptor.getValue()).hasSize(30);
        assertThat(candidateCaptor.getValue()).extracting(PlaceResponse::category)
                .contains("FOOD", "CAFE", "NATURE");
        assertThat(candidateCaptor.getValue()).extracting(PlaceResponse::placeId)
                .contains(31L, 32L)
                .doesNotContain(29L, 30L);
    }

    @Test
    void generateDraftItinerariesIncludesRegionBalanceCandidatesWhenTopThirtyUsesOneRegion() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = new java.util.ArrayList<>(
                java.util.stream.IntStream.rangeClosed(1, 30)
                        .mapToObj(placeId -> placeWithFoodScoreAndRegion((long) placeId, "NATURE", 1000 - placeId, "EAST"))
                        .toList()
        );
        candidatePlaces.add(placeWithFoodScoreAndRegion(31L, "NATURE", 1, "WEST"));
        candidatePlaces.add(placeWithFoodScoreAndRegion(32L, "NATURE", 1, "NORTH"));
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(1L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(1L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(eq(trip), anyList())).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        itineraryGenerateService.generateDraftItineraries(1L);

        ArgumentCaptor<List<PlaceResponse>> candidateCaptor = ArgumentCaptor.forClass(List.class);
        verify(itineraryPromptGenerator).generate(eq(trip), candidateCaptor.capture());
        assertThat(candidateCaptor.getValue()).hasSize(30);
        assertThat(candidateCaptor.getValue()).extracting(PlaceResponse::region)
                .contains("EAST", "WEST", "NORTH");
        assertThat(candidateCaptor.getValue()).extracting(PlaceResponse::placeId)
                .contains(31L, 32L)
                .doesNotContain(29L, 30L);
    }

    @Test
    void generateDraftItinerariesIncludesMustVisitPlaceOutsideTopThirty() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = java.util.stream.IntStream.rangeClosed(1, 31)
                .mapToObj(placeId -> placeWithFoodScore((long) placeId, "NATURE", 100 - placeId))
                .toList();
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(List.of(31L), null);
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(31L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(31L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(eq(trip), anyList(), eq(request))).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        itineraryGenerateService.generateDraftItineraries(1L, request);

        ArgumentCaptor<List<PlaceResponse>> candidateCaptor = ArgumentCaptor.forClass(List.class);
        verify(itineraryPromptGenerator).generate(eq(trip), candidateCaptor.capture(), eq(request));
        assertThat(candidateCaptor.getValue()).hasSize(30);
        assertThat(candidateCaptor.getValue()).extracting(PlaceResponse::placeId)
                .contains(31L)
                .doesNotContain(30L);
    }

    @Test
    void generateDraftItinerariesRemovesExcludedPlaceIdsBeforeCandidateSelection() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = java.util.stream.IntStream.rangeClosed(1, 35)
                .mapToObj(placeId -> placeWithFoodScore((long) placeId, "NATURE", placeId))
                .toList();
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(null, List.of(35L));
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(34L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(34L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(eq(trip), anyList(), eq(request))).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        itineraryGenerateService.generateDraftItineraries(1L, request);

        ArgumentCaptor<List<PlaceResponse>> candidateCaptor = ArgumentCaptor.forClass(List.class);
        verify(itineraryPromptGenerator).generate(eq(trip), candidateCaptor.capture(), eq(request));
        assertThat(candidateCaptor.getValue()).hasSize(30);
        assertThat(candidateCaptor.getValue()).extracting(PlaceResponse::placeId)
                .doesNotContain(35L);
    }

    @Test
    void generateDraftItinerariesBoostsPreferredCategoriesBeforeConceptScore() {
        Trip trip = trip(1L, TripConcept.FOOD);
        PlaceResponse highScoreNature = placeWithFoodScore(10L, "NATURE", 100);
        PlaceResponse lowScoreFood = placeWithFoodScore(20L, "FOOD", 1);
        List<PlaceResponse> candidatePlaces = List.of(highScoreNature, lowScoreFood);
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(
                null,
                null,
                null,
                List.of(PlaceCategory.FOOD)
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(20L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(20L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(eq(trip), anyList(), eq(request))).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        itineraryGenerateService.generateDraftItineraries(1L, request);

        ArgumentCaptor<List<PlaceResponse>> candidateCaptor = ArgumentCaptor.forClass(List.class);
        verify(itineraryPromptGenerator).generate(eq(trip), candidateCaptor.capture(), eq(request));
        assertThat(candidateCaptor.getValue()).extracting(PlaceResponse::placeId)
                .containsExactly(20L, 10L);
    }

    @Test
    void generateDraftItinerariesSortsCandidatesByConceptScoreAndPlaceId() {
        Trip trip = trip(1L, TripConcept.FOOD);
        PlaceResponse lowScore = placeWithFoodScore(10L, "NATURE", 1);
        PlaceResponse highScoreHighId = placeWithFoodScore(30L, "NATURE", 100);
        PlaceResponse highScoreLowId = placeWithFoodScore(20L, "NATURE", 100);
        List<PlaceResponse> candidatePlaces = List.of(lowScore, highScoreHighId, highScoreLowId);
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(20L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(20L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(eq(trip), anyList())).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        itineraryGenerateService.generateDraftItineraries(1L);

        ArgumentCaptor<List<PlaceResponse>> candidateCaptor = ArgumentCaptor.forClass(List.class);
        verify(itineraryPromptGenerator).generate(eq(trip), candidateCaptor.capture());
        assertThat(candidateCaptor.getValue()).extracting(PlaceResponse::placeId)
                .containsExactly(20L, 30L, 10L);
    }

    @Test
    void generateDraftItinerariesExcludesPlaceIdsRegardlessOfPreferredCategories() {
        Trip trip = trip(1L, TripConcept.FOOD);
        PlaceResponse firstPlace = place(10L, "FOOD", 60);
        PlaceResponse excludedPlace = place(20L, "FOOD", 90);
        List<PlaceResponse> candidatePlaces = List.of(firstPlace, excludedPlace);
        List<PlaceResponse> filteredCandidatePlaces = List.of(firstPlace);
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(
                null,
                List.of(20L),
                null,
                List.of(PlaceCategory.FOOD)
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, filteredCandidatePlaces, request)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        List<ItineraryCreateRequest> drafts = itineraryGenerateService.generateDraftItineraries(1L, request);

        assertThat(drafts).extracting(ItineraryCreateRequest::placeId)
                .containsExactly(10L);
        verify(itineraryPromptGenerator).generate(trip, filteredCandidatePlaces, request);
        verify(candidatePlaceValidator).validatePlaceIds(filteredCandidatePlaces, List.of(10L));
    }

    @Test
    void generateItinerariesSavesDraftsAndReturnsResponses() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)
        );
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);
        when(itineraryService.createItinerary(1L, request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)))
                .thenReturn(response(100L, 1L, 10L, 1));
        when(itineraryService.createItinerary(1L, request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)))
                .thenReturn(response(200L, 1L, 20L, 2));

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L);

        assertThat(responses).extracting(ItineraryResponse::itineraryId)
                .containsExactly(100L, 200L);
        assertThat(responses).extracting(ItineraryResponse::placeId)
                .containsExactly(10L, 20L);
        verify(candidatePlaceValidator).validatePlaceIds(candidatePlaces, List.of(10L, 20L));
        verify(itineraryService).createItinerary(
                1L,
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        verify(itineraryService).createItinerary(
                1L,
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)
        );
    }

    @Test
    void generateItinerariesSavesWithDayTimeWindowWhenOverrideStartsBeforeTripDailyStartTime() {
        Trip trip = trip(
                1L,
                TripConcept.FOOD,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(11, 0),
                LocalTime.of(18, 0)
        );
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 60),
                place(30L, 60)
        );
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(
                null,
                null,
                null,
                null,
                List.of(new ItineraryDayTimeWindowRequest(2, LocalTime.of(10, 0), LocalTime.of(18, 0)))
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(11, 0), LocalTime.of(12, 0), 0),
                llmItem(20L, 2, LocalTime.of(10, 0), LocalTime.of(11, 0), 0),
                llmItem(30L, 3, LocalTime.of(11, 0), LocalTime.of(12, 0), 0)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, 1, LocalTime.of(11, 0), LocalTime.of(12, 0), 0),
                request(20L, 2, 1, LocalTime.of(10, 0), LocalTime.of(11, 0), 0),
                request(30L, 3, 1, LocalTime.of(11, 0), LocalTime.of(12, 0), 0)
        );
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces, request)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);
        when(itineraryService.createGeneratedItinerary(
                1L,
                request(10L, 1, 1, LocalTime.of(11, 0), LocalTime.of(12, 0), 0),
                LocalTime.of(11, 0),
                LocalTime.of(18, 0)
        )).thenReturn(response(100L, 1L, 10L, 1));
        when(itineraryService.createGeneratedItinerary(
                1L,
                request(20L, 2, 1, LocalTime.of(10, 0), LocalTime.of(11, 0), 0),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0)
        )).thenReturn(response(200L, 1L, 20L, 2));
        when(itineraryService.createGeneratedItinerary(
                1L,
                request(30L, 3, 1, LocalTime.of(11, 0), LocalTime.of(12, 0), 0),
                LocalTime.of(11, 0),
                LocalTime.of(18, 0)
        )).thenReturn(response(300L, 1L, 30L, 3));

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L, request);

        assertThat(responses).extracting(ItineraryResponse::itineraryId)
                .containsExactly(100L, 200L, 300L);
        verify(itineraryService).createGeneratedItinerary(
                1L,
                request(20L, 2, 1, LocalTime.of(10, 0), LocalTime.of(11, 0), 0),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0)
        );
        verify(itineraryService, never()).createItinerary(eq(1L), any());
    }

    @Test
    void regenerateItinerariesAllowsExistingItineraryAndReplacesItWithNewItinerary() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);
        when(itineraryService.createItinerary(1L, request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)))
                .thenReturn(response(100L, 1L, 10L, 1));
        when(itineraryService.createItinerary(1L, request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)))
                .thenReturn(response(200L, 1L, 20L, 2));

        List<ItineraryResponse> responses = itineraryGenerateService.regenerateItineraries(1L);

        assertThat(responses).extracting(ItineraryResponse::itineraryId)
                .containsExactly(100L, 200L);
        assertThat(responses).extracting(ItineraryResponse::placeId)
                .containsExactly(10L, 20L);
        verify(itineraryRepository, never()).existsByTrip_TripId(1L);
        verify(candidatePlaceValidator).validatePlaceIds(candidatePlaces, List.of(10L, 20L));
        InOrder inOrder = inOrder(itineraryRepository, itineraryService);
        inOrder.verify(itineraryRepository).deleteByTrip_TripId(1L);
        inOrder.verify(itineraryService).createItinerary(
                1L,
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        inOrder.verify(itineraryService).createItinerary(
                1L,
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)
        );
    }

    @Test
    void regenerateItinerariesSavesWithDayTimeWindowAfterValidationAndDelete() {
        Trip trip = trip(
                1L,
                TripConcept.FOOD,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(11, 0),
                LocalTime.of(18, 0)
        );
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 60),
                place(30L, 60)
        );
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(
                null,
                null,
                null,
                null,
                List.of(new ItineraryDayTimeWindowRequest(2, LocalTime.of(10, 0), LocalTime.of(18, 0)))
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(11, 0), LocalTime.of(12, 0), 0),
                llmItem(20L, 2, LocalTime.of(10, 0), LocalTime.of(11, 0), 0),
                llmItem(30L, 3, LocalTime.of(11, 0), LocalTime.of(12, 0), 0)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, 1, LocalTime.of(11, 0), LocalTime.of(12, 0), 0),
                request(20L, 2, 1, LocalTime.of(10, 0), LocalTime.of(11, 0), 0),
                request(30L, 3, 1, LocalTime.of(11, 0), LocalTime.of(12, 0), 0)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces, request)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);
        when(itineraryService.createGeneratedItinerary(
                1L,
                request(10L, 1, 1, LocalTime.of(11, 0), LocalTime.of(12, 0), 0),
                LocalTime.of(11, 0),
                LocalTime.of(18, 0)
        )).thenReturn(response(100L, 1L, 10L, 1));
        when(itineraryService.createGeneratedItinerary(
                1L,
                request(20L, 2, 1, LocalTime.of(10, 0), LocalTime.of(11, 0), 0),
                LocalTime.of(10, 0),
                LocalTime.of(18, 0)
        )).thenReturn(response(200L, 1L, 20L, 2));
        when(itineraryService.createGeneratedItinerary(
                1L,
                request(30L, 3, 1, LocalTime.of(11, 0), LocalTime.of(12, 0), 0),
                LocalTime.of(11, 0),
                LocalTime.of(18, 0)
        )).thenReturn(response(300L, 1L, 30L, 3));

        List<ItineraryResponse> responses = itineraryGenerateService.regenerateItineraries(1L, request);

        assertThat(responses).extracting(ItineraryResponse::itineraryId)
                .containsExactly(100L, 200L, 300L);
        InOrder inOrder = inOrder(itineraryRepository, itineraryService);
        inOrder.verify(itineraryRepository).deleteByTrip_TripId(1L);
        inOrder.verify(itineraryService).createGeneratedItinerary(
                1L,
                request(10L, 1, 1, LocalTime.of(11, 0), LocalTime.of(12, 0), 0),
                LocalTime.of(11, 0),
                LocalTime.of(18, 0)
        );
        verify(itineraryService, never()).createItinerary(eq(1L), any());
    }

    @Test
    void generateItinerariesSucceedsWhenMustVisitPlaceIdsAreIncluded() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        List<PlaceResponse> selectedCandidatePlaces = List.of(
                candidatePlaces.get(1),
                candidatePlaces.get(0)
        );
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(List.of(20L), null);
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)
        );
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, selectedCandidatePlaces, request)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);
        when(itineraryService.createItinerary(1L, request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)))
                .thenReturn(response(100L, 1L, 10L, 1));
        when(itineraryService.createItinerary(1L, request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)))
                .thenReturn(response(200L, 1L, 20L, 2));

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L, request);

        assertThat(responses).extracting(ItineraryResponse::placeId)
                .containsExactly(10L, 20L);
        verify(candidatePlaceValidator).validatePlaceIds(selectedCandidatePlaces, List.of(10L, 20L));
    }

    @Test
    void generateItinerariesRetriesWhenMustVisitPlaceIdIsMissingAndThenSucceeds() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        List<PlaceResponse> selectedCandidatePlaces = List.of(
                candidatePlaces.get(1),
                candidatePlaces.get(0)
        );
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(List.of(20L), null);
        String prompt = "prompt";
        String firstRawResponse = "first raw response";
        String secondRawResponse = "second raw response";
        List<LlmItineraryItemResponse> firstParsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        List<LlmItineraryItemResponse> secondParsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        List<ItineraryCreateRequest> firstCreateRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        List<ItineraryCreateRequest> secondCreateRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)
        );
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, selectedCandidatePlaces, request)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(firstRawResponse, secondRawResponse);
        when(llmItineraryJsonParser.parse(firstRawResponse)).thenReturn(firstParsedItems);
        when(llmItineraryJsonParser.parse(secondRawResponse)).thenReturn(secondParsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(firstParsedItems)).thenReturn(firstCreateRequests);
        when(llmItineraryResponseConverter.toCreateRequests(secondParsedItems)).thenReturn(secondCreateRequests);
        when(itineraryService.createItinerary(1L, request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)))
                .thenReturn(response(100L, 1L, 10L, 1));
        when(itineraryService.createItinerary(1L, request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)))
                .thenReturn(response(200L, 1L, 20L, 2));

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L, request);

        assertThat(responses).extracting(ItineraryResponse::placeId)
                .containsExactly(10L, 20L);
        verify(llmClient, times(2)).generate(anyString());
        verify(candidatePlaceValidator).validatePlaceIds(selectedCandidatePlaces, List.of(10L, 20L));
    }

    @Test
    void generateItinerariesRetriesWhenExcludedPlaceIdIsIncludedAndThenSucceeds() {
        Trip trip = trip(1L, TripConcept.FOOD);
        PlaceResponse firstPlace = place(10L, 60);
        PlaceResponse secondPlace = place(20L, 90);
        PlaceResponse excludedPlace = place(30L, 120);
        List<PlaceResponse> candidatePlaces = List.of(firstPlace, secondPlace, excludedPlace);
        List<PlaceResponse> filteredCandidatePlaces = List.of(firstPlace, secondPlace);
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(null, List.of(30L));
        String prompt = "prompt";
        String firstRawResponse = "first raw response";
        String secondRawResponse = "second raw response";
        List<LlmItineraryItemResponse> firstParsedItems = List.of(
                llmItem(30L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        List<LlmItineraryItemResponse> secondParsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        List<ItineraryCreateRequest> firstCreateRequests = List.of(
                request(30L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        List<ItineraryCreateRequest> secondCreateRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)
        );
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, filteredCandidatePlaces, request)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(firstRawResponse, secondRawResponse);
        when(llmItineraryJsonParser.parse(firstRawResponse)).thenReturn(firstParsedItems);
        when(llmItineraryJsonParser.parse(secondRawResponse)).thenReturn(secondParsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(firstParsedItems)).thenReturn(firstCreateRequests);
        when(llmItineraryResponseConverter.toCreateRequests(secondParsedItems)).thenReturn(secondCreateRequests);
        when(itineraryService.createItinerary(1L, request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)))
                .thenReturn(response(100L, 1L, 10L, 1));
        when(itineraryService.createItinerary(1L, request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)))
                .thenReturn(response(200L, 1L, 20L, 2));

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L, request);

        assertThat(responses).extracting(ItineraryResponse::placeId)
                .containsExactly(10L, 20L);
        verify(llmClient, times(2)).generate(anyString());
        verify(candidatePlaceValidator).validatePlaceIds(filteredCandidatePlaces, List.of(10L, 20L));
    }

    @Test
    void generateItinerariesRetriesWhenFirstLlmResponseHasDuplicatedPlaceIdAndThenSucceeds() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        String prompt = "prompt";
        String firstRawResponse = "first raw response";
        String secondRawResponse = "second raw response";
        List<LlmItineraryItemResponse> firstParsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(10L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        List<LlmItineraryItemResponse> secondParsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        List<ItineraryCreateRequest> firstCreateRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(10L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        List<ItineraryCreateRequest> secondCreateRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)
        );
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(firstRawResponse, secondRawResponse);
        when(llmItineraryJsonParser.parse(firstRawResponse)).thenReturn(firstParsedItems);
        when(llmItineraryJsonParser.parse(secondRawResponse)).thenReturn(secondParsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(firstParsedItems)).thenReturn(firstCreateRequests);
        when(llmItineraryResponseConverter.toCreateRequests(secondParsedItems)).thenReturn(secondCreateRequests);
        doThrow(new IllegalArgumentException("Place id must not be duplicated in generated itinerary. placeId=10"))
                .doNothing()
                .when(candidatePlaceValidator)
                .validatePlaceIds(candidatePlaces, List.of(10L, 10L));
        doNothing().when(candidatePlaceValidator).validatePlaceIds(candidatePlaces, List.of(10L, 20L));
        when(itineraryService.createItinerary(1L, request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)))
                .thenReturn(response(100L, 1L, 10L, 1));
        when(itineraryService.createItinerary(1L, request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)))
                .thenReturn(response(200L, 1L, 20L, 2));

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L);

        assertThat(responses).extracting(ItineraryResponse::placeId)
                .containsExactly(10L, 20L);
        verify(llmClient, times(2)).generate(anyString());
        verify(candidatePlaceValidator).validatePlaceIds(candidatePlaces, List.of(10L, 10L));
        verify(candidatePlaceValidator).validatePlaceIds(candidatePlaces, List.of(10L, 20L));
        verify(itineraryService).createItinerary(
                1L,
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        verify(itineraryService).createItinerary(
                1L,
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)
        );
    }

    @Test
    void generateItinerariesSavesFallbackWhenAllRetriesFailValidation() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(10L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(10L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);
        doThrow(new IllegalArgumentException("Place id must not be duplicated in generated itinerary. placeId=10"))
                .when(candidatePlaceValidator)
                .validatePlaceIds(candidatePlaces, List.of(10L, 10L));

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L);

        assertThat(responses).hasSize(1);
        verify(llmClient, times(3)).generate(anyString());
        verify(candidatePlaceValidator, times(3)).validatePlaceIds(candidatePlaces, List.of(10L, 10L));
        verify(itineraryService).createItinerary(eq(1L), any());
    }

    @Test
    void generateItinerariesUsesCalculatedTravelMinutesBeforeSaving() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 15),
                llmItem(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 99)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 15),
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 99)
        );
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);
        when(itineraryService.createItinerary(1L, request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)))
                .thenReturn(response(100L, 1L, 10L, 1));
        when(itineraryService.createItinerary(1L, request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)))
                .thenReturn(response(200L, 1L, 20L, 2));

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L);

        assertThat(responses).extracting(ItineraryResponse::placeId)
                .containsExactly(10L, 20L);
        verify(candidatePlaceValidator).validatePlaceIds(candidatePlaces, List.of(10L, 20L));
        verify(routeCalculationAdapter).calculateTravelMinutes(null, candidatePlaces.get(0));
        verify(routeCalculationAdapter).calculateTravelMinutes(candidatePlaces.get(0), candidatePlaces.get(1));
        verify(itineraryService).createItinerary(
                1L,
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        verify(itineraryService).createItinerary(
                1L,
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)
        );
    }

    @Test
    void generateItinerariesSavesWhenCalculatedTravelMinutesIsAtMaximum() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(20L, 2, LocalTime.of(11, 30), LocalTime.of(12, 30), 30)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(20L, 2, LocalTime.of(11, 30), LocalTime.of(12, 30), 30)
        );
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);
        doReturn(0).when(routeCalculationAdapter).calculateTravelMinutes(null, candidatePlaces.get(0));
        doReturn(90).when(routeCalculationAdapter).calculateTravelMinutes(candidatePlaces.get(0), candidatePlaces.get(1));
        when(itineraryService.createItinerary(1L, request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)))
                .thenReturn(response(100L, 1L, 10L, 1));
        when(itineraryService.createItinerary(1L, request(20L, 2, LocalTime.of(11, 30), LocalTime.of(12, 30), 90)))
                .thenReturn(response(200L, 1L, 20L, 2));

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L);

        assertThat(responses).extracting(ItineraryResponse::placeId)
                .containsExactly(10L, 20L);
        verify(itineraryService).createItinerary(
                1L,
                request(20L, 2, LocalTime.of(11, 30), LocalTime.of(12, 30), 90)
        );
    }

    @Test
    void generateItinerariesSavesFallbackWhenCalculatedTravelMinutesExceedsMaximum() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(20L, 2, LocalTime.of(11, 40), LocalTime.of(12, 40), 30)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(20L, 2, LocalTime.of(11, 40), LocalTime.of(12, 40), 30)
        );
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);
        doReturn(0).when(routeCalculationAdapter).calculateTravelMinutes(null, candidatePlaces.get(0));
        doReturn(91).when(routeCalculationAdapter).calculateTravelMinutes(candidatePlaces.get(0), candidatePlaces.get(1));

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L);

        assertThat(responses).hasSize(1);
        verify(llmClient, times(3)).generate(anyString());
        verify(itineraryService).createItinerary(eq(1L), any());
    }

    @Test
    void generateItinerariesRetriesWhenTravelMinutesExceedsMaximumAndThenSucceeds() {
        Trip trip = trip(1L, TripConcept.FOOD);
        PlaceResponse startPlace = place(10L, 60);
        PlaceResponse farPlace = place(20L, 90, 33.305833, 126.289444);
        PlaceResponse nearPlace = place(30L, 90);
        List<PlaceResponse> candidatePlaces = List.of(startPlace, farPlace, nearPlace);
        String prompt = "prompt";
        String firstRawResponse = "first raw response";
        String secondRawResponse = "second raw response";
        List<LlmItineraryItemResponse> firstParsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(20L, 2, LocalTime.of(12, 40), LocalTime.of(13, 40), 30)
        );
        List<LlmItineraryItemResponse> secondParsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(30L, 2, LocalTime.of(10, 30), LocalTime.of(11, 30), 30)
        );
        List<ItineraryCreateRequest> firstCreateRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(20L, 2, LocalTime.of(12, 40), LocalTime.of(13, 40), 30)
        );
        List<ItineraryCreateRequest> secondCreateRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(30L, 2, LocalTime.of(10, 30), LocalTime.of(11, 30), 30)
        );
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(firstRawResponse, secondRawResponse);
        when(llmItineraryJsonParser.parse(firstRawResponse)).thenReturn(firstParsedItems);
        when(llmItineraryJsonParser.parse(secondRawResponse)).thenReturn(secondParsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(firstParsedItems)).thenReturn(firstCreateRequests);
        when(llmItineraryResponseConverter.toCreateRequests(secondParsedItems)).thenReturn(secondCreateRequests);
        when(itineraryService.createItinerary(1L, request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)))
                .thenReturn(response(100L, 1L, 10L, 1));
        when(itineraryService.createItinerary(1L, request(30L, 2, LocalTime.of(10, 30), LocalTime.of(11, 30), 5)))
                .thenReturn(response(300L, 1L, 30L, 2));

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L);

        assertThat(responses).extracting(ItineraryResponse::placeId)
                .containsExactly(10L, 30L);
        verify(llmClient, times(2)).generate(anyString());
        verify(itineraryService).createItinerary(
                1L,
                request(30L, 2, LocalTime.of(10, 30), LocalTime.of(11, 30), 5)
        );
    }

    @Test
    void generateItinerariesSavesFallbackWhenFirstStartTimeBeforeDailyStartTime() {
        Trip trip = trip(1L, TripConcept.FOOD, LocalTime.of(10, 30));
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)
        );
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L);

        assertThat(responses).hasSize(1);
        verify(candidatePlaceValidator, times(3)).validatePlaceIds(candidatePlaces, List.of(10L, 20L));
        verify(itineraryService).createItinerary(eq(1L), any());
    }

    @Test
    void generateItinerariesSavesFallbackWhenEndTimeAfterDailyEndTime() {
        Trip trip = trip(1L, TripConcept.FOOD, LocalTime.of(9, 0), LocalTime.of(10, 30));
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)
        );
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L);

        assertThat(responses).hasSize(1);
        verify(llmClient, times(3)).generate(anyString());
        verify(candidatePlaceValidator, times(3)).validatePlaceIds(candidatePlaces, List.of(10L, 20L));
        verify(itineraryService).createItinerary(eq(1L), any());
    }

    @Test
    void generateItinerariesSavesFallbackWhenStartTimeBeforeDayTimeWindow() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90),
                place(30L, 60)
        );
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(
                null,
                null,
                null,
                null,
                List.of(new ItineraryDayTimeWindowRequest(1, LocalTime.of(14, 0), LocalTime.of(18, 0)))
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(13, 0), LocalTime.of(14, 0), 0),
                llmItem(20L, 2, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(30L, 3, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, LocalTime.of(13, 0), LocalTime.of(14, 0), 0),
                request(20L, 2, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(30L, 3, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces, request)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L, request);

        assertThat(responses).hasSize(1);
        verify(llmClient, times(3)).generate(anyString());
        verify(itineraryService).createGeneratedItinerary(
                eq(1L),
                any(),
                eq(LocalTime.of(14, 0)),
                eq(LocalTime.of(18, 0))
        );
    }

    @Test
    void generateItinerariesSavesFallbackWhenTripDayIsMissing() {
        Trip trip = trip(
                1L,
                TripConcept.FOOD,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0)
        );
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90),
                place(30L, 120)
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(20L, 3, 1, LocalTime.of(9, 0), LocalTime.of(10, 30), 0)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(20L, 3, 1, LocalTime.of(9, 0), LocalTime.of(10, 30), 0)
        );
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L);

        assertThat(responses).hasSize(3);
        verify(candidatePlaceValidator, times(3)).validatePlaceIds(candidatePlaces, List.of(10L, 20L));
        verify(itineraryService, times(3)).createItinerary(eq(1L), any());
    }

    @Test
    void generateDraftItinerariesAllowsEveryTripDayCovered() {
        Trip trip = trip(
                1L,
                TripConcept.FOOD,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0)
        );
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90),
                place(30L, 120)
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(20L, 2, 1, LocalTime.of(9, 0), LocalTime.of(10, 30), 0),
                llmItem(30L, 3, 1, LocalTime.of(9, 0), LocalTime.of(11, 0), 0)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(20L, 2, 1, LocalTime.of(9, 0), LocalTime.of(10, 30), 0),
                request(30L, 3, 1, LocalTime.of(9, 0), LocalTime.of(11, 0), 0)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        List<ItineraryCreateRequest> drafts = itineraryGenerateService.generateDraftItineraries(1L);

        assertThat(drafts).extracting(ItineraryCreateRequest::dayNo)
                .containsExactly(1, 2, 3);
        verify(candidatePlaceValidator).validatePlaceIds(candidatePlaces, List.of(10L, 20L, 30L));
    }

    @Test
    void generateDraftItinerariesAllowsFirstStartTimeAtOrAfterDailyStartTime() {
        Trip trip = trip(1L, TripConcept.FOOD, LocalTime.of(10, 30));
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(10, 30), LocalTime.of(11, 30), 0),
                llmItem(20L, 2, LocalTime.of(12, 0), LocalTime.of(13, 0), 30)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, LocalTime.of(10, 30), LocalTime.of(11, 30), 0),
                request(20L, 2, LocalTime.of(12, 0), LocalTime.of(13, 0), 30)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        List<ItineraryCreateRequest> drafts = itineraryGenerateService.generateDraftItineraries(1L);

        assertThat(drafts).hasSize(2);
        assertThat(drafts.get(0).startTime()).isEqualTo(LocalTime.of(10, 30));
        verify(candidatePlaceValidator).validatePlaceIds(candidatePlaces, List.of(10L, 20L));
    }

    @Test
    void generateItinerariesSavesFallbackWhenLlmFails() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        String prompt = "prompt";
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenThrow(
                LlmException.of(LlmFailureType.INSUFFICIENT_QUOTA, "OpenAI quota exceeded.")
        );
        when(itineraryService.createItinerary(
                1L,
                fallbackRequest(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        )).thenReturn(response(100L, 1L, 10L, 1));

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L);

        assertThat(responses).extracting(ItineraryResponse::placeId)
                .containsExactly(10L);
        verify(llmClient).generate(prompt);
        verify(llmItineraryJsonParser, never()).parse("raw response");
        verify(llmItineraryResponseConverter, never()).toCreateRequests(List.of());
        verify(candidatePlaceValidator).validatePlaceIds(candidatePlaces, List.of(10L));
        verify(itineraryService).createItinerary(
                1L,
                fallbackRequest(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
    }

    @Test
    void regenerateItinerariesDeletesExistingItineraryAfterFallbackWhenLlmFails() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        String prompt = "prompt";
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenThrow(
                LlmException.of(LlmFailureType.INSUFFICIENT_QUOTA, "OpenAI quota exceeded.")
        );
        when(itineraryService.createItinerary(
                1L,
                fallbackRequest(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        )).thenReturn(response(100L, 1L, 10L, 1));

        List<ItineraryResponse> responses = itineraryGenerateService.regenerateItineraries(1L);

        assertThat(responses).extracting(ItineraryResponse::placeId)
                .containsExactly(10L);
        verify(llmClient).generate(prompt);
        InOrder inOrder = inOrder(itineraryRepository, itineraryService);
        inOrder.verify(itineraryRepository).deleteByTrip_TripId(1L);
        inOrder.verify(itineraryService).createItinerary(
                1L,
                fallbackRequest(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
    }

    @Test
    void regenerateItinerariesDeletesExistingItineraryAfterFallbackWhenDraftValidationFails() {
        Trip trip = trip(1L, TripConcept.FOOD, LocalTime.of(10, 30));
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                llmItem(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0),
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 5)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        List<ItineraryResponse> responses = itineraryGenerateService.regenerateItineraries(1L);

        assertThat(responses).hasSize(1);
        verify(candidatePlaceValidator, times(3)).validatePlaceIds(candidatePlaces, List.of(10L, 20L));
        InOrder inOrder = inOrder(itineraryRepository, itineraryService);
        inOrder.verify(itineraryRepository).deleteByTrip_TripId(1L);
        inOrder.verify(itineraryService).createItinerary(eq(1L), any());
    }

    @Test
    void regenerateItinerariesDeletesExistingItineraryAfterFallbackWhenMustVisitMissingAfterRetries() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        List<PlaceResponse> selectedCandidatePlaces = List.of(
                candidatePlaces.get(1),
                candidatePlaces.get(0)
        );
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(List.of(20L), null);
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, selectedCandidatePlaces, request)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        List<ItineraryResponse> responses = itineraryGenerateService.regenerateItineraries(1L, request);

        assertThat(responses).hasSize(1);
        verify(llmClient, times(3)).generate(anyString());
        InOrder inOrder = inOrder(itineraryRepository, itineraryService);
        inOrder.verify(itineraryRepository).deleteByTrip_TripId(1L);
        inOrder.verify(itineraryService).createItinerary(eq(1L), any());
    }

    @Test
    void generateItinerariesRejectsTripThatAlreadyHasItinerary() {
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(true);

        assertThatThrownBy(() -> itineraryGenerateService.generateItineraries(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Itinerary already exists for this trip.");
        verify(tripRepository, never()).findById(1L);
        verify(placeService, never()).findCandidatePlaces(TripConcept.FOOD);
        verify(itineraryService, never()).createItinerary(
                1L,
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
    }

    private Trip trip(Long tripId, TripConcept concept) {
        return trip(tripId, concept, LocalTime.of(9, 0));
    }

    private void assertGenerateItinerariesSucceedsWithPaceItemCount(ItineraryPace pace, int itemCount) {
        Trip trip = trip(1L, TripConcept.FOOD);
        int candidateCount = switch (pace) {
            case RELAXED -> Math.max(itemCount, 3);
            case NORMAL -> Math.max(itemCount, 4);
            case BUSY -> Math.max(itemCount, 5);
        };
        List<PlaceResponse> candidatePlaces = places(candidateCount);
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(null, null, pace);
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = llmItems(itemCount);
        List<ItineraryCreateRequest> createRequests = createRequests(itemCount);
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces, request)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);
        stubSuccessfulItinerarySaves(itemCount);

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L, request);

        assertThat(responses).hasSize(itemCount);
        verify(itineraryService, times(itemCount)).createItinerary(eq(1L), any());
    }

    private void assertGenerateItinerariesSavesFallbackWithPaceItemCount(
            ItineraryPace pace,
            int itemCount,
            int expectedFallbackItemCount
    ) {
        Trip trip = trip(1L, TripConcept.FOOD);
        int candidateCount = switch (pace) {
            case RELAXED -> Math.max(itemCount, 3);
            case NORMAL -> Math.max(itemCount, 4);
            case BUSY -> Math.max(itemCount, 5);
        };
        List<PlaceResponse> candidatePlaces = places(candidateCount);
        ItineraryGenerateRequest request = new ItineraryGenerateRequest(null, null, pace);
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = llmItems(itemCount);
        List<ItineraryCreateRequest> createRequests = createRequests(itemCount);
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces, request)).thenReturn(prompt);
        when(llmClient.generate(anyString())).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L, request);

        assertThat(responses).hasSize(expectedFallbackItemCount);
        verify(llmClient, times(3)).generate(anyString());
        verify(itineraryService, times(expectedFallbackItemCount)).createItinerary(eq(1L), any());
    }

    private List<PlaceResponse> places(int count) {
        List<PlaceResponse> places = new java.util.ArrayList<>();
        for (int index = 0; index < count; index++) {
            places.add(place(placeIdAt(index), 60));
        }
        return places;
    }

    private List<LlmItineraryItemResponse> llmItems(int count) {
        List<LlmItineraryItemResponse> items = new java.util.ArrayList<>();
        for (int index = 0; index < count; index++) {
            items.add(llmItem(
                    placeIdAt(index),
                    index + 1,
                    itineraryStartTimeAt(index),
                    itineraryEndTimeAt(index),
                    index == 0 ? 0 : 30
            ));
        }
        return items;
    }

    private List<ItineraryCreateRequest> createRequests(int count) {
        List<ItineraryCreateRequest> requests = new java.util.ArrayList<>();
        for (int index = 0; index < count; index++) {
            requests.add(request(
                    placeIdAt(index),
                    index + 1,
                    itineraryStartTimeAt(index),
                    itineraryEndTimeAt(index),
                    index == 0 ? 0 : 5
            ));
        }
        return requests;
    }

    private void stubSuccessfulItinerarySaves(int count) {
        for (int index = 0; index < count; index++) {
            Long placeId = placeIdAt(index);
            when(itineraryService.createItinerary(
                    1L,
                    request(
                            placeId,
                            index + 1,
                            itineraryStartTimeAt(index),
                            itineraryEndTimeAt(index),
                            index == 0 ? 0 : 5
                    )
            )).thenReturn(response(100L + index, 1L, placeId, index + 1));
        }
    }

    private Long placeIdAt(int index) {
        return 10L + (index * 10L);
    }

    private LocalTime itineraryStartTimeAt(int index) {
        return LocalTime.of(9, 0).plusMinutes(index * 70L);
    }

    private LocalTime itineraryEndTimeAt(int index) {
        return itineraryStartTimeAt(index).plusMinutes(60);
    }

    private Trip trip(Long tripId, TripConcept concept, LocalTime dailyStartTime) {
        return trip(tripId, concept, dailyStartTime, LocalTime.of(18, 0));
    }

    private Trip trip(Long tripId, TripConcept concept, LocalTime dailyStartTime, LocalTime dailyEndTime) {
        return trip(
                tripId,
                concept,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 1),
                dailyStartTime,
                dailyEndTime
        );
    }

    private Trip trip(
            Long tripId,
            TripConcept concept,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime dailyStartTime,
            LocalTime dailyEndTime
    ) {
        Trip trip = Trip.create(
                "JEJU",
                startDate,
                endDate,
                dailyStartTime,
                dailyEndTime,
                concept,
                Transportation.RENT_CAR,
                "SEOGWIPO"
        );
        setId(trip, "tripId", tripId);
        return trip;
    }

    private PlaceResponse place(Long placeId, Integer avgStayMinutes) {
        return place(placeId, "NATURE", avgStayMinutes);
    }

    private PlaceResponse place(Long placeId, Integer avgStayMinutes, Double latitude, Double longitude) {
        return place(placeId, "NATURE", avgStayMinutes, latitude, longitude);
    }

    private PlaceResponse place(Long placeId, String category, Integer avgStayMinutes) {
        return place(placeId, category, avgStayMinutes, 33.0, 126.0);
    }

    private PlaceResponse place(
            Long placeId,
            String category,
            Integer avgStayMinutes,
            Double latitude,
            Double longitude
    ) {
        return new PlaceResponse(
                placeId,
                "Place " + placeId,
                category,
                "EAST",
                "JEJU",
                latitude,
                longitude,
                avgStayMinutes,
                false,
                true,
                1,
                2,
                3,
                4,
                5,
                4,
                3,
                "description"
        );
    }

    private PlaceResponse placeWithFoodScore(Long placeId, String category, Integer foodScore) {
        return placeWithFoodScoreAndRegion(placeId, category, foodScore, "EAST");
    }

    private PlaceResponse placeWithFoodScoreAndRegion(
            Long placeId,
            String category,
            Integer foodScore,
            String region
    ) {
        return new PlaceResponse(
                placeId,
                "Place " + placeId,
                category,
                region,
                "JEJU",
                33.0,
                126.0,
                60,
                false,
                true,
                1,
                2,
                foodScore,
                4,
                5,
                4,
                3,
                "description"
        );
    }

    private LlmItineraryItemResponse llmItem(
            Long placeId,
            Integer orderNo,
            LocalTime startTime,
            LocalTime endTime,
            Integer travelMinutesFromPrevious
    ) {
        return llmItem(placeId, 1, orderNo, startTime, endTime, travelMinutesFromPrevious);
    }

    private LlmItineraryItemResponse llmItem(
            Long placeId,
            Integer dayNo,
            Integer orderNo,
            LocalTime startTime,
            LocalTime endTime,
            Integer travelMinutesFromPrevious
    ) {
        return new LlmItineraryItemResponse(
                placeId,
                dayNo,
                orderNo,
                startTime,
                endTime,
                travelMinutesFromPrevious,
                "Mock LLM itinerary item selected from candidate places."
        );
    }

    private ItineraryCreateRequest request(
            Long placeId,
            Integer orderNo,
            LocalTime startTime,
            LocalTime endTime,
            Integer travelMinutesFromPrevious
    ) {
        return request(placeId, 1, orderNo, startTime, endTime, travelMinutesFromPrevious);
    }

    private ItineraryCreateRequest request(
            Long placeId,
            Integer dayNo,
            Integer orderNo,
            LocalTime startTime,
            LocalTime endTime,
            Integer travelMinutesFromPrevious
    ) {
        return new ItineraryCreateRequest(
                placeId,
                dayNo,
                orderNo,
                startTime,
                endTime,
                travelMinutesFromPrevious,
                "Mock itinerary item selected from candidate places."
        );
    }

    private ItineraryCreateRequest fallbackRequest(
            Long placeId,
            Integer orderNo,
            LocalTime startTime,
            LocalTime endTime,
            Integer travelMinutesFromPrevious
    ) {
        return new ItineraryCreateRequest(
                placeId,
                1,
                orderNo,
                startTime,
                endTime,
                travelMinutesFromPrevious,
                "Fallback itinerary item generated from candidate places."
        );
    }

    private ItineraryResponse response(Long itineraryId, Long tripId, Long placeId, Integer orderNo) {
        return new ItineraryResponse(
                itineraryId,
                tripId,
                placeId,
                new PlaceSummaryResponse(
                        placeId,
                        "Place " + placeId,
                        "NATURE",
                        "EAST",
                        "JEJU",
                        33.0,
                        126.0,
                        "description"
                ),
                1,
                orderNo,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                0,
                "Mock itinerary item selected from candidate places."
        );
    }

    private void setId(Object target, String fieldName, Long id) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, id);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
