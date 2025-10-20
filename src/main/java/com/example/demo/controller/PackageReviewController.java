package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.PackageReview;
import com.example.demo.service.PackageReviewService;
import com.example.demo.util.JwtUtil;
import com.google.api.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
                    .body(new ApiResponse<>(false, "Only customers are allowed to post package reviews.", null));
            }

            // Step 4: Validate if the booking belongs to this user
            Integer bookingCustomerId = reviewService.getCustomerIdByBookingId(packageBookingId);

            if (bookingCustomerId == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "No booking found for this Package Booking.", null));
            }

            if (!Objects.equals(bookingCustomerId, userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, "You are not authorized to review this package booking.", null));
            }

            // STEP 5: package booking must be finished
            System.out.println(reviewService.getPackageBookingStatus(packageBookingId));
            if(!Objects.equals(reviewService.getPackageBookingStatus(packageBookingId), "FINISHED")){
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, "Please first finish your package to give review.", null));
            }
            // STEP 6: no review exists already
            if(reviewService.doesReviewExist(packageBookingId)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(false, "Review already exists.", null));
            }
            // Step 7: Proceed to create the review
            review.setPackage_booking_id(packageBookingId);
            review.setCreated_at(LocalDateTime.now());
            reviewService.addReview(review);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Success", null));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null ));
        }
    }


    // ✅ GET /api/package-reviews/package/{package_id}
    @GetMapping("/package/{package_id}")
    public ResponseEntity<?> getReviewsByPackageId(@PathVariable Integer package_id) {
        List<PackageReview> reviews = reviewService.getReviewsByPackageId(package_id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(true, "Successfully fetched the reviews", reviews));
    }

    // ✅ GET /api/package-reviews/user
    @GetMapping("/user")
    public ResponseEntity<?> getReviewsByUserId(@RequestHeader("Authorization") String authHeader) {
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
                        .body(new ApiResponse<>(false, "Only customers can view their reviews.", null));
            }
            List<PackageReview> reviews = reviewService.getReviewsByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>(true, "Successfully fetched the reviews", reviews));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null ));
        }
    }

    // ✅ DELETE /api/package-reviews/{review_id}
    @DeleteMapping("/{review_id}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer review_id,
                                          @RequestHeader("Authorization") String authHeader) {
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token found");
        }
        try{
            String  token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            Integer userId = jwtUtil.getUserIdFromToken(token);
            if (!Objects.equals(userType, "CUSTOMER")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false,  "Only customers can delete their reviews.", null));
            }
            Integer userIdFromDB = reviewService.getCustomerIdByReviewId(review_id);
            reviewService.deleteReview(review_id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>(true, "Review Deleted Successfully.", null));

        } catch(Exception e){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // take list of package booking ids and return list of booleans, if the corresponding booking ids have
    // review ids or not
    @GetMapping("/exists/{packageBookingId}")
    public ResponseEntity<?> getReviewsByPackageId(
            @PathVariable Integer packageBookingId,
            @RequestHeader("Authorization") String authHeader
    ) {
        try{
            Boolean result = reviewService.doesReviewExist(packageBookingId);
            System.out.println(result);
            if(result){
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiResponse<>(true, "Review already exists", null));
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Review Not Found", null));
            }
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
