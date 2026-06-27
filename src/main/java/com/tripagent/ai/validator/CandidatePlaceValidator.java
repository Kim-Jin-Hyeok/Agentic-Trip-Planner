package com.tripagent.ai.validator;

import com.tripagent.place.dto.PlaceResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CandidatePlaceValidator {

    public void validatePlaceIds(List<PlaceResponse> candidatePlaces, List<Long> placeIds) {
        if (candidatePlaces == null || candidatePlaces.isEmpty()) {
            throw new IllegalArgumentException("Candidate places are required.");
        }
        if (placeIds == null || placeIds.isEmpty()) {
            throw new IllegalArgumentException("Place ids are required.");
        }

        Set<Long> candidatePlaceIds = candidatePlaces.stream()
                .map(PlaceResponse::placeId)
                .collect(Collectors.toSet());

        for (Long placeId : placeIds) {
            if (placeId == null) {
                throw new IllegalArgumentException("Place id must not be null.");
            }
            if (!candidatePlaceIds.contains(placeId)) {
                throw new IllegalArgumentException("Place id is not included in candidate places. placeId=" + placeId);
            }
        }
    }
}
