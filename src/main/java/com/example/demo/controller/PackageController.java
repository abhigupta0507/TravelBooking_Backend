package com.example.demo.controller;

import com.example.demo.model.TourPackage;
import com.example.demo.service.PackageService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<TourPackage> getAllPackages(){
        List<TourPackage> thePackages = packageService.findAllPackages();
        return thePackages;
    }
}
