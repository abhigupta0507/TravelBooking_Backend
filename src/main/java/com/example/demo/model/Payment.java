package com.example.demo.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Payment {

    private Integer payment_id;
    private String booking_type;
    private Timestamp payment_datetime;
    private BigDecimal amount;
    private String payment_mode;
    private String status;
    private String transaction_reference;
    private BigDecimal refund_amount;

    public Payment() {}

    public Payment(Integer payment_id, String booking_type, Timestamp payment_datetime,
                   BigDecimal amount, String payment_mode, String status,
                   String transaction_reference, BigDecimal refund_amount) {
        this.payment_id = payment_id;
        this.booking_type = booking_type;
        this.payment_datetime = payment_datetime;
        this.amount = amount;
        this.payment_mode = payment_mode;
        this.status = status;
        this.transaction_reference = transaction_reference;
        this.refund_amount = refund_amount;
    }

    public Integer getPayment_id() { return payment_id; }
    public void setPayment_id(Integer payment_id) { this.payment_id = payment_id; }

    public String getBooking_type() { return booking_type; }
    public void setBooking_type(String booking_type) { this.booking_type = booking_type; }

    public Timestamp getPayment_datetime() { return payment_datetime; }
    public void setPayment_datetime(Timestamp payment_datetime) { this.payment_datetime = payment_datetime; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPayment_mode() { return payment_mode; }
    public void setPayment_mode(String payment_mode) { this.payment_mode = payment_mode; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTransaction_reference() { return transaction_reference; }
    public void setTransaction_reference(String transaction_reference) { this.transaction_reference = transaction_reference; }

    public BigDecimal getRefund_amount() { return refund_amount; }
    public void setRefund_amount(BigDecimal refund_amount) { this.refund_amount = refund_amount; }

    @Override
    public String toString() {
        return "Payment{" +
                "payment_id=" + payment_id +
                ", booking_type='" + booking_type + '\'' +
                ", payment_datetime=" + payment_datetime +
                ", amount=" + amount +
                ", payment_mode='" + payment_mode + '\'' +
                ", status='" + status + '\'' +
                ", transaction_reference='" + transaction_reference + '\'' +
                ", refund_amount=" + refund_amount +
                '}';
    }
}
