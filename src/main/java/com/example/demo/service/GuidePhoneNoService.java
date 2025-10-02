package com.example.demo.service;

import com.example.demo.dao.GuidePhoneNoDao;
import com.example.demo.dao.AuthDao;
import com.example.demo.dao.GuideDao;
import com.example.demo.model.Vendor;
import com.example.demo.model.Guide;
import com.example.demo.model.GuidePhoneNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuidePhoneNoService {

    @Autowired
    private GuidePhoneNoDao guidePhoneNoDao;

    @Autowired
    private AuthDao authDao;

    @Autowired
    private GuideDao guideDao;

    // Add additional phone number for a guide
    public int addGuidePhoneNo(Integer vendorId, Integer guideId, String phoneNo) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) throw new RuntimeException("Vendor not found");
        if (!"Guide_Provider".equalsIgnoreCase(vendor.getService_type()))
            throw new RuntimeException("Only vendors with service type 'Guide_Provider' can add guide phone numbers");

        // Ensure the guide belongs to this vendor
        Guide guide = guideDao.getGuideByVendorAndId(vendorId, guideId);
        if (guide == null) throw new RuntimeException("Guide not found for this vendor");

        if (phoneNo == null || phoneNo.isEmpty()) throw new RuntimeException("Phone number cannot be empty");

        return guidePhoneNoDao.addGuidePhoneNo(guideId, phoneNo);
    }

    // Get all phone numbers of a guide
    public List<GuidePhoneNo> getPhoneNosByGuide(Integer vendorId, Integer guideId) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) throw new RuntimeException("Vendor not found");
        if (!"Guide_Provider".equalsIgnoreCase(vendor.getService_type()))
            throw new RuntimeException("Only vendors with service type 'Guide_Provider' can view guide phone numbers");

        // Ensure the guide belongs to this vendor
        Guide guide = guideDao.getGuideByVendorAndId(vendorId, guideId);
        if (guide == null) throw new RuntimeException("Guide not found for this vendor");

        return guidePhoneNoDao.getPhoneNosByGuide(guideId);
    }

    // Delete a specific phone number of a guide
    public int deleteGuidePhoneNo(Integer vendorId, Integer guideId, String phoneNo) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) throw new RuntimeException("Vendor not found");
        if (!"Guide_Provider".equalsIgnoreCase(vendor.getService_type()))
            throw new RuntimeException("Only vendors with service type 'Guide_Provider' can delete guide phone numbers");

        Guide guide = guideDao.getGuideByVendorAndId(vendorId, guideId);
        if (guide == null) throw new RuntimeException("Guide not found for this vendor");

        int deleted = guidePhoneNoDao.deleteGuidePhoneNo(guideId, phoneNo);
        if (deleted == 0) {
            throw new RuntimeException("No such phone number found for this guide");
        }
        return deleted;
    }
}
