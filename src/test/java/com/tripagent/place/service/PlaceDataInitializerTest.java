package com.tripagent.place.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.tripagent.place.domain.Place;
import com.tripagent.place.dto.PlaceCategory;
import com.tripagent.place.repository.PlaceRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class PlaceDataInitializerTest {

    private static final int MIN_PLACES_FOR_THREE_NIGHTS_FOUR_DAYS_BUSY = 20;
    private static final int MIN_PLACES_PER_REGION = 4;
    private static final int MIN_FOOD_PLACES = 6;
    private static final int MIN_CAFE_PLACES = 5;
    private static final int MIN_INDOOR_PLACES = 8;

    private final PlaceDataInitializer initializer = new PlaceDataInitializer(
            org.mockito.Mockito.mock(PlaceRepository.class)
    );

    @Test
    void seedPlacesAreEnoughForThreeNightsFourDaysBusyItinerary() {
        List<Place> places = initializer.seedPlaces();

        assertThat(places).hasSizeGreaterThanOrEqualTo(MIN_PLACES_FOR_THREE_NIGHTS_FOUR_DAYS_BUSY);
    }

    @Test
    void seedPlacesHaveBalancedRegionCoverage() {
        List<Place> places = initializer.seedPlaces();
        Map<String, Long> countByRegion = countByRegion(places);

        assertThat(countByRegion)
                .containsKeys("EAST", "WEST", "NORTH", "SOUTH");
        assertThat(countByRegion.values())
                .allSatisfy(count -> assertThat(count).isGreaterThanOrEqualTo(MIN_PLACES_PER_REGION));
    }

    @Test
    void seedPlacesHaveEnoughMealAndRestPlaces() {
        List<Place> places = initializer.seedPlaces();
        Map<String, Long> countByCategory = countByCategory(places);

        assertThat(countByCategory.getOrDefault("FOOD", 0L)).isGreaterThanOrEqualTo(MIN_FOOD_PLACES);
        assertThat(countByCategory.getOrDefault("CAFE", 0L)).isGreaterThanOrEqualTo(MIN_CAFE_PLACES);
    }

    @Test
    void seedPlacesHaveEnoughIndoorPlacesForRainyDays() {
        List<Place> places = initializer.seedPlaces();

        long indoorPlaceCount = places.stream()
                .filter(Place::getIndoorYn)
                .count();

        assertThat(indoorPlaceCount).isGreaterThanOrEqualTo(MIN_INDOOR_PLACES);
    }

    @Test
    void seedPlaceCategoriesMatchSupportedPlaceCategories() {
        Set<String> supportedCategoryNames = Arrays.stream(PlaceCategory.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        assertThat(initializer.seedPlaces())
                .extracting(Place::getCategory)
                .allSatisfy(category -> assertThat(supportedCategoryNames).contains(category));
    }

    @Test
    void productionSeedSqlMatchesJavaSeedPlaces() throws IOException {
        List<Place> seedPlaces = initializer.seedPlaces();
        String sql = Files.readString(Path.of("db", "seed", "places.sql"));
        long insertStatementCount = sql.lines()
                .filter(line -> line.startsWith("INSERT INTO places"))
                .count();

        assertThat(insertStatementCount).isEqualTo(seedPlaces.size());
        assertThat(seedPlaces)
                .allSatisfy(place -> {
                    assertThat(sql).contains("'" + place.getName() + "'");
                    assertThat(sql).contains("'" + place.getAddress() + "'");
                });
    }

    private Map<String, Long> countByRegion(List<Place> places) {
        return places.stream()
                .collect(Collectors.groupingBy(Place::getRegion, Collectors.counting()));
    }

    private Map<String, Long> countByCategory(List<Place> places) {
        return places.stream()
                .collect(Collectors.groupingBy(Place::getCategory, Collectors.counting()));
    }
}
