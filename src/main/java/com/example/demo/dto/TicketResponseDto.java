package com.example.demo.dto;

public class TicketResponseDto {
    private String response_text;
    private Boolean is_customer_visible;

    public TicketResponseDto() {}

    // Getters and Setters
    public String getResponse_text() { return response_text; }
    public void setResponse_text(String response_text) { this.response_text = response_text; }
    public Boolean getIs_customer_visible() { return is_customer_visible; }
    public void setIs_customer_visible(Boolean is_customer_visible) { this.is_customer_visible = is_customer_visible; }

}
