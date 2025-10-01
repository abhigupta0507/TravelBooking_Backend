package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.PackageStatus;
import com.example.demo.model.TourPackage;
import com.example.demo.service.PackageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<TourPackage>>> getPackages(@RequestParam(required = false) PackageStatus status) {
        try {
            List<TourPackage> packages;
            String message;

            if (status != null) {
                // Case 1: A status filter is provided
                packages = packageService.findPackagesByStatus(status);
                message = "Successfully retrieved " + packages.size() + " package(s) with status: " + status;
            } else {
                // Case 2: No filter is provided, get all packages
                packages = packageService.findAllPackages();
                message = "Successfully retrieved all " + packages.size() + " package(s).";
            }

            // Even if the list is empty, the query was successful.
            // So we return a 200 OK status.
            ApiResponse<List<TourPackage>> response = new ApiResponse<>(true, message, packages);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Catch any unexpected errors from the service or DAO layer
            ApiResponse<List<TourPackage>> errorResponse = new ApiResponse<>(false, "An internal server error occurred.", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @GetMapping("/{packageSlug}")
    public ResponseEntity<ApiResponse<TourPackage>> getPackageBySlug(@PathVariable String packageSlug) {
        TourPackage thePackage = packageService.findPackageBySlug(packageSlug);

        if (thePackage == null) {
            // If the package is not found, return a 404 Not Found response
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Package with slug '" + packageSlug + "' not found.", null));
        } else {
            // If the package is found, return a 200 OK response with the package data
            return ResponseEntity
                    .ok(new ApiResponse<>(true, "Package found.", thePackage));
        }
    }


}
