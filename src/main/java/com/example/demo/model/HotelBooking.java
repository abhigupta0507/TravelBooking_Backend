package com.example.demo.model;

import java.sql.Date;
import java.sql.Timestamp;

public class HotelBooking {

    private Integer booking_id;
    private Date check_in_date;
    private Date check_out_date;
    private Integer no_of_rooms;
    private String room_type;
    private Timestamp booking_date;
    private Integer guest_count;
    private String status;
    private int cost;
    private Integer number_of_nights;
    private Integer hotel_id;
    private Integer room_id;
    private Integer customer_id;

    public HotelBooking() {}

    public HotelBooking(HotelBooking theHotelBooking) {
        this.check_in_date = theHotelBooking.check_in_date;
        this.check_out_date = theHotelBooking.check_out_date;
        this.no_of_rooms = theHotelBooking.no_of_rooms;
        this.room_type = theHotelBooking.room_type;
        this.guest_count = theHotelBooking.guest_count;
        this.status = theHotelBooking.status;
        this.cost = theHotelBooking.cost;
        this.number_of_nights = theHotelBooking.number_of_nights;
        this.hotel_id = theHotelBooking.hotel_id;
        this.room_id = theHotelBooking.room_id;
        this.customer_id = theHotelBooking.customer_id;
        this.booking_id = theHotelBooking.booking_id;
        this.booking_date = theHotelBooking.booking_date;
    }




    public HotelBooking(Date check_in_date, Date check_out_date, Integer no_of_rooms, String room_type, Integer guest_count, String status, int cost, Integer number_of_nights, Integer hotel_id, Integer room_id, Integer customer_id) {
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
        this.no_of_rooms = no_of_rooms;
        this.room_type = room_type;
        this.guest_count = guest_count;
        this.status = status;
        this.cost = cost;
        this.number_of_nights = number_of_nights;
        this.hotel_id = hotel_id;
        this.room_id = room_id;
        this.customer_id = customer_id;
    }

    public HotelBooking(Integer booking_id, Date check_in_date, Date check_out_date, Integer no_of_rooms,
                        String room_type, Timestamp booking_date, Integer guest_count, String status,
                        int cost, Integer number_of_nights, Integer hotel_id,
                        Integer room_id, Integer customer_id) {
        this.booking_id = booking_id;
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
        this.no_of_rooms = no_of_rooms;
        this.room_type = room_type;
        this.booking_date = booking_date;
        this.guest_count = guest_count;
        this.status = status;
        this.cost = cost;
        this.number_of_nights = number_of_nights;
        this.hotel_id = hotel_id;
        this.room_id = room_id;
        this.customer_id = customer_id;
    }

    public Integer getBooking_id() { return booking_id; }
    public void setBooking_id(Integer booking_id) { this.booking_id = booking_id; }

    public Date getCheck_in_date() { return check_in_date; }
    public void setCheck_in_date(Date check_in_date) { this.check_in_date = check_in_date; }

    public Date getCheck_out_date() { return check_out_date; }
    public void setCheck_out_date(Date check_out_date) { this.check_out_date = check_out_date; }

    public Integer getNo_of_rooms() { return no_of_rooms; }
    public void setNo_of_rooms(Integer no_of_rooms) { this.no_of_rooms = no_of_rooms; }

    public String getRoom_type() { return room_type; }
    public void setRoom_type(String room_type) { this.room_type = room_type; }

    public Timestamp getBooking_date() { return booking_date; }
    public void setBooking_date(Timestamp booking_date) { this.booking_date = booking_date; }

    public Integer getGuest_count() { return guest_count; }
    public void setGuest_count(Integer guest_count) { this.guest_count = guest_count; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }

    public Integer getNumber_of_nights() { return number_of_nights; }
    public void setNumber_of_nights(Integer number_of_nights) { this.number_of_nights = number_of_nights; }

    public Integer getHotel_id() { return hotel_id; }
    public void setHotel_id(Integer hotel_id) { this.hotel_id = hotel_id; }

    public Integer getRoom_id() { return room_id; }
    public void setRoom_id(Integer room_id) { this.room_id = room_id; }

    public Integer getCustomer_id() { return customer_id; }
    public void setCustomer_id(Integer customer_id) { this.customer_id = customer_id; }

    @Override
    public String toString() {
        return "HotelBooking{" +
                "booking_id=" + booking_id +
                ", check_in_date=" + check_in_date +
                ", check_out_date=" + check_out_date +
                ", no_of_rooms=" + no_of_rooms +
                ", room_type='" + room_type + '\'' +
                ", booking_date=" + booking_date +
                ", guest_count=" + guest_count +
                ", status='" + status + '\'' +
                ", cost=" + cost +
                ", number_of_nights=" + number_of_nights +
                ", hotel_id=" + hotel_id +
                ", room_id=" + room_id +
                ", customer_id=" + customer_id +
                '}';
    }
}
