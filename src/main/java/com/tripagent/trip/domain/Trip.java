package com.tripagent.trip.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(
        name = "trips",
        indexes = {
                @Index(name = "idx_trips_visibility_trip_id", columnList = "visibility, tripId"),
                @Index(
                        name = "idx_trips_visibility_like_count_view_count_trip_id",
                        columnList = "visibility, likeCount, viewCount, tripId"
                ),
                @Index(name = "idx_trips_visibility_concept_nights", columnList = "visibility, concept, nights"),
                @Index(name = "idx_trips_visibility_destination", columnList = "visibility, destination")
        }
)
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    @Column(length = 100)
    private String title;

    @Column(nullable = false, length = 50)
    private String destination;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Integer nights;

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
    private Long startPlaceId;

    @Column
    private Long endPlaceId;

    @Column
    private Long ownerId;

    @Column(nullable = false)
    private Long likeCount;

    @Column(nullable = false)
    private Long viewCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TripVisibility visibility;

    protected Trip() {
    }

    private Trip(
            String title,
            String destination,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime dailyStartTime,
            LocalTime dailyEndTime,
            TripConcept concept,
            Transportation transportation,
            String lastAccommodationArea,
            Long startPlaceId,
            Long endPlaceId,
            Long ownerId
    ) {
        this.title = title;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.nights = Math.toIntExact(ChronoUnit.DAYS.between(startDate, endDate));
        this.dailyStartTime = dailyStartTime;
        this.dailyEndTime = dailyEndTime;
        this.concept = concept;
        this.transportation = transportation;
        this.lastAccommodationArea = lastAccommodationArea;
        this.startPlaceId = startPlaceId;
        this.endPlaceId = endPlaceId;
        this.ownerId = ownerId;
        this.likeCount = 0L;
        this.viewCount = 0L;
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
                destination + " 여행",
                destination,
                startDate,
                endDate,
                dailyStartTime,
                dailyEndTime,
                concept,
                transportation,
                lastAccommodationArea,
                null,
                null,
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
        return create(
                destination + " 여행",
                destination,
                startDate,
                endDate,
                dailyStartTime,
                dailyEndTime,
                concept,
                transportation,
                lastAccommodationArea,
                null,
                null,
                ownerId
        );
    }

    public static Trip create(
            String title,
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
        return create(
                title,
                destination,
                startDate,
                endDate,
                dailyStartTime,
                dailyEndTime,
                concept,
                transportation,
                lastAccommodationArea,
                null,
                null,
                ownerId
        );
    }

    public static Trip create(
            String title,
            String destination,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime dailyStartTime,
            LocalTime dailyEndTime,
            TripConcept concept,
            Transportation transportation,
            String lastAccommodationArea,
            Long startPlaceId,
            Long endPlaceId,
            Long ownerId
    ) {
        return new Trip(
                title,
                destination,
                startDate,
                endDate,
                dailyStartTime,
                dailyEndTime,
                concept,
                transportation,
                lastAccommodationArea,
                startPlaceId,
                endPlaceId,
                ownerId
        );
    }

    public Long getTripId() {
        return tripId;
    }

    public String getTitle() {
        if (title == null || title.isBlank()) {
            return destination + " 여행";
        }
        return title;
    }

    public void changeTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Trip title is required.");
        }
        String normalizedTitle = title.trim();
        if (normalizedTitle.length() > 100) {
            throw new IllegalArgumentException("Trip title must be less than or equal to 100 characters.");
        }
        this.title = normalizedTitle;
    }

    public void changeConditions(
            LocalDate startDate,
            LocalDate endDate,
            LocalTime dailyStartTime,
            LocalTime dailyEndTime,
            TripConcept concept,
            String lastAccommodationArea
    ) {
        changeConditions(
                startDate,
                endDate,
                dailyStartTime,
                dailyEndTime,
                concept,
                lastAccommodationArea,
                startPlaceId,
                endPlaceId
        );
    }

    public void changeConditions(
            LocalDate startDate,
            LocalDate endDate,
            LocalTime dailyStartTime,
            LocalTime dailyEndTime,
            TripConcept concept,
            String lastAccommodationArea,
            Long startPlaceId,
            Long endPlaceId
    ) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.nights = Math.toIntExact(ChronoUnit.DAYS.between(startDate, endDate));
        this.dailyStartTime = dailyStartTime;
        this.dailyEndTime = dailyEndTime;
        this.concept = concept;
        this.lastAccommodationArea = lastAccommodationArea;
        this.startPlaceId = startPlaceId;
        this.endPlaceId = endPlaceId;
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

    public Integer getNights() {
        return nights;
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

    public Long getStartPlaceId() {
        return startPlaceId;
    }

    public Long getEndPlaceId() {
        return endPlaceId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void increaseLikeCount() {
        likeCount++;
    }

    public void decreaseLikeCount() {
        if (likeCount <= 0) {
            throw new IllegalStateException("Trip likeCount cannot be negative.");
        }
        likeCount--;
    }

    public void increaseViewCount() {
        viewCount++;
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
