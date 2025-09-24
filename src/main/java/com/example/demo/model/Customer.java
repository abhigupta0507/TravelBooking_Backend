package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Customer {

    private Integer customer_id;
    private String first_name;
    private String last_name;
    private String phone;
    private String email;
    private String password;
    private LocalDate date_of_birth;
    private String gender;
    private String emergency_contact_first_name;
    private String emergency_contact_last_name;
    private String emergency_contact_no;
    private LocalDateTime created_at;

    public Customer(String password) {
        this.password = password;
    }

    public Customer(Integer customer_id, String first_name, String last_name, String phone, String email, String password,
                    LocalDate date_of_birth, String gender, String emergency_contact_first_name,
                    String emergency_contact_last_name, String emergency_contact_no, LocalDateTime created_at) {
        this.customer_id = customer_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.emergency_contact_first_name = emergency_contact_first_name;
        this.emergency_contact_last_name = emergency_contact_last_name;
        this.emergency_contact_no = emergency_contact_no;
        this.created_at = created_at;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(LocalDate date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmergency_contact_first_name() {
        return emergency_contact_first_name;
    }

    public void setEmergency_contact_first_name(String emergency_contact_first_name) {
        this.emergency_contact_first_name = emergency_contact_first_name;
    }

    public String getEmergency_contact_last_name() {
        return emergency_contact_last_name;
    }

    public void setEmergency_contact_last_name(String emergency_contact_last_name) {
        this.emergency_contact_last_name = emergency_contact_last_name;
    }

    public String getEmergency_contact_no() {
        return emergency_contact_no;
    }

    public void setEmergency_contact_no(String emergency_contact_no) {
        this.emergency_contact_no = emergency_contact_no;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customer_id=" + customer_id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", date_of_birth=" + date_of_birth +
                ", gender='" + gender + '\'' +
                ", emergency_contact_first_name='" + emergency_contact_first_name + '\'' +
                ", emergency_contact_last_name='" + emergency_contact_last_name + '\'' +
                ", emergency_contact_no='" + emergency_contact_no + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}
