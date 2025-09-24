package com.example.demo.model;

import java.math.BigDecimal;

public class RoomType {

    private Integer hotel_id;
    private Integer room_id;
    private Boolean balcony_available;
    private BigDecimal cost_per_night;
    private String type;
    private String bed_type;
    private Integer max_capacity;
    private Integer number_of_rooms_available;

    public RoomType() {}

    public RoomType(Integer hotel_id, Integer room_id, Boolean balcony_available, BigDecimal cost_per_night,
                    String type, String bed_type, Integer max_capacity, Integer number_of_rooms_available) {
        this.hotel_id = hotel_id;
        this.room_id = room_id;
        this.balcony_available = balcony_available;
        this.cost_per_night = cost_per_night;
        this.type = type;
        this.bed_type = bed_type;
        this.max_capacity = max_capacity;
        this.number_of_rooms_available = number_of_rooms_available;
    }

    public Integer getHotel_id() { return hotel_id; }
    public void setHotel_id(Integer hotel_id) { this.hotel_id = hotel_id; }

    public Integer getRoom_id() { return room_id; }
    public void setRoom_id(Integer room_id) { this.room_id = room_id; }

    public Boolean getBalcony_available() { return balcony_available; }
    public void setBalcony_available(Boolean balcony_available) { this.balcony_available = balcony_available; }

    public BigDecimal getCost_per_night() { return cost_per_night; }
    public void setCost_per_night(BigDecimal cost_per_night) { this.cost_per_night = cost_per_night; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getBed_type() { return bed_type; }
    public void setBed_type(String bed_type) { this.bed_type = bed_type; }

    public Integer getMax_capacity() { return max_capacity; }
    public void setMax_capacity(Integer max_capacity) { this.max_capacity = max_capacity; }

    public Integer getNumber_of_rooms_available() { return number_of_rooms_available; }
    public void setNumber_of_rooms_available(Integer number_of_rooms_available) { this.number_of_rooms_available = number_of_rooms_available; }

    @Override
    public String toString() {
        return "RoomType{" +
                "hotel_id=" + hotel_id +
                ", room_id=" + room_id +
                ", balcony_available=" + balcony_available +
                ", cost_per_night=" + cost_per_night +
                ", type='" + type + '\'' +
                ", bed_type='" + bed_type + '\'' +
                ", max_capacity=" + max_capacity +
                ", number_of_rooms_available=" + number_of_rooms_available +
                '}';
    }
}
