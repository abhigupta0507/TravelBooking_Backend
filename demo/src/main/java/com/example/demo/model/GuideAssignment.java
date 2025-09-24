package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class GuideAssignment {

    private Integer assignment_id;
    private LocalDate assignment_date;
    private Integer duration;
    private LocalDate start_date;
    private LocalTime start_time;
    private LocalDate end_date;
    private LocalTime end_time;
    private BigDecimal cost;
    private String status;
    private Integer package_booking_id;
    private Integer guide_id;
    private Integer item_id;
    private Integer package_id;

    public GuideAssignment() {}

    public GuideAssignment(Integer assignment_id, LocalDate assignment_date, Integer duration,
                            LocalDate start_date, LocalTime start_time, LocalDate end_date, LocalTime end_time,
                            BigDecimal cost, String status, Integer package_booking_id, Integer guide_id,
                            Integer item_id, Integer package_id) {
        this.assignment_id = assignment_id;
        this.assignment_date = assignment_date;
        this.duration = duration;
        this.start_date = start_date;
        this.start_time = start_time;
        this.end_date = end_date;
        this.end_time = end_time;
        this.cost = cost;
        this.status = status;
        this.package_booking_id = package_booking_id;
        this.guide_id = guide_id;
        this.item_id = item_id;
        this.package_id = package_id;
    }

    public Integer getAssignment_id() { return assignment_id; }
    public void setAssignment_id(Integer assignment_id) { this.assignment_id = assignment_id; }

    public LocalDate getAssignment_date() { return assignment_date; }
    public void setAssignment_date(LocalDate assignment_date) { this.assignment_date = assignment_date; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public LocalDate getStart_date() { return start_date; }
    public void setStart_date(LocalDate start_date) { this.start_date = start_date; }

    public LocalTime getStart_time() { return start_time; }
    public void setStart_time(LocalTime start_time) { this.start_time = start_time; }

    public LocalDate getEnd_date() { return end_date; }
    public void setEnd_date(LocalDate end_date) { this.end_date = end_date; }

    public LocalTime getEnd_time() { return end_time; }
    public void setEnd_time(LocalTime end_time) { this.end_time = end_time; }

    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getPackage_booking_id() { return package_booking_id; }
    public void setPackage_booking_id(Integer package_booking_id) { this.package_booking_id = package_booking_id; }

    public Integer getGuide_id() { return guide_id; }
    public void setGuide_id(Integer guide_id) { this.guide_id = guide_id; }

    public Integer getItem_id() { return item_id; }
    public void setItem_id(Integer item_id) { this.item_id = item_id; }

    public Integer getPackage_id() { return package_id; }
    public void setPackage_id(Integer package_id) { this.package_id = package_id; }

    @Override
    public String toString() {
        return "Guide_Assignment{" +
                "assignment_id=" + assignment_id +
                ", assignment_date=" + assignment_date +
                ", duration=" + duration +
                ", start_date=" + start_date +
                ", start_time=" + start_time +
                ", end_date=" + end_date +
                ", end_time=" + end_time +
                ", cost=" + cost +
                ", status='" + status + '\'' +
                ", package_booking_id=" + package_booking_id +
                ", guide_id=" + guide_id +
                ", item_id=" + item_id +
                ", package_id=" + package_id +
                '}';
    }
}
