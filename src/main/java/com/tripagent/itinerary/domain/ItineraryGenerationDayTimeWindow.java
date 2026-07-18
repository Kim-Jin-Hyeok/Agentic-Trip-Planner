package com.tripagent.itinerary.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalTime;

@Embeddable
public class ItineraryGenerationDayTimeWindow {

    @Column(nullable = false)
    private Integer dayNo;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    protected ItineraryGenerationDayTimeWindow() {
    }

    public ItineraryGenerationDayTimeWindow(Integer dayNo, LocalTime startTime, LocalTime endTime) {
        this.dayNo = dayNo;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Integer getDayNo() {
        return dayNo;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
