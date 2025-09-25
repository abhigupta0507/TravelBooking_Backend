package com.example.demo.model;

import java.time.LocalDate;

public class User {
    private String email;
    private String phone;
    private String password;
    private String userType;

    public User() {}

    public User(String email, String phone, String password, String userType) {
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.userType = userType;
    }

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public void setJoiningDate(LocalDate localDate) {
        // Default implementation - can be overridden by subclasses if needed
    }
}