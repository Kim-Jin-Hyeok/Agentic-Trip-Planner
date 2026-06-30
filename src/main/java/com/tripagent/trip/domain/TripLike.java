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

@Entity
@Table(
        name = "trip_likes",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_trip_likes_trip_user",
                columnNames = {"trip_id", "user_id"}
        )
)
public class TripLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    protected TripLike() {
    }

    private TripLike(Trip trip, Long userId) {
        this.trip = trip;
        this.userId = userId;
    }

    public static TripLike create(Trip trip, Long userId) {
        if (trip == null) {
            throw new IllegalArgumentException("Trip is required.");
        }
        if (userId == null) {
            throw new IllegalArgumentException("Trip like userId is required.");
        }
        return new TripLike(trip, userId);
    }

    public Long getTripLikeId() {
        return tripLikeId;
    }

    public Trip getTrip() {
        return trip;
    }

    public Long getUserId() {
        return userId;
    }
}
