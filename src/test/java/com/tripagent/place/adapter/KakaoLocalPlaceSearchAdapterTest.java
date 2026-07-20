package com.tripagent.place.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.net.http.HttpTimeoutException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.ResourceAccessException;

class KakaoLocalPlaceSearchAdapterTest {

    @Test
    void mapsKakaoKeywordSearchResponse() {
        RestClient.Builder restClientBuilder = RestClient.builder().baseUrl("https://dapi.kakao.com");
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        KakaoLocalPlaceSearchAdapter adapter = new KakaoLocalPlaceSearchAdapter(
                restClientBuilder.build(),
                "test-key"
        );
        server.expect(requestTo(containsString("/v2/local/search/keyword.json")))
                .andExpect(requestTo(containsString("size=5")))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "KakaoAK test-key"))
                .andRespond(withSuccess("""
                        {
                          "documents": [
                            {
                              "id": "7936768",
                              "place_name": "새별오름",
                              "address_name": "제주특별자치도 제주시 애월읍 봉성리 산 59-8",
                              "road_address_name": "",
                              "x": "126.3577306657398",
                              "y": "33.3661276358495",
                              "category_name": "여행 > 관광,명소 > 오름",
                              "place_url": "http://place.map.kakao.com/7936768"
                            }
                          ]
                        }
                        """, MediaType.APPLICATION_JSON));

        List<PlaceSearchCandidate> candidates = adapter.search("제주 새별오름", 5);

        assertThat(candidates).containsExactly(new PlaceSearchCandidate(
                "7936768",
                "새별오름",
                "제주특별자치도 제주시 애월읍 봉성리 산 59-8",
                "",
                33.3661276358495,
                126.3577306657398,
                "여행 > 관광,명소 > 오름",
                "http://place.map.kakao.com/7936768"
        ));
        server.verify();
    }

    @Test
    void rejectsSearchWhenApiKeyIsMissing() {
        KakaoLocalPlaceSearchAdapter adapter = new KakaoLocalPlaceSearchAdapter(
                RestClient.builder().baseUrl("https://dapi.kakao.com").build(),
                " "
        );

        assertThatThrownBy(() -> adapter.search("제주 새별오름", 5))
                .isInstanceOf(PlaceSearchAdapterException.class)
                .hasMessage("Kakao Local API key is not configured.")
                .satisfies(exception -> assertThat(((PlaceSearchAdapterException) exception).getFailureType())
                        .isEqualTo(PlaceSearchFailureType.CONFIGURATION));
    }

    @Test
    void classifiesTooManyRequestsAsRateLimited() {
        RestClient.Builder restClientBuilder = RestClient.builder().baseUrl("https://dapi.kakao.com");
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        KakaoLocalPlaceSearchAdapter adapter = new KakaoLocalPlaceSearchAdapter(
                restClientBuilder.build(), "test-key"
        );
        server.expect(requestTo(containsString("/v2/local/search/keyword.json")))
                .andRespond(withStatus(HttpStatus.TOO_MANY_REQUESTS));

        assertThatThrownBy(() -> adapter.search("제주 호텔", 5))
                .isInstanceOf(PlaceSearchAdapterException.class)
                .satisfies(exception -> assertThat(((PlaceSearchAdapterException) exception).getFailureType())
                        .isEqualTo(PlaceSearchFailureType.RATE_LIMITED));
        server.verify();
    }

    @Test
    void classifiesRequestTimeout() {
        RestClient.Builder restClientBuilder = RestClient.builder().baseUrl("https://dapi.kakao.com");
        MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
        KakaoLocalPlaceSearchAdapter adapter = new KakaoLocalPlaceSearchAdapter(
                restClientBuilder.build(), "test-key"
        );
        server.expect(requestTo(containsString("/v2/local/search/keyword.json")))
                .andRespond(request -> {
                    throw new ResourceAccessException("timeout", new HttpTimeoutException("timeout"));
                });

        assertThatThrownBy(() -> adapter.search("제주 호텔", 5))
                .isInstanceOf(PlaceSearchAdapterException.class)
                .satisfies(exception -> assertThat(((PlaceSearchAdapterException) exception).getFailureType())
                        .isEqualTo(PlaceSearchFailureType.TIMEOUT));
        server.verify();
    }
}
