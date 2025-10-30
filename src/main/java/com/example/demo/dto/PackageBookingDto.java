package com.example.demo.dto;

import com.example.demo.model.Booking;
import com.example.demo.model.HotelBooking;
import com.example.demo.model.PackageBooking;

public class PackageBookingDto extends PackageBooking {
    private Booking parent_booking;

    public PackageBookingDto(){
        super();
    }

    public PackageBookingDto(PackageBooking thePackageBooking){
        super(thePackageBooking);
    }


    public PackageBookingDto(PackageBooking thePackageBooking, Booking theBooking){
        super(thePackageBooking);
        this.parent_booking = theBooking;
    }


    public Booking getParent_booking() {
        return parent_booking;
    }

    public void setParent_booking(Booking parent_booking) {
        this.parent_booking = parent_booking;
    }

    @Override
    public String toString() {
        return "PackageBookingDto{" +
                "parent_booking=" + parent_booking +
                '}';
    }
}
