package com.example.demo.dto;

import com.example.demo.model.Booking;
import com.example.demo.model.Payment;
import com.example.demo.model.Refund;

/**
 * A DTO that aggregates complete details for a refund,
 * including the original payment and booking information.
 */
public class RefundDetailDto {

    private Refund refundDetails;
    private Payment paymentDetails;
    private Booking bookingDetails;

    public RefundDetailDto() {}

    public RefundDetailDto(Refund refundDetails, Payment paymentDetails, Booking bookingDetails) {
        this.refundDetails = refundDetails;
        this.paymentDetails = paymentDetails;
        this.bookingDetails = bookingDetails;
    }

    // Static factory method for clean creation in the service layer
    public static RefundDetailDto from(Refund refund, Payment payment, Booking booking) {
        return new RefundDetailDto(refund, payment, booking);
    }

    // --- Getters and Setters ---
    public Refund getRefundDetails() { return refundDetails; }
    public void setRefundDetails(Refund refundDetails) { this.refundDetails = refundDetails; }
    public Payment getPaymentDetails() { return paymentDetails; }
    public void setPaymentDetails(Payment paymentDetails) { this.paymentDetails = paymentDetails; }
    public Booking getBookingDetails() { return bookingDetails; }
    public void setBookingDetails(Booking bookingDetails) { this.bookingDetails = bookingDetails; }
}
