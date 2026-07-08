package com.tripagent.trip.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;

@Entity
@Table(
        name = "trip_views",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_trip_views_trip_user_date",
                columnNames = {"trip_id", "user_id", "view_date"}
        )
)
public class TripView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripViewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "view_date", nullable = false)
    private LocalDate viewDate;

    protected TripView() {
    }

    private TripView(Trip trip, Long userId, LocalDate viewDate) {
        this.trip = trip;
        this.userId = userId;
        this.viewDate = viewDate;
    }

    public static TripView create(Trip trip, Long userId, LocalDate viewDate) {
        if (trip == null) {
            throw new IllegalArgumentException("Trip is required.");
        }
        if (userId == null) {
            throw new IllegalArgumentException("Trip view userId is required.");
        }
        if (viewDate == null) {
            throw new IllegalArgumentException("Trip viewDate is required.");
        }
        return new TripView(trip, userId, viewDate);
    }

    public Long getTripViewId() {
        return tripViewId;
    }

    public Trip getTrip() {
        return trip;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDate getViewDate() {
        return viewDate;
    }
}
