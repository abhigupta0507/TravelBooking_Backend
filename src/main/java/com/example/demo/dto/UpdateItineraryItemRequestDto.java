package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO for updating an existing ItineraryItem.
 * Note: package_id is not included as we don't allow moving an item to a different package.
 */
public class UpdateItineraryItemRequestDto {

    @NotNull(message = "Day number is required.")
    @Min(value = 1, message = "Day number must be at least 1.")
    private Integer day_number;

    private Integer duration;

    @NotNull(message = "A start time is required.")
    private LocalDateTime start_time;

    @NotNull(message = "An end time is required.")
    private LocalDateTime end_time;

    @NotBlank(message = "A title for the itinerary item is required.")
    private String title;

    private String description;
    private String street_name;
    private String city;
    private String state;
    private String pin;

    // --- Getters and Setters ---

    public Integer getDay_number() { return day_number; }
    public void setDay_number(Integer day_number) { this.day_number = day_number; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public LocalDateTime getStart_time() { return start_time; }
    public void setStart_time(LocalDateTime start_time) { this.start_time = start_time; }
    public LocalDateTime getEnd_time() { return end_time; }
    public void setEnd_time(LocalDateTime end_time) { this.end_time = end_time; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStreet_name() { return street_name; }
    public void setStreet_name(String street_name) { this.street_name = street_name; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }
}
