// AuthResponse.java
package com.example.demo.dto;


import com.example.demo.model.Customer;
import com.example.demo.model.Vendor;

public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String userType;
    private Integer userId;
    private String email;

    public AuthResponse(String accessToken, String refreshToken, String userType,
                        Integer userId, String email) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userType = userType;
        this.userId = userId;
        this.email = email;
    }

//    public AuthResponse(String accessToken, String refreshToken, Customer theCustomer) {
//        this.accessToken = accessToken;
//        this.refreshToken = refreshToken;
//    }
//
//    public AuthResponse(String accessToken, String refreshToken, Vendor theVendor) {
//
//    }

    // Getters and Setters
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}