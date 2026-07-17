package com.tripagent.place.adapter;

import java.util.List;

public interface PlaceSearchAdapter {

    List<PlaceSearchCandidate> search(String query, int size);
}
