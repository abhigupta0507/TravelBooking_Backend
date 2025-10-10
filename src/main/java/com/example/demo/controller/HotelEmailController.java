package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.HotelEmail;
import com.example.demo.service.AuthService;
import com.example.demo.service.HotelEmailService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/hotels/{hotelId}/emails")
//@CrossOrigin(origins = "*")
public class HotelEmailController {

    @Autowired
    private HotelEmailService hotelEmailService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    @PostMapping("/")
    public ResponseEntity<ApiResponse<Integer>> addHotelEmail(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer hotelId,
            @RequestBody Map<String, List<String>> payload) {

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(new ApiResponse<>(false, "No token found", null));
            }

            List<String> emails = payload.get("emails");
            if (emails == null || emails.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Email cannot be empty", null));
            }

            String token = authHeader.substring(7);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);
            String serviceType = authService.getVendorServiceType(vendorId);
            String userType = jwtUtil.getUserTypeFromToken(token);
            // checks if user is a vendor
            if(!Objects.equals(userType, "VENDOR")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Only Vendors are allowed", null));
            }
            // checks if user is a Hotel_Provider
            if(!Objects.equals(serviceType, "Hotel_Provider")){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Only Hotel_Providers are allowed", null));
            }
            // Step 2: Check ownership
            Integer hotelVendorId = hotelEmailService.getVendorIdByHotelId(hotelId);
            if (!Objects.equals(vendorId, hotelVendorId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "Only Hotel_Provider of this hotel is allowed to add Phone Number to it.", null));
            }

            int added = hotelEmailService.addHotelEmail(vendorId, hotelId, emails);
            return ResponseEntity.ok(new ApiResponse<>(true, "Email added successfully", added));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<HotelEmail>>> getHotelEmails(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer hotelId) {

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(new ApiResponse<>(false, "No token found", null));
            }

            String token = authHeader.substring(7);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);

            List<HotelEmail> emails = hotelEmailService.getEmailsByHotel(hotelId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Emails fetched successfully", emails));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<ApiResponse<Integer>> deleteHotelEmail(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer hotelId,
            @PathVariable String email) {

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(new ApiResponse<>(false, "No token found", null));
            }

            String token = authHeader.substring(7);
            Integer vendorIdFromToken = jwtUtil.getUserIdFromToken(token);
            String role = jwtUtil.getUserTypeFromToken(token);
            String serviceType =  authService.getVendorServiceType(vendorIdFromToken);

            if (!(role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("VENDOR"))) {
                return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied. Only ADMIN or VENDOR can delete.", null));
            }

            if (role.equalsIgnoreCase("VENDOR")) {
                if(!Objects.equals(serviceType, "Hotel_Provider")){
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new ApiResponse<>(false, "Only Hotel_Provider of this hotel is allowed to delete.", null));
                }
                Integer hotelVendorId = hotelEmailService.getVendorIdByHotelId(hotelId);
                if (!vendorIdFromToken.equals(hotelVendorId)) {
                    return ResponseEntity.status(403).body(new ApiResponse<>(false, "You are not authorized to delete this hotel's email.", null));
                }
            }

            int deleted = hotelEmailService.deleteHotelEmail(vendorIdFromToken, hotelId, email);
            if (deleted > 0) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Email deleted successfully", deleted));
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Failed to delete email", null));
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

}
