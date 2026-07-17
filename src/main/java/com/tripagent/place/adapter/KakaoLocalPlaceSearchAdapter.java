package com.tripagent.place.adapter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tripagent.place.config.KakaoLocalProperties;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class KakaoLocalPlaceSearchAdapter implements PlaceSearchAdapter {

    private static final int MAX_PAGE_SIZE = 15;

    private final RestClient restClient;
    private final String apiKey;

    @Autowired
    public KakaoLocalPlaceSearchAdapter(
            KakaoLocalProperties properties,
            RestClient.Builder restClientBuilder
    ) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(properties.getConnectTimeout())
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(properties.getReadTimeout());
        this.restClient = restClientBuilder
                .baseUrl(properties.getBaseUrl())
                .requestFactory(requestFactory)
                .build();
        this.apiKey = properties.getApiKey();
    }

    KakaoLocalPlaceSearchAdapter(RestClient restClient, String apiKey) {
        this.restClient = restClient;
        this.apiKey = apiKey;
    }

    @Override
    public List<PlaceSearchCandidate> search(String query, int size) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new PlaceSearchAdapterException("Kakao Local API key is not configured.");
        }
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Place search query is required.");
        }
        int normalizedSize = Math.max(1, Math.min(size, MAX_PAGE_SIZE));

        try {
            KakaoLocalResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/local/search/keyword.json")
                            .queryParam("query", query.trim())
                            .queryParam("size", normalizedSize)
                            .build())
                    .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + apiKey.trim())
                    .retrieve()
                    .body(KakaoLocalResponse.class);
            if (response == null || response.documents() == null) {
                throw new PlaceSearchAdapterException("Kakao Local place search response is empty.");
            }
            return response.documents().stream()
                    .map(this::toCandidate)
                    .filter(Objects::nonNull)
                    .toList();
        } catch (RestClientException exception) {
            throw new PlaceSearchAdapterException("Kakao Local place search request failed.", exception);
        }
    }

    private PlaceSearchCandidate toCandidate(KakaoLocalDocument document) {
        if (document == null
                || document.id() == null || document.id().isBlank()
                || document.placeName() == null || document.placeName().isBlank()) {
            return null;
        }
        try {
            return new PlaceSearchCandidate(
                    document.id(),
                    document.placeName(),
                    document.addressName(),
                    document.roadAddressName(),
                    Double.parseDouble(document.y()),
                    Double.parseDouble(document.x()),
                    document.categoryName(),
                    document.placeUrl()
            );
        } catch (NumberFormatException | NullPointerException exception) {
            return null;
        }
    }

    record KakaoLocalResponse(List<KakaoLocalDocument> documents) {
    }

    record KakaoLocalDocument(
            String id,
            @JsonProperty("place_name") String placeName,
            @JsonProperty("address_name") String addressName,
            @JsonProperty("road_address_name") String roadAddressName,
            String x,
            String y,
            @JsonProperty("category_name") String categoryName,
            @JsonProperty("place_url") String placeUrl
    ) {
    }
}
