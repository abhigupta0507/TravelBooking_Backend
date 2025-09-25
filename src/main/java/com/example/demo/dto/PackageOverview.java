package com.example.demo.dto;

public class PackageOverview {
    private int packageId;
    private String name;
    private String tour_type;
    private String itinerary_summary;
    private int duration_days;
    private int max_capacity;

    private String image_url;
    private String status;


    private Float avg_rating;



    public PackageOverview() {
    }

    public PackageOverview(String name, String tour_type, String itinerary_summary, int duration_days, int max_capacity, String image_url, String status, Float avg_rating) {
        this.name = name;
        this.tour_type = tour_type;
        this.itinerary_summary = itinerary_summary;
        this.duration_days = duration_days;
        this.max_capacity = max_capacity;
        this.image_url = image_url;
        this.status = status;
        this.avg_rating = avg_rating;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
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

    public String getItinerary_summary() {
        return itinerary_summary;
    }

    public void setItinerary_summary(String itinerary_summary) {
        this.itinerary_summary = itinerary_summary;
    }

    public int getDuration_days() {
        return duration_days;
    }

    public void setDuration_days(int duration_days) {
        this.duration_days = duration_days;
    }

    public int getMax_capacity() {
        return max_capacity;
    }

    public void setMax_capacity(int max_capacity) {
        this.max_capacity = max_capacity;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(Float avg_rating) {
        this.avg_rating = avg_rating;
    }

    @Override
    public String toString() {
        return "PackageOverview{" +
                "packageId=" + packageId +
                ", name='" + name + '\'' +
                ", tour_type='" + tour_type + '\'' +
                ", itinerary_summary='" + itinerary_summary + '\'' +
                ", duration_days=" + duration_days +
                ", max_capacity=" + max_capacity +
                ", image_url='" + image_url + '\'' +
                ", status='" + status + '\'' +
                ", avg_rating=" + avg_rating +
                '}';
    }


}
