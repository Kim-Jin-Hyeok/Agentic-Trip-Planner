package com.tripagent.itinerary.domain;

import com.tripagent.itinerary.dto.ItineraryDayTimeWindowRequest;
import com.tripagent.itinerary.dto.ItineraryGenerateRequest;
import com.tripagent.itinerary.dto.ItineraryPace;
import com.tripagent.place.dto.PlaceCategory;
import com.tripagent.trip.domain.Trip;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "itinerary_generation_preferences")
public class ItineraryGenerationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long preferenceId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false, unique = true)
    private Trip trip;

    @ElementCollection
    @CollectionTable(name = "itinerary_generation_must_visit_places",
            joinColumns = @JoinColumn(name = "preference_id"))
    @OrderColumn(name = "list_order")
    @Column(name = "place_id", nullable = false)
    private List<Long> mustVisitPlaceIds = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "itinerary_generation_excluded_places",
            joinColumns = @JoinColumn(name = "preference_id"))
    @OrderColumn(name = "list_order")
    @Column(name = "place_id", nullable = false)
    private List<Long> excludedPlaceIds = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ItineraryPace pace;

    @ElementCollection
    @CollectionTable(name = "itinerary_generation_preferred_categories",
            joinColumns = @JoinColumn(name = "preference_id"))
    @OrderColumn(name = "list_order")
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 20)
    private List<PlaceCategory> preferredCategories = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "itinerary_generation_day_time_windows",
            joinColumns = @JoinColumn(name = "preference_id"))
    @OrderColumn(name = "list_order")
    private List<ItineraryGenerationDayTimeWindow> dayTimeWindows = new ArrayList<>();

    @Column(nullable = false)
    private boolean rainyDayMode;

    @ElementCollection
    @CollectionTable(name = "itinerary_generation_rainy_days",
            joinColumns = @JoinColumn(name = "preference_id"))
    @OrderColumn(name = "list_order")
    @Column(name = "day_no", nullable = false)
    private List<Integer> rainyDayNos = new ArrayList<>();

    protected ItineraryGenerationPreference() {
    }

    private ItineraryGenerationPreference(Trip trip) {
        this.trip = trip;
    }

    public static ItineraryGenerationPreference create(Trip trip, ItineraryGenerateRequest request) {
        ItineraryGenerationPreference preference = new ItineraryGenerationPreference(trip);
        preference.update(request);
        return preference;
    }

    public void update(ItineraryGenerateRequest request) {
        mustVisitPlaceIds.clear();
        mustVisitPlaceIds.addAll(request.normalizedMustVisitPlaceIds());
        excludedPlaceIds.clear();
        excludedPlaceIds.addAll(request.normalizedExcludedPlaceIds());
        pace = request.normalizedPace();
        preferredCategories.clear();
        preferredCategories.addAll(request.normalizedPreferredCategories());
        dayTimeWindows.clear();
        request.normalizedDayTimeWindows().stream()
                .map(window -> new ItineraryGenerationDayTimeWindow(
                        window.dayNo(), window.startTime(), window.endTime()))
                .forEach(dayTimeWindows::add);
        rainyDayMode = request.normalizedRainyDayMode();
        rainyDayNos.clear();
        rainyDayNos.addAll(request.normalizedRainyDayNos());
    }

    public ItineraryGenerateRequest toRequest() {
        List<ItineraryDayTimeWindowRequest> windows = dayTimeWindows.stream()
                .map(window -> new ItineraryDayTimeWindowRequest(
                        window.getDayNo(), window.getStartTime(), window.getEndTime()))
                .toList();
        return new ItineraryGenerateRequest(
                List.copyOf(mustVisitPlaceIds),
                List.copyOf(excludedPlaceIds),
                pace,
                List.copyOf(preferredCategories),
                windows,
                rainyDayMode,
                List.copyOf(rainyDayNos)
        );
    }
}
