package com.tripagent.place.service;

import com.tripagent.place.domain.Place;
import com.tripagent.place.repository.PlaceRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile({"local", "dev"})
public class PlaceDataInitializer implements CommandLineRunner {

    private final PlaceRepository placeRepository;

    public PlaceDataInitializer(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        for (Place place : seedPlaces()) {
            if (!placeRepository.existsByNameAndAddress(place.getName(), place.getAddress())) {
                placeRepository.save(place);
            }
        }
    }

    private List<Place> seedPlaces() {
        return List.of(
                Place.create(
                        "성산일출봉",
                        "NATURE",
                        "EAST",
                        "제주특별자치도 서귀포시 성산읍 성산리 1",
                        33.458056,
                        126.942500,
                        90,
                        false,
                        true,
                        2,
                        5,
                        1,
                        1,
                        5,
                        4,
                        4,
                        "제주 동쪽을 대표하는 일출 명소입니다.",
                        true
                ),
                Place.create(
                        "비자림",
                        "NATURE",
                        "EAST",
                        "제주특별자치도 제주시 구좌읍 비자숲길 55",
                        33.491111,
                        126.811389,
                        80,
                        false,
                        true,
                        3,
                        5,
                        1,
                        1,
                        4,
                        4,
                        5,
                        "조용한 숲길을 걸으며 쉬기 좋은 장소입니다.",
                        true
                ),
                Place.create(
                        "오설록 티뮤지엄",
                        "CAFE",
                        "WEST",
                        "제주특별자치도 서귀포시 안덕면 신화역사로 15",
                        33.305833,
                        126.289444,
                        70,
                        true,
                        true,
                        5,
                        4,
                        2,
                        5,
                        4,
                        4,
                        4,
                        "녹차밭과 디저트를 함께 즐길 수 있는 실내외 복합 장소입니다.",
                        true
                ),
                Place.create(
                        "동문재래시장",
                        "FOOD",
                        "NORTH",
                        "제주특별자치도 제주시 관덕로14길 20",
                        33.511667,
                        126.526111,
                        90,
                        true,
                        false,
                        4,
                        2,
                        5,
                        2,
                        3,
                        3,
                        4,
                        "제주 먹거리와 기념품을 둘러보기 좋은 전통시장입니다.",
                        true
                ),
                Place.create(
                        "카멜리아힐",
                        "GARDEN",
                        "WEST",
                        "제주특별자치도 서귀포시 안덕면 병악로 166",
                        33.289444,
                        126.370000,
                        100,
                        false,
                        true,
                        3,
                        4,
                        1,
                        2,
                        5,
                        5,
                        4,
                        "계절별 꽃과 산책로를 즐길 수 있는 정원형 관광지입니다.",
                        true
                )
        );
    }
}
