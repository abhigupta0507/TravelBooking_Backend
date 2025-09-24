package com.example.demo.model;

import java.time.LocalDateTime;

public class HotelAssignment {
    private Integer hotel_assignment_id;
    private LocalDateTime created_at;
    private Integer package_booking_id;
    private Integer hotel_booking_id;
    private Integer package_id;
    private Integer item_id;

    public HotelAssignment() {}

    public HotelAssignment(Integer hotel_assignment_id, LocalDateTime created_at, Integer package_booking_id,
                            Integer hotel_booking_id, Integer package_id, Integer item_id) {
        this.hotel_assignment_id = hotel_assignment_id;
        this.created_at = created_at;
        this.package_booking_id = package_booking_id;
        this.hotel_booking_id = hotel_booking_id;
        this.package_id = package_id;
        this.item_id = item_id;
    }

    public Integer getHotel_assignment_id() {
        return hotel_assignment_id;
    }

    public void setHotel_assignment_id(Integer hotel_assignment_id) {
        this.hotel_assignment_id = hotel_assignment_id;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public Integer getPackage_booking_id() {
        return package_booking_id;
    }

    public void setPackage_booking_id(Integer package_booking_id) {
        this.package_booking_id = package_booking_id;
    }

    public Integer getHotel_booking_id() {
        return hotel_booking_id;
    }

    public void setHotel_booking_id(Integer hotel_booking_id) {
        this.hotel_booking_id = hotel_booking_id;
    }

    public Integer getPackage_id() {
        return package_id;
    }

    public void setPackage_id(Integer package_id) {
        this.package_id = package_id;
    }

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    @Override
    public String toString() {
        return "Hotel_Assignment{" +
                "hotel_assignment_id=" + hotel_assignment_id +
                ", created_at=" + created_at +
                ", package_booking_id=" + package_booking_id +
                ", hotel_booking_id=" + hotel_booking_id +
                ", package_id=" + package_id +
                ", item_id=" + item_id +
                '}';
    }
}
