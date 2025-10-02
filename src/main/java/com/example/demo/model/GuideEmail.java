package com.example.demo.model;

public class GuideEmail {
    private Integer guide_id;
    private String email;

    public GuideEmail() {}

    public GuideEmail(Integer guide_id, String email) {
        this.guide_id = guide_id;
        this.email = email;
    }

    public Integer getGuide_id() {
        return guide_id;
    }

    public void setGuide_id(Integer guide_id) {
        this.guide_id = guide_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "GuideEmail{" +
                "guide_id=" + guide_id +
                ", email='" + email + '\'' +
                '}';
    }
}
