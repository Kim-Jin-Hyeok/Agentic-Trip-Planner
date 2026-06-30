package com.tripagent.trip.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    @Column(nullable = false, length = 50)
    private String destination;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private LocalTime dailyStartTime;

    @Column(nullable = false)
    private LocalTime dailyEndTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TripConcept concept;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Transportation transportation;

    @Column(length = 50)
    private String lastAccommodationArea;

    @Column
    private Long ownerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TripVisibility visibility;

    protected Trip() {
    }

    private Trip(
            String destination,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime dailyStartTime,
            LocalTime dailyEndTime,
            TripConcept concept,
            Transportation transportation,
            String lastAccommodationArea,
            Long ownerId
    ) {
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dailyStartTime = dailyStartTime;
        this.dailyEndTime = dailyEndTime;
        this.concept = concept;
        this.transportation = transportation;
        this.lastAccommodationArea = lastAccommodationArea;
        this.ownerId = ownerId;
        this.visibility = TripVisibility.PRIVATE;
    }

    public static Trip create(
            String destination,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime dailyStartTime,
            LocalTime dailyEndTime,
            TripConcept concept,
            Transportation transportation,
            String lastAccommodationArea
    ) {
        return create(
                destination,
                startDate,
                endDate,
                dailyStartTime,
                dailyEndTime,
                concept,
                transportation,
                lastAccommodationArea,
                null
        );
    }

    public static Trip create(
            String destination,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime dailyStartTime,
            LocalTime dailyEndTime,
            TripConcept concept,
            Transportation transportation,
            String lastAccommodationArea,
            Long ownerId
    ) {
        return new Trip(
                destination,
                startDate,
                endDate,
                dailyStartTime,
                dailyEndTime,
                concept,
                transportation,
                lastAccommodationArea,
                ownerId
        );
    }

    public Long getTripId() {
        return tripId;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalTime getDailyStartTime() {
        return dailyStartTime;
    }

    public LocalTime getDailyEndTime() {
        return dailyEndTime;
    }

    public TripConcept getConcept() {
        return concept;
    }

    public Transportation getTransportation() {
        return transportation;
    }

    public String getLastAccommodationArea() {
        return lastAccommodationArea;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public TripVisibility getVisibility() {
        return visibility;
    }

    public void changeVisibility(TripVisibility visibility) {
        if (visibility == null) {
            throw new IllegalArgumentException("Trip visibility is required.");
        }
        this.visibility = visibility;
    }
}
