package com.example.demo.controller;

import com.example.demo.dto.PackageStatus;
import com.example.demo.model.TourPackage;
import com.example.demo.service.PackageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@CrossOrigin("*")
public class PackageController {

    private PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @GetMapping("/")
    public List<TourPackage> getPackages(@RequestParam(required = false) PackageStatus status) {
        if (status != null) {
            return packageService.findPackagesByStatus(status);
        } else {
            return packageService.findAllPackages();
        }
    }


}
