package com.example.demo.service;

import com.example.demo.dao.PaymentDao;
import com.example.demo.model.Payment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private PaymentDao paymentDao;

    public PaymentService(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }

    @Transactional
    public Payment createHotelPayment(Integer hotelBookingId, Double amount, String sessionId) {
        try {
            // Create payment record
            int paymentId = paymentDao.createPayment(
                    "HOTEL",
                    BigDecimal.valueOf(amount),
                    "STRIPE",
                    "COMPLETED",
                    sessionId
            );

            // Update hotel booking status to CONFIRMED
            paymentDao.updateHotelBookingStatus(hotelBookingId, "CONFIRMED");

            // Create booking record
            paymentDao.createBooking("HOTEL", "CONFIRMED", hotelBookingId, paymentId);

            // Return the payment
            return paymentDao.getPaymentById(paymentId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process payment: " + e.getMessage(), e);
        }
    }

    public Payment getPaymentById(Integer paymentId) {
        try {
            return paymentDao.getPaymentById(paymentId);
        } catch (Exception e) {
            throw new RuntimeException("Payment not found", e);
        }
    }
}