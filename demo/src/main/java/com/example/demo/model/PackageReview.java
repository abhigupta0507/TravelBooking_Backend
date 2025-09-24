package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PackageReview {
    private Integer review_id;
    private BigDecimal overall_rating;
    private String title;
    private String body;
    private Boolean could_recommend;
    private LocalDateTime created_at;
    private Integer package_booking_id;

    public PackageReview() {}

    public PackageReview(Integer review_id, BigDecimal overall_rating, String title, String body,
                          Boolean could_recommend, LocalDateTime created_at, Integer package_booking_id) {
        this.review_id = review_id;
        this.overall_rating = overall_rating;
        this.title = title;
        this.body = body;
        this.could_recommend = could_recommend;
        this.created_at = created_at;
        this.package_booking_id = package_booking_id;
    }

    public Integer getReview_id() {
        return review_id;
    }

    public void setReview_id(Integer review_id) {
        this.review_id = review_id;
    }

    public BigDecimal getOverall_rating() {
        return overall_rating;
    }

    public void setOverall_rating(BigDecimal overall_rating) {
        this.overall_rating = overall_rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getCould_recommend() {
        return could_recommend;
    }

    public void setCould_recommend(Boolean could_recommend) {
        this.could_recommend = could_recommend;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public Integer getPackage_booking_id() {
        return package_booking_id;
    }

    public void setPackage_booking_id(Integer package_booking_id) {
        this.package_booking_id = package_booking_id;
    }

    @Override
    public String toString() {
        return "Package_Review{" +
                "review_id=" + review_id +
                ", overall_rating=" + overall_rating +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", could_recommend=" + could_recommend +
                ", created_at=" + created_at +
                ", package_booking_id=" + package_booking_id +
                '}';
    }
}
