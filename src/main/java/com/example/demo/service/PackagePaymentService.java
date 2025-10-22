package com.example.demo.service;

import com.example.demo.dao.PaymentDao;
import com.example.demo.daor.PackageBookingDao;
import com.example.demo.model.Payment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PackagePaymentService {

    private PaymentDao paymentDao;
    private PackageBookingDao packageBookingDao;
    private PackageBookingService packageBookingService;

    public PackagePaymentService(PaymentDao paymentDao,
                                 PackageBookingDao packageBookingDao,
                                 PackageBookingService packageBookingService) {
        this.paymentDao = paymentDao;
        this.packageBookingDao = packageBookingDao;
        this.packageBookingService = packageBookingService;
    }

    @Transactional
    public Payment createPackagePayment(Integer packageBookingId, Double amount,
                                        String sessionId, Integer userId) {
        try {
            // Create payment record
            int paymentId = paymentDao.createPayment(
                    "PACKAGE",
                    BigDecimal.valueOf(amount),
                    "STRIPE",
                    "COMPLETED",
                    sessionId
            );

            // Update package booking status to CONFIRMED
            packageBookingDao.changePackageBookingStatus("CONFIRMED",packageBookingId);
            paymentDao.createPackageBookingRecord("PACKAGE","CONFIRMED",packageBookingId, paymentId);

            // Create booking record


            // Assign guides, transport, and hotels after payment confirmation
            packageBookingService.assignGuidesToPackageBooking(packageBookingId);
            packageBookingService.assignTransportToPackageBooking(packageBookingId);
            packageBookingService.bookHotelsForPackageBooking(packageBookingId, userId);

            // Return the payment
            return paymentDao.getPaymentById(paymentId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process package payment: " + e.getMessage(), e);
        }
    }

//    private void updatePackageBookingStatus(Integer packageBookingId, String status) {
//        String sql = "UPDATE Package_Booking SET status = ? WHERE booking_id = ?";
//        // Assuming you have access to jdbcTemplate through PaymentDao or inject it
//        // For now, we'll create a method in PackageBookingDao
//    }



    public Payment getPaymentById(Integer paymentId) {
        try {
            return paymentDao.getPaymentById(paymentId);
        } catch (Exception e) {
            throw new RuntimeException("Payment not found", e);
        }
    }
}