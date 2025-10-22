package com.example.demo.service;

import com.example.demo.dao.HotelBookingDao;
import com.example.demo.dao.PaymentDao;
import com.example.demo.exception.AlreadyExistsException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.model.Booking;
import com.example.demo.model.HotelBooking;
import com.example.demo.model.Payment;
import com.example.demo.model.Refund;
import com.example.demo.util.AuthorizationService;
import com.example.demo.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private PaymentDao paymentDao;
    private HotelBookingDao hotelBookingDao;
    private AuthorizationService authorizationService;
    private JwtUtil jwtUtil;

    public PaymentService(PaymentDao paymentDao,JwtUtil jwtUtil,HotelBookingDao hotelBookingDao,AuthorizationService authorizationService) {
        this.paymentDao = paymentDao;
        this.authorizationService = authorizationService;
        this.hotelBookingDao = hotelBookingDao;
        this.jwtUtil=jwtUtil;
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


    @Transactional
    public Refund createRefundRequest(Refund refundRequest, Integer userId) {
        // Step 1: Check if a finalized refund already exists.
        if (paymentDao.hasFinalizedRefund(refundRequest.getPayment_id())) {
            throw new AlreadyExistsException("A completed or processing refund for this payment already exists.");
        }

        // Step 2: Fetch related booking and payment details.
        Booking theBooking = paymentDao.getBookingByPaymentId(refundRequest.getPayment_id());
        Payment thePayment = paymentDao.getPaymentById(refundRequest.getPayment_id());

        // Step 3: Verify that the user requesting the refund is the one who made the booking.
        if ("HOTEL".equals(theBooking.getBooking_type())) {
            HotelBooking hotelBooking = hotelBookingDao.getHotelBookingById(theBooking.getHotel_booking_id());
            if (!hotelBooking.getCustomer_id().equals(userId)) {
                throw new UnauthorizedException("You are not authorized to request a refund for this booking.");
            }
        } // Add similar logic for "PACKAGE" bookings here...

        // Step 4: Calculate refund amounts using BigDecimal for precision.
        BigDecimal processingCharge = thePayment.getAmount().multiply(new BigDecimal("0.10"));
        BigDecimal refundAmount = thePayment.getAmount().subtract(processingCharge);

        // Step 5: Populate the refund object with the calculated and initial data.
        refundRequest.setRefund_amount(refundAmount);
        refundRequest.setProcessing_charges(processingCharge);
        refundRequest.setRefund_status("PROCESSING");
        refundRequest.setReference(null);
        refundRequest.setProcessed_at(LocalDateTime.now());

        // Step 6: Call the DAO to create the refund and get the new ID.
        Integer newRefundId = paymentDao.createRefundRequest(refundRequest);
        refundRequest.setRefund_id(newRefundId);

        // Step 7: Return the fully populated refund object.
        return refundRequest;
    }

    /**
     * Retrieves all refunds after verifying the user is an admin.
     * @param authHeader The authorization token.
     * @return A list of all refunds.
     */
    public List<Refund> getAllRefunds(String authHeader) {
        authorizationService.verifyAdminStaff(authHeader);
        return paymentDao.findAllRefunds();
    }

    /**
     * Retrieves all refunds of a specific status after verifying the user is an admin.
     * @param authHeader The authorization token.
     * @param status The status to filter by.
     * @return A list of filtered refunds.
     */
    public List<Refund> getRefundsByStatus(String authHeader, String status) {
        authorizationService.verifyAdminStaff(authHeader);
        return paymentDao.findAllByStatus(status);
    }

}