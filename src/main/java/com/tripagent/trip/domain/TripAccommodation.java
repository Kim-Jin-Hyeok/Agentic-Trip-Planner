package com.tripagent.trip.domain;

import com.tripagent.accommodation.domain.Accommodation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;

@Entity
@Table(
        name = "trip_accommodations",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_trip_accommodations_trip_stay_date",
                columnNames = {"trip_id", "stay_date"}
        ),
        indexes = {
                @Index(name = "idx_trip_accommodations_trip_date", columnList = "trip_id, stay_date"),
                @Index(name = "idx_trip_accommodations_accommodation", columnList = "accommodation_id")
        }
)
public class TripAccommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripAccommodationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accommodation_id", nullable = false)
    private Accommodation accommodation;

    @Column(nullable = false)
    private LocalDate stayDate;

    protected TripAccommodation() {
    }

    private TripAccommodation(Trip trip, Accommodation accommodation, LocalDate stayDate) {
        this.trip = trip;
        this.accommodation = accommodation;
        this.stayDate = stayDate;
    }

    public static TripAccommodation create(Trip trip, Accommodation accommodation, LocalDate stayDate) {
        return new TripAccommodation(trip, accommodation, stayDate);
    }

    public Long getTripAccommodationId() {
        return tripAccommodationId;
    }

    public Trip getTrip() {
        return trip;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public LocalDate getStayDate() {
        return stayDate;
    }
}
