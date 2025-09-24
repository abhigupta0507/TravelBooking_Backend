package com.example.demo.dto;


public class SignupRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String userType; // CUSTOMER, VENDOR, STAFF
    private String role; // For staff: CONTENT_CREATOR, HELP_DESK
    private String dateOfBirth; // For customer
    private String gender; // For customer
    private String emergencyContactFirstName;
    private String emergencyContactLastName;
    private String emergencyContactNo;
    private String serviceType; // For vendor
    private String contactPersonFirstName; // For vendor
    private String contactPersonLastName; // For vendor

    // Getters and Setters
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

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getEmergencyContactFirstName() { return emergencyContactFirstName; }
    public void setEmergencyContactFirstName(String emergencyContactFirstName) {
        this.emergencyContactFirstName = emergencyContactFirstName;
    }

    public String getEmergencyContactLastName() { return emergencyContactLastName; }
    public void setEmergencyContactLastName(String emergencyContactLastName) {
        this.emergencyContactLastName = emergencyContactLastName;
    }

    public String getEmergencyContactNo() { return emergencyContactNo; }
    public void setEmergencyContactNo(String emergencyContactNo) {
        this.emergencyContactNo = emergencyContactNo;
    }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getContactPersonFirstName() { return contactPersonFirstName; }
    public void setContactPersonFirstName(String contactPersonFirstName) {
        this.contactPersonFirstName = contactPersonFirstName;
    }

    public String getContactPersonLastName() { return contactPersonLastName; }
    public void setContactPersonLastName(String contactPersonLastName) {
        this.contactPersonLastName = contactPersonLastName;
    }
}