package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/public")
    public ResponseEntity<ApiResponse<String>> publicEndpoint() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Public endpoint accessed", "This is a public endpoint"));
    }

    @GetMapping("/protected")
    public ResponseEntity<ApiResponse<String>> protectedEndpoint(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Integer userId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);

            String message = String.format("Protected endpoint accessed by user: %s (ID: %d, Type: %s)",
                    email, userId, userType);
            return ResponseEntity.ok(new ApiResponse<>(true, "Protected endpoint accessed", message));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "Protected endpoint accessed",
                "User authenticated: " + email));
    }

    @GetMapping("/customer")
    public ResponseEntity<ApiResponse<String>> customerOnly(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userType = jwtUtil.getUserTypeFromToken(token);

        if (!"CUSTOMER".equals(userType)) {
            return ResponseEntity.status(403)
                    .body(new ApiResponse<>(false, "Access denied", "Customer access only"));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "Customer endpoint accessed",
                "Welcome customer!"));
    }

    @GetMapping("/vendor")
    public ResponseEntity<ApiResponse<String>> vendorOnly(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userType = jwtUtil.getUserTypeFromToken(token);

        if (!"VENDOR".equals(userType)) {
            return ResponseEntity.status(403)
                    .body(new ApiResponse<>(false, "Access denied", "Vendor access only"));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "Vendor endpoint accessed",
                "Welcome vendor!"));
    }

    @GetMapping("/staff")
    public ResponseEntity<ApiResponse<String>> staffOnly(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userType = jwtUtil.getUserTypeFromToken(token);

        if (!"STAFF".equals(userType)) {
            return ResponseEntity.status(403)
                    .body(new ApiResponse<>(false, "Access denied", "Staff access only"));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "Staff endpoint accessed",
                "Welcome staff member!"));
    }
}