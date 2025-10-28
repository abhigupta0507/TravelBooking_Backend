package com.example.demo.dto;

import com.example.demo.model.Booking;
import com.example.demo.model.HotelBooking;

public class HotelBookingDto extends HotelBooking {
    private Booking parentBooking;

    public HotelBookingDto(){
        super();
    }

    public HotelBookingDto(HotelBooking theHotelBooking){
        super(theHotelBooking);
    }


    public HotelBookingDto(HotelBooking theHotelBooking, Booking theBooking){
        super(theHotelBooking);
        this.parentBooking = theBooking;
    }



    public Booking getParentBooking() {
        return parentBooking;
    }

    public void setParentBooking(Booking parentBooking) {
        this.parentBooking = parentBooking;
    }

    @Override
    public String toString() {
        return "HotelBookingDto{" +
                "parentBooking=" + parentBooking +
                '}';
    }
}
