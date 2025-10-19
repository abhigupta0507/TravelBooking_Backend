package com.example.demo.controller;

import com.example.demo.model.PackageReview;
import com.example.demo.service.PackageReviewService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/package-reviews")
public class PackageReviewController {

    private final PackageReviewService reviewService;
    @Autowired
    private JwtUtil jwtUtil;

    public PackageReviewController(PackageReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{packageBookingId}")
    public ResponseEntity<?> createPackageReview(
            @PathVariable Integer packageBookingId,
            @RequestBody PackageReview review,
            @RequestHeader("Authorization") String authHeader) {

        // Step 1: Check Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token found");
        }

        try {
            // Step 2: Extract token details
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            // Step 3: Only CUSTOMERS can add reviews
            if (!Objects.equals(userType, "CUSTOMER")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only customers are allowed to post package reviews.");
            }

            // Step 4: Validate if the booking belongs to this user
            Integer bookingCustomerId = reviewService.getCustomerIdByBookingId(packageBookingId);

            if (bookingCustomerId == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No booking found for this package_booking_id.");
            }

            if (!Objects.equals(bookingCustomerId, userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You are not authorized to review this package booking.");
            }

            // package booking must be finished

            // no review exists already

            // Step 5: Proceed to create the review
            review.setPackage_booking_id(packageBookingId);
            review.setCreated_at(LocalDateTime.now());
            reviewService.addReview(review);

            return ResponseEntity.status(HttpStatus.CREATED).body("Package review added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating review: " + e.getMessage());
        }
    }


    // ✅ GET /api/package-reviews/package/{package_id}
    @GetMapping("/package/{package_id}")
    public ResponseEntity<List<PackageReview>> getReviewsByPackageId(@PathVariable Integer package_id) {
        List<PackageReview> reviews = reviewService.getReviewsByPackageId(package_id);
        return ResponseEntity.ok(reviews);
    }

    // ✅ GET /api/package-reviews/user/{user_id}
    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<PackageReview>> getReviewsByUserId(@PathVariable Integer user_id) {
        List<PackageReview> reviews = reviewService.getReviewsByUserId(user_id);
        return ResponseEntity.ok(reviews);
    }

    // ✅ DELETE /api/package-reviews/{review_id}
    @DeleteMapping("/{review_id}")
    public ResponseEntity<String> deleteReview(@PathVariable Integer review_id) {
        reviewService.deleteReview(review_id);
        return ResponseEntity.ok("Review deleted successfully!");
    }

    // take list of package booking ids and return list of booleans, if the corresponding booking ids have
    // review ids or not
}
