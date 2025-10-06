package com.example.demo.service;

import com.example.demo.dao.GuidePhoneNoDao;
import com.example.demo.dao.AuthDao;
import com.example.demo.dao.GuideDao;
import com.example.demo.dao.VendorPhoneDao;
import com.example.demo.model.Vendor;
import com.example.demo.model.Guide;
import com.example.demo.model.GuidePhoneNo;
import com.example.demo.model.VendorPhone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorPhoneService {

    @Autowired
    private VendorPhoneDao vendorPhoneDao;

    @Autowired
    private AuthDao authDao;

    @Autowired
    private GuideDao guideDao;

    public VendorPhoneService(VendorPhoneDao vendorPhoneDao, AuthDao authDao, GuideDao guideDao) {
        this.vendorPhoneDao = vendorPhoneDao;
        this.authDao = authDao;
        this.guideDao = guideDao;
    }

    // Add additional phone number for a guide
    public int addVendorPhoneNo(Integer vendorId, String phoneNo) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) throw new RuntimeException("Vendor not found");
        if (phoneNo == null || phoneNo.isEmpty()) throw new RuntimeException("Phone number cannot be empty");

        return vendorPhoneDao.addVendorPhoneNo(vendorId, phoneNo);
    }

    // Get all phone numbers of a vendor
    public List<VendorPhone> getPhoneNosByGuide(Integer vendorId) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) throw new RuntimeException("Vendor not found");

        return vendorPhoneDao.getPhoneNosByVendor(vendorId);
    }

    // Delete a specific phone number of a vendor
    public int deleteGuidePhoneNo(Integer vendorId, String phoneNo) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) throw new RuntimeException("Vendor not found");

        int deleted = vendorPhoneDao.deleteVendorPhoneNo(vendorId, phoneNo);
        if (deleted == 0) {
            throw new RuntimeException("No such phone number found for this vendor");
        }
        return deleted;
    }
}
