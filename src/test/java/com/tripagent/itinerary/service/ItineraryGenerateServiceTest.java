package com.tripagent.itinerary.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
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
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.itinerary.repository.ItineraryRepository;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.service.PlaceService;
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
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(prompt)).thenReturn(rawResponse);
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
                .containsExactly(0, 30);
        verify(placeService).findCandidatePlaces(TripConcept.FOOD);
        verify(itineraryPromptGenerator).generate(trip, candidatePlaces);
        verify(llmClient).generate(prompt);
        verify(llmItineraryJsonParser).parse(rawResponse);
        verify(llmItineraryResponseConverter).toCreateRequests(parsedItems);
        verify(candidatePlaceValidator).validatePlaceIds(candidatePlaces, List.of(10L, 20L));
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
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(prompt)).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);
        when(itineraryService.createItinerary(1L, request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)))
                .thenReturn(response(100L, 1L, 10L, 1));
        when(itineraryService.createItinerary(1L, request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)))
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
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
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
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(prompt)).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);
        when(itineraryService.createItinerary(1L, request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)))
                .thenReturn(response(100L, 1L, 10L, 1));
        when(itineraryService.createItinerary(1L, request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)))
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
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
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
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(prompt)).thenReturn(firstRawResponse, secondRawResponse);
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
        when(itineraryService.createItinerary(1L, request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)))
                .thenReturn(response(200L, 1L, 20L, 2));

        List<ItineraryResponse> responses = itineraryGenerateService.generateItineraries(1L);

        assertThat(responses).extracting(ItineraryResponse::placeId)
                .containsExactly(10L, 20L);
        verify(llmClient, times(2)).generate(prompt);
        verify(candidatePlaceValidator).validatePlaceIds(candidatePlaces, List.of(10L, 10L));
        verify(candidatePlaceValidator).validatePlaceIds(candidatePlaces, List.of(10L, 20L));
        verify(itineraryService).createItinerary(
                1L,
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        verify(itineraryService).createItinerary(
                1L,
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
    }

    @Test
    void generateItinerariesFailsWhenAllRetriesFailValidation() {
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
        when(llmClient.generate(prompt)).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);
        doThrow(new IllegalArgumentException("Place id must not be duplicated in generated itinerary. placeId=10"))
                .when(candidatePlaceValidator)
                .validatePlaceIds(candidatePlaces, List.of(10L, 10L));

        assertThatThrownBy(() -> itineraryGenerateService.generateItineraries(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Place id must not be duplicated in generated itinerary. placeId=10");
        verify(llmClient, times(3)).generate(prompt);
        verify(candidatePlaceValidator, times(3)).validatePlaceIds(candidatePlaces, List.of(10L, 10L));
        verify(itineraryService, never()).createItinerary(
                1L,
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
    }

    @Test
    void generateItinerariesRejectsInvalidFirstTravelMinutesBeforeSaving() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 15),
                llmItem(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 15),
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(prompt)).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        assertThatThrownBy(() -> itineraryGenerateService.generateItineraries(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("First itinerary item of each day must have travelMinutesFromPrevious 0. dayNo=1");
        verify(candidatePlaceValidator, times(3)).validatePlaceIds(candidatePlaces, List.of(10L, 20L));
        verify(itineraryService, never()).createItinerary(
                1L,
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 15)
        );
        verify(itineraryService, never()).createItinerary(
                1L,
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
    }

    @Test
    void generateItinerariesRejectsFirstStartTimeBeforeDailyStartTimeBeforeSaving() {
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
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        when(itineraryRepository.existsByTrip_TripId(1L)).thenReturn(false);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(prompt)).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        assertThatThrownBy(() -> itineraryGenerateService.generateItineraries(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("First itinerary item of each day must start at or after trip dailyStartTime. dayNo=1");
        verify(candidatePlaceValidator, times(3)).validatePlaceIds(candidatePlaces, List.of(10L, 20L));
        verify(itineraryService, never()).createItinerary(
                1L,
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
        verify(itineraryService, never()).createItinerary(
                1L,
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
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
        when(llmClient.generate(prompt)).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        List<ItineraryCreateRequest> drafts = itineraryGenerateService.generateDraftItineraries(1L);

        assertThat(drafts).hasSize(2);
        assertThat(drafts.get(0).startTime()).isEqualTo(LocalTime.of(10, 30));
        verify(candidatePlaceValidator).validatePlaceIds(candidatePlaces, List.of(10L, 20L));
    }

    @Test
    void generateItinerariesDoesNotSaveWhenLlmFails() {
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
        when(llmClient.generate(prompt)).thenThrow(
                LlmException.of(LlmFailureType.INSUFFICIENT_QUOTA, "OpenAI quota exceeded.")
        );

        assertThatThrownBy(() -> itineraryGenerateService.generateItineraries(1L))
                .isInstanceOf(LlmException.class)
                .hasMessage("OpenAI quota exceeded.");
        verify(llmClient).generate(prompt);
        verify(llmItineraryJsonParser, never()).parse("raw response");
        verify(llmItineraryResponseConverter, never()).toCreateRequests(List.of());
        verify(candidatePlaceValidator, never()).validatePlaceIds(candidatePlaces, List.of(10L, 20L));
        verify(itineraryService, never()).createItinerary(
                1L,
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
    }

    @Test
    void regenerateItinerariesDoesNotDeleteExistingItineraryWhenLlmFails() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        String prompt = "prompt";
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(prompt)).thenThrow(
                LlmException.of(LlmFailureType.INSUFFICIENT_QUOTA, "OpenAI quota exceeded.")
        );

        assertThatThrownBy(() -> itineraryGenerateService.regenerateItineraries(1L))
                .isInstanceOf(LlmException.class)
                .hasMessage("OpenAI quota exceeded.");
        verify(llmClient).generate(prompt);
        verify(itineraryRepository, never()).deleteByTrip_TripId(1L);
        verify(itineraryService, never()).createItinerary(
                1L,
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 0)
        );
    }

    @Test
    void regenerateItinerariesDoesNotDeleteExistingItineraryWhenDraftValidationFails() {
        Trip trip = trip(1L, TripConcept.FOOD);
        List<PlaceResponse> candidatePlaces = List.of(
                place(10L, 60),
                place(20L, 90)
        );
        String prompt = "prompt";
        String rawResponse = "raw response";
        List<LlmItineraryItemResponse> parsedItems = List.of(
                llmItem(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 15),
                llmItem(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        List<ItineraryCreateRequest> createRequests = List.of(
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 15),
                request(20L, 2, LocalTime.of(10, 30), LocalTime.of(12, 0), 30)
        );
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(placeService.findCandidatePlaces(TripConcept.FOOD)).thenReturn(candidatePlaces);
        when(itineraryPromptGenerator.generate(trip, candidatePlaces)).thenReturn(prompt);
        when(llmClient.generate(prompt)).thenReturn(rawResponse);
        when(llmItineraryJsonParser.parse(rawResponse)).thenReturn(parsedItems);
        when(llmItineraryResponseConverter.toCreateRequests(parsedItems)).thenReturn(createRequests);

        assertThatThrownBy(() -> itineraryGenerateService.regenerateItineraries(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("First itinerary item of each day must have travelMinutesFromPrevious 0. dayNo=1");
        verify(candidatePlaceValidator, times(3)).validatePlaceIds(candidatePlaces, List.of(10L, 20L));
        verify(itineraryRepository, never()).deleteByTrip_TripId(1L);
        verify(itineraryService, never()).createItinerary(
                1L,
                request(10L, 1, LocalTime.of(9, 0), LocalTime.of(10, 0), 15)
        );
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

    private Trip trip(Long tripId, TripConcept concept, LocalTime dailyStartTime) {
        Trip trip = Trip.create(
                "JEJU",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                dailyStartTime,
                concept,
                Transportation.RENT_CAR,
                "SEOGWIPO"
        );
        setId(trip, "tripId", tripId);
        return trip;
    }

    private PlaceResponse place(Long placeId, Integer avgStayMinutes) {
        return new PlaceResponse(
                placeId,
                "Place " + placeId,
                "NATURE",
                "EAST",
                "JEJU",
                33.0,
                126.0,
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

    private LlmItineraryItemResponse llmItem(
            Long placeId,
            Integer orderNo,
            LocalTime startTime,
            LocalTime endTime,
            Integer travelMinutesFromPrevious
    ) {
        return new LlmItineraryItemResponse(
                placeId,
                1,
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
        return new ItineraryCreateRequest(
                placeId,
                1,
                orderNo,
                startTime,
                endTime,
                travelMinutesFromPrevious,
                "Mock itinerary item selected from candidate places."
        );
    }

    private ItineraryResponse response(Long itineraryId, Long tripId, Long placeId, Integer orderNo) {
        return new ItineraryResponse(
                itineraryId,
                tripId,
                placeId,
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
