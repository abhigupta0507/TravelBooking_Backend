package com.example.demo.service;

import com.example.demo.dao.PackageDAO;
import com.example.demo.dto.PackageDetailDto;
import com.example.demo.dto.PackageStatus;
import com.example.demo.model.ItineraryItem;
import com.example.demo.model.TourPackage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageService {
    public PackageService(PackageDAO packageDAO) {
        this.packageDAO = packageDAO;
    }

    private PackageDAO packageDAO;

    public List<TourPackage> findAllPackages() {
        return packageDAO.findAllPackages();
    }

    public  List<TourPackage> findPackagesByStatus(PackageStatus status){
        return packageDAO.findAllPackagesByStatus(status);
    }

    public PackageDetailDto findPackageBySlug(String slug){
        TourPackage thePackage = packageDAO.findPackageBySlug(slug);
        List<ItineraryItem> theItems = packageDAO.findAllItineraryItemsByPackageId(thePackage.getPackageId());
        return PackageDetailDto.from(thePackage,theItems);
    }
}
