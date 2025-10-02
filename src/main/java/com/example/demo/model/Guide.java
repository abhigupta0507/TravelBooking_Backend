package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Guide {

    private Integer guide_id;
    private String first_name;
    private String last_name;
    private Integer experience;
    private BigDecimal cost_per_hour;
    private String profile_photo;
    private Boolean availability;
    private LocalDateTime created_at;
    private Integer vendor_id;
    private String primary_email;
    private String primary_phone;
    private String primary_language;



    public Guide() {}

    public Guide(Integer guide_id, String first_name, String last_name, Integer experience, BigDecimal cost_per_hour,
                 String profile_photo, Boolean availability, LocalDateTime created_at, Integer vendor_id, String primary_email,String primary_phone,String primary_language) {
        this.guide_id = guide_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.experience = experience;
        this.cost_per_hour = cost_per_hour;
        this.profile_photo = profile_photo;
        this.availability = availability;
        this.created_at = created_at;
        this.vendor_id = vendor_id;
        this.primary_email=primary_email;
        this.primary_language=primary_language;
        this.primary_phone=primary_phone;
    }

    public Integer getGuide_id() {
        return guide_id;
    }

    public void setGuide_id(Integer guide_id) {
        this.guide_id = guide_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public BigDecimal getCost_per_hour() {
        return cost_per_hour;
    }

    public void setCost_per_hour(BigDecimal cost_per_hour) {
        this.cost_per_hour = cost_per_hour;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public Integer getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(Integer vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getPrimary_language() {
        return primary_language;
    }

    public void setPrimary_language(String primary_language) {
        this.primary_language = primary_language;
    }

    public String getPrimary_phone() {
        return primary_phone;
    }

    public void setPrimary_phone(String primary_phone) {
        this.primary_phone = primary_phone;
    }

    public String getPrimary_email() {
        return primary_email;
    }

    public void setPrimary_email(String primary_email) {
        this.primary_email = primary_email;
    }

    @Override
    public String toString() {
        return "Guide{" +
                "guide_id=" + guide_id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", experience=" + experience +
                ", cost_per_hour=" + cost_per_hour +
                ", profile_photo='" + profile_photo + '\'' +
                ", availability=" + availability +
                ", created_at=" + created_at +
                ", vendor_id=" + vendor_id +
                ", primary_email='" + primary_email + '\'' +
                ", primary_phone='" + primary_phone + '\'' +
                ", primary_language='" + primary_language + '\'' +
                '}';
    }


}
