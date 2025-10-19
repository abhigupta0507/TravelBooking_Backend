package com.example.demo.model;

public class IncludeRooms {
    private Integer package_id;
    private Integer hotel_id;
    private Integer room_id;
    private Integer check_in_day;
    private Integer check_out_day;

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

    public IncludeRooms() {}

    public IncludeRooms(Integer package_id, Integer hotel_id, Integer room_id, Integer check_in_day, Integer check_out_day) {
        this.package_id = package_id;
        this.hotel_id = hotel_id;
        this.room_id = room_id;
        this.check_in_day = check_in_day;
        this.check_out_day = check_out_day;
    }

    public Integer getPackage_id() {
        return package_id;
    }

    public void setPackage_id(Integer package_id) {
        this.package_id = package_id;
    }

    public Integer getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(Integer hotel_id) {
        this.hotel_id = hotel_id;
    }

    public Integer getRoom_id() {
        return room_id;
    }

    public void setRoom_id(Integer room_id) {
        this.room_id = room_id;
    }

    @Override
    public String toString() {
        return "IncludeRooms{" +
                "package_id=" + package_id +
                ", hotel_id=" + hotel_id +
                ", room_id=" + room_id +
                ", check_in_day=" + check_in_day +
                ", check_out_day=" + check_out_day +
                '}';
    }
}
