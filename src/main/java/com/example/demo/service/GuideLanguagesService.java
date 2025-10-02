package com.example.demo.service;

import com.example.demo.dao.GuideLanguagesDao;
import com.example.demo.dao.AuthDao;
import com.example.demo.dao.GuideDao;
import com.example.demo.model.Vendor;
import com.example.demo.model.Guide;
import com.example.demo.model.GuideLanguages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuideLanguagesService {

    @Autowired
    private GuideLanguagesDao guideLanguageDao;

    @Autowired
    private AuthDao authDao;

    @Autowired
    private GuideDao guideDao;

    // Add a language for a guide
    public int addGuideLanguage(Integer vendorId, Integer guideId, String language) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) throw new RuntimeException("Vendor not found");
        if (!"Guide_Provider".equalsIgnoreCase(vendor.getService_type()))
            throw new RuntimeException("Only vendors with service type 'Guide_Provider' can add guide languages");

        // Ensure the guide belongs to this vendor
        Guide guide = guideDao.getGuideByVendorAndId(vendorId, guideId);
        if (guide == null) throw new RuntimeException("Guide not found for this vendor");

        if (language == null || language.isEmpty()) throw new RuntimeException("Language cannot be empty");

        return guideLanguageDao.addGuideLanguage(guideId, language);
    }

    // Get all languages of a guide
    public List<GuideLanguages> getLanguagesByGuide(Integer vendorId, Integer guideId) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) throw new RuntimeException("Vendor not found");
        if (!"Guide_Provider".equalsIgnoreCase(vendor.getService_type()))
            throw new RuntimeException("Only vendors with service type 'Guide_Provider' can view guide languages");

        // Ensure the guide belongs to this vendor
        Guide guide = guideDao.getGuideByVendorAndId(vendorId, guideId);
        if (guide == null) throw new RuntimeException("Guide not found for this vendor");

        return guideLanguageDao.getLanguagesByGuide(guideId);
    }

    // Delete a specific language of a guide
    public int deleteGuideLanguage(Integer vendorId, Integer guideId, String language) {
        Vendor vendor = authDao.findVendorById(vendorId);
        if (vendor == null) throw new RuntimeException("Vendor not found");
        if (!"Guide_Provider".equalsIgnoreCase(vendor.getService_type()))
            throw new RuntimeException("Only vendors with service type 'Guide_Provider' can delete guide languages");

        Guide guide = guideDao.getGuideByVendorAndId(vendorId, guideId);
        if (guide == null) throw new RuntimeException("Guide not found for this vendor");


        int deleted = guideLanguageDao.deleteGuideLanguage(guideId, language);
        if (deleted == 0) {
            throw new RuntimeException("No such language found for this guide");
        }
        return deleted;
    }
}
