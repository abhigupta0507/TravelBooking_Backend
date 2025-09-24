package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TourPackage {

    private Integer package_id;
    private String name;
    private String tour_type;
    private Integer duration_days;
    private Integer max_capacity;
    private String itinerary_summary;
    private String status;
    private BigDecimal avg_rating;
    private LocalDateTime created_at;

    public TourPackage() {}

    public TourPackage(Integer package_id, String name, String tour_type, Integer duration_days,
                        Integer max_capacity, String itinerary_summary, String status,
                        BigDecimal avg_rating, LocalDateTime created_at) {
        this.package_id = package_id;
        this.name = name;
        this.tour_type = tour_type;
        this.duration_days = duration_days;
        this.max_capacity = max_capacity;
        this.itinerary_summary = itinerary_summary;
        this.status = status;
        this.avg_rating = avg_rating;
        this.created_at = created_at;
    }

    public Integer getPackage_id() {
        return package_id;
    }

    public void setPackage_id(Integer package_id) {
        this.package_id = package_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTour_type() {
        return tour_type;
    }

    public void setTour_type(String tour_type) {
        this.tour_type = tour_type;
    }

    public Integer getDuration_days() {
        return duration_days;
    }

    public void setDuration_days(Integer duration_days) {
        this.duration_days = duration_days;
    }

    public Integer getMax_capacity() {
        return max_capacity;
    }

    public void setMax_capacity(Integer max_capacity) {
        this.max_capacity = max_capacity;
    }

    public String getItinerary_summary() {
        return itinerary_summary;
    }

    public void setItinerary_summary(String itinerary_summary) {
        this.itinerary_summary = itinerary_summary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(BigDecimal avg_rating) {
        this.avg_rating = avg_rating;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Tour_Package{" +
                "package_id=" + package_id +
                ", name='" + name + '\'' +
                ", tour_type='" + tour_type + '\'' +
                ", duration_days=" + duration_days +
                ", max_capacity=" + max_capacity +
                ", itinerary_summary='" + itinerary_summary + '\'' +
                ", status='" + status + '\'' +
                ", avg_rating=" + avg_rating +
                ", created_at=" + created_at +
                '}';
    }
}
