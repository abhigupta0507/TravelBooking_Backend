package com.example.demo.model;

public class PackageCustomer {
    private Integer customer_id;
    private Integer package_booking_id;

    public PackageCustomer() {}

    public PackageCustomer(Integer customer_id, Integer package_booking_id) {
        this.customer_id = customer_id;
        this.package_booking_id = package_booking_id;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public Integer getPackage_booking_id() {
        return package_booking_id;
    }

    public void setPackage_booking_id(Integer package_booking_id) {
        this.package_booking_id = package_booking_id;
    }

    @Override
    public String toString() {
        return "Package_Customer{" +
                "customer_id=" + customer_id +
                ", package_booking_id=" + package_booking_id +
                '}';
    }
}
