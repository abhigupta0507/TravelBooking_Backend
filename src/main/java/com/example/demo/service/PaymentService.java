package com.example.demo.service;

import com.example.demo.dao.HotelBookingDao;
import com.example.demo.dao.PaymentDao;
import com.example.demo.dto.RefundDetailDto;
import com.example.demo.dto.UpdateRefundRequestDto;
import com.example.demo.exception.AlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
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

    /**
     * Updates the status and reference of a refund.
     * @return The fully updated Refund object.
     * @throws SecurityException if the user is not an admin.
     * @throws ResourceNotFoundException if the refund does not exist.
     * @throws IllegalStateException if the refund is already in a final state.
     */
    public Refund updateRefundStatus(String authHeader, Integer paymentId, Integer refundId, UpdateRefundRequestDto requestDto) {
        // Step 1: Authorize the user as an admin.
        authorizationService.verifyAdminStaff(authHeader);

        // Step 2: Find the refund.
        Refund existingRefund = paymentDao.findRefundById(paymentId, refundId);
        if (existingRefund == null) {
            throw new ResourceNotFoundException("Refund with Payment ID " + paymentId + " and Refund ID " + refundId + " not found.");
        }

        // Step 3: Business Rule - Prevent updating a finalized refund.
        String currentStatus = existingRefund.getRefund_status();
        if ("COMPLETED".equalsIgnoreCase(requestDto.getStatus())) {
            if (requestDto.getReference() == null || requestDto.getReference().isBlank()) {
                throw new IllegalArgumentException("A transaction reference must be provided when marking a refund as COMPLETED.");
            }
        }

        if ("COMPLETED".equalsIgnoreCase(currentStatus) || "FAILED".equalsIgnoreCase(currentStatus)) {
            throw new IllegalStateException("Cannot update a refund that is already " + currentStatus + ".");
        }

        // Step 4: Apply the updates from the DTO.
        existingRefund.setRefund_status(requestDto.getStatus().toUpperCase());
        existingRefund.setReference(requestDto.getReference());

        // Step 5: Persist the changes.
        paymentDao.updateRefund(existingRefund);

        // Step 6: Return the updated object.
        return existingRefund;
    }

    public RefundDetailDto getRefundDetails(String authHeader, Integer paymentId, Integer refundId) {
        // 1. Fetch all required data pieces from the DAO.
        authorizationService.verifyAdminStaff(authHeader);
        Refund refund = paymentDao.findRefundById(paymentId, refundId);
        if (refund == null) {
            throw new ResourceNotFoundException("Refund with ID " + refundId + " for payment " + paymentId + " not found.");
        }

        Payment payment = paymentDao.findPaymentById(paymentId);
        Booking booking = paymentDao.getBookingByPaymentId(paymentId);

        if (payment == null || booking == null) {
            throw new ResourceNotFoundException("Associated payment or booking details not found.");
        }

        // 2. Perform Authorization Check: User must be the owner OR an admin.
        String token = authHeader.substring(7);
        Integer requestUserId = jwtUtil.getUserIdFromToken(token);
        Integer ownerCustomerId = paymentDao.getCustomerIdForBooking(booking);

        // Check if the user is the owner
        boolean isOwner = ownerCustomerId != null && ownerCustomerId.equals(requestUserId);

        // If not the owner, check if they are an admin.
        if (!isOwner) {
            try {
                // This will throw a SecurityException if the user is not a valid admin.
                authorizationService.verifyAdminStaff(authHeader);
            } catch (SecurityException e) {
                // If the check fails, the user is neither the owner nor an admin.
                throw new UnauthorizedException("You are not authorized to view these refund details.");
            }
        }

        // 3. If authorization passes, assemble and return the DTO.
        return RefundDetailDto.from(refund, payment, booking);
    }

}