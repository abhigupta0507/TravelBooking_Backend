package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Refund {
    private Integer payment_id;
    private Integer refund_id;
    private BigDecimal refund_amount;
    private BigDecimal processing_charges;
    private String refund_reason;
    private LocalDateTime processed_at;
    private String reference;
    private String refund_status; // ("PROCESSING","COMPLETED","REJECTED")

    public Refund() {}

    public Refund(Integer payment_id, Integer refund_id, BigDecimal refund_amount, BigDecimal processing_charges,
                  String refund_reason, LocalDateTime processed_at, String reference, String refund_status) {
        this.payment_id = payment_id;
        this.refund_id = refund_id;
        this.refund_amount = refund_amount;
        this.processing_charges = processing_charges;
        this.refund_reason = refund_reason;
        this.processed_at = processed_at;
        this.reference = reference;
        this.refund_status = refund_status;
    }

    public Refund(Integer payment_id, String refund_reason, String refund_status){
        this.refund_status = refund_status;
        this.refund_reason = refund_reason;
        this.payment_id = payment_id;
    }

    public Integer getPayment_id() { return payment_id; }
    public void setPayment_id(Integer payment_id) { this.payment_id = payment_id; }

    public Integer getRefund_id() { return refund_id; }
    public void setRefund_id(Integer refund_id) { this.refund_id = refund_id; }

    public BigDecimal getRefund_amount() { return refund_amount; }
    public void setRefund_amount(BigDecimal refund_amount) { this.refund_amount = refund_amount; }

    public BigDecimal getProcessing_charges() { return processing_charges; }
    public void setProcessing_charges(BigDecimal processing_charges) { this.processing_charges = processing_charges; }

    public String getRefund_reason() { return refund_reason; }
    public void setRefund_reason(String refund_reason) { this.refund_reason = refund_reason; }

    public LocalDateTime getProcessed_at() { return processed_at; }
    public void setProcessed_at(LocalDateTime processed_at) { this.processed_at = processed_at; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getRefund_status() { return refund_status; }
    public void setRefund_status(String refund_status) { this.refund_status = refund_status; }

    @Override
    public String toString() {
        return "Refund{" +
                "payment_id=" + payment_id +
                ", refund_id=" + refund_id +
                ", refund_amount=" + refund_amount +
                ", processing_charges=" + processing_charges +
                ", refund_reason='" + refund_reason + '\'' +
                ", processed_at=" + processed_at +
                ", reference='" + reference + '\'' +
                ", refund_status='" + refund_status + '\'' +
                '}';
    }
}
