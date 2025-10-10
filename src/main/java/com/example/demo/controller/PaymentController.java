package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.Hotel;
import com.example.demo.model.HotelBooking;
import com.example.demo.model.Payment;
import com.example.demo.model.RoomType;
import com.example.demo.service.EmailService;
import com.example.demo.service.HotelBookingService;
import com.example.demo.service.HotelService;
import com.example.demo.service.PaymentService;
import com.example.demo.util.JwtUtil;
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
}