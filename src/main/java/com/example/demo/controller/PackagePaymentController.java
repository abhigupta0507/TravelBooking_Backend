package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.PackageBooking;
import com.example.demo.model.Payment;
import com.example.demo.service.EmailService;
import com.example.demo.service.PackageBookingService;
import com.example.demo.service.PackagePaymentService;
import com.example.demo.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments/packages")
public class PackagePaymentController {

    private PackagePaymentService packagePaymentService;
    private PackageBookingService packageBookingService;
    private JwtUtil jwtUtil;
    private EmailService emailService;

    public PackagePaymentController(PackagePaymentService packagePaymentService,
                                    PackageBookingService packageBookingService,
                                    JwtUtil jwtUtil,
                                    EmailService emailService) {
        this.packagePaymentService = packagePaymentService;
        this.packageBookingService = packageBookingService;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<?>> confirmPackagePayment(
            @RequestBody Map<String, Object> paymentData,
            @RequestHeader("Authorization") String authHeader) {

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
            }

            String token = authHeader.substring(7);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            Integer packageBookingId = (Integer) paymentData.get("package_booking_id");
            String sessionId = (String) paymentData.get("session_id");
            Double amount = ((Number) paymentData.get("amount")).doubleValue();

            // Create payment and update booking status
            Payment payment = packagePaymentService.createPackagePayment(
                    packageBookingId, amount, sessionId, userId
            );

            // Get booking details for email
            PackageBooking booking = packageBookingService.getPackageBookingById(packageBookingId);

            // Send confirmation email
            String email = jwtUtil.getEmailFromToken(token);
            String subject = "Package Booking Confirmed";
            String msgBody = generatePackageConfirmationEmail(booking, userId);

            EmailDetails theEmail = new EmailDetails(email, msgBody, subject);
            emailService.sendSimpleMail(theEmail);

            packageBookingService.confirmPackageBookingStatus(packageBookingId);

            return ResponseEntity.ok(new ApiResponse<>(true, "Payment confirmed successfully", payment));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    private String generatePackageConfirmationEmail(PackageBooking booking, Integer userId) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333; line-height: 1.6;'>" +
                "<div style='max-width: 600px; margin: auto; border: 1px solid #e0e0e0; border-radius: 8px; padding: 20px;'>" +
                "<h2 style='color: #2b6cb0; text-align: center;'>Package Booking Confirmation</h2>" +
                "<p>Dear Guest,</p>" +
                "<p>Your tour package booking is <strong>confirmed</strong>!</p>" +
                "<p>" +
                "<strong>Booking ID:</strong> " + booking.getBooking_id() + "<br>" +
                "<strong>Start Date:</strong> " + booking.getStart_date() + "<br>" +
                "<strong>Number of People:</strong> " + booking.getNumber_of_people() + "<br>" +
                "<strong>Total Cost:</strong> ₹" + booking.getTotal_cost() +
                "</p>" +
                "<p>Keep your Booking ID for future reference.</p>" +
                "<hr style='border: none; border-top: 1px solid #ddd;'/>" +
                "<p style='text-align: center;'>Thank you for choosing <strong>TravelPro</strong>.<br>" +
                "We wish you a wonderful journey!</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}

//class EmailDetails {
//    private String recipient;
//    private String msgBody;
//    private String subject;
//
//    public EmailDetails(String recipient, String msgBody, String subject) {
//        this.recipient = recipient;
//        this.msgBody = msgBody;
//        this.subject = subject;
//    }
//
//    public String getRecipient() { return recipient; }
//    public String getMsgBody() { return msgBody; }
//    public String getSubject() { return subject; }
//}