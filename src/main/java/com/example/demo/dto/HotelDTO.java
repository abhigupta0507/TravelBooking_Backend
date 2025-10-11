package com.example.demo.dto;

import java.math.BigDecimal;

public class HotelDTO {
    private int hotelId;
    private String name;
    private String street;
    private String city;
    private String state;
    private String pin;
    private BigDecimal rating;
    private int totalRooms;
    private int vendorId;
    private String primary_phone;
    private String primary_email;
    private String image_url;

    public HotelDTO() {
    }

    public HotelDTO(int hotelId, String name, String street, String city, String state, String pin, BigDecimal rating, int totalRooms, int vendorId,  String primary_phone, String primary_email,
                    String image_url) {
        this.hotelId = hotelId;
        this.name = name;
        this.street = street;
        this.city = city;
        this.state = state;
        this.pin = pin;
        this.rating = rating;
        this.totalRooms = totalRooms;
        this.vendorId = vendorId;
        this.primary_phone = primary_phone;
        this.primary_email = primary_email;
        this.image_url = image_url;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
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

    public int getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getPrimary_phone() {return primary_phone;}
    public String getPrimary_email() {return primary_email;}
    public void setPrimary_phone(String primary_phone) {this.primary_phone = primary_phone;}
    public void setPrimary_email(String primary_email) {this.primary_email = primary_email;}
    public String getImage_url() {return image_url;}
    public void setImage_url(String image_url) {this.image_url = image_url;}

    @Override
    public String toString() {
        return "HotelDTO{" +
                "hotelId=" + hotelId +
                ", name='" + name + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", pin='" + pin + '\'' +
                ", rating=" + rating +
                ", totalRooms=" + totalRooms +
                ", vendorId=" + vendorId +
                ", primary_phone='" + primary_phone + '\'' +
                ", primary_email='" + primary_email + '\'' +
                ", image_url='" + image_url + '\'' +
                '}';
    }
}
