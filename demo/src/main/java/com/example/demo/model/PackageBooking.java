package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class PackageBooking {

    private Integer booking_id;
    private LocalDate booking_date;
    private LocalTime booking_time;
    private String status;
    private Integer number_of_people;
    private BigDecimal total_cost;
    private Integer customer_id;
    private Integer package_id;

    public PackageBooking() {}

    public PackageBooking(Integer booking_id, LocalDate booking_date, LocalTime booking_time, String status,
                           Integer number_of_people, BigDecimal total_cost, Integer customer_id, Integer package_id) {
        this.booking_id = booking_id;
        this.booking_date = booking_date;
        this.booking_time = booking_time;
        this.status = status;
        this.number_of_people = number_of_people;
        this.total_cost = total_cost;
        this.customer_id = customer_id;
        this.package_id = package_id;
    }

    public Integer getBooking_id() { return booking_id; }
    public void setBooking_id(Integer booking_id) { this.booking_id = booking_id; }

    public LocalDate getBooking_date() { return booking_date; }
    public void setBooking_date(LocalDate booking_date) { this.booking_date = booking_date; }

    public LocalTime getBooking_time() { return booking_time; }
    public void setBooking_time(LocalTime booking_time) { this.booking_time = booking_time; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getNumber_of_people() { return number_of_people; }
    public void setNumber_of_people(Integer number_of_people) { this.number_of_people = number_of_people; }

    public BigDecimal getTotal_cost() { return total_cost; }
    public void setTotal_cost(BigDecimal total_cost) { this.total_cost = total_cost; }

    public Integer getCustomer_id() { return customer_id; }
    public void setCustomer_id(Integer customer_id) { this.customer_id = customer_id; }

    public Integer getPackage_id() { return package_id; }
    public void setPackage_id(Integer package_id) { this.package_id = package_id; }

    @Override
    public String toString() {
        return "Package_Booking{" +
                "booking_id=" + booking_id +
                ", booking_date=" + booking_date +
                ", booking_time=" + booking_time +
                ", status='" + status + '\'' +
                ", number_of_people=" + number_of_people +
                ", total_cost=" + total_cost +
                ", customer_id=" + customer_id +
                ", package_id=" + package_id +
                '}';
    }
}
