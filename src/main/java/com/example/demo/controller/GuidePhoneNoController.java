package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.GuidePhoneNo;
import com.example.demo.service.AuthService;
import com.example.demo.service.GuidePhoneNoService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/guides/{guideId}/phones")
//@CrossOrigin(origins = "*")
public class GuidePhoneNoController {

    @Autowired
    private GuidePhoneNoService guidePhoneNoService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    // Add a new phone number for a guide
    @PostMapping("/")
    public ResponseEntity<?> addGuidePhoneNo(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer guideId,
            @RequestBody Map<String, String> payload) {

        try {
            String phoneNo=payload.get("phoneNo");
            String token = authHeader.substring(7);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);

            String userType = jwtUtil.getUserTypeFromToken(token);

            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to create guide phone no.");
            }


            String serviceType = authService.getVendorServiceType(vendorId);
            if(!Objects.equals(serviceType, "Guide_Provider")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only Guide_Providers are allowed to create guide phone no.");
            }

            int added = guidePhoneNoService.addGuidePhoneNo(vendorId, guideId, phoneNo);
            return ResponseEntity.ok(new ApiResponse<>(true, "Phone number added successfully", added));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Get all phone numbers for a guide
    @GetMapping("/")
    public ResponseEntity<?> getPhoneNos(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer guideId) {

        try {
            String token = authHeader.substring(7);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);

            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to get guide phone no.");
            }


            String serviceType = authService.getVendorServiceType(vendorId);
            if(!Objects.equals(serviceType, "Guide_Provider")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only Guide_Providers are allowed to get guide phone no.");
            }


            List<GuidePhoneNo> phoneNos = guidePhoneNoService.getPhoneNosByGuide(vendorId, guideId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Phone numbers fetched successfully", phoneNos));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Delete a specific phone number of a guide
    @DeleteMapping("/{phoneNo}")
    public ResponseEntity<?> deleteGuidePhoneNo(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer guideId,
            @PathVariable String phoneNo) {

        try {
            String token = authHeader.substring(7);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);

            String userType = jwtUtil.getUserTypeFromToken(token);

            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to delete guide phone no.");
            }


            String serviceType = authService.getVendorServiceType(vendorId);
            if(!Objects.equals(serviceType, "Guide_Provider")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only Guide_Providers are allowed to delete guide phone no.");
            }

            int deleted = guidePhoneNoService.deleteGuidePhoneNo(vendorId, guideId, phoneNo);
            return ResponseEntity.ok(new ApiResponse<>(true, "Phone number deleted successfully", deleted));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
