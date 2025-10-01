package com.example.demo.service;

import com.example.demo.dao.PackageDAO;
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
        List<TourPackage> thePackages = packageDAO.findAllPackages();
        return thePackages;
    }
}
