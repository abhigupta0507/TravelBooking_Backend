package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.Transport;
import com.example.demo.service.TransportService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transports")
//@CrossOrigin(origins = "*")
public class TransportController {

    @Autowired
    private TransportService transportService;

    @Autowired
    private JwtUtil jwtUtil;

    // Add new transport
    @PostMapping("/")
    public ResponseEntity<ApiResponse<Integer>> addTransport(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Transport transport) {
        try {
            String token = authHeader.substring(7);
            Integer loggedInVendorId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);

            Integer transportId = transportService.addTransport(transport, loggedInVendorId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transport added successfully", transportId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Get all transports of a vendor
    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<Transport>>> getTransportsByVendor(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Integer loggedInVendorId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);

            List<Transport> transports = transportService.getTransportsByVendor(loggedInVendorId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transports fetched successfully", transports));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }


    @GetMapping("/{transportId}")
    public ResponseEntity<ApiResponse<Transport>> getTransportByVendorAndId(
            @PathVariable Integer transportId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Integer loggedInVendorId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);

            Transport transport = transportService.getTransportByVendorAndId(loggedInVendorId, transportId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transport fetched successfully", transport));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Update transport
    @PutMapping("/{transportId}")
    public ResponseEntity<ApiResponse<Integer>> updateTransport(
            @PathVariable Integer transportId,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Transport transport) {
        try {
            String token = authHeader.substring(7);
            Integer loggedInVendorId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);

            int updated = transportService.updateTransport(loggedInVendorId, transportId, transport);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transport updated successfully", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Delete transport
    @DeleteMapping("/{transportId}")
    public ResponseEntity<ApiResponse<Integer>> deleteTransport(
            @PathVariable Integer transportId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Integer loggedInVendorId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);

            int deleted = transportService.deleteTransport(loggedInVendorId, transportId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transport deleted successfully", deleted));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Optional: Get all transports (admin/public)
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Transport>>> getAllTransports() {
        try {
            List<Transport> transports = transportService.getAllTransports();
            return ResponseEntity.ok(new ApiResponse<>(true, "All transports fetched successfully", transports));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
