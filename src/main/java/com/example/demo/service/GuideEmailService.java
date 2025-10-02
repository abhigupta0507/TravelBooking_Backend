package com.example.demo.service;

import com.example.demo.dao.GuideEmailDao;
import com.example.demo.dao.AuthDao;
import com.example.demo.dao.GuideDao;
import com.example.demo.model.Vendor;
import com.example.demo.model.Guide;
import com.example.demo.model.GuideEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuideEmailService {

    @Autowired
    private GuideEmailDao guideEmailDao;

    @Autowired
    private AuthDao authDao;

    @Autowired
    private GuideDao guideDao;

    // Add additional email for a guide
    public int addGuideEmail(Integer vendorId, Integer guideId, String email) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) throw new RuntimeException("Vendor not found");
        if (!"Guide_Provider".equalsIgnoreCase(vendor.getService_type()))
            throw new RuntimeException("Only vendors with service type 'Guide_Provider' can add guide emails");

        // Ensure the guide belongs to this vendor
        Guide guide = guideDao.getGuideByVendorAndId(vendorId, guideId);
        if (guide == null) throw new RuntimeException("Guide not found for this vendor");

        if (email == null || email.isEmpty()) throw new RuntimeException("Email cannot be empty");

        return guideEmailDao.addGuideEmail(guideId, email);
    }

    // Get all emails of a guide
    public List<GuideEmail> getEmailsByGuide(Integer vendorId, Integer guideId) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) throw new RuntimeException("Vendor not found");
        if (!"Guide_Provider".equalsIgnoreCase(vendor.getService_type()))
            throw new RuntimeException("Only vendors with service type 'Guide_Provider' can view guide emails");

        // Ensure the guide belongs to this vendor
        Guide guide = guideDao.getGuideByVendorAndId(vendorId, guideId);
        if (guide == null) throw new RuntimeException("Guide not found for this vendor");

        return guideEmailDao.getEmailsByGuide(guideId);
    }

    // Delete a specific email of a guide
    public int deleteGuideEmail(Integer vendorId, Integer guideId, String email) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) throw new RuntimeException("Vendor not found");
        if (!"Guide_Provider".equalsIgnoreCase(vendor.getService_type()))
            throw new RuntimeException("Only vendors with service type 'Guide_Provider' can delete guide emails");

        Guide guide = guideDao.getGuideByVendorAndId(vendorId, guideId);
        if (guide == null) throw new RuntimeException("Guide not found for this vendor");

        // Prevent deletion of primary email

        int deleted = guideEmailDao.deleteGuideEmail(guideId, email);
        if (deleted == 0) {
            throw new RuntimeException("No such email found for this guide");
        }
        return deleted;
    }
}
