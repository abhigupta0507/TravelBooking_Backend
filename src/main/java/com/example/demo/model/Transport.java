package com.example.demo.model;

import java.math.BigDecimal;

public class Transport {

    private Integer driver_id;
    private String first_name;
    private String last_name;
    private String license_no;
    private String vehicle_model;
    private String vehicle_type;
    private String vehicle_reg_no;
    private Boolean vehicle_ac;
    private Integer vehicle_seating_capacity;
    private BigDecimal cost;
    private Boolean availability;
    private Integer vendor_id;
    private String primary_phone;

    public Transport() {}

    public Transport(Integer driver_id, String first_name, String last_name, String license_no,
                     String vehicle_model, String vehicle_type, String vehicle_reg_no, Boolean vehicle_ac,
                     Integer vehicle_seating_capacity, BigDecimal cost, Boolean availability, Integer vendor_id) {
        this.driver_id = driver_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.license_no = license_no;
        this.vehicle_model = vehicle_model;
        this.vehicle_type = vehicle_type;
        this.vehicle_reg_no = vehicle_reg_no;
        this.vehicle_ac = vehicle_ac;
        this.vehicle_seating_capacity = vehicle_seating_capacity;
        this.cost = cost;
        this.availability = availability;
        this.vendor_id = vendor_id;
    }

    public Integer getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(Integer driver_id) {
        this.driver_id = driver_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getLicense_no() {
        return license_no;
    }

    public void setLicense_no(String license_no) {
        this.license_no = license_no;
    }

    public String getVehicle_model() {
        return vehicle_model;
    }

    public void setVehicle_model(String vehicle_model) {
        this.vehicle_model = vehicle_model;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getVehicle_reg_no() {
        return vehicle_reg_no;
    }

    public void setVehicle_reg_no(String vehicle_reg_no) {
        this.vehicle_reg_no = vehicle_reg_no;
    }

    public Boolean getVehicle_ac() {
        return vehicle_ac;
    }

    public void setVehicle_ac(Boolean vehicle_ac) {
        this.vehicle_ac = vehicle_ac;
    }

    public Integer getVehicle_seating_capacity() {
        return vehicle_seating_capacity;
    }

    public void setVehicle_seating_capacity(Integer vehicle_seating_capacity) {
        this.vehicle_seating_capacity = vehicle_seating_capacity;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public Integer getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(Integer vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getPrimary_phone() {
        return primary_phone;
    }

    public void setPrimary_phone(String primary_phone) {
        this.primary_phone = primary_phone;
    }

    @Override
    public String toString() {
        return "Transport{" +
                "driver_id=" + driver_id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", license_no='" + license_no + '\'' +
                ", vehicle_model='" + vehicle_model + '\'' +
                ", vehicle_type='" + vehicle_type + '\'' +
                ", vehicle_reg_no='" + vehicle_reg_no + '\'' +
                ", vehicle_ac=" + vehicle_ac +
                ", vehicle_seating_capacity=" + vehicle_seating_capacity +
                ", cost=" + cost +
                ", availability=" + availability +
                ", vendor_id=" + vendor_id +
                ", primary_phone='" + primary_phone + '\'' +
                '}';
    }


}
