package com.tripagent.itinerary.domain;

public enum ItineraryGenerationSource {
    LLM,
    LLM_ADJUSTED,
    FALLBACK,
    MANUAL,
    UNKNOWN
}
