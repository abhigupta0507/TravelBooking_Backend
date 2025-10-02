package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.Guide;
import com.example.demo.model.Vendor;
import com.example.demo.service.GuideService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guides")
@CrossOrigin(origins = "*")
public class GuideController {

    @Autowired
    private GuideService guideService;

    @Autowired
    private JwtUtil jwtUtil;

    // Add new guide
    @PostMapping("/")
    public ResponseEntity<ApiResponse<Integer>> addGuide(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Guide guide) {

        try {
            String token = authHeader.substring(7);
            Integer loggedInVendorId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);

            // Authorization check: only vendor itself can add guides

            Integer guideId = guideService.addGuide(guide, loggedInVendorId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Guide added successfully", guideId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Get all guides of the vendor
    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<Guide>>> getGuidesByVendor(
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7);
            Integer loggedInVendorId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);



            List<Guide> guides = guideService.getGuidesByVendor(loggedInVendorId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Guides fetched successfully", guides));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Get a single guide of the vendor
    @GetMapping("/{guideId}")
    public ResponseEntity<ApiResponse<Guide>> getGuideByVendorAndId(
            @PathVariable Integer guideId,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7);
            Integer loggedInVendorId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);



            Guide guide = guideService.getGuideByVendorAndId(loggedInVendorId, guideId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Guide fetched successfully", guide));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Update guide
    @PutMapping("/{guideId}")
    public ResponseEntity<ApiResponse<Integer>> updateGuide(
            @PathVariable Integer guideId,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Guide guide) {

        try {
            String token = authHeader.substring(7);
            Integer loggedInVendorId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);



            int updated = guideService.updateGuide(loggedInVendorId, guideId, guide);
            return ResponseEntity.ok(new ApiResponse<>(true, "Guide updated successfully", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Delete guide
    @DeleteMapping("/{guideId}")
    public ResponseEntity<ApiResponse<Integer>> deleteGuide(
            @PathVariable Integer guideId,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7);
            Integer loggedInVendorId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);



            int deleted = guideService.deleteGuide(loggedInVendorId, guideId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Guide deleted successfully", deleted));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Optional: Get all guides (admin or public)
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Guide>>> getAllGuides() {
        try {
            List<Guide> guides = guideService.getAllGuides();
            return ResponseEntity.ok(new ApiResponse<>(true, "All guides fetched successfully", guides));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
