package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.RefundRequestDto;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.model.*;
import com.example.demo.service.EmailService;
import com.example.demo.service.HotelBookingService;
import com.example.demo.service.HotelService;
import com.example.demo.service.PaymentService;
import com.example.demo.util.JwtUtil;
import com.google.api.gax.rpc.AlreadyExistsException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private PaymentService paymentService;
    private JwtUtil jwtUtil;
    private EmailService emailService;
    private HotelService hotelService;
    private HotelBookingService hotelBookingService;

    public PaymentController(PaymentService paymentService, HotelBookingService hotelBookingService,JwtUtil jwtUtil,HotelService hotelService, EmailService emailService) {
        this.paymentService = paymentService;
        this.jwtUtil = jwtUtil;
        this.hotelBookingService=hotelBookingService;
        this.hotelService=hotelService;
        this.emailService=emailService;
    }

    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<?>> confirmPayment(
            @RequestBody Map<String, Object> paymentData,
            @RequestHeader("Authorization") String authHeader) {

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
            }

            String token = authHeader.substring(7);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            Integer hotelBookingId = (Integer) paymentData.get("hotel_booking_id");
            String sessionId = (String) paymentData.get("session_id");
            Double amount = ((Number) paymentData.get("amount")).doubleValue();

            Payment payment = paymentService.createHotelPayment(hotelBookingId, amount, sessionId);

            HotelBooking theBooking = hotelBookingService.getHotelBooking(hotelBookingId);
            Hotel theHotel = hotelService.findHotelById(theBooking.getHotel_id());
            RoomType theRoom = hotelService.getRoomFromId(theBooking.getHotel_id(),theBooking.getRoom_id());

            String email = jwtUtil.getEmailFromToken(token);
            String subject = "Hotel Booking Confirmed";
            String msgBody =
                    "<html>" +
                            "<body style='font-family: Arial, sans-serif; color: #333; line-height: 1.6;'>" +
                            "<div style='max-width: 600px; margin: auto; border: 1px solid #e0e0e0; border-radius: 8px; padding: 20px;'>" +
                            "<h2 style='color: #2b6cb0; text-align: center;'>Booking Confirmation</h2>" +
                            "<p>Dear Guest,</p>" +
                            "<p>Your booking is <strong>confirmed</strong> at <strong>" + theHotel.getName() + "</strong>.</p>" +
                            "<p>" +
                            "<strong>Check-in:</strong> " + theBooking.getCheck_in_date() + "<br>" +
                            "<strong>Check-out:</strong> " + theBooking.getCheck_out_date() + "<br>" +
                            "<strong>Room Type:</strong> " + theRoom.getType() + "<br>" +
                            "<strong>Number of Rooms:</strong> " + theBooking.getNo_of_rooms() + "<br>" +
                            "<strong>Guests:</strong> " + theBooking.getGuest_count() +
                            "</p>" +
                            "<p>Your Booking ID: <strong>" + theBooking.getBooking_id() + "</strong>. Keep it for future reference.</p>" +
                            "<hr style='border: none; border-top: 1px solid #ddd;'/>" +
                            "<p style='text-align: center;'>Thank you for choosing <strong>TravelPro</strong>.<br>" +
                            "We wish you a pleasant stay!</p>" +
                            "</div>" +
                            "</body>" +
                            "</html>";

            EmailDetails theEmail=new EmailDetails(email,msgBody,subject);
            String status = emailService.sendSimpleMail(theEmail);

            return ResponseEntity.ok(new ApiResponse<>(true, "Payment confirmed successfully", payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<?>> getPaymentById(
            @PathVariable Integer paymentId,
            @RequestHeader("Authorization") String authHeader) {

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
            }

            Payment payment = paymentService.getPaymentById(paymentId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Payment retrieved successfully", payment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }


    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<ApiResponse<Refund>> requestRefund(
            @PathVariable Integer paymentId,
            @Valid @RequestBody RefundRequestDto refundDto,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            // The DTO only carries the reason. The service calculates the rest.
            Refund refundRequest = new Refund();
            refundRequest.setPayment_id(paymentId);
            refundRequest.setRefund_reason(refundDto.getRefund_reason());

            Refund createdRefund = paymentService.createRefundRequest(refundRequest, userId);

            ApiResponse<Refund> response = new ApiResponse<>(true, "Refund request created successfully.", createdRefund);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT) // 409 Conflict is better for existing resources
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN) // 403 Forbidden for auth failures
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "An error occurred: " + e.getMessage(), null));
        }
    }
}