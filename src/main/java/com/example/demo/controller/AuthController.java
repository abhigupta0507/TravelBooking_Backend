package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.Customer;
import com.example.demo.model.Staff;
import com.example.demo.model.User;
import com.example.demo.model.Vendor;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;
    private JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> signup(@RequestBody SignupRequest request) {
        try {
            AuthResponse response = authService.signup(request);
            return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request.getEmail(), request.getPassword(), request.getUserType());
            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            AuthResponse response = authService.refreshToken(request.getRefreshToken());
            return ResponseEntity.ok(new ApiResponse<>(true, "Token refreshed successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        // In a simple implementation, logout is handled on the client side by removing tokens
        // You can implement token blacklisting here if needed
        return ResponseEntity.ok(new ApiResponse<>(true, "Logged out successfully", null));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        System.out.println(token);
        try{

            String userType = jwtUtil.getUserTypeFromToken(token);
            String email= jwtUtil.getEmailFromToken(token);
            System.out.println(userType);
            User user= authService.getUserByEmail(email,userType);
            return ResponseEntity.ok(new ApiResponse<>(true,"Successfully fetched profile", user));
//            if(userType=="CUSTOMER"){
//                Customer theCustomer = authService.getCustomerByEmail(email);
//                return ResponseEntity.ok(new ApiResponse<>(true,"Profile fetched Successfully", theCustomer));
//            }
//            if(userType=="VENDOR"){
//                Vendor theVendor = authService.getVendorByEmail(email);
//                return ResponseEntity.ok(new ApiResponse<>(true,"Profile fetched Successfully", theCustomer));
//            }
//            if(userType=="STAFF"){
//                Staff theStaff = authService.getStaffByEmail(email);
//                return ResponseEntity.ok(new ApiResponse<>(true,"Profile fetched Successfully", theCustomer));
//            }
        }
        catch(Exception e){
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}