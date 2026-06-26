package com.tripagent.place.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "places")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false, length = 50)
    private String region;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Integer avgStayMinutes;

    @Column(nullable = false)
    private Boolean indoorYn;

    @Column(nullable = false)
    private Boolean parkingYn;

    @Column(nullable = false)
    private Integer rainyDayScore;

    @Column(nullable = false)
    private Integer healingScore;

    @Column(nullable = false)
    private Integer foodScore;

    @Column(nullable = false)
    private Integer cafeScore;

    @Column(nullable = false)
    private Integer photoScore;

    @Column(nullable = false)
    private Integer coupleScore;

    @Column(nullable = false)
    private Integer familyScore;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Boolean useYn;

    protected Place() {
    }

    private Place(
            String name,
            String category,
            String region,
            String address,
            Double latitude,
            Double longitude,
            Integer avgStayMinutes,
            Boolean indoorYn,
            Boolean parkingYn,
            Integer rainyDayScore,
            Integer healingScore,
            Integer foodScore,
            Integer cafeScore,
            Integer photoScore,
            Integer coupleScore,
            Integer familyScore,
            String description,
            Boolean useYn
    ) {
        this.name = name;
        this.category = category;
        this.region = region;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.avgStayMinutes = avgStayMinutes;
        this.indoorYn = indoorYn;
        this.parkingYn = parkingYn;
        this.rainyDayScore = rainyDayScore;
        this.healingScore = healingScore;
        this.foodScore = foodScore;
        this.cafeScore = cafeScore;
        this.photoScore = photoScore;
        this.coupleScore = coupleScore;
        this.familyScore = familyScore;
        this.description = description;
        this.useYn = useYn;
    }

    public static Place create(
            String name,
            String category,
            String region,
            String address,
            Double latitude,
            Double longitude,
            Integer avgStayMinutes,
            Boolean indoorYn,
            Boolean parkingYn,
            Integer rainyDayScore,
            Integer healingScore,
            Integer foodScore,
            Integer cafeScore,
            Integer photoScore,
            Integer coupleScore,
            Integer familyScore,
            String description,
            Boolean useYn
    ) {
        return new Place(
                name,
                category,
                region,
                address,
                latitude,
                longitude,
                avgStayMinutes,
                indoorYn,
                parkingYn,
                rainyDayScore,
                healingScore,
                foodScore,
                cafeScore,
                photoScore,
                coupleScore,
                familyScore,
                description,
                useYn
        );
    }

    public Long getPlaceId() {
        return placeId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
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

    public Integer getAvgStayMinutes() {
        return avgStayMinutes;
    }

    public Boolean getIndoorYn() {
        return indoorYn;
    }

    public Boolean getParkingYn() {
        return parkingYn;
    }

    public Integer getRainyDayScore() {
        return rainyDayScore;
    }

    public Integer getHealingScore() {
        return healingScore;
    }

    public Integer getFoodScore() {
        return foodScore;
    }

    public Integer getCafeScore() {
        return cafeScore;
    }

    public Integer getPhotoScore() {
        return photoScore;
    }

    public Integer getCoupleScore() {
        return coupleScore;
    }

    public Integer getFamilyScore() {
        return familyScore;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getUseYn() {
        return useYn;
    }
}
