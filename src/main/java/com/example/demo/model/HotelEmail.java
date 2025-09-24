package com.example.demo.model;

public class HotelEmail {

    private Integer hotel_id;
    private String email;

    public HotelEmail() {}

    public HotelEmail(Integer hotel_id, String email) {
        this.hotel_id = hotel_id;
        this.email = email;
    }

    public Integer getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(Integer hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "HotelEmail{" +
                "hotel_id=" + hotel_id +
                ", email='" + email + '\'' +
                '}';
    }
}
