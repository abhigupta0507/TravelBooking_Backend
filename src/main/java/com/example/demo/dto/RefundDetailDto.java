package com.example.demo.dto;

import com.example.demo.model.*;

/**
 * A DTO that aggregates complete details for a refund,
 * including the original payment and booking information.
 */
public class RefundDetailDto {

    private Refund refundDetails;
    private Payment paymentDetails;
    private Booking bookingDetails;
    private HotelBooking hotelBooking;
    private PackageBooking packageBooking;
    private Customer customer;

    public RefundDetailDto() {}

    public RefundDetailDto(Customer customer, Refund refundDetails, Payment paymentDetails, Booking bookingDetails, HotelBooking hotelBooking) {
        this.refundDetails = refundDetails;
        this.paymentDetails = paymentDetails;
        this.bookingDetails = bookingDetails;
        this.hotelBooking = hotelBooking;
        this.customer = customer;
    }
    public RefundDetailDto(Customer customer, Refund refundDetails, Payment paymentDetails, Booking bookingDetails, PackageBooking packageBooking) {
        this.refundDetails = refundDetails;
        this.paymentDetails = paymentDetails;
        this.bookingDetails = bookingDetails;
        this.packageBooking = packageBooking;
        this.customer = customer;
    }

    // Static factory method for clean creation in the service layer
    public static RefundDetailDto from(Refund refund, Payment payment, Booking booking, HotelBooking hotelBooking, Customer customer) {
        return new RefundDetailDto(customer, refund, payment, booking, hotelBooking);
    }

    public static RefundDetailDto from(Refund refund, Payment payment, Booking booking, PackageBooking packageBooking, Customer customer) {
        return new RefundDetailDto(customer, refund, payment, booking, packageBooking);
    }

    // --- Getters and Setters ---

    public HotelBooking getHotelBooking() {
        return hotelBooking;
    }

    public void setHotelBooking(HotelBooking hotelBooking) {
        this.hotelBooking = hotelBooking;
    }

    public PackageBooking getPackageBooking() {
        return packageBooking;
    }

    public void setPackageBooking(PackageBooking packageBooking) {
        this.packageBooking = packageBooking;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Refund getRefundDetails() { return refundDetails; }
    public void setRefundDetails(Refund refundDetails) { this.refundDetails = refundDetails; }
    public Payment getPaymentDetails() { return paymentDetails; }
    public void setPaymentDetails(Payment paymentDetails) { this.paymentDetails = paymentDetails; }
    public Booking getBookingDetails() { return bookingDetails; }
    public void setBookingDetails(Booking bookingDetails) { this.bookingDetails = bookingDetails; }

    @Override
    public String toString() {
        return "RefundDetailDto{" +
                "refundDetails=" + refundDetails +
                ", paymentDetails=" + paymentDetails +
                ", bookingDetails=" + bookingDetails +
                ", hotelBooking=" + hotelBooking +
                ", packageBooking=" + packageBooking +
                ", customer=" + customer +
                '}';
    }
}
