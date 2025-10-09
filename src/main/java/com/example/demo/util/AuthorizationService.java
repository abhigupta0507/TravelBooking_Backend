package com.example.demo.util;

import com.example.demo.service.AuthService;
import com.example.demo.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * A dedicated service to handle all authorization-related checks.
 * This centralizes security logic, making the application cleaner and more secure.
 */
@Service
public class AuthorizationService {

    private final JwtUtil jwtUtil;
    private final AuthService authService; // For fetching user roles

    public AuthorizationService(JwtUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    /**
     * Verifies that the user is an authenticated staff member with an 'ADMIN' role.
     * Throws a SecurityException if the authorization fails for any reason.
     * Does nothing if authorization is successful.
     *
     * @param authHeader The "Authorization" header from the HTTP request.
     * @throws SecurityException if the user is not authorized.
     */
    public void verifyAdminStaff(String authHeader) throws SecurityException {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new SecurityException("Missing or invalid Authorization header.");
        }

        try {
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            int userId = jwtUtil.getUserIdFromToken(token);

            if (!"STAFF".equalsIgnoreCase(userType)) {
                throw new SecurityException("Access Denied: Only staff members can perform this action.");
            }

            // You will need to ensure getStaffRole() exists and works in AuthService
            String role = authService.getStaffRole(userId);
            if (!"ADMIN".equalsIgnoreCase(role)) {
                throw new SecurityException("Access Denied: Administrator privileges required.");
            }
            // If we reach here, the user is an authorized admin staff.
        } catch (JwtException e) {
            // This catches errors like expired tokens, malformed tokens, etc.
            throw new SecurityException("Invalid or expired token.");
        }
    }

    public void verifyPackageManagerStaff(String authHeader) throws SecurityException {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new SecurityException("Missing or invalid Authorization header.");
        }

        try {
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            int userId = jwtUtil.getUserIdFromToken(token);

            if (!"STAFF".equalsIgnoreCase(userType)) {
                throw new SecurityException("Access Denied: Only staff members can perform this action.");
            }

            // You will need to ensure getStaffRole() exists and works in AuthService
            String role = authService.getStaffRole(userId);
            if (!"PackageManager".equalsIgnoreCase(role)) {
                throw new SecurityException("Access Denied: PackageManager privileges required.");
            }
            // If we reach here, the user is an authorized admin staff.
        } catch (JwtException e) {
            // This catches errors like expired tokens, malformed tokens, etc.
            throw new SecurityException("Invalid or expired token.");
        }
    }
}
