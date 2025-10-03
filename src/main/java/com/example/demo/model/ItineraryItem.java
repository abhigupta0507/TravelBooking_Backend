package com.example.demo.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ItineraryItem {

    private Integer package_id;
    private Integer item_id;
    private Integer day_number;
    private Integer duration;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private String title;
    private String description;
    private String street_name;
    private String city;
    private String state;
    private String pin;
    private LocalDateTime created_at;

    public ItineraryItem() {}

    public ItineraryItem(Integer package_id, Integer item_id, Integer day_number, Integer duration,
                         LocalDateTime start_time, LocalDateTime end_time, String title, String description,
                          String street_name, String city, String state, String pin, LocalDateTime created_at) {
        this.package_id = package_id;
        this.item_id = item_id;
        this.day_number = day_number;
        this.duration = duration;
        this.start_time = start_time;
        this.end_time = end_time;
        this.title = title;
        this.description = description;
        this.street_name = street_name;
        this.city = city;
        this.state = state;
        this.pin = pin;
        this.created_at = created_at;
    }

    public ItineraryItem(Integer package_id,Integer day_number,Integer duration,LocalDateTime start_time,LocalDateTime end_time,String title,String description,String street_name,String city,String state,String pin,LocalDateTime created_at){
        this.package_id = package_id;
        this.day_number = day_number;
        this.duration = duration;
        this.start_time = start_time;
        this.end_time = end_time;
        this.title = title;
        this.description = description;
        this.street_name = street_name;
        this.city = city;
        this.state = state;
        this.pin = pin;
        this.created_at = created_at;
    }

    public Integer getPackage_id() { return package_id; }
    public void setPackage_id(Integer package_id) { this.package_id = package_id; }

    public Integer getItem_id() { return item_id; }
    public void setItem_id(Integer item_id) { this.item_id = item_id; }

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

    public LocalDateTime getCreated_at() { return created_at; }
    public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }

    @Override
    public String toString() {
        return "Itinerary_Item{" +
                "package_id=" + package_id +
                ", item_id=" + item_id +
                ", day_number=" + day_number +
                ", duration='" + duration + '\'' +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", street_name='" + street_name + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", pin='" + pin + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
