package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.HotelReview;
import com.example.demo.model.HotelBooking;
import com.example.demo.service.AuthService;
import com.example.demo.service.HotelReviewService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/hotel_review")
//@CrossOrigin(origins = "*")
public class HotelReviewController {

    @Autowired
    private HotelReviewService hotelReviewService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    // ✅ GET all reviews for a hotel (public)
    @GetMapping("/{hotelId}")
    public ResponseEntity<ApiResponse<List<HotelReview>>> getReviewsByHotel(@PathVariable Integer hotelId) {
        try {
            List<HotelReview> reviews = hotelReviewService.getReviewsByHotelId(hotelId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Reviews fetched successfully", reviews));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // ✅ GET specific review for a hotel (public)
    @GetMapping("/{hotelId}/{reviewId}")
    public ResponseEntity<ApiResponse<HotelReview>> getReviewByHotelAndReviewId(
            @PathVariable Integer hotelId,
            @PathVariable Integer reviewId) {

        try {
            HotelReview review = hotelReviewService.getReviewByHotelIdAndReviewId(hotelId, reviewId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Review fetched successfully", review));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // ✅ GET review by bookingId (CUSTOMER only)
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<HotelReview>> getReviewByBooking(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer bookingId) {

        try {
            String token = authHeader.substring(7);
            Integer userId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);

            if (!Objects.equals(userType, "CUSTOMER")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Only customers can view their booking reviews", null));
            }

            HotelReview review = hotelReviewService.getReviewByBookingId(bookingId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Review fetched successfully", review));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // ✅ POST review for a booking (CUSTOMER only)
    @PostMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<Integer>> addReview(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer bookingId,
            @RequestBody Map<String, Object> payload) {

        try {
            String token = authHeader.substring(7);
            Integer userId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);

            if (!Objects.equals(userType, "CUSTOMER")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Only customers can add reviews", null));
            }

            // Extract review fields
            HotelReview review = new HotelReview();
            review.setOverall_rating(new java.math.BigDecimal(payload.get("overall_rating").toString()));
            review.setCleanliness_rating(new java.math.BigDecimal(payload.get("cleanliness_rating").toString()));
            review.setReview_title((String) payload.get("review_title"));
            review.setReview_body((String) payload.get("review_body"));
            // stay_date is automatically set in DAO to checkout_date

            Integer reviewId = hotelReviewService.createHotelReview(review, bookingId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Review added successfully", reviewId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // ✅ DELETE review (CUSTOMER only)
    @DeleteMapping("/booking/{bookingId}/{reviewId}")
    public ResponseEntity<ApiResponse<Integer>> deleteReview(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer bookingId,
            @PathVariable Integer reviewId) {

        try {
            String token = authHeader.substring(7);
            Integer userId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);

            if (!Objects.equals(userType, "CUSTOMER")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Only customers can delete their reviews", null));
            }

            int deleted = hotelReviewService.deleteHotelReview(bookingId, reviewId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Review deleted successfully", deleted));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
