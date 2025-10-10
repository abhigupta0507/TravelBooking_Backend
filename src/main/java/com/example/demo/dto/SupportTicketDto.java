package com.example.demo.dto;

public class SupportTicketDto {
    private String category;
    private String ticket_title;
    private String ticket_description;
    private Integer booking_id;

    public SupportTicketDto() {}

    // Getters and Setters
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getTicket_title() { return ticket_title; }
    public void setTicket_title(String ticket_title) { this.ticket_title = ticket_title; }
    public String getTicket_description() { return ticket_description; }
    public void setTicket_description(String ticket_description) { this.ticket_description = ticket_description; }
    public Integer getBooking_id() { return booking_id; }
    public void setBooking_id(Integer booking_id) { this.booking_id = booking_id; }
}
