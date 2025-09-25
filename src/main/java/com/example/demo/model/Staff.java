package com.example.demo.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Staff extends User {

    private Integer staff_id;
    private String employee_code;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private Date joining_date;
    private String password;
    private String role;
    private BigDecimal salary;

    public Staff() {
        super();
    }

    public Staff(String password) {
        super();
        this.password = password;
    }

    public Staff(Integer staff_id, String employee_code, String first_name, String last_name,
                 String email, String phone, Date joining_date, String password, String role, BigDecimal salary) {
        super(email, phone, password, "STAFF");
        this.staff_id = staff_id;
        this.employee_code = employee_code;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.joining_date = joining_date;
        this.password = password;
        this.role = role;
        this.salary = salary;
    }

    // Getters and Setters
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
        super.setPassword(password);
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
        super.setEmail(email);
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
        super.setPhone(phone);
    }

    public Integer getStaff_id() { return staff_id; }
    public void setStaff_id(Integer staff_id) { this.staff_id = staff_id; }

    public String getEmployee_code() { return employee_code; }
    public void setEmployee_code(String employee_code) { this.employee_code = employee_code; }

    public String getFirst_name() { return first_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }

    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }

    public Date getJoining_date() { return joining_date; }
    public void setJoining_date(Date joining_date) { this.joining_date = joining_date; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }

    @Override
    public String toString() {
        return "Staff{" +
                "staff_id=" + staff_id +
                ", employee_code='" + employee_code + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", joining_date=" + joining_date +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", salary=" + salary +
                ", userType='" + getUserType() + '\'' +
                '}';
    }
}