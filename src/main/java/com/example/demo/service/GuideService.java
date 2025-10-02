package com.example.demo.service;

import com.example.demo.dao.GuideDao;
import com.example.demo.dao.AuthDao;
import com.example.demo.model.Guide;
import com.example.demo.model.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GuideService {

    @Autowired
    private GuideDao guideDao;

    @Autowired
    private AuthDao authDao;

    // Create new guide (only if vendor is Guide_Provider)
    public Integer addGuide(Guide guide, Integer vendorId) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) {
            throw new RuntimeException("Vendor not found");
        }
        if (!"Guide_Provider".equalsIgnoreCase(vendor.getService_type())) {
            throw new RuntimeException("Only vendors with service type 'Guide_Provider' can add guides");
        }

        if (guide.getFirst_name() == null || guide.getFirst_name().isEmpty()) {
            throw new RuntimeException("First name is required");
        }
        if (guide.getPrimary_email() == null || guide.getPrimary_email().isEmpty()) {
            throw new RuntimeException("Primary email is required");
        }
        if (guide.getPrimary_phone() == null || guide.getPrimary_phone().isEmpty()) {
            throw new RuntimeException("Primary phone is required");
        }

        return guideDao.createGuide(guide, vendorId);
    }

    // Get all guides across all vendors
    public List<Guide> getAllGuides() {
        return guideDao.getAllGuides();
    }

    // Get all guides of a specific vendor
    public List<Guide> getGuidesByVendor(Integer vendorId) {
        return guideDao.getGuidesByVendor(vendorId);
    }

    // Get single guide of a vendor
    public Guide getGuideByVendorAndId(Integer vendorId, Integer guideId) {
        Guide guide = guideDao.getGuideByVendorAndId(vendorId, guideId);
        if (guide == null) {
            throw new RuntimeException("Guide not found");
        }
        return guide;
    }

    // Update a guide
    public int updateGuide(Integer vendorId, Integer guideId, Guide updatedGuide) {
        Vendor vendor = authDao.findVendorById(vendorId);   // ✅ reuse method
        if (vendor == null) {
            throw new RuntimeException("Vendor not found");
        }
        if (!"Guide_Provider".equalsIgnoreCase(vendor.getService_type())) {
            throw new RuntimeException("Only vendors with service type 'Guide_Provider' can update guides");
        }

        Guide existing = guideDao.getGuideByVendorAndId(vendorId, guideId);
        if (existing == null) {
            throw new RuntimeException("Guide not found");
        }

        return guideDao.updateGuide(vendorId, guideId, updatedGuide);
    }

    // Delete a guide
    public int deleteGuide(Integer vendorId, Integer guideId) {
        Vendor vendor = authDao.findVendorById(vendorId);   // ✅ reuse method
        if (vendor == null) {
            throw new RuntimeException("Vendor not found");
        }
        if (!"Guide_Provider".equalsIgnoreCase(vendor.getService_type())) {
            throw new RuntimeException("Only vendors with service type 'Guide_Provider' can delete guides");
        }

        Guide existing = guideDao.getGuideByVendorAndId(vendorId, guideId);
        if (existing == null) {
            throw new RuntimeException("Guide not found");
        }
        return guideDao.deleteGuide(vendorId, guideId);
    }
}
