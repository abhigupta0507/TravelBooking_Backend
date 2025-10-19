package com.example.demo.dto;

import com.example.demo.dao.HotelDAO;
import com.example.demo.dao.PackageDAO;
import com.example.demo.model.Hotel;
import com.example.demo.model.RoomType;

import java.math.BigDecimal;

public class IncludeRoomDetailDto {
    private Integer hotel_id;
    private String hotel_name;
    private String hotel_street;
    private String hotel_city;
    private String hotel_state;
    private String hotel_pin;
    private BigDecimal hotel_rating;
    private Integer hotel_total_rooms;
    private Integer hotel_vendor_id;
    private String hotel_primary_phone;
    private String hotel_primary_email;
    private String hotel_image_url;

    private Integer room_id;
    private Boolean room_balcony_available;
    private int room_cost_per_night;
    private String room_type;
    private String room_bed_type;
    private Integer room_max_capacity;
    private Integer room_total_rooms;
    private Integer check_in_day;
    private Integer check_out_day;


    public IncludeRoomDetailDto() {

    }


    public IncludeRoomDetailDto(Hotel hotel, RoomType roomType, Integer check_in_day, Integer check_out_day){
        this.hotel_city = hotel.getCity();
        this.hotel_id = hotel.getHotel_id();
        this.hotel_name = hotel.getName();
        this.hotel_street = hotel.getStreet();
        this.hotel_city = hotel.getCity();
        this.hotel_state = hotel.getState();
        this.hotel_pin = hotel.getPin();
        this.hotel_rating = hotel.getRating();
        this.hotel_total_rooms = hotel.getTotal_rooms();
        this.hotel_vendor_id = hotel.getVendor_id();
        this.hotel_primary_phone = hotel.getPrimary_phone();
        this.hotel_primary_email = hotel.getPrimary_email();
        this.hotel_image_url = hotel.getImage_url();
        this.room_id = roomType.getRoom_id();
        this.room_balcony_available = roomType.getBalcony_available();
        this.room_cost_per_night = roomType.getCost_per_night();
        this.room_type = roomType.getType();
        this.room_bed_type = roomType.getBed_type();
        this.room_max_capacity = roomType.getMax_capacity();
        this.room_total_rooms = roomType.getTotal_rooms();
        this.check_in_day = check_in_day;
        this.check_out_day = check_out_day;
    }


    public Integer getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(Integer hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public String getHotel_street() {
        return hotel_street;
    }

    public void setHotel_street(String hotel_street) {
        this.hotel_street = hotel_street;
    }

    public String getHotel_city() {
        return hotel_city;
    }

    public void setHotel_city(String hotel_city) {
        this.hotel_city = hotel_city;
    }

    public String getHotel_state() {
        return hotel_state;
    }

    public void setHotel_state(String hotel_state) {
        this.hotel_state = hotel_state;
    }

    public String getHotel_pin() {
        return hotel_pin;
    }

    public void setHotel_pin(String hotel_pin) {
        this.hotel_pin = hotel_pin;
    }

    public BigDecimal getHotel_rating() {
        return hotel_rating;
    }

    public void setHotel_rating(BigDecimal hotel_rating) {
        this.hotel_rating = hotel_rating;
    }

    public Integer getHotel_total_rooms() {
        return hotel_total_rooms;
    }

    public void setHotel_total_rooms(Integer hotel_total_rooms) {
        this.hotel_total_rooms = hotel_total_rooms;
    }

    public Integer getHotel_vendor_id() {
        return hotel_vendor_id;
    }

    public void setHotel_vendor_id(Integer hotel_vendor_id) {
        this.hotel_vendor_id = hotel_vendor_id;
    }

    public String getHotel_primary_phone() {
        return hotel_primary_phone;
    }

    public void setHotel_primary_phone(String hotel_primary_phone) {
        this.hotel_primary_phone = hotel_primary_phone;
    }

    public String getHotel_primary_email() {
        return hotel_primary_email;
    }

    public void setHotel_primary_email(String hotel_primary_email) {
        this.hotel_primary_email = hotel_primary_email;
    }

    public String getHotel_image_url() {
        return hotel_image_url;
    }

    public void setHotel_image_url(String hotel_image_url) {
        this.hotel_image_url = hotel_image_url;
    }

    public Integer getRoom_id() {
        return room_id;
    }

    public void setRoom_id(Integer room_id) {
        this.room_id = room_id;
    }

    public Boolean getRoom_balcony_available() {
        return room_balcony_available;
    }

    public void setRoom_balcony_available(Boolean room_balcony_available) {
        this.room_balcony_available = room_balcony_available;
    }

    public int getRoom_cost_per_night() {
        return room_cost_per_night;
    }

    public void setRoom_cost_per_night(int room_cost_per_night) {
        this.room_cost_per_night = room_cost_per_night;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public String getRoom_bed_type() {
        return room_bed_type;
    }

    public void setRoom_bed_type(String room_bed_type) {
        this.room_bed_type = room_bed_type;
    }

    public Integer getRoom_max_capacity() {
        return room_max_capacity;
    }

    public void setRoom_max_capacity(Integer room_max_capacity) {
        this.room_max_capacity = room_max_capacity;
    }

    public Integer getRoom_total_rooms() {
        return room_total_rooms;
    }

    public void setRoom_total_rooms(Integer room_total_rooms) {
        this.room_total_rooms = room_total_rooms;
    }

    public Integer getCheck_in_day() {
        return check_in_day;
    }

    public void setCheck_in_day(Integer check_in_day) {
        this.check_in_day = check_in_day;
    }

    public Integer getCheck_out_day() {
        return check_out_day;
    }

    public void setCheck_out_day(Integer check_out_day) {
        this.check_out_day = check_out_day;
    }

    @Override
    public String toString() {
        return "IncludeRoomDetailDto{" +
                "hotel_id=" + hotel_id +
                ", hotel_name='" + hotel_name + '\'' +
                ", hotel_street='" + hotel_street + '\'' +
                ", hotel_city='" + hotel_city + '\'' +
                ", hotel_state='" + hotel_state + '\'' +
                ", hotel_pin='" + hotel_pin + '\'' +
                ", hotel_rating=" + hotel_rating +
                ", hotel_total_rooms=" + hotel_total_rooms +
                ", hotel_vendor_id=" + hotel_vendor_id +
                ", hotel_primary_phone='" + hotel_primary_phone + '\'' +
                ", hotel_primary_email='" + hotel_primary_email + '\'' +
                ", hotel_image_url='" + hotel_image_url + '\'' +
                ", room_id=" + room_id +
                ", room_balcony_available=" + room_balcony_available +
                ", room_cost_per_night=" + room_cost_per_night +
                ", room_type='" + room_type + '\'' +
                ", room_bed_type='" + room_bed_type + '\'' +
                ", room_max_capacity=" + room_max_capacity +
                ", room_total_rooms=" + room_total_rooms +
                ", check_in_day=" + check_in_day +
                ", check_out_day=" + check_out_day +
                '}';
    }
}
