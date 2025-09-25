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

    @Override
    public String toString() {
        return "SignupRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", userType='" + userType + '\'' +
                ", role='" + role + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", gender='" + gender + '\'' +
                ", emergencyContactFirstName='" + emergencyContactFirstName + '\'' +
                ", emergencyContactLastName='" + emergencyContactLastName + '\'' +
                ", emergencyContactNo='" + emergencyContactNo + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", contactPersonFirstName='" + contactPersonFirstName + '\'' +
                ", contactPersonLastName='" + contactPersonLastName + '\'' +
               // ", service_type='" + service_type + '\'' +
                ", street_name='" + street_name + '\'' +
                ", amount_due='" + amount_due + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", ifsc_code='" + ifsc_code + '\'' +
                ", Account_no='" + Account_no + '\'' +
                ", status='" + status + '\'' +
                ", vendorName='" + vendorName + '\'' +
                ", pin='" + pin + '\'' +
                '}';
    }

    private String serviceType; // For vendor
    private String contactPersonFirstName; // For vendor

    private String contactPersonLastName; // For vendor
    private String street_name;
    private String amount_due;
    private String vendorName;

    public String getAccount_no() {
        return Account_no;
    }

    public void setAccount_no(String account_no) {
        Account_no = account_no;
    }

    private String city;
    private String state;
    private String ifsc_code;
    private String Account_no;

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

   // private String amount_due;
    private String status;


    public String getStreet_name() {
        return street_name;
    }

    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIfsc_code() {
        return ifsc_code;
    }

    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code = ifsc_code;
    }

    public String getAmount_due() {
        return amount_due;
    }

    public void setAmount_due(String amount_due) {
        this.amount_due = amount_due;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    private String pin;

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