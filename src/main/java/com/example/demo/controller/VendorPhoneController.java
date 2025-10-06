package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.GuidePhoneNo;
import com.example.demo.model.VendorPhone;
import com.example.demo.service.GuidePhoneNoService;
import com.example.demo.service.VendorPhoneService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/vendors/{vendorId}/phones")
//@CrossOrigin(origins = "*")
public class VendorPhoneController {

    @Autowired
    private VendorPhoneService vendorPhoneService;

    @Autowired
    private JwtUtil jwtUtil;

    // Add a new phone number for a guide
    @PostMapping("/")
    public ResponseEntity<ApiResponse<Integer>> addVendorPhoneNo(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer vendorId,
            @RequestBody Map<String, String> payload) {

        try {
            String phoneNo=payload.get("phoneNo");
            String token = authHeader.substring(7);
            Integer vendorIdFromToken = jwtUtil.getUserIdFromToken(token);

            if(!Objects.equals(vendorIdFromToken, vendorId)){
                return ResponseEntity.badRequest().body(new ApiResponse<>(false,"Not authorized! Different Id's", null));
            }

            int added = vendorPhoneService.addVendorPhoneNo(vendorId, phoneNo);
            return ResponseEntity.ok(new ApiResponse<>(true, "Phone number added successfully", added));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Get all phone numbers for a vendor
    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<VendorPhone>>> getPhoneNos(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer vendorId) {

        try {
            String token = authHeader.substring(7);
            Integer vendorIdFromToken = jwtUtil.getUserIdFromToken(token);
            if(!Objects.equals(vendorIdFromToken, vendorId)){
                return ResponseEntity.badRequest().body(new ApiResponse<>(false,"Not authorized! Different Id's", null));
            }

            List<VendorPhone> phoneNos = vendorPhoneService.getPhoneNosByGuide(vendorId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Phone numbers fetched successfully", phoneNos));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Delete a specific phone number of a vendor
    @DeleteMapping("/{phoneNo}")
    public ResponseEntity<ApiResponse<Integer>> deleteVendorPhoneNo(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer vendorId,
            @PathVariable String phoneNo) {

        try {
            String token = authHeader.substring(7);
            Integer vendorIdFromToken = jwtUtil.getUserIdFromToken(token);
            if(!Objects.equals(vendorIdFromToken, vendorId)){
                return ResponseEntity.badRequest().body(new ApiResponse<>(false,"Not authorized! Different Id's", null));
            }
            int deleted = vendorPhoneService.deleteGuidePhoneNo(vendorId, phoneNo);
            return ResponseEntity.ok(new ApiResponse<>(true, "Phone number deleted successfully", deleted));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
