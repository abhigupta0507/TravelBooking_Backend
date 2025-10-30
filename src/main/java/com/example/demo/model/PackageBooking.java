package com.example.demo.model;

import java.sql.Date;
import java.time.LocalDate;
import java.sql.Timestamp;

public class PackageBooking {

    private Integer booking_id;
    private Timestamp booking_date;
    private String status;
    private Integer number_of_people;
    private int total_cost;
    private Integer customer_id;
    private Integer package_id;
    private Date start_date;
    private String package_name;

    public PackageBooking() {}

    public PackageBooking(PackageBooking thePackageBooking){
        this.booking_id = thePackageBooking.booking_id;
        this.booking_date = thePackageBooking.booking_date;
        this.status = thePackageBooking.status;
        this.number_of_people = thePackageBooking.number_of_people;
        this.total_cost = thePackageBooking.total_cost;
        this.customer_id = thePackageBooking.customer_id;
        this.package_id = thePackageBooking.package_id;
        this.start_date=thePackageBooking.start_date;
    }

    public PackageBooking(Integer booking_id, Timestamp booking_date, String status,
                          Integer number_of_people, int total_cost, Integer customer_id, Integer package_id, Date start_date) {
        this.booking_id = booking_id;
        this.booking_date = booking_date;
        this.status = status;
        this.number_of_people = number_of_people;
        this.total_cost = total_cost;
        this.customer_id = customer_id;
        this.package_id = package_id;
        this.start_date=start_date;
    }

    public PackageBooking(Integer number_of_people, int total_cost, Integer customer_id, Integer package_id, Date start_date) {
        this.number_of_people = number_of_people;
        this.total_cost = total_cost;
        this.customer_id = customer_id;
        this.package_id = package_id;
        this.start_date = start_date;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Integer getBooking_id() { return booking_id; }
    public void setBooking_id(Integer booking_id) { this.booking_id = booking_id; }

    public Timestamp getBooking_date() { return booking_date; }
    public void setBooking_date(Timestamp booking_date) { this.booking_date = booking_date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getNumber_of_people() { return number_of_people; }
    public void setNumber_of_people(Integer number_of_people) { this.number_of_people = number_of_people; }

    public int getTotal_cost() { return total_cost; }
    public void setTotal_cost(int total_cost) { this.total_cost = total_cost; }

    public Integer getCustomer_id() { return customer_id; }
    public void setCustomer_id(Integer customer_id) { this.customer_id = customer_id; }

    public Integer getPackage_id() { return package_id; }
    public void setPackage_id(Integer package_id) { this.package_id = package_id; }

    public String getPackage_name() { return package_name; }
    public  void setPackage_name(String package_name) { this.package_name = package_name; }

    @Override
    public String toString() {
        return "PackageBooking{" +
                "booking_id=" + booking_id +
                ", booking_date=" + booking_date +
                ", status='" + status + '\'' +
                ", number_of_people=" + number_of_people +
                ", total_cost=" + total_cost +
                ", customer_id=" + customer_id +
                ", package_id=" + package_id +
                ", start_date=" + start_date +
                ", package_name='" + package_name + '\'' +
                '}';
    }
}
