package com.example.demo.model;

public class User {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String userType;
    private String role;

    public User() {}

    public User(Integer id, String firstName, String lastName, String email,
                String phone, String password, String userType, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.userType = userType;
        this.role = role;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}