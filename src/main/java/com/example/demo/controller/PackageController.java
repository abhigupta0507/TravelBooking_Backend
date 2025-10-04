package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.ItineraryItem;
import com.example.demo.model.TourPackage;
import com.example.demo.service.AuthService;
import com.example.demo.service.PackageService;
import com.example.demo.util.AuthorizationService;
import com.example.demo.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.TaskSchedulerRouter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/packages")
@CrossOrigin("*")
public class PackageController {

    private PackageService packageService;
    private JwtUtil jwtUtil;
    private AuthService authService;
    private AuthorizationService authorizationService;

    public PackageController(PackageService packageService, JwtUtil jwtUtil, AuthService authService) {
        this.packageService = packageService;
        this.jwtUtil = jwtUtil;
        this.authService = authService;
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

    @PostMapping("/")
    public ResponseEntity<ApiResponse<TourPackage>> createPackage(@Valid @RequestBody CreatePackageRequestDto packageDto, @RequestHeader("Authorization") String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("No token found");
        }

        try{
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            int user_id = jwtUtil.getUserIdFromToken(token);

            if (!Objects.equals(userType, "STAFF")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Be a staff bro!", null));
            }

            String role = authService.getStaffRole(user_id);
            if (!Objects.equals(role, "admin")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Be a admin bro!", null));
            }

            TourPackage createdPackage = packageService.createPackage(packageDto);
            // Build the location URI for the newly created resource
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest() // Starts with the current request path (/api/packages/)
                    .path("/{packageSlug}") // Appends the slug placeholder
                    .buildAndExpand(createdPackage.getSlug()) // Fills the placeholder with the new slug
                    .toUri();

            // Create the response body
            ApiResponse<TourPackage> response = new ApiResponse<>(true, "Tour package created successfully.", createdPackage);

            // Return a 201 Created response with the location header and the new package in the body
            return ResponseEntity.created(location).body(response);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse<>(false,e.getMessage(),null));
        }
    }


    @GetMapping("/{packageSlug}")
    public ResponseEntity<ApiResponse<PackageDetailDto>> getPackageBySlug(@PathVariable String packageSlug) {
        PackageDetailDto thePackage = packageService.findPackageBySlug(packageSlug);

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


    @PostMapping("/itinerary")
    public ResponseEntity<ApiResponse<ItineraryItem>> createItineraryItem(@Valid @RequestBody ItineraryItem theItem, @RequestHeader("Authorization") String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("No token found");
        }

        try{
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            int user_id = jwtUtil.getUserIdFromToken(token);

            if (!Objects.equals(userType, "STAFF")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Be a staff bro!", null));
            }

            String role = authService.getStaffRole(user_id);
            if (!Objects.equals(role, "admin")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Be a admin bro!", null));
            }

            System.out.println(theItem);

            ItineraryItem createdItem = packageService.createItineraryItem(theItem);

            System.out.println(createdItem);
            // The service handles authorization and creation

            // Create the response body
            ApiResponse<ItineraryItem> response = new ApiResponse<>(true, "Itinerary item created successfully.", createdItem);

            // Return a 201 Created response with the new item in the body.
            // No location URI is generated.
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse<>(false,e.getMessage(),null));
        }
    }

    @PutMapping("/{packageSlug}")
    public ResponseEntity<ApiResponse<TourPackage>> updatePackage(
            @PathVariable String packageSlug,
            @Valid @RequestBody UpdatePackageRequestDto packageDto,
            @RequestHeader("Authorization") String authHeader) {
        try {
            TourPackage updatedPackage = packageService.updatePackage(packageSlug, packageDto, authHeader);
            ApiResponse<TourPackage> response = new ApiResponse<>(true, "Package updated successfully.", updatedPackage);
            return ResponseEntity.ok(response);

        } catch (SecurityException e) {
            // Correctly handle authorization failures with a 403 status.
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
        catch (Exception e) {
            // Correctly handle the "not found" case with a 404 status.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

}
