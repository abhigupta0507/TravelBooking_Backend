package com.example.demo.dto;

public class ChangeStatusDto {
    private String prevStatus;
    private String newStatus;

    public ChangeStatusDto(String prevStatus, String newStatus) {
        this.prevStatus = prevStatus;
        this.newStatus = newStatus;
    }

    public String getPrevStatus() {
        return prevStatus;
    }

    public void setPrevStatus(String prevStatus) {
        this.prevStatus = prevStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }
}
