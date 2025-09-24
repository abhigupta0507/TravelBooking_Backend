package com.example.demo.model;

public class IncludeRooms {
    private Integer package_id;
    private Integer hotel_id;
    private Integer room_id;

    public IncludeRooms() {}

    public IncludeRooms(Integer package_id, Integer hotel_id, Integer room_id) {
        this.package_id = package_id;
        this.hotel_id = hotel_id;
        this.room_id = room_id;
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
        return "Include_Rooms{" +
                "package_id=" + package_id +
                ", hotel_id=" + hotel_id +
                ", room_id=" + room_id +
                '}';
    }
}
