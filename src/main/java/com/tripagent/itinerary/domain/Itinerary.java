package com.tripagent.itinerary.domain;

import com.tripagent.place.domain.Place;
import com.tripagent.trip.domain.Trip;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;

@Entity
@Table(name = "itineraries")
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itineraryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(nullable = false)
    private Integer dayNo;

    @Column(nullable = false)
    private Integer orderNo;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private Integer travelMinutesFromPrevious;

    @Column(length = 1000)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ItineraryGenerationSource generationSource;

    protected Itinerary() {
    }

    private Itinerary(
            Trip trip,
            Place place,
            Integer dayNo,
            Integer orderNo,
            LocalTime startTime,
            LocalTime endTime,
            Integer travelMinutesFromPrevious,
            String reason,
            ItineraryGenerationSource generationSource
    ) {
        this.trip = trip;
        this.place = place;
        this.dayNo = dayNo;
        this.orderNo = orderNo;
        this.startTime = startTime;
        this.endTime = endTime;
        this.travelMinutesFromPrevious = travelMinutesFromPrevious;
        this.reason = reason;
        this.generationSource = generationSource;
    }

    public static Itinerary create(
            Trip trip,
            Place place,
            Integer dayNo,
            Integer orderNo,
            LocalTime startTime,
            LocalTime endTime,
            Integer travelMinutesFromPrevious,
            String reason
    ) {
        return create(
                trip, place, dayNo, orderNo, startTime, endTime,
                travelMinutesFromPrevious, reason, ItineraryGenerationSource.MANUAL
        );
    }

    public static Itinerary create(
            Trip trip,
            Place place,
            Integer dayNo,
            Integer orderNo,
            LocalTime startTime,
            LocalTime endTime,
            Integer travelMinutesFromPrevious,
            String reason,
            ItineraryGenerationSource generationSource
    ) {
        return new Itinerary(
                trip,
                place,
                dayNo,
                orderNo,
                startTime,
                endTime,
                travelMinutesFromPrevious,
                reason,
                generationSource == null ? ItineraryGenerationSource.MANUAL : generationSource
        );
    }

    public void update(
            Place place,
            Integer dayNo,
            Integer orderNo,
            LocalTime startTime,
            LocalTime endTime,
            Integer travelMinutesFromPrevious,
            String reason
    ) {
        this.place = place;
        this.dayNo = dayNo;
        this.orderNo = orderNo;
        this.startTime = startTime;
        this.endTime = endTime;
        this.travelMinutesFromPrevious = travelMinutesFromPrevious;
        this.reason = reason;
    }

    public void markAsUserAdjusted() {
        this.generationSource = ItineraryGenerationSource.USER_ADJUSTED;
    }

    public Long getItineraryId() {
        return itineraryId;
    }

    public Trip getTrip() {
        return trip;
    }

    public Place getPlace() {
        return place;
    }

    public Integer getDayNo() {
        return dayNo;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Integer getTravelMinutesFromPrevious() {
        return travelMinutesFromPrevious;
    }

    public String getReason() {
        return reason;
    }

    public ItineraryGenerationSource getGenerationSource() {
        return generationSource == null ? ItineraryGenerationSource.UNKNOWN : generationSource;
    }
}
