package com.example.demo.service;

import com.example.demo.dao.PackageDAO;
import com.example.demo.dto.PackageOverview;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageService {
    public PackageService(PackageDAO packageDAO) {
        this.packageDAO = packageDAO;
    }

    private PackageDAO packageDAO;

    public List<PackageOverview> findAllPackages() {
        List<PackageOverview> thePackages = packageDAO.findAllPackages();
        return thePackages;
    }
}
