package com.example.demo.security;

import java.security.Principal;

public class UserPrincipal implements Principal {
    private String email;
    private String userType;
    private Long userId;

    public UserPrincipal(String email, String userType, Long userId) {
        this.email = email;
        this.userType = userType;
        this.userId = userId;
    }

    @Override
    public String getName() {
        return email;
    }

    public String getEmail() { return email; }
    public String getUserType() { return userType; }
    public Long getUserId() { return userId; }
}