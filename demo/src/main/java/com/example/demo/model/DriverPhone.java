package com.example.demo.model;

public class DriverPhone {

    private Integer driver_id;
    private String phone;

    public DriverPhone() {}

    public DriverPhone(Integer driver_id, String phone) {
        this.driver_id = driver_id;
        this.phone = phone;
    }

    public Integer getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(Integer driver_id) {
        this.driver_id = driver_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "DriverPhone{" +
                "driver_id=" + driver_id +
                ", phone='" + phone + '\'' +
                '}';
    }
}
