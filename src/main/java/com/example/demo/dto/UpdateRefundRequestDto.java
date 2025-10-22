package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for an admin to update the status and reference of a refund.
 */
public class UpdateRefundRequestDto {

    @NotBlank(message = "A new refund status is required (e.g., COMPLETED, REJECTED).")
    private String status;

    private String reference;

    // --- Getters and Setters ---
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
