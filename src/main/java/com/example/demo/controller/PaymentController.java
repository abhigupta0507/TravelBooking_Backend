package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.Payment;
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

    public PaymentController(PaymentService paymentService, JwtUtil jwtUtil) {
        this.paymentService = paymentService;
        this.jwtUtil = jwtUtil;
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

            //System.out.println("Reached this request...");
            Integer hotelBookingId = (Integer) paymentData.get("hotel_booking_id");
            //System.out.println("Reached here Hotel Booking Id := "+hotelBookingId);
            String sessionId = (String) paymentData.get("session_id");
            //System.out.println("Session Id : =" + sessionId);
            Double amount = ((Number) paymentData.get("amount")).doubleValue();
            //System.out.println("amount:==  "+amount);

            // Create payment and update booking
            Payment payment = paymentService.createHotelPayment(hotelBookingId, amount, sessionId);

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