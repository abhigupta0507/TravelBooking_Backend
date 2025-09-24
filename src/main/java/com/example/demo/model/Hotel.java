package com.example.demo.model;

import java.math.BigDecimal;

public class Hotel {

    private Integer hotel_id;
    private String name;
    private String street;
    private String city;
    private String state;
    private String pin;
    private BigDecimal rating;
    private Integer total_rooms;
    private Integer vendor_id;

    public Hotel() {}

    public Hotel(Integer hotel_id, String name, String street, String city, String state, String pin,
                 BigDecimal rating, Integer total_rooms, Integer vendor_id) {
        this.hotel_id = hotel_id;
        this.name = name;
        this.street = street;
        this.city = city;
        this.state = state;
        this.pin = pin;
        this.rating = rating;
        this.total_rooms = total_rooms;
        this.vendor_id = vendor_id;
    }

    public Integer getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(Integer hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getTotal_rooms() {
        return total_rooms;
    }

    public void setTotal_rooms(Integer total_rooms) {
        this.total_rooms = total_rooms;
    }

    public Integer getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(Integer vendor_id) {
        this.vendor_id = vendor_id;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "hotel_id=" + hotel_id +
                ", name='" + name + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", pin='" + pin + '\'' +
                ", rating=" + rating +
                ", total_rooms=" + total_rooms +
                ", vendor_id=" + vendor_id +
                '}';
    }
}
