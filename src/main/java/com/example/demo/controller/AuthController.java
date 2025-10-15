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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.demo.util.JwtUtil;

import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
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
            String email=request.getEmail();
            String password=request.getPassword();
            String userType=request.getUserType();

            AuthResponse response = authService.login(email,password,userType);
            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    //refresh our access tokens
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            AuthResponse response = authService.refreshToken(request.getRefreshToken());
            System.out.println("Refreshed token");
            return ResponseEntity.ok(new ApiResponse<>(true, "Token refreshed successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    //LOGOUT:
    //It is assumed to be done on client side by removing tokens.
    //we do not implement blacklisting of tokens here!


    //ProfileOwners (all 3) can see their profile (no password)
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

            //profile id in url should match id in token!
            if (!userId.equals(profileId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "Access Denied: You are not authorized to view this profile", null));
            }

            User user = authService.getUserByIdAndUserType(profileId, userType);

            user.setPassword(null);
            user.setUserType(userType);

            return ResponseEntity.ok(new ApiResponse<>(true, "Successfully fetched profile", user));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    //update a user profile (only customer and vendor)
    @PutMapping("/profile/{profileId}")
    public ResponseEntity<ApiResponse<Object>> updateProfile(@PathVariable int profileId, @RequestHeader("Authorization") String authHeader, @RequestBody SignupRequest profile) {
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

            //no staff should be updated via this route.
            //staff are updated via admin through a diff route.
//            if(!(Objects.equals(userType, "CUSTOMER")) && !(Objects.equals(userType, "VENDOR"))){
//                return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                        .body(new ApiResponse<>(false, "This route only for customers and vendors", null));
//            }

            //update and fetch updated user
            User user = authService.updateProfile(profileId, userType, profile);

            //since we are retrieving entire data from schema we remove password and also set usertype.
            //rest is entirely from model schema
            user.setPassword(null);
            user.setUserType(userType);

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
            String token = authHeader.substring(7);

            String userType = jwtUtil.getUserTypeFromToken(token);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            if (!userId.equals(profileId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "Access Denied: You are not authorized to change the password for this profile", null));
            }

            authService.changePassword(profileId, userType, passChange);

            return ResponseEntity.ok(new ApiResponse<>(true, "Password changed successfully.", null));

        }
        catch (RuntimeException e) {
            // Catches errors thrown from your service layer (e.g., "Incorrect current password")
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }


    @PatchMapping("/vendors/change-status/{vendorId}")
    public ResponseEntity<ApiResponse<?>> changeVendorStatus(@RequestHeader("Authorization") String authHeader,@PathVariable int vendorId, @RequestBody ChangeStatusDto changeStatusDto){
        try{
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
            }
            String token = authHeader.substring(7);

            String userType = jwtUtil.getUserTypeFromToken(token);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            if (!userId.equals(vendorId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "Access Denied", null));
            }

            authService.changeVendorStatus(vendorId,changeStatusDto.getNewStatus());

            return ResponseEntity.ok().body(new ApiResponse<>(true,"Status Changed successfully",null));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse<>(false,e.getMessage(),null));
        }
    }
}