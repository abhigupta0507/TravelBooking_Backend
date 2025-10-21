package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.GuideEmail;
import com.example.demo.service.AuthService;
import com.example.demo.service.GuideEmailService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/guides/{guideId}/emails")
//@CrossOrigin(origins = "*")
public class GuideEmailController {

    @Autowired
    private GuideEmailService guideEmailService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;



    // Add a new email for a guide
    @PostMapping("/")
    public ResponseEntity<?> addGuideEmail(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer guideId,
            @RequestBody Map<String, String> payload) {

        try {
            String email=payload.get("email");
            String token = authHeader.substring(7);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);

            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to create guide emails.");
            }


            String serviceType = authService.getVendorServiceType(vendorId);
            if(!Objects.equals(serviceType, "Guide_Provider")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only Guide_Providers are allowed to create guide emails.");
            }

            int added = guideEmailService.addGuideEmail(vendorId, guideId, email);
            return ResponseEntity.ok(new ApiResponse<>(true, "Email added successfully", added));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Get all emails for a guide
    @GetMapping("/")
    public ResponseEntity<?> getEmails(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer guideId) {

        try {
            String token = authHeader.substring(7);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);

            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to get guide emails.");
            }


            String serviceType = authService.getVendorServiceType(vendorId);
            if(!Objects.equals(serviceType, "Guide_Provider")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only Guide_Providers are allowed to get guide emails.");
            }

            List<GuideEmail> emails = guideEmailService.getEmailsByGuide(vendorId, guideId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Emails fetched successfully", emails));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Delete a specific email of a guide
    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteGuideEmail(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer guideId,
            @PathVariable String email) {

        try {
            String token = authHeader.substring(7);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);

            String userType = jwtUtil.getUserTypeFromToken(token);

            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to delete guide emails.");
            }


            String serviceType = authService.getVendorServiceType(vendorId);
            if(!Objects.equals(serviceType, "Guide_Provider")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only Guide_Providers are allowed to delete guide emails.");
            }

            int deleted = guideEmailService.deleteGuideEmail(vendorId, guideId, email);
            return ResponseEntity.ok(new ApiResponse<>(true, "Email deleted successfully", deleted));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

}
