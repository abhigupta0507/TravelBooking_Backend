package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.GuideEmail;
import com.example.demo.model.VendorEmail;
import com.example.demo.service.GuideEmailService;
import com.example.demo.service.VendorEmailService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/vendors/{vendorId}/emails")
//@CrossOrigin(origins = "*")
public class VendorEmailController {

    @Autowired
    private VendorEmailService vendorEmailService;

    @Autowired
    private JwtUtil jwtUtil;

    // Add a new email for a guide
    @PostMapping("/")
    public ResponseEntity<ApiResponse<Integer>> addGuideEmail(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer vendorId,
            @RequestBody Map<String, String> payload) {

        try {
            String email=payload.get("email");
            String token = authHeader.substring(7);
            Integer vendorIdFromToken = jwtUtil.getUserIdFromToken(token);
            if(!Objects.equals(vendorIdFromToken, vendorId)){
                return ResponseEntity.badRequest().body(new ApiResponse<>(false,"Not authorized! Different Id's", null));
            }
            int added = vendorEmailService.addVendorEmail(vendorId, email);
            return ResponseEntity.ok(new ApiResponse<>(true, "Email added successfully", added));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Get all emails for a vendor
    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<VendorEmail>>> getEmails(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer vendorId) {

        try {
            String token = authHeader.substring(7);
            Integer vendorIdFromToken = jwtUtil.getUserIdFromToken(token);
            if(!Objects.equals(vendorIdFromToken, vendorId)){
                return ResponseEntity.badRequest().body(new ApiResponse<>(false,"Not authorized! Different Id's", null));
            }
            List<VendorEmail> emails = vendorEmailService.getEmailsByVendor(vendorId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Emails fetched successfully", emails));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Delete a specific email of a vendor
    @DeleteMapping("/{email}")
    public ResponseEntity<ApiResponse<Integer>> deleteVendorEmail(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer vendorId,
            @PathVariable String email) {

        try {
            String token = authHeader.substring(7);
            Integer vendorIdFromToken = jwtUtil.getUserIdFromToken(token);
            if(!Objects.equals(vendorIdFromToken, vendorId)){
                return ResponseEntity.badRequest().body(new ApiResponse<>(false,"Not authorized! Different Id's", null));
            }
            int deleted = vendorEmailService.deleteVendorEmail(vendorId,email);
            return ResponseEntity.ok(new ApiResponse<>(true, "Email deleted successfully", deleted));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

}
