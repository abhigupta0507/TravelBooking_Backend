package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.DriverPhone;
import com.example.demo.service.DriverPhoneService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transports/{driverId}/phones")
//@CrossOrigin(origins = "*")
public class DriverPhoneController {

    @Autowired
    private DriverPhoneService driverPhoneService;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ Add a new phone number for a driver
    @PostMapping("/")
    public ResponseEntity<ApiResponse<Integer>> addDriverPhone(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer driverId,
            @RequestBody Map<String, String> payload) {

        try {
            String phone = payload.get("phone");
            String token = authHeader.substring(7);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);

            int added = driverPhoneService.addDriverPhone(vendorId, driverId, phone);
            return ResponseEntity.ok(new ApiResponse<>(true, "Phone number added successfully", added));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // ✅ Get all phone numbers for a driver
    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<DriverPhone>>> getPhonesByDriver(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer driverId) {

        try {
            String token = authHeader.substring(7);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);

            List<DriverPhone> phoneNos = driverPhoneService.getPhonesByDriver(vendorId, driverId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Phone numbers fetched successfully", phoneNos));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // ✅ Delete a specific phone number of a driver
    @DeleteMapping("/{phone}")
    public ResponseEntity<ApiResponse<Integer>> deleteDriverPhone(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer driverId,
            @PathVariable String phone) {

        try {
            String token = authHeader.substring(7);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);

            int deleted = driverPhoneService.deleteDriverPhone(vendorId, driverId, phone);
            return ResponseEntity.ok(new ApiResponse<>(true, "Phone number deleted successfully", deleted));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
