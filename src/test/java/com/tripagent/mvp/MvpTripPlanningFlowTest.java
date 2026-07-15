package com.tripagent.mvp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tripagent.ai.llm.LlmClient;
import com.tripagent.ai.llm.LlmItineraryResponseConverter;
import com.tripagent.ai.llm.parser.LlmItineraryJsonParser;
import com.tripagent.ai.prompt.ItineraryPromptGenerator;
import com.tripagent.ai.validator.CandidatePlaceValidator;
import com.tripagent.itinerary.domain.Itinerary;
import com.tripagent.itinerary.dto.ItineraryGenerateRequest;
import com.tripagent.itinerary.dto.ItineraryPace;
import com.tripagent.itinerary.dto.ItineraryResponse;
import com.tripagent.itinerary.policy.AccommodationAreaRegionMapper;
import com.tripagent.itinerary.repository.ItineraryRepository;
import com.tripagent.itinerary.service.ItineraryGenerateService;
import com.tripagent.itinerary.service.ItineraryService;
import com.tripagent.member.repository.MemberRepository;
import com.tripagent.place.domain.Place;
import com.tripagent.place.dto.PlaceResponse;
import com.tripagent.place.repository.PlaceRepository;
import com.tripagent.place.service.PlaceService;
import com.tripagent.route.SimpleRouteCalculationAdapter;
import com.tripagent.trip.domain.Transportation;
import com.tripagent.trip.domain.Trip;
import com.tripagent.trip.domain.TripConcept;
import com.tripagent.trip.domain.TripVisibility;
import com.tripagent.trip.dto.TripCreateRequest;
import com.tripagent.trip.dto.TripDetailResponse;
import com.tripagent.trip.dto.TripResponse;
import com.tripagent.trip.repository.TripLikeRepository;
import com.tripagent.trip.repository.TripAccommodationRepository;
import com.tripagent.trip.repository.TripRepository;
import com.tripagent.trip.repository.TripViewRepository;
import com.tripagent.trip.service.TripService;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MvpTripPlanningFlowTest {

    private static final Long OWNER_ID = 100L;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private ItineraryRepository itineraryRepository;

    @Mock
    private TripAccommodationRepository tripAccommodationRepository;

    @Mock
    private TripLikeRepository tripLikeRepository;

    @Mock
    private TripViewRepository tripViewRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private PlaceService placeService;

    @Mock
    private LlmClient llmClient;

    private TripService tripService;
    private ItineraryGenerateService itineraryGenerateService;

    private final Map<Long, Trip> trips = new HashMap<>();
    private final Map<Long, Place> places = new HashMap<>();
    private final List<Itinerary> itineraries = new ArrayList<>();
    private final AtomicLong tripIdSequence = new AtomicLong(1L);
    private final AtomicLong itineraryIdSequence = new AtomicLong(1L);

    @BeforeEach
    void setUp() {
        ItineraryService itineraryService = new ItineraryService(
                itineraryRepository,
                tripRepository,
                placeRepository
        );
        tripService = new TripService(
                tripRepository,
                itineraryRepository,
                tripAccommodationRepository,
                tripLikeRepository,
                tripViewRepository,
                memberRepository
        );
        itineraryGenerateService = new ItineraryGenerateService(
                tripRepository,
                placeService,
                new ItineraryPromptGenerator(new SimpleRouteCalculationAdapter()),
                llmClient,
                new LlmItineraryJsonParser(new ObjectMapper().registerModule(new JavaTimeModule())),
                new LlmItineraryResponseConverter(),
                new CandidatePlaceValidator(),
                itineraryService,
                itineraryRepository,
                new SimpleRouteCalculationAdapter(),
                new AccommodationAreaRegionMapper()
        );

        stubTripRepository();
        stubPlaceRepository();
        stubItineraryRepository();
    }

    @Test
    void mvpTripPlanningFlowCreatesGeneratesReadsAndPublishesTrip() {
        List<Place> candidatePlaces = List.of(
                place(10L, "사려니숲길", "NATURE", "EAST", 33.4400, 126.6700),
                place(20L, "제주 향토식당", "FOOD", "EAST", 33.4410, 126.6710),
                place(30L, "동쪽 바다 카페", "CAFE", "EAST", 33.4420, 126.6720),
                place(40L, "오름 산책로", "NATURE", "WEST", 33.4430, 126.6730),
                place(50L, "서쪽 해물식당", "FOOD", "WEST", 33.4440, 126.6740),
                place(60L, "노을 카페", "CAFE", "WEST", 33.4450, 126.6750)
        );
        candidatePlaces.forEach(place -> places.put(place.getPlaceId(), place));
        when(placeService.findCandidatePlaces(TripConcept.HEALING))
                .thenReturn(candidatePlaces.stream().map(PlaceResponse::from).toList());
        when(llmClient.generate(anyString())).thenReturn(validLlmResponse());

        TripResponse createdTrip = tripService.createTrip(tripCreateRequest(), OWNER_ID);
        List<ItineraryResponse> generatedItineraries = itineraryGenerateService.generateItineraries(
                createdTrip.tripId(),
                new ItineraryGenerateRequest(null, null, ItineraryPace.RELAXED),
                OWNER_ID
        );
        TripDetailResponse tripDetail = tripService.getTrip(createdTrip.tripId(), OWNER_ID);
        TripResponse publishedTrip = tripService.updateTripVisibility(
                createdTrip.tripId(),
                OWNER_ID,
                TripVisibility.PUBLIC
        );

        Set<Long> candidatePlaceIds = Set.of(10L, 20L, 30L, 40L, 50L, 60L);
        assertThat(createdTrip.visibility()).isEqualTo(TripVisibility.PRIVATE);
        assertThat(generatedItineraries).hasSize(6);
        assertThat(generatedItineraries)
                .extracting(ItineraryResponse::placeId)
                .containsExactly(10L, 20L, 30L, 40L, 50L, 60L);
        assertThat(candidatePlaceIds).containsAll(
                generatedItineraries.stream()
                        .map(ItineraryResponse::placeId)
                        .toList()
        );
        assertThat(tripDetail.itineraries())
                .extracting(ItineraryResponse::dayNo)
                .containsExactly(1, 1, 1, 2, 2, 2);
        assertThat(tripDetail.itineraries())
                .extracting(ItineraryResponse::orderNo)
                .containsExactly(1, 2, 3, 1, 2, 3);
        assertThat(publishedTrip.visibility()).isEqualTo(TripVisibility.PUBLIC);
    }

    private void stubTripRepository() {
        when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> {
            Trip trip = invocation.getArgument(0);
            setId(trip, "tripId", tripIdSequence.getAndIncrement());
            trips.put(trip.getTripId(), trip);
            return trip;
        });
        when(tripRepository.findById(anyLong())).thenAnswer(invocation ->
                Optional.ofNullable(trips.get(invocation.getArgument(0))));
    }

    private void stubPlaceRepository() {
        when(placeRepository.findById(anyLong())).thenAnswer(invocation ->
                Optional.ofNullable(places.get(invocation.getArgument(0))));
    }

    private void stubItineraryRepository() {
        when(itineraryRepository.existsByTrip_TripId(anyLong())).thenAnswer(invocation -> {
            Long tripId = invocation.getArgument(0);
            return itineraries.stream()
                    .anyMatch(itinerary -> tripId.equals(itinerary.getTrip().getTripId()));
        });
        when(itineraryRepository.existsByTrip_TripIdAndDayNoAndOrderNo(anyLong(), any(), any()))
                .thenAnswer(invocation -> {
                    Long tripId = invocation.getArgument(0);
                    Integer dayNo = invocation.getArgument(1);
                    Integer orderNo = invocation.getArgument(2);
                    return itineraries.stream()
                            .anyMatch(itinerary -> tripId.equals(itinerary.getTrip().getTripId())
                                    && dayNo.equals(itinerary.getDayNo())
                                    && orderNo.equals(itinerary.getOrderNo()));
                });
        when(itineraryRepository.findByTrip_TripIdAndDayNo(anyLong(), any()))
                .thenAnswer(invocation -> {
                    Long tripId = invocation.getArgument(0);
                    Integer dayNo = invocation.getArgument(1);
                    return itineraries.stream()
                            .filter(itinerary -> tripId.equals(itinerary.getTrip().getTripId()))
                            .filter(itinerary -> dayNo.equals(itinerary.getDayNo()))
                            .sorted(Comparator.comparing(Itinerary::getOrderNo))
                            .toList();
                });
        when(itineraryRepository.findByTrip_TripIdOrderByDayNoAscOrderNoAsc(anyLong()))
                .thenAnswer(invocation -> {
                    Long tripId = invocation.getArgument(0);
                    return itineraries.stream()
                            .filter(itinerary -> tripId.equals(itinerary.getTrip().getTripId()))
                            .sorted(Comparator.comparing(Itinerary::getDayNo)
                                    .thenComparing(Itinerary::getOrderNo))
                            .toList();
                });
        when(itineraryRepository.save(any(Itinerary.class))).thenAnswer(invocation -> {
            Itinerary itinerary = invocation.getArgument(0);
            setId(itinerary, "itineraryId", itineraryIdSequence.getAndIncrement());
            itineraries.add(itinerary);
            return itinerary;
        });
    }

    private TripCreateRequest tripCreateRequest() {
        return new TripCreateRequest(
                "JEJU",
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 2),
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                TripConcept.HEALING,
                Transportation.RENT_CAR,
                "EAST"
        );
    }

    private Place place(
            Long placeId,
            String name,
            String category,
            String region,
            Double latitude,
            Double longitude
    ) {
        Place place = Place.create(
                name,
                category,
                region,
                "제주특별자치도",
                latitude,
                longitude,
                60,
                false,
                true,
                5,
                10,
                5,
                5,
                7,
                5,
                5,
                name + " 설명",
                true
        );
        setId(place, "placeId", placeId);
        return place;
    }

    private String validLlmResponse() {
        return """
                [
                  {
                    "placeId": 10,
                    "dayNo": 1,
                    "orderNo": 1,
                    "startTime": "09:00:00",
                    "endTime": "10:00:00",
                    "travelMinutesFromPrevious": 0,
                    "reason": "후보 장소를 기반으로 첫 일정을 시작합니다."
                  },
                  {
                    "placeId": 20,
                    "dayNo": 1,
                    "orderNo": 2,
                    "startTime": "10:30:00",
                    "endTime": "11:30:00",
                    "travelMinutesFromPrevious": 30,
                    "reason": "가까운 후보 식당으로 이동합니다."
                  },
                  {
                    "placeId": 30,
                    "dayNo": 1,
                    "orderNo": 3,
                    "startTime": "12:00:00",
                    "endTime": "13:00:00",
                    "travelMinutesFromPrevious": 30,
                    "reason": "식사 후 후보 카페에서 쉽니다."
                  },
                  {
                    "placeId": 40,
                    "dayNo": 2,
                    "orderNo": 1,
                    "startTime": "09:00:00",
                    "endTime": "10:00:00",
                    "travelMinutesFromPrevious": 0,
                    "reason": "둘째 날 후보 자연 명소를 방문합니다."
                  },
                  {
                    "placeId": 50,
                    "dayNo": 2,
                    "orderNo": 2,
                    "startTime": "10:30:00",
                    "endTime": "11:30:00",
                    "travelMinutesFromPrevious": 30,
                    "reason": "후보 식당으로 이동합니다."
                  },
                  {
                    "placeId": 60,
                    "dayNo": 2,
                    "orderNo": 3,
                    "startTime": "12:00:00",
                    "endTime": "13:00:00",
                    "travelMinutesFromPrevious": 30,
                    "reason": "마지막 후보 카페에서 일정을 마무리합니다."
                  }
                ]
                """;
    }

    private void setId(Object target, String fieldName, Long id) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, id);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Failed to set test id field. fieldName=" + fieldName, exception);
        }
    }
}
