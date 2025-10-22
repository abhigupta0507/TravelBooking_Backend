//////package com.example.demo.controller;
//////
//////
//////import com.example.demo.dto.ApiResponse;
//////import com.example.demo.model.ItineraryItem;
//////import com.example.demo.model.PackageBooking;
//////import com.example.demo.model.Traveller;
//////import com.example.demo.service.PackageBookingService;
//////import com.example.demo.service.StripeService;
//////import com.example.demo.util.JwtUtil;
//////import org.springframework.beans.factory.annotation.Autowired;
//////import org.springframework.http.HttpStatus;
//////import org.springframework.http.ResponseEntity;
//////import org.springframework.web.bind.annotation.*;
//////
//////import java.util.List;
//////
//////@RestController
//////@RequestMapping("/api/bookings/packages")
//////public class PackageBookingController {
//////
//////    @Autowired
//////    private JwtUtil jwtUtil;
//////
//////    private PackageBookingService packageBookingService;
//////    private StripeService stripeService;
//////
//////    public PackageBookingController(JwtUtil jwtUtil, PackageBookingService packageBookingService, StripeService stripeService) {
//////        this.jwtUtil = jwtUtil;
//////        this.packageBookingService = packageBookingService;
//////        this.stripeService = stripeService;
//////    }
//////
//////    @PostMapping("/")
//////    public ResponseEntity<?> createPackageBooking(@RequestBody PackageBooking packageBooking, @RequestHeader("Authorization") String authHeader){
//////        try{
//////            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//////                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//////                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
//////            }
//////
//////            String token = authHeader.substring(7);
//////            Integer userId = jwtUtil.getUserIdFromToken(token);
//////
//////            //int packageId = packageBooking.getPackage_id();
//////            System.out.println(packageBooking);
//////            packageBookingService.createPackageBooking(userId, packageBooking);
//////            return ResponseEntity.ok(HttpStatus.CREATED);
//////
//////        }
//////        catch (Exception e){
//////            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false,e.getMessage(),null));
//////        }
//////    }
//////
//////    @PostMapping("/travellers/{packageBookingId}")
//////    public ResponseEntity<?> createTravellerList(@RequestBody List<Traveller> theTravellers,@PathVariable int packageBookingId, @RequestHeader("Authorization") String authHeader){
//////        try{
//////            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//////                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//////                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
//////            }
//////
//////            String token = authHeader.substring(7);
//////            Integer userId = jwtUtil.getUserIdFromToken(token);
//////
//////            packageBookingService.assignTravellersToPackageBooking(theTravellers,userId,packageBookingId);
//////            packageBookingService.assignGuidesToPackageBooking(packageBookingId);
//////            packageBookingService.assignTransportToPackageBooking(packageBookingId);
//////            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true,"CREATED ",null));
//////        }
//////        catch (Exception e){
//////            throw new RuntimeException(e);
//////        }
//////    }
//////
//////    @PostMapping("/hotels/{packageBookingId}")
//////    public String createHotelBooking(@PathVariable int packageBookingId, @RequestHeader("Authorization") String authHeader){
//////        try{
//////            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//////                return "Missing auth header";
//////            }
//////
//////            String token = authHeader.substring(7);
//////            Integer userId = jwtUtil.getUserIdFromToken(token);
//////
//////            packageBookingService.bookHotelsForPackageBooking(packageBookingId,userId);
//////            return "CREATED";
//////        }
//////        catch (Exception  e){
//////            return e.getMessage();
//////        }
//////    }
////// }
////package com.example.demo.controller;
////
////import com.example.demo.dto.ApiResponse;
////import com.example.demo.dto.PackageBookingRequest;
////import com.example.demo.dto.ProductRequest;
////import com.example.demo.dto.StripeResponse;
////import com.example.demo.model.PackageBooking;
////import com.example.demo.model.Traveller;
////import com.example.demo.service.PackageBookingService;
////import com.example.demo.service.StripeService;
////import com.example.demo.util.JwtUtil;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.http.HttpStatus;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////
////import java.util.List;
////import java.util.Map;
////
////@RestController
////@RequestMapping("/api/bookings/packages")
////public class PackageBookingController {
////
////    @Autowired
////    private JwtUtil jwtUtil;
////
////    private PackageBookingService packageBookingService;
////    private StripeService stripeService;
////
////    public PackageBookingController(JwtUtil jwtUtil, PackageBookingService packageBookingService, StripeService stripeService) {
////        this.jwtUtil = jwtUtil;
////        this.packageBookingService = packageBookingService;
////        this.stripeService = stripeService;
////    }
////
////    /**
////     * Create package booking with travellers and initiate payment
////     * This endpoint combines booking creation, traveller assignment, and payment initiation
////     */
////    @PostMapping("/")
////    public ResponseEntity<?> createPackageBookingWithPayment(
////            @RequestBody PackageBookingRequest request,
////            @RequestHeader("Authorization") String authHeader) {
////        try {
////            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
////                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
////                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
////            }
////
////            String token = authHeader.substring(7);
////            Integer userId = jwtUtil.getUserIdFromToken(token);
////
////            // Validate travellers count matches number_of_people
////            if (request.getTravellers() == null ||
////                    request.getTravellers().size() != request.getPackageBooking().getNumber_of_people()) {
////                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
////                        .body(new ApiResponse<>(false, "Number of travellers must match number of people", null));
////            }
////
////            // Create package booking (PENDING status)
//////            int packageBookingId = packageBookingService.createPackageBookingWithTravellers(
//////                    userId,
//////                    request.getPackageBooking(),
//////                    request.getTravellers()
//////            );
////            int packageBookingId = packageBookingService.createPackageBooking(userId,request.getPackageBooking());
////            packageBookingService.assignTravellersToPackageBooking(request.getTravellers(),userId,packageBookingId)
////
////
////
////            // Get the created booking to return cost
////            PackageBooking createdBooking = packageBookingService.getPackageBookingById(packageBookingId);
////
////            // Prepare Stripe checkout
////            ProductRequest productRequest = new ProductRequest();
////            productRequest.setName("Tour Package Booking #" + packageBookingId);
////            productRequest.setAmount((long) createdBooking.getTotal_cost() * 100L); // Convert to paisa
////            productRequest.setQuantity(1L);
////            productRequest.setCurrency("inr");
////
////            StripeResponse stripeResponse = stripeService.checkoutProducts(productRequest);
////
////            // Return booking details with Stripe session info
////            Map<String, Object> responseData = Map.of(
////                    "packageBookingId", packageBookingId,
////                    "totalCost", createdBooking.getTotal_cost(),
////                    "sessionId", stripeResponse.getSessionId(),
////                    "sessionUrl", stripeResponse.getSessionUrl()
////            );
////
////            return ResponseEntity.ok(new ApiResponse<>(true, "Package booking created, proceed to payment", responseData));
////
////        } catch (Exception e) {
////            e.printStackTrace();
////            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
////                    .body(new ApiResponse<>(false, e.getMessage(), null));
////        }
////    }
////
////    /**
////     * Assign guides and transport after payment confirmation
////     * This should be called internally after payment is confirmed
////     */
////    @PostMapping("/{packageBookingId}/assign-resources")
////    public ResponseEntity<?> assignResources(
////            @PathVariable int packageBookingId,
////            @RequestHeader("Authorization") String authHeader) {
////        try {
////            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
////                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
////                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
////            }
////
////            String token = authHeader.substring(7);
////            Integer userId = jwtUtil.getUserIdFromToken(token);
////
////            // Assign guides
////            packageBookingService.assignGuidesToPackageBooking(packageBookingId);
////
////            // Assign transport
////            packageBookingService.assignTransportToPackageBooking(packageBookingId);
////
////            // Book hotels
////            packageBookingService.bookHotelsForPackageBooking(packageBookingId, userId);
////
////            return ResponseEntity.ok(new ApiResponse<>(true, "Resources assigned successfully", null));
////
////        } catch (Exception e) {
////            e.printStackTrace();
////            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
////                    .body(new ApiResponse<>(false, e.getMessage(), null));
////        }
////    }
////
////    /**
////     * Get package booking details
//////     */
//////    @GetMapping("/{packageBookingId}")
//////    public ResponseEntity<?> getPackageBooking(
//////            @PathVariable int packageBookingId,
//////            @RequestHeader("Authorization") String authHeader) {
//////        try {
//////            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//////                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//////                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
//////            }
//////
//////            String token = authHeader.substring(7);
//////            Integer userId = jwtUtil.getUserIdFromToken(token);
//////
//////            PackageBooking booking = packageBookingService.getPackageBookingById(packageBookingId);
//////
//////            // Verify booking belongs to user
//////            if (!booking.getCustomer_id().equals(userId)) {
//////                return ResponseEntity.status(HttpStatus.FORBIDDEN)
//////                        .body(new ApiResponse<>(false, "This booking doesn't belong to you", null));
//////            }
//////
//////            return ResponseEntity.ok(new ApiResponse<>(true, "Package booking retrieved", booking));
//////
//////        } catch (Exception e) {
//////            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//////                    .body(new ApiResponse<>(false, e.getMessage(), null));
//////        }
//////    }
//////
//////    /**
//////     * Get all package bookings for logged-in customer
//////     */
//////    @GetMapping("/my")
//////    public ResponseEntity<?> getMyPackageBookings(
//////            @RequestHeader("Authorization") String authHeader,
//////            @RequestParam(name = "status", required = false) String status) {
//////        try {
//////            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//////                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//////                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
//////            }
//////
//////            String token = authHeader.substring(7);
//////            Integer userId = jwtUtil.getUserIdFromToken(token);
//////
//////            List<PackageBooking> bookings = packageBookingService.getAllPackageBookingsOfCustomer(userId, status);
//////
//////            return ResponseEntity.ok(new ApiResponse<>(true, "Package bookings retrieved", bookings));
//////
//////        } catch (Exception e) {
//////            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//////                    .body(new ApiResponse<>(false, e.getMessage(), null));
//////        }
//////    }
////}
//package com.example.demo.controller;
//
//import com.example.demo.dto.ApiResponse;
//import com.example.demo.dto.PackageBookingRequest;
//import com.example.demo.dto.ProductRequest;
//import com.example.demo.dto.StripeResponse;
//import com.example.demo.model.PackageBooking;
//import com.example.demo.model.Traveller;
//import com.example.demo.service.PackageBookingService;
//import com.example.demo.service.StripeService;
//import com.example.demo.util.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/bookings/packages")
//public class PackageBookingController {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    private PackageBookingService packageBookingService;
//    private StripeService stripeService;
//
//    public PackageBookingController(JwtUtil jwtUtil, PackageBookingService packageBookingService, StripeService stripeService) {
//        this.jwtUtil = jwtUtil;
//        this.packageBookingService = packageBookingService;
//        this.stripeService = stripeService;
//    }
//
//    /**
//     * Create package booking with travellers and initiate payment
//     * This endpoint combines booking creation, traveller assignment, and payment initiation
//     */
//    @PostMapping("/")
//    public ResponseEntity<?> createPackageBookingWithPayment(
//            @RequestBody PackageBookingRequest request,
//            @RequestHeader("Authorization") String authHeader) {
//        try {
//            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
//            }
//
//            String token = authHeader.substring(7);
//            Integer userId = jwtUtil.getUserIdFromToken(token);
//
//            // Validate travellers count matches number_of_people
//            if (request.getTravellers() == null ||
//                    request.getTravellers().size() != request.getPackageBooking().getNumber_of_people()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body(new ApiResponse<>(false, "Number of travellers must match number of people", null));
//            }
//
//            // Create package booking (PENDING status)
//            int packageBookingId = packageBookingService.createPackageBookingWithTravellers(
//                    userId,
//                    request.getPackageBooking(),
//                    request.getTravellers()
//            );
//
//            // Get the created booking to return cost
//            PackageBooking createdBooking = packageBookingService.getPackageBookingById(packageBookingId);
//
//            // Prepare Stripe checkout
//            ProductRequest productRequest = new ProductRequest();
//            productRequest.setName("Tour Package Booking #" + packageBookingId);
//            productRequest.setAmount((long) createdBooking.getTotal_cost() * 100L); // Convert to paisa
//            productRequest.setQuantity(1L);
//            productRequest.setCurrency("inr");
//
//            StripeResponse stripeResponse = stripeService.checkoutProducts(productRequest);
//
//            // Return booking details with Stripe session info
//            Map<String, Object> responseData = Map.of(
//                    "packageBookingId", packageBookingId,
//                    "totalCost", createdBooking.getTotal_cost(),
//                    "sessionId", stripeResponse.getSessionId(),
//                    "sessionUrl", stripeResponse.getSessionUrl()
//            );
//
//            return ResponseEntity.ok(new ApiResponse<>(true, "Package booking created, proceed to payment", responseData));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ApiResponse<>(false, e.getMessage(), null));
//        }
//    }
//
//    /**
//     * Assign guides and transport after payment confirmation
//     * This should be called internally after payment is confirmed
//     */
//    @PostMapping("/{packageBookingId}/assign-resources")
//    public ResponseEntity<?> assignResources(
//            @PathVariable int packageBookingId,
//            @RequestHeader("Authorization") String authHeader) {
//        try {
//            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
//            }
//
//            String token = authHeader.substring(7);
//            Integer userId = jwtUtil.getUserIdFromToken(token);
//
//            // Assign guides
//            packageBookingService.assignGuidesToPackageBooking(packageBookingId);
//
//            // Assign transport
//            packageBookingService.assignTransportToPackageBooking(packageBookingId);
//
//            // Book hotels
//            packageBookingService.bookHotelsForPackageBooking(packageBookingId, userId);
//
//            return ResponseEntity.ok(new ApiResponse<>(true, "Resources assigned successfully", null));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ApiResponse<>(false, e.getMessage(), null));
//        }
//    }
//
//    /**
//     * Get package booking details
//     */
//    @GetMapping("/{packageBookingId}")
//    public ResponseEntity<?> getPackageBooking(
//            @PathVariable int packageBookingId,
//            @RequestHeader("Authorization") String authHeader) {
//        try {
//            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
//            }
//
//            String token = authHeader.substring(7);
//            Integer userId = jwtUtil.getUserIdFromToken(token);
//
//            PackageBooking booking = packageBookingService.getPackageBookingById(packageBookingId);
//
//            // Verify booking belongs to user
//            if (!booking.getCustomer_id().equals(userId)) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                        .body(new ApiResponse<>(false, "This booking doesn't belong to you", null));
//            }
//
//            return ResponseEntity.ok(new ApiResponse<>(true, "Package booking retrieved", booking));
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse<>(false, e.getMessage(), null));
//        }
//    }
//
//    /**
//     * Get all package bookings for logged-in customer
//     */
//    @GetMapping("/my")
//    public ResponseEntity<?> getMyPackageBookings(
//            @RequestHeader("Authorization") String authHeader,
//            @RequestParam(name = "status", required = false) String status) {
//        try {
//            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
//            }
//
//            String token = authHeader.substring(7);
//            Integer userId = jwtUtil.getUserIdFromToken(token);
//
//            List<PackageBooking> bookings = packageBookingService.getAllPackageBookingsOfCustomer(userId, status);
//
//            return ResponseEntity.ok(new ApiResponse<>(true, "Package bookings retrieved", bookings));
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ApiResponse<>(false, e.getMessage(), null));
//        }
//    }
//}
package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.PackageBookingRequest;
import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.StripeResponse;
import com.example.demo.model.PackageBooking;
import com.example.demo.service.PackageBookingService;
import com.example.demo.service.StripeService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    /**
     * Create package booking with travellers and initiate payment
     */
    @PostMapping("/")
    public ResponseEntity<?> createPackageBookingWithPayment(
            @RequestBody PackageBookingRequest request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
            }

            String token = authHeader.substring(7);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            // Validate travellers count matches number_of_people
            if (request.getTravellers() == null ||
                    request.getTravellers().size() != request.getPackageBooking().getNumber_of_people()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Number of travellers must match number of people", null));
            }

            // Create package booking (PENDING status)
            int packageBookingId = packageBookingService.createPackageBookingWithTravellers(
                    userId,
                    request.getPackageBooking(),
                    request.getTravellers()
            );

            // Get the created booking to return cost
            PackageBooking createdBooking = packageBookingService.getPackageBookingById(packageBookingId);

            // Prepare Stripe checkout
            ProductRequest productRequest = new ProductRequest();
            productRequest.setName("Tour Package Booking #" + packageBookingId);
            productRequest.setAmount((long) createdBooking.getTotal_cost() * 100L);
            productRequest.setQuantity(1L);
            productRequest.setCurrency("inr");

            StripeResponse stripeResponse = stripeService.checkoutProducts(productRequest, "PACKAGE");

            // Return booking details with Stripe session info
            Map<String, Object> responseData = Map.of(
                    "packageBookingId", packageBookingId,
                    "totalCost", createdBooking.getTotal_cost(),
                    "sessionId", stripeResponse.getSessionId(),
                    "sessionUrl", stripeResponse.getSessionUrl()
            );

            return ResponseEntity.ok(new ApiResponse<>(true, "Package booking created, proceed to payment", responseData));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    /**
     * Get package booking details
     */
    @GetMapping("/{packageBookingId}")
    public ResponseEntity<?> getPackageBooking(
            @PathVariable int packageBookingId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
            }

            String token = authHeader.substring(7);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            PackageBooking booking = packageBookingService.getPackageBookingById(packageBookingId);

            // Verify booking belongs to user
            if (!booking.getCustomer_id().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(false, "This booking doesn't belong to you", null));
            }

            return ResponseEntity.ok(new ApiResponse<>(true, "Package booking retrieved", booking));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    /**
     * Get all package bookings for logged-in customer
     */
    @GetMapping("/my")
    public ResponseEntity<?> getMyPackageBookings(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(name = "status", required = false) String status) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
            }

            String token = authHeader.substring(7);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            List<PackageBooking> bookings = packageBookingService.getAllPackageBookingsOfCustomer(userId, status);

            return ResponseEntity.ok(new ApiResponse<>(true, "Package bookings retrieved", bookings));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}