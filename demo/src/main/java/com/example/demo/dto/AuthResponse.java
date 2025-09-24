// AuthResponse.java
package com.example.demo.dto;


public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String userType;
    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;

    public AuthResponse(String accessToken, String refreshToken, String userType,
                        Integer userId, String firstName, String lastName, String email) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userType = userType;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Getters and Setters
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}