package com.example.demo.model;

public class PackageTraveler {
    private Integer traveler_id;
    private Integer package_booking_id;

    public PackageTraveler() {}

    public PackageTraveler(Integer traveler_id, Integer package_booking_id) {
        this.traveler_id = traveler_id;
        this.package_booking_id = package_booking_id;
    }

    public Integer getTraveler_id() {
        return traveler_id;
    }

    public void setTraveler_id(Integer traveler_id) {
        this.traveler_id = traveler_id;
    }

    public Integer getPackage_booking_id() {
        return package_booking_id;
    }

    public void setPackage_booking_id(Integer package_booking_id) {
        this.package_booking_id = package_booking_id;
    }

    @Override
    public String toString() {
        return "Package_Traveler{" +
                "traveler_id=" + traveler_id +
                ", package_booking_id=" + package_booking_id +
                '}';
    }
}
