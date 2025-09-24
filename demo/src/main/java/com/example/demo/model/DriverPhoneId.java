package com.example.demo.model;


import java.io.Serializable;
import java.util.Objects;

public class DriverPhoneId implements Serializable {
    private Integer driverId;
    private String phone;

    public DriverPhoneId() {}

    public DriverPhoneId(Integer driverId, String phone) {
        this.driverId = driverId;
        this.phone = phone;
    }

    // equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DriverPhoneId)) return false;
        DriverPhoneId that = (DriverPhoneId) o;
        return Objects.equals(driverId, that.driverId) &&
                Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverId, phone);
    }
}
