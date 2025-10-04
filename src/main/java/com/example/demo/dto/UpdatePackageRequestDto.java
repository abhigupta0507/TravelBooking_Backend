package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * A DTO representing the data that can be updated for a TourPackage.
 * Includes validation to ensure data integrity.
 */
public class UpdatePackageRequestDto {

    @NotBlank(message = "Package name cannot be blank.")
    @Size(min = 5, max = 255, message = "Package name must be between 5 and 255 characters.")
    private String name;

    @NotBlank(message = "Tour type cannot be blank.")
    private String tour_type;

    private String image_url;

    @NotNull(message = "Duration in days is required.")
    @Min(value = 1, message = "Duration must be at least 1 day.")
    private Integer duration_days;

    @NotNull(message = "Price is required.")
    @Min(value = 0, message = "Price cannot be negative.")
    private Integer price;

    @NotNull(message = "Maximum capacity is required.")
    @Min(value = 1, message = "Capacity must be at least 1.")
    private Integer max_capacity;

    private String itinerary_summary;

    @NotNull(message = "A status (e.g., UPCOMING, ACTIVE) is required.")
    private PackageStatus status;

    // --- Getters and Setters ---

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTour_type() { return tour_type; }
    public void setTour_type(String tour_type) { this.tour_type = tour_type; }
    public String getImage_url() { return image_url; }
    public void setImage_url(String image_url) { this.image_url = image_url; }
    public Integer getDuration_days() { return duration_days; }
    public void setDuration_days(Integer duration_days) { this.duration_days = duration_days; }
    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    public Integer getMax_capacity() { return max_capacity; }
    public void setMax_capacity(Integer max_capacity) { this.max_capacity = max_capacity; }
    public String getItinerary_summary() { return itinerary_summary; }
    public void setItinerary_summary(String itinerary_summary) { this.itinerary_summary = itinerary_summary; }
    public PackageStatus getStatus() { return status; }
    public void setStatus(PackageStatus status) { this.status = status; }
}

