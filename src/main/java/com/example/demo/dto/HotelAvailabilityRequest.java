package com.example.demo.dto;

import org.springframework.stereotype.Repository;

import java.sql.Date;


@Repository
public class HotelAvailabilityRequest {
    private Date check_in_date;
    private Date check_out_date;
    private int hotel_id;
    private int room_id;
    private int required_rooms;

//    public HotelAvailabilityRequest(Date check_in_date, Date check_out_date, int hotel_id, int room_id, int required_rooms) {
//        this.check_in_date = check_in_date;
//        this.check_out_date = check_out_date;
//        this.hotel_id = hotel_id;
//        this.room_id = room_id;
//        this.required_rooms = required_rooms;
//    }

    public Date getCheck_in_date() {
        return check_in_date;
    }

    public void setCheck_in_date(Date check_in_date) {
        this.check_in_date = check_in_date;
    }

    public Date getCheck_out_date() {
        return check_out_date;
    }

    public void setCheck_out_date(Date check_out_date) {
        this.check_out_date = check_out_date;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getRequired_rooms() {
        return required_rooms;
    }

    public void setRequired_rooms(int required_rooms) {
        this.required_rooms = required_rooms;
    }
}
