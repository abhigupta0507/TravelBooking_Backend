package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * A DTO for handling the creation of a new refund request.
 * It contains the necessary information provided by the client.
 */
public class RefundRequestDto {


    @NotBlank(message = "A reason for the refund is required.")
    private String refund_reason;

    // --- Constructors ---

    public RefundRequestDto() {
    }

    public RefundRequestDto(Integer paymentId, BigDecimal refundAmount, String refundReason) {
        this.refund_reason = refundReason;
    }

    public String getRefund_reason() {
        return refund_reason;
    }

    public void setRefund_reason(String refund_reason) {
        this.refund_reason = refund_reason;
    }
}
