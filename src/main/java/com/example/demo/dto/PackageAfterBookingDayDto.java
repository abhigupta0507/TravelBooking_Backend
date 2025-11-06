package com.example.demo.dto;

import com.example.demo.model.*;

public class PackageAfterBookingDayDto {
    private GuideAssignment guideAssignment;
    private Guide guide;
    private TransportAssignment transportAssignment;
    private Transport transport;
    private HotelAssignment hotelAssignment;
    private HotelBooking hotelBooking;
    private Hotel hotel;
    private RoomType roomType;

    public PackageAfterBookingDayDto(RoomType roomType, Hotel hotel, HotelBooking hotelBooking, HotelAssignment hotelAssignment, Transport transport, TransportAssignment transportAssignment, Guide guide, GuideAssignment guideAssignment) {
        this.roomType = roomType;
        this.hotel = hotel;
        this.hotelBooking = hotelBooking;
        this.hotelAssignment = hotelAssignment;
        this.transport = transport;
        this.transportAssignment = transportAssignment;
        this.guide = guide;
        this.guideAssignment = guideAssignment;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public HotelBooking getHotelBooking() {
        return hotelBooking;
    }

    public void setHotelBooking(HotelBooking hotelBooking) {
        this.hotelBooking = hotelBooking;
    }

    public HotelAssignment getHotelAssignment() {
        return hotelAssignment;
    }

    public void setHotelAssignment(HotelAssignment hotelAssignment) {
        this.hotelAssignment = hotelAssignment;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public TransportAssignment getTransportAssignment() {
        return transportAssignment;
    }

    public void setTransportAssignment(TransportAssignment transportAssignment) {
        this.transportAssignment = transportAssignment;
    }

    public Guide getGuide() {
        return guide;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
    }

    public GuideAssignment getGuideAssignment() {
        return guideAssignment;
    }

    public void setGuideAssignment(GuideAssignment guideAssignment) {
        this.guideAssignment = guideAssignment;
    }

    @Override
    public String toString() {
        return "PackageAfterBookingDayDto{" +
                "guideAssignment=" + guideAssignment +
                ", guide=" + guide +
                ", transportAssignment=" + transportAssignment +
                ", transport=" + transport +
                ", hotelAssignment=" + hotelAssignment +
                ", hotelBooking=" + hotelBooking +
                ", hotel=" + hotel +
                ", roomType=" + roomType +
                '}';
    }
}
