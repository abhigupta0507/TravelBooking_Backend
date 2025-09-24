package com.example.demo.model;

public class VendorEmail {
    private Integer vendor_id;
    private String email;

    public VendorEmail() {}

    public VendorEmail(Integer vendor_id, String email) {
        this.vendor_id = vendor_id;
        this.email = email;
    }

    public Integer getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(Integer vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Vendor_Email{" +
                "vendor_id=" + vendor_id +
                ", email='" + email + '\'' +
                '}';
    }
}
