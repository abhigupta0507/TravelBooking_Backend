package com.example.demo.model;

import java.sql.Timestamp;

public class Booking {

    private Integer booking_id;
    private String booking_type;
    private Timestamp created_at;
    private String booking_status;
    private Integer package_booking_id;
    private Integer hotel_booking_id;
    private Integer payment_id;

    public Booking() {}

    public Booking(Integer booking_id, String booking_type, Timestamp created_at,
                   String booking_status, Integer package_booking_id,
                   Integer hotel_booking_id, Integer payment_id) {
        this.booking_id = booking_id;
        this.booking_type = booking_type;
        this.created_at = created_at;
        this.booking_status = booking_status;
        this.package_booking_id = package_booking_id;
        this.hotel_booking_id = hotel_booking_id;
        this.payment_id = payment_id;
    }

    public Integer getBooking_id() { return booking_id; }
    public void setBooking_id(Integer booking_id) { this.booking_id = booking_id; }

    public String getBooking_type() { return booking_type; }
    public void setBooking_type(String booking_type) { this.booking_type = booking_type; }

    public Timestamp getCreated_at() { return created_at; }
    public void setCreated_at(Timestamp created_at) { this.created_at = created_at; }

    public String getBooking_status() { return booking_status; }
    public void setBooking_status(String booking_status) { this.booking_status = booking_status; }

    public Integer getPackage_booking_id() { return package_booking_id; }
    public void setPackage_booking_id(Integer package_booking_id) { this.package_booking_id = package_booking_id; }

    public Integer getHotel_booking_id() { return hotel_booking_id; }
    public void setHotel_booking_id(Integer hotel_booking_id) { this.hotel_booking_id = hotel_booking_id; }

    public Integer getPayment_id() { return payment_id; }
    public void setPayment_id(Integer payment_id) { this.payment_id = payment_id; }

    @Override
    public String toString() {
        return "Booking{" +
                "booking_id=" + booking_id +
                ", booking_type='" + booking_type + '\'' +
                ", created_at=" + created_at +
                ", booking_status='" + booking_status + '\'' +
                ", package_booking_id=" + package_booking_id +
                ", hotel_booking_id=" + hotel_booking_id +
                ", payment_id=" + payment_id +
                '}';
    }
}
