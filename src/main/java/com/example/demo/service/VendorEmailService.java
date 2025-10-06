package com.example.demo.service;

import com.example.demo.dao.GuideEmailDao;
import com.example.demo.dao.AuthDao;
import com.example.demo.dao.GuideDao;
import com.example.demo.dao.VendorEmailDao;
import com.example.demo.model.Vendor;
import com.example.demo.model.Guide;
import com.example.demo.model.GuideEmail;
import com.example.demo.model.VendorEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorEmailService {

    @Autowired
    private VendorEmailDao vendorEmailDao;

    @Autowired
    private AuthDao authDao;

    public VendorEmailService(VendorEmailDao vendorEmailDao, AuthDao authDao) {
        this.vendorEmailDao = vendorEmailDao;
        this.authDao = authDao;
    }

    // Add additional email for a guide
    public int addVendorEmail(Integer vendorId, String email) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) throw new RuntimeException("Vendor not found");

        if (email == null || email.isEmpty()) throw new RuntimeException("Email cannot be empty");

        return vendorEmailDao.addVendorEmail(vendorId, email);
    }

    // Get all emails of a vendor
    public List<VendorEmail> getEmailsByVendor(Integer vendorId) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) throw new RuntimeException("Vendor not found");

        return vendorEmailDao.getEmailsByVendor(vendorId);
    }

    // Delete a specific email of a vendor
    public int deleteVendorEmail(Integer vendorId, String email) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) throw new RuntimeException("Vendor not found");


        int deleted = vendorEmailDao.deleteVendorEmail(vendorId, email);
        if (deleted == 0) {
            throw new RuntimeException("No such email found for this vendor");
        }
        return deleted;
    }
}
