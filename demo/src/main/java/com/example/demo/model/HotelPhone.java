package com.example.demo.model;

public class HotelPhone {

    private Integer hotel_id;
    private String phone;

    public HotelPhone() {}

    public HotelPhone(Integer hotel_id, String phone) {
        this.hotel_id = hotel_id;
        this.phone = phone;
    }

    public Integer getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(Integer hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "HotelPhone{" +
                "hotel_id=" + hotel_id +
                ", phone='" + phone + '\'' +
                '}';
    }
}
