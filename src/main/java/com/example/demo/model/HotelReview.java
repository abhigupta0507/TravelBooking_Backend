package com.example.demo.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class HotelReview {

    private Integer hotel_review_id;
    private BigDecimal overall_rating;
    private BigDecimal cleanliness_rating;
    private String review_title;
    private String review_body;
    private Date stay_date;
    private LocalDateTime created_at;
    private Integer hotel_booking_id;

    public HotelReview() {}

    public HotelReview(Integer hotel_review_id, BigDecimal overall_rating, BigDecimal cleanliness_rating,
                       String review_title, String review_body, Date stay_date,
                       LocalDateTime created_at, Integer hotel_booking_id) {
        this.hotel_review_id = hotel_review_id;
        this.overall_rating = overall_rating;
        this.cleanliness_rating = cleanliness_rating;
        this.review_title = review_title;
        this.review_body = review_body;
        this.stay_date = stay_date;
        this.created_at = created_at;
        this.hotel_booking_id = hotel_booking_id;
    }

    public Integer getHotel_review_id() { return hotel_review_id; }
    public void setHotel_review_id(Integer hotel_review_id) { this.hotel_review_id = hotel_review_id; }

    public BigDecimal getOverall_rating() { return overall_rating; }
    public void setOverall_rating(BigDecimal overall_rating) { this.overall_rating = overall_rating; }

    public BigDecimal getCleanliness_rating() { return cleanliness_rating; }
    public void setCleanliness_rating(BigDecimal cleanliness_rating) { this.cleanliness_rating = cleanliness_rating; }

    public String getReview_title() { return review_title; }
    public void setReview_title(String review_title) { this.review_title = review_title; }

    public String getReview_body() { return review_body; }
    public void setReview_body(String review_body) { this.review_body = review_body; }

    public Date getStay_date() { return stay_date; }
    public void setStay_date(Date stay_date) { this.stay_date = stay_date; }

    public LocalDateTime getCreated_at() { return created_at; }
    public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }

    public Integer getHotel_booking_id() { return hotel_booking_id; }
    public void setHotel_booking_id(Integer hotel_booking_id) { this.hotel_booking_id = hotel_booking_id; }

    @Override
    public String toString() {
        return "HotelReview{" +
                "hotel_review_id=" + hotel_review_id +
                ", overall_rating=" + overall_rating +
                ", cleanliness_rating=" + cleanliness_rating +
                ", review_title='" + review_title + '\'' +
                ", review_body='" + review_body + '\'' +
                ", stay_date=" + stay_date +
                ", created_at=" + created_at +
                ", hotel_booking_id=" + hotel_booking_id +
                '}';
    }
}
