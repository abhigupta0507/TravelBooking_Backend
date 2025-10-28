package com.example.demo.dto;

public class AddressDto {
    private String street;
    private String city;
    private String state;
    private String pin;

    public AddressDto(String street, String city, String state, String pin) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.pin = pin;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
