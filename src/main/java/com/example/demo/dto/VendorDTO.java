package com.example.demo.dto;

import java.math.BigDecimal;

public class VendorDTO {
    private int vendor_id;
    private String vendor_name;
    private String service_type;
    private String contact_person_first_name;
    private String contact_person_last_name;
    private String email;
   // private String password;
    private String phone;
    private String street_name;
    private String city;
    private String state;
    private String pin;
    private java.math.BigDecimal amt_due;
    private String account_no;
    private String ifsc_code;
    private String status;


    public VendorDTO(int vendorID,String vendor_name, String service_type, String contact_person_first_name, String contact_person_last_name, String email, String phone, String street_name, String city, String state, String pin, BigDecimal amt_due, String account_no, String ifsc_code, String status) {
        this.vendor_id=vendorID;
        this.vendor_name = vendor_name;
        this.service_type = service_type;
        this.contact_person_first_name = contact_person_first_name;
        this.contact_person_last_name = contact_person_last_name;
        this.email = email;
        this.phone = phone;
        this.street_name = street_name;
        this.city = city;
        this.state = state;
        this.pin = pin;
        this.amt_due = amt_due;
        this.account_no = account_no;
        this.ifsc_code = ifsc_code;
        this.status = status;
        this.vendor_id = vendor_id;
    }


    public int getVendorId() {
        return vendor_id;
    }

    public void setVendorId(int vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getContact_person_first_name() {
        return contact_person_first_name;
    }

    public void setContact_person_first_name(String contact_person_first_name) {
        this.contact_person_first_name = contact_person_first_name;
    }

    public String getContact_person_last_name() {
        return contact_person_last_name;
    }

    public void setContact_person_last_name(String contact_person_last_name) {
        this.contact_person_last_name = contact_person_last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

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

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public BigDecimal getAmt_due() {
        return amt_due;
    }

    public void setAmt_due(BigDecimal amt_due) {
        this.amt_due = amt_due;
    }

    public String getAccount_no() {
        return account_no;
    }

    public void setAccount_no(String account_no) {
        this.account_no = account_no;
    }

    public String getIfsc_code() {
        return ifsc_code;
    }

    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code = ifsc_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Vendor{" +
                "vendor_id=" + vendor_id +
                ", vendor_name='" + vendor_name + '\'' +
                ", service_type='" + service_type + '\'' +
                ", contact_person_first_name='" + contact_person_first_name + '\'' +
                ", contact_person_last_name='" + contact_person_last_name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", street_name='" + street_name + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", pin='" + pin + '\'' +
                ", amt_due=" + amt_due +
                ", account_no='" + account_no + '\'' +
                ", ifsc_code='" + ifsc_code + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
