package com.example.demo.model;

public class VendorPhone {
    private Integer vendor_id;
    private String phone;

    public VendorPhone() {}

    public VendorPhone(Integer vendor_id, String phone) {
        this.vendor_id = vendor_id;
        this.phone = phone;
    }

    public Integer getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(Integer vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Vendor_Phone{" +
                "vendor_id=" + vendor_id +
                ", phone='" + phone + '\'' +
                '}';
    }
}
