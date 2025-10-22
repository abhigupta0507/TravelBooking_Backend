package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class TransportAssignment {

    private Integer assignment_id;
    private String pickup_street;
    private String pickup_city;
    private String pickup_state;
    private String pickup_pin;
    private String drop_street;
    private String drop_city;
    private String drop_state;
    private String drop_pin;
    private String est_time;
    private LocalDate start_date;
    private LocalDate end_date;
    private int total_distance;
    private int cost;
    private String status;
    private Integer package_booking_id;
    private Integer item_id;
    private Integer package_id;
    private Integer driver_id;

    public TransportAssignment() {}

    public TransportAssignment(Integer assignment_id, String pickup_street, String pickup_city, String pickup_state,
                                String pickup_pin, String drop_street, String drop_city, String drop_state, String drop_pin,
                                String est_time, LocalDate start_date, LocalTime start_time, LocalDate end_date, LocalTime end_time,
                                int total_distance, int cost, String status, Integer package_booking_id,
                                Integer item_id, Integer package_id, Integer driver_id) {
        this.assignment_id = assignment_id;
        this.pickup_street = pickup_street;
        this.pickup_city = pickup_city;
        this.pickup_state = pickup_state;
        this.pickup_pin = pickup_pin;
        this.drop_street = drop_street;
        this.drop_city = drop_city;
        this.drop_state = drop_state;
        this.drop_pin = drop_pin;
        this.est_time = est_time;
        this.start_date = start_date;
        this.end_date = end_date;
        this.total_distance = total_distance;
        this.cost = cost;
        this.status = status;
        this.package_booking_id = package_booking_id;
        this.item_id = item_id;
        this.package_id = package_id;
        this.driver_id = driver_id;
    }

    public Integer getAssignment_id() { return assignment_id; }
    public void setAssignment_id(Integer assignment_id) { this.assignment_id = assignment_id; }

    public String getPickup_street() { return pickup_street; }
    public void setPickup_street(String pickup_street) { this.pickup_street = pickup_street; }

    public String getPickup_city() { return pickup_city; }
    public void setPickup_city(String pickup_city) { this.pickup_city = pickup_city; }

    public String getPickup_state() { return pickup_state; }
    public void setPickup_state(String pickup_state) { this.pickup_state = pickup_state; }

    public String getPickup_pin() { return pickup_pin; }
    public void setPickup_pin(String pickup_pin) { this.pickup_pin = pickup_pin; }

    public String getDrop_street() { return drop_street; }
    public void setDrop_street(String drop_street) { this.drop_street = drop_street; }

    public String getDrop_city() { return drop_city; }
    public void setDrop_city(String drop_city) { this.drop_city = drop_city; }

    public String getDrop_state() { return drop_state; }
    public void setDrop_state(String drop_state) { this.drop_state = drop_state; }

    public String getDrop_pin() { return drop_pin; }
    public void setDrop_pin(String drop_pin) { this.drop_pin = drop_pin; }

    public String getEst_time() { return est_time; }
    public void setEst_time(String est_time) { this.est_time = est_time; }

    public LocalDate getStart_date() { return start_date; }
    public void setStart_date(LocalDate start_date) { this.start_date = start_date; }

    public LocalDate getEnd_date() { return end_date; }
    public void setEnd_date(LocalDate end_date) { this.end_date = end_date; }

    public int getTotal_distance() { return total_distance; }
    public void setTotal_distance(int total_distance) { this.total_distance = total_distance; }

    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getPackage_booking_id() { return package_booking_id; }
    public void setPackage_booking_id(Integer package_booking_id) { this.package_booking_id = package_booking_id; }

    public Integer getItem_id() { return item_id; }
    public void setItem_id(Integer item_id) { this.item_id = item_id; }

    public Integer getPackage_id() { return package_id; }
    public void setPackage_id(Integer package_id) { this.package_id = package_id; }

    public Integer getDriver_id() { return driver_id; }
    public void setDriver_id(Integer driver_id) { this.driver_id = driver_id; }

    @Override
    public String toString() {
        return "Transport_Assignment{" +
                "assignment_id=" + assignment_id +
                ", pickup_street='" + pickup_street + '\'' +
                ", pickup_city='" + pickup_city + '\'' +
                ", pickup_state='" + pickup_state + '\'' +
                ", pickup_pin='" + pickup_pin + '\'' +
                ", drop_street='" + drop_street + '\'' +
                ", drop_city='" + drop_city + '\'' +
                ", drop_state='" + drop_state + '\'' +
                ", drop_pin='" + drop_pin + '\'' +
                ", est_time='" + est_time + '\'' +
                ", start_date=" + start_date +
                ", total_distance=" + total_distance +
                ", cost=" + cost +
                ", status='" + status + '\'' +
                ", package_booking_id=" + package_booking_id +
                ", item_id=" + item_id +
                ", package_id=" + package_id +
                ", driver_id=" + driver_id +
                '}';
    }
}
