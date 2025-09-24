package com.example.demo.model;

public class GuidePhoneNo {
    private Integer guide_id;
    private String phone_no;

    public GuidePhoneNo() {}

    public GuidePhoneNo(Integer guide_id, String phone_no) {
        this.guide_id = guide_id;
        this.phone_no = phone_no;
    }

    public Integer getGuide_id() {
        return guide_id;
    }

    public void setGuide_id(Integer guide_id) {
        this.guide_id = guide_id;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    @Override
    public String toString() {
        return "Guide_Phone_No{" +
                "guide_id=" + guide_id +
                ", phone_no='" + phone_no + '\'' +
                '}';
    }
}
