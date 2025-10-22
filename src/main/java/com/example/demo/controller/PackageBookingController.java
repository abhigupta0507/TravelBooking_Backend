package com.example.demo.controller;


import com.example.demo.dto.ApiResponse;
import com.example.demo.model.ItineraryItem;
import com.example.demo.model.PackageBooking;
import com.example.demo.model.Traveller;
import com.example.demo.service.PackageBookingService;
import com.example.demo.service.StripeService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings/packages")
public class PackageBookingController {

    @Autowired
    private JwtUtil jwtUtil;

    private PackageBookingService packageBookingService;
    private StripeService stripeService;

    public PackageBookingController(JwtUtil jwtUtil, PackageBookingService packageBookingService, StripeService stripeService) {
        this.jwtUtil = jwtUtil;
        this.packageBookingService = packageBookingService;
        this.stripeService = stripeService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createPackageBooking(@RequestBody PackageBooking packageBooking, @RequestHeader("Authorization") String authHeader){
        try{
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
            }

            String token = authHeader.substring(7);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            //int packageId = packageBooking.getPackage_id();
            System.out.println(packageBooking);
            packageBookingService.createPackageBooking(userId, packageBooking);
            return ResponseEntity.ok(HttpStatus.CREATED);

        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false,e.getMessage(),null));
        }
    }

    @PostMapping("/travellers/{packageBookingId}")
    public ResponseEntity<?> createTravellerList(@RequestBody List<Traveller> theTravellers,@PathVariable int packageBookingId, @RequestHeader("Authorization") String authHeader){
        try{
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
            }

            String token = authHeader.substring(7);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            packageBookingService.assignTravellersToPackageBooking(theTravellers,userId,packageBookingId);
            packageBookingService.assignGuidesToPackageBooking(packageBookingId);
            packageBookingService.assignTransportToPackageBooking(packageBookingId);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true,"CREATED ",null));
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/hotels/{packageBookingId}")
    public String createHotelBooking(@PathVariable int packageBookingId, @RequestHeader("Authorization") String authHeader){
        try{
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return "Missing auth header";
            }

            String token = authHeader.substring(7);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            packageBookingService.bookHotelsForPackageBooking(packageBookingId,userId);
            return "CREATED";
        }
        catch (Exception  e){
            return e.getMessage();
        }
    }
 }
