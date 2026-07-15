package com.tripagent.accommodation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "accommodations",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_accommodations_name_address",
                columnNames = {"name", "address"}
        ),
        indexes = {
                @Index(name = "idx_accommodations_use_region", columnList = "useYn, region"),
                @Index(name = "idx_accommodations_use_type", columnList = "useYn, accommodationType")
        }
)
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accommodationId;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AccommodationType accommodationType;

    @Column(nullable = false, length = 50)
    private String region;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(length = 1000)
    private String description;

    @Column(length = 1000)
    private String thumbnailUrl;

    @Column(nullable = false)
    private Boolean parkingYn;

    @Column(nullable = false)
    private Boolean useYn;

    protected Accommodation() {
    }

    private Accommodation(
            String name,
            AccommodationType accommodationType,
            String region,
            String address,
            Double latitude,
            Double longitude,
            String description,
            String thumbnailUrl,
            Boolean parkingYn,
            Boolean useYn
    ) {
        this.name = name;
        this.accommodationType = accommodationType;
        this.region = region;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.parkingYn = parkingYn;
        this.useYn = useYn;
    }

    public static Accommodation create(
            String name,
            AccommodationType accommodationType,
            String region,
            String address,
            Double latitude,
            Double longitude,
            String description,
            String thumbnailUrl,
            Boolean parkingYn,
            Boolean useYn
    ) {
        return new Accommodation(
                name,
                accommodationType,
                region,
                address,
                latitude,
                longitude,
                description,
                thumbnailUrl,
                parkingYn,
                useYn
        );
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public String getName() {
        return name;
    }

    public AccommodationType getAccommodationType() {
        return accommodationType;
    }

    public String getRegion() {
        return region;
    }

    public String getAddress() {
        return address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public Boolean getParkingYn() {
        return parkingYn;
    }

    public Boolean getUseYn() {
        return useYn;
    }
}
