package com.example.demo.model;

import java.time.LocalDateTime;

public class TicketResponse {
    private Integer response_id;
    private String sender;
    private String response_text;
    private String response_type;
    private Boolean is_customer_visible;
    private LocalDateTime created_at;
    private Integer ticket_id;
    private Integer staff_id;

    public TicketResponse() {}

    public TicketResponse(Integer response_id, String sender, String response_text, String response_type,
                           Boolean is_customer_visible, LocalDateTime created_at, Integer ticket_id, Integer staff_id) {
        this.response_id = response_id;
        this.sender = sender;
        this.response_text = response_text;
        this.response_type = response_type;
        this.is_customer_visible = is_customer_visible;
        this.created_at = created_at;
        this.ticket_id = ticket_id;
        this.staff_id = staff_id;
    }

    public Integer getResponse_id() { return response_id; }
    public void setResponse_id(Integer response_id) { this.response_id = response_id; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getResponse_text() { return response_text; }
    public void setResponse_text(String response_text) { this.response_text = response_text; }

    public String getResponse_type() { return response_type; }
    public void setResponse_type(String response_type) { this.response_type = response_type; }

    public Boolean getIs_customer_visible() { return is_customer_visible; }
    public void setIs_customer_visible(Boolean is_customer_visible) { this.is_customer_visible = is_customer_visible; }

    public LocalDateTime getCreated_at() { return created_at; }
    public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }

    public Integer getTicket_id() { return ticket_id; }
    public void setTicket_id(Integer ticket_id) { this.ticket_id = ticket_id; }

    public Integer getStaff_id() { return staff_id; }
    public void setStaff_id(Integer staff_id) { this.staff_id = staff_id; }

    @Override
    public String toString() {
        return "Ticket_Response{" +
                "response_id=" + response_id +
                ", sender='" + sender + '\'' +
                ", response_text='" + response_text + '\'' +
                ", response_type='" + response_type + '\'' +
                ", is_customer_visible=" + is_customer_visible +
                ", created_at=" + created_at +
                ", ticket_id=" + ticket_id +
                ", staff_id=" + staff_id +
                '}';
    }
}
