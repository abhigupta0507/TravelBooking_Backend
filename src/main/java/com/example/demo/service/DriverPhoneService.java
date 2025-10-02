package com.example.demo.service;

import com.example.demo.dao.AuthDao;
import com.example.demo.dao.TransportDao;
import com.example.demo.dao.DriverPhoneDao;
import com.example.demo.model.Vendor;
import com.example.demo.model.Transport;
import com.example.demo.model.DriverPhone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverPhoneService {

    @Autowired
    private DriverPhoneDao driverPhoneDao;

    @Autowired
    private AuthDao authDao;

    @Autowired
    private TransportDao transportDao;

    // ✅ Add phone number for a driver
    public int addDriverPhone(Integer vendorId, Integer driverId, String phone) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) throw new RuntimeException("Vendor not found");
        if (!"Transport_Provider".equalsIgnoreCase(vendor.getService_type()))
            throw new RuntimeException("Only vendors with service type 'Transport_Provider' can add driver phone numbers");

        // Ensure the driver belongs to this vendor
        Transport driver = transportDao.getTransportByVendorAndId(vendorId, driverId);
        if (driver == null) throw new RuntimeException("Driver not found for this vendor");

        if (phone == null || phone.isEmpty()) throw new RuntimeException("Phone number cannot be empty");

        return driverPhoneDao.addDriverPhone(driverId, phone);
    }

    // ✅ Get all phone numbers of a driver
    public List<DriverPhone> getPhonesByDriver(Integer vendorId, Integer driverId) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) throw new RuntimeException("Vendor not found");
        if (!"Transport_Provider".equalsIgnoreCase(vendor.getService_type()))
            throw new RuntimeException("Only vendors with service type 'Transport_Provider' can view driver phone numbers");

        Transport driver = transportDao.getTransportByVendorAndId(vendorId, driverId);
        if (driver == null) throw new RuntimeException("Driver not found for this vendor");

        return driverPhoneDao.getPhonesByDriver(driverId);
    }

    // ✅ Delete a specific phone number of a driver
    public int deleteDriverPhone(Integer vendorId, Integer driverId, String phone) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) throw new RuntimeException("Vendor not found");
        if (!"Transport_Provider".equalsIgnoreCase(vendor.getService_type()))
            throw new RuntimeException("Only vendors with service type 'Transport_Provider' can delete driver phone numbers");

        Transport driver = transportDao.getTransportByVendorAndId(vendorId, driverId);
        if (driver == null) throw new RuntimeException("Driver not found for this vendor");

        int deleted = driverPhoneDao.deleteDriverPhone(driverId, phone);
        if (deleted == 0) {
            throw new RuntimeException("No such phone number found for this driver");
        }
        return deleted;
    }
}
