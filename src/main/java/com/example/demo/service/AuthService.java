package com.example.demo.service;

import com.example.demo.dao.AuthDao;
import com.example.demo.dto.SignupRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.model.Staff;
import com.example.demo.model.User;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthDao authDao;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse signup(SignupRequest request) {
        // Check if email already exists
        if (authDao.emailExists(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Validate user type
        if (!isValidUserType(request.getUserType())) {
            throw new RuntimeException("Invalid user type");
        }


        // Validate role for staff
        if ("STAFF".equalsIgnoreCase(request.getUserType())) {
            if (request.getRole() == null ||
                    (!request.getRole().equalsIgnoreCase("CONTENT_CREATOR") &&
                            !request.getRole().equalsIgnoreCase("HELP_DESK"))) {
                throw new RuntimeException("Invalid role for staff. Must be CONTENT_CREATOR or HELP_DESK");
            }
        }

        Integer userId = null;
        String userType = request.getUserType().toUpperCase();

        switch (userType) {
            case "CUSTOMER":
                userId = authDao.createCustomer(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPhone(),
                        request.getDateOfBirth(),
                        request.getGender(),
                        request.getEmergencyContactFirstName(),
                        request.getEmergencyContactLastName(),
                        request.getEmergencyContactNo()
                );
                break;
            case "VENDOR":
                userId = authDao.createVendor(
                        request.getFirstName() + " " + request.getLastName() + " Services", // vendor_name
                        request.getServiceType(),
                        request.getContactPersonFirstName() != null ? request.getContactPersonFirstName() : request.getFirstName(),
                        request.getContactPersonLastName() != null ? request.getContactPersonLastName() : request.getLastName(),
                        request.getEmail(),
                        request.getPhone()
                );
                break;
            case "STAFF":
                userId = authDao.createStaff(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPhone(),
                        request.getRole().toUpperCase()
                );
                break;
        }

        if (userId == null) {
            throw new RuntimeException("Failed to create user");
        }

        // Generate tokens
        String accessToken = jwtUtil.generateAccessToken(userId, request.getEmail(), userType);
        String refreshToken = jwtUtil.generateRefreshToken(userId, request.getEmail(),userType);

        return new AuthResponse(accessToken, refreshToken, userType, userId,
                request.getFirstName(), request.getLastName(), request.getEmail());
    }

    public AuthResponse login(String email, String password, String userType) {
        if (!isValidUserType(userType)) {
            throw new RuntimeException("Invalid user type");
        }

        User user = authDao.findUserByEmailAndType(email, userType);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // For this simple implementation, we're not storing passwords in a separate table
        // You might want to extend this to include password verification
        // For now, we'll just proceed with login

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getEmail(), user.getUserType());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getEmail(),user.getUserType());

        return new AuthResponse(accessToken, refreshToken, user.getUserType(), user.getId(),
                user.getFirstName(), user.getLastName(), user.getEmail());
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtUtil.isValidToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String email = jwtUtil.getEmailFromToken(refreshToken);
        Integer userId = jwtUtil.getUserIdFromToken(refreshToken);
        String userType = jwtUtil.getUserTypeFromToken(refreshToken);

        // Try to find user in all tables
        User user = authDao.findUserByEmailAndType(email,userType);
        System.out.println(user);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getEmail(), userType);
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getEmail(),userType);

        return new AuthResponse(accessToken, newRefreshToken, user.getUserType(), user.getId(),
                user.getFirstName(), user.getLastName(), user.getEmail());
    }

    private User findUserById(Integer userId) {
        // Try to find in Customer table
        try {
            User user = authDao.findUserByEmailAndType("", "CUSTOMER");
            // This is a simplified approach - you'd need to modify the DAO to support finding by ID
        } catch (Exception e) {
            // Continue to next table

        }

        // For simplicity, we'll assume the refresh token is valid and return a basic user
        // In a real implementation, you'd want to fetch user details by ID
        User user = new User();
        user.setId(userId);
        return user;
    }

    private boolean isValidUserType(String userType) {
        return userType != null &&
                (userType.equalsIgnoreCase("CUSTOMER") ||
                        userType.equalsIgnoreCase("VENDOR"));
//                        userType.equalsIgnoreCase("STAFF"));
    }

    public User getUserByEmail(String email,String userType) {
        return authDao.findUserByEmailAndType(email,userType);
    }
//    public Staff getCustomerByEmail(String email,String userType) {
//        return authDao.findCustomerByEmail(email,userType);
//    }
//    public Staff getVendorByEmail(String email, String userType) {
//        return authDao.findVendorByEmail(email,userType);
//    }
}