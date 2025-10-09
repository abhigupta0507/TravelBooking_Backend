package com.example.demo.dto;

import com.example.demo.model.Hotel;
import com.example.demo.model.RoomType;
import java.util.List;

public class HotelWithRoom {
    private Hotel hotel;
    private List<RoomType> rooms;

    public HotelWithRoom(Hotel hotel, List<RoomType> rooms) {
        this.hotel = hotel;
        this.rooms = rooms;
    }

    public Hotel getHotel() { return hotel; }
    public List<RoomType> getRooms() { return rooms; }
}
