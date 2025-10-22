package com.example.demo.dto;

import com.example.demo.model.PackageBooking;
import com.example.demo.model.Traveller;

import java.util.List;

public class PackageBookingRequest {
    private List<Traveller> travellers;
    private PackageBooking packageBooking;

    public PackageBookingRequest(List<Traveller> travellers, PackageBooking packageBooking) {
        this.travellers = travellers;
        this.packageBooking = packageBooking;
    }

    public List<Traveller> getTravellers() {
        return travellers;
    }

    public void setTravellers(List<Traveller> travellers) {
        this.travellers = travellers;
    }

    public PackageBooking getPackageBooking() {
        return packageBooking;
    }

    public void setPackageBooking(PackageBooking packageBooking) {
        this.packageBooking = packageBooking;
    }
}
