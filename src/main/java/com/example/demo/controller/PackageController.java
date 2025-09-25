package com.example.demo.controller;

import com.example.demo.dto.PackageOverview;
import com.example.demo.service.PackageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @GetMapping("/")
    public List<PackageOverview> getAllPackages(){
        List<PackageOverview> thePackages = packageService.findAllPackages();
        return thePackages;
    }
}
