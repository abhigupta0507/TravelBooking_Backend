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
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/packages")
//@CrossOrigin("*")
public class PackageController {

    private PackageService packageService;
    private JwtUtil jwtUtil;
    private AuthService authService;

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
        try{
            System.out.println("Hello World");
            TourPackage createdPackage = packageService.createPackage(authHeader,packageDto);
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
        try{
            System.out.println(theItem);
            ItineraryItem createdItem = packageService.createItineraryItem(authHeader, theItem);
            // Create the response body
            ApiResponse<ItineraryItem> response = new ApiResponse<>(true, "Itinerary item created successfully.", createdItem);

            // Return a 201 Created response with the new item in the body.
            // No location URI is generated.
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse<>(false,e.getMessage(),null));
        }
    }

    @PostMapping("/itineraries")
    public ResponseEntity<ApiResponse<List<ItineraryItem>>> createItineraryItem(@Valid @RequestBody List<ItineraryItem> theItems, @RequestHeader("Authorization") String authHeader){
        try{
            System.out.println(theItems);
            List<ItineraryItem> createdItems = new ArrayList<>();

            for(ItineraryItem theItem: theItems){
                ItineraryItem createdItem = packageService.createItineraryItem(authHeader, theItem);
                createdItems.add(createdItem);
            }
            // Create the response body
            ApiResponse<List<ItineraryItem>> response = new ApiResponse<>(true, "Itinerary item created successfully.", createdItems);

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

    @PutMapping("/{packageSlug}/{itemId}")
    public ResponseEntity<ApiResponse<ItineraryItem>> updateItineraryItem(
            @PathVariable String packageSlug, @PathVariable Integer itemId,
            @Valid @RequestBody UpdateItineraryItemRequestDto itemDto,
            @RequestHeader("Authorization") String authHeader) {
        try {
            ItineraryItem updatedItem = packageService.updateItineraryItem(packageSlug, itemId, itemDto, authHeader);
            ApiResponse<ItineraryItem> response = new ApiResponse<>(true, "Item updated successfully.", updatedItem);
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

    @DeleteMapping("/{packageSlug}/{itemId}")
    public ResponseEntity<ApiResponse<String>> deleteItineraryItem(@PathVariable String packageSlug, @PathVariable Integer itemId,@RequestHeader("Authorization") String authHeader){
        try {
            int deleted = packageService.deleteItineraryItem(packageSlug,itemId,authHeader);
            if (deleted == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false,"Itinerary Item not found or not deleted",null));
            }

            return ResponseEntity.ok(new ApiResponse<>(true,"Itinerary Item deleted Successfully",null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false,"Error deleting Itinerary Item: " + e.getMessage(),null));
        }
    }

    @DeleteMapping("/{packageSlug}")
    public ResponseEntity<ApiResponse<String>> deletePackage(@PathVariable String packageSlug, @RequestHeader("Authorization") String authHeader){
        try {
            int deleted = packageService.deletePackage(packageSlug,authHeader);
            if (deleted == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false,"Tour Package not found or not deleted",null));
            }

            return ResponseEntity.ok(new ApiResponse<>(true,"Tour Package deleted Successfully",null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false,"Error deleting Tour Package: " + e.getMessage(),null));
        }
    }


}