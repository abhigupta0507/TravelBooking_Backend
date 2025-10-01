package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.Customer;
import com.example.demo.model.Staff;
import com.example.demo.model.User;
import com.example.demo.model.Vendor;
import com.example.demo.service.AuthService;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
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

    @Autowired
    private JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> signup(@RequestBody SignupRequest request) {
        try {
            System.out.println(request);
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

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<ApiResponse<Object>> getProfile(@PathVariable int profileId, @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
        }

        String token = authHeader.substring(7);

        try {
            String userType = jwtUtil.getUserTypeFromToken(token);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            if (!userId.equals(profileId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "Access Denied: You are not authorized to view this profile", null));
            }

            User user = authService.getUserByIdAndUserType(profileId, userType);
            user.setPassword(null);
            user.setUserType(userType);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "User not found", null));
            }

            return ResponseEntity.ok(new ApiResponse<>(true, "Successfully fetched profile", user));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/profile/{profileId}")
    public ResponseEntity<ApiResponse<Object>> updateProfile(@PathVariable int profileId, @RequestHeader("Authorization") String authHeader, @RequestBody SignupRequest profile) {
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
//        }

        //System.out.println(profileId);
        //System.out.println(profile);
        String token = authHeader.substring(7);

        try {
            String userType = jwtUtil.getUserTypeFromToken(token);
            //String email = jwtUtil.getEmailFromToken(token);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            if (!userId.equals(profileId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "Access Denied: You are not authorized to view this profile", null));
            }

            Object user = authService.updateProfile(profileId, userType, profile);


            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "User not found", null));
            }

            return ResponseEntity.ok(new ApiResponse<>(true, "Successfully fetched profile", user));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/change-password/{profileId}")
    public ResponseEntity<ApiResponse<Object>> changePassword(
            @PathVariable int profileId,
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody PasswordChangeRequestDto passChange) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
        }

        try {
            // Step 1: Parse the token
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            // Step 2: Check if the user is authorized to perform this action
            if (!userId.equals(profileId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "Access Denied: You are not authorized to change the password for this profile", null));
            }

            // Step 3: Call the service to perform the password change
            authService.changePassword(profileId, userType, passChange);

            // Step 4: Return a successful response
            return ResponseEntity.ok(new ApiResponse<>(true, "Password changed successfully.", null));

        } catch (RuntimeException e) {
            // Catches errors thrown from your service layer (e.g., "Incorrect current password")
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}