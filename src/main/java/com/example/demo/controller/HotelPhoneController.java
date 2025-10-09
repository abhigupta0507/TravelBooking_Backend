package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.HotelPhone;
import com.example.demo.service.HotelPhoneService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/hotels/{hotelId}/phones")
//@CrossOrigin(origins = "*")
public class HotelPhoneController {
    @Autowired
    private HotelPhoneService hotelPhoneService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/")
    public ResponseEntity<ApiResponse<String>> addHotelPhones(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer hotelId,
            @RequestBody Map<String, List<String>> payload) {

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(new ApiResponse<>(false, "No token found", null));
            }

            String token = authHeader.substring(7);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);

            // Expecting: { "phoneNumbers": ["123", "456"] }
            List<String> phoneNumbers = payload.get("phoneNumbers");
            if (phoneNumbers == null || phoneNumbers.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Phone numbers cannot be empty", null));
            }

            hotelPhoneService.addPhones(hotelId, phoneNumbers);
            return ResponseEntity.ok(new ApiResponse<>(true, "Phone numbers added successfully", null));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<HotelPhone>>> getHotelPhones(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer hotelId) {

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(new ApiResponse<>(false, "No token found", null));
            }

            String token = authHeader.substring(7);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);

            List<HotelPhone> phones = hotelPhoneService.getPhonesByHotelId(hotelId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Hotel phone numbers fetched successfully", phones));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{phoneNo}")
    public ResponseEntity<ApiResponse<String>> deleteHotelPhone(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer hotelId,
            @PathVariable String phoneNo) {

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(new ApiResponse<>(false, "No token found", null));
            }

            String token = authHeader.substring(7);
            Integer vendorIdFromToken = jwtUtil.getUserIdFromToken(token);
            String role = jwtUtil.getUserTypeFromToken(token);

            if (!(role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("VENDOR"))) {
                return ResponseEntity.status(403).body(new ApiResponse<>(false, "Access denied. Only ADMIN or VENDOR can delete.", null));
            }

            if (role.equalsIgnoreCase("VENDOR")) {
                Integer hotelVendorId = hotelPhoneService.getVendorIdByHotelId(hotelId);
                if (!vendorIdFromToken.equals(hotelVendorId)) {
                    System.out.println(vendorIdFromToken);
                    System.out.println(hotelVendorId);
                    return ResponseEntity.status(403).body(new ApiResponse<>(false, "You are not authorized to delete this hotel's phone number.", null));
                }
            }

            boolean deleted = hotelPhoneService.deletePhone(hotelId, phoneNo);
            if (deleted) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Phone number deleted successfully", null));
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Failed to delete phone number", null));
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
