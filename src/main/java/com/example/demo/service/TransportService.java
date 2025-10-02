package com.example.demo.service;

import com.example.demo.dao.TransportDao;
import com.example.demo.dao.AuthDao;
import com.example.demo.model.Transport;
import com.example.demo.model.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportService {

    @Autowired
    private TransportDao transportDao;

    @Autowired
    private AuthDao authDao;

    // ✅ Create new transport (only if vendor is Transport_Provider)
    public Integer addTransport(Transport transport, Integer vendorId) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) {
            throw new RuntimeException("Vendor not found");
        }
        if (!"Transport_Provider".equalsIgnoreCase(vendor.getService_type())) {
            throw new RuntimeException("Only vendors with service type 'Transport_Provider' can add transports");
        }

        if (transport.getFirst_name() == null || transport.getFirst_name().isEmpty()) {
            throw new RuntimeException("Driver first name is required");
        }
        if (transport.getLicense_no() == null || transport.getLicense_no().isEmpty()) {
            throw new RuntimeException("License number is required");
        }
        if (transport.getVehicle_reg_no() == null || transport.getVehicle_reg_no().isEmpty()) {
            throw new RuntimeException("Vehicle registration number is required");
        }

        return transportDao.createTransport(transport, vendorId);
    }

    // ✅ Get all transports across all vendors
    public List<Transport> getAllTransports() {
        return transportDao.getAllTransports();
    }

    // ✅ Get all transports of a specific vendor
    public List<Transport> getTransportsByVendor(Integer vendorId) {
        return transportDao.getTransportsByVendor(vendorId);
    }

    // ✅ Get single transport of a vendor
    public Transport getTransportByVendorAndId(Integer vendorId, Integer driverId) {
        Transport transport = transportDao.getTransportByVendorAndId(vendorId, driverId);
        if (transport == null) {
            throw new RuntimeException("Transport not found");
        }
        return transport;
    }

    // ✅ Update a transport
    public int updateTransport(Integer vendorId, Integer driverId, Transport updatedTransport) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) {
            throw new RuntimeException("Vendor not found");
        }
        if (!"Transport_Provider".equalsIgnoreCase(vendor.getService_type())) {
            throw new RuntimeException("Only vendors with service type 'Transport_Provider' can update transports");
        }

        Transport existing = transportDao.getTransportByVendorAndId(vendorId, driverId);
        if (existing == null) {
            throw new RuntimeException("Transport not found");
        }

        return transportDao.updateTransport(vendorId, driverId, updatedTransport);
    }

    // ✅ Delete a transport
    public int deleteTransport(Integer vendorId, Integer driverId) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) {
            throw new RuntimeException("Vendor not found");
        }
        if (!"Transport_Provider".equalsIgnoreCase(vendor.getService_type())) {
            throw new RuntimeException("Only vendors with service type 'Transport_Provider' can delete transports");
        }

        Transport existing = transportDao.getTransportByVendorAndId(vendorId, driverId);
        if (existing == null) {
            throw new RuntimeException("Transport not found");
        }

        return transportDao.deleteTransport(vendorId, driverId);
    }
}
