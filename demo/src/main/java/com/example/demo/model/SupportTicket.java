package com.example.demo.model;

import java.time.LocalDateTime;

public class SupportTicket {
    private Integer ticket_id;
    private String category;
    private String status;
    private String priority;
    private String customer_satisfaction;
    private LocalDateTime created_at;
    private LocalDateTime resolved_at;
    private String ticket_title;
    private String ticket_description;
    private Integer booking_id;

    public SupportTicket() {}

    public SupportTicket(Integer ticket_id, String category, String status, String priority,
                          String customer_satisfaction, LocalDateTime created_at, LocalDateTime resolved_at,
                          String ticket_title, String ticket_description, Integer booking_id) {
        this.ticket_id = ticket_id;
        this.category = category;
        this.status = status;
        this.priority = priority;
        this.customer_satisfaction = customer_satisfaction;
        this.created_at = created_at;
        this.resolved_at = resolved_at;
        this.ticket_title = ticket_title;
        this.ticket_description = ticket_description;
        this.booking_id = booking_id;
    }

    public Integer getTicket_id() { return ticket_id; }
    public void setTicket_id(Integer ticket_id) { this.ticket_id = ticket_id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getCustomer_satisfaction() { return customer_satisfaction; }
    public void setCustomer_satisfaction(String customer_satisfaction) { this.customer_satisfaction = customer_satisfaction; }

    public LocalDateTime getCreated_at() { return created_at; }
    public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }

    public LocalDateTime getResolved_at() { return resolved_at; }
    public void setResolved_at(LocalDateTime resolved_at) { this.resolved_at = resolved_at; }

    public String getTicket_title() { return ticket_title; }
    public void setTicket_title(String ticket_title) { this.ticket_title = ticket_title; }

    public String getTicket_description() { return ticket_description; }
    public void setTicket_description(String ticket_description) { this.ticket_description = ticket_description; }

    public Integer getBooking_id() { return booking_id; }
    public void setBooking_id(Integer booking_id) { this.booking_id = booking_id; }

    @Override
    public String toString() {
        return "Support_Ticket{" +
                "ticket_id=" + ticket_id +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", customer_satisfaction='" + customer_satisfaction + '\'' +
                ", created_at=" + created_at +
                ", resolved_at=" + resolved_at +
                ", ticket_title='" + ticket_title + '\'' +
                ", ticket_description='" + ticket_description + '\'' +
                ", booking_id=" + booking_id +
                '}';
    }
}
