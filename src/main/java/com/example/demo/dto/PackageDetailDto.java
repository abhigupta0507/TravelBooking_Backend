package com.example.demo.dto;

import com.example.demo.model.ItineraryItem;
import com.example.demo.model.TourPackage;

import java.util.List;

/**
 * A DTO that combines a TourPackage with its list of ItineraryItems.
 * This is used for the package details API response.
 */
public class PackageDetailDto {
    private int packageId;
    private String name;
    private String tour_type;
    private String itinerary_summary;
    private int duration_days;
    private int max_capacity;

    private String image_url;
    private PackageStatus status;


    private int price;
    private Float avg_rating;

    private List<ItineraryItem> itinerary;

    public PackageDetailDto() {
    }

    /**
     * A helper method to easily convert from a TourPackage model to this DTO.
     * @param tourPackage The source TourPackage object from the database.
     * @param itineraryItems The list of itinerary items for this package.
     * @return A fully populated PackageDetailDto.
     */
    public static PackageDetailDto from(TourPackage tourPackage, List<ItineraryItem> itineraryItems) {
        PackageDetailDto dto = new PackageDetailDto();
        dto.setPackageId(tourPackage.getPackageId());
        dto.setName(tourPackage.getName());
        dto.setTour_type(tourPackage.getTour_type());
        dto.setItinerary_summary(tourPackage.getItinerary_summary());
        dto.setDuration_days(tourPackage.getDuration_days());
        dto.setMax_capacity(tourPackage.getMax_capacity());
        dto.setImage_url(tourPackage.getImage_url());
        dto.setStatus(tourPackage.getStatus());
        dto.setPrice(tourPackage.getPrice());
        dto.setAvg_rating(tourPackage.getAvg_rating());

        // Add the itinerary list
        dto.setItinerary(itineraryItems);

        return dto;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public Float getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(Float avg_rating) {
        this.avg_rating = avg_rating;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public PackageStatus getStatus() {
        return status;
    }

    public void setStatus(PackageStatus status) {
        this.status = status;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getMax_capacity() {
        return max_capacity;
    }

    public void setMax_capacity(int max_capacity) {
        this.max_capacity = max_capacity;
    }

    public int getDuration_days() {
        return duration_days;
    }

    public void setDuration_days(int duration_days) {
        this.duration_days = duration_days;
    }

    public String getItinerary_summary() {
        return itinerary_summary;
    }

    public void setItinerary_summary(String itinerary_summary) {
        this.itinerary_summary = itinerary_summary;
    }

    public String getTour_type() {
        return tour_type;
    }

    public void setTour_type(String tour_type) {
        this.tour_type = tour_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ItineraryItem> getItinerary() {
        return itinerary;
    }

    public void setItinerary(List<ItineraryItem> itinerary) {
        this.itinerary = itinerary;
    }
}
