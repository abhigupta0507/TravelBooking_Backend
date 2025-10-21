package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.Transport;
import com.example.demo.service.AuthService;
import com.example.demo.service.TransportService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/transports")
//@CrossOrigin(origins = "*")
public class TransportController {

    @Autowired
    private TransportService transportService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    // Add new transport
    @PostMapping("/")
    public ResponseEntity<?> addTransport(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Transport transport) {
        try {
            String token = authHeader.substring(7);
            Integer loggedInVendorId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);
            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to create transport.");
            }


            String serviceType = authService.getVendorServiceType(loggedInVendorId);
            if(!Objects.equals(serviceType, "Transport_Provider")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only Transport_Providers are allowed to create transports.");
            }

            Integer transportId = transportService.addTransport(transport, loggedInVendorId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transport added successfully", transportId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Get all transports of a vendor
    @GetMapping("/")
    public ResponseEntity<?> getTransportsByVendor(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Integer loggedInVendorId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);

            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to get transport.");
            }
            String serviceType = authService.getVendorServiceType(loggedInVendorId);
            if(!Objects.equals(serviceType, "Transport_Provider")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only Transport_Providers are allowed to get transports");
            }

            List<Transport> transports = transportService.getTransportsByVendor(loggedInVendorId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transports fetched successfully", transports));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }


    @GetMapping("/{transportId}")
    public ResponseEntity<?> getTransportByVendorAndId(
            @PathVariable Integer transportId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Integer loggedInVendorId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);

            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to get transport by id");
            }

            String serviceType = authService.getVendorServiceType(loggedInVendorId);
            if(!Objects.equals(serviceType, "Transport_Provider")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only Transport_Providers are allowed to get transport by id.");
            }

            Transport transport = transportService.getTransportByVendorAndId(loggedInVendorId, transportId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transport fetched successfully", transport));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Update transport
    @PutMapping("/{transportId}")
    public ResponseEntity<?> updateTransport(
            @PathVariable Integer transportId,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Transport transport) {
        try {
            String token = authHeader.substring(7);
            Integer loggedInVendorId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);

            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to update transport.");
            }

            String serviceType = authService.getVendorServiceType(loggedInVendorId);
            if(!Objects.equals(serviceType, "Transport_Provider")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only Transport_Providers are allowed to update transports.");
            }

            int updated = transportService.updateTransport(loggedInVendorId, transportId, transport);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transport updated successfully", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Delete transport
    @DeleteMapping("/{transportId}")
    public ResponseEntity<?> deleteTransport(
            @PathVariable Integer transportId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            Integer loggedInVendorId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);

            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to delete transport.");
            }

            String serviceType = authService.getVendorServiceType(loggedInVendorId);
            if(!Objects.equals(serviceType, "Transport_Provider")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only Transport_Providers are allowed to delete transports.");
            }

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
