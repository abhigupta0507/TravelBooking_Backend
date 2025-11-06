package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.model.Booking;
import com.example.demo.model.HotelBooking;
import com.example.demo.model.RoomType;
import com.example.demo.service.HotelBookingService;
import com.example.demo.service.StripeService;
import com.example.demo.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private JwtUtil jwtUtil;
    private HotelBookingService hotelBookingService;
    private StripeService stripeService;

    public BookingController(JwtUtil jwtUtil, HotelBookingService hotelBookingService, StripeService stripeService) {
        this.jwtUtil = jwtUtil;
        this.hotelBookingService = hotelBookingService;
        this.stripeService = stripeService;
    }

    // Check room availability
    @PostMapping("/hotels/check-availability")
    public ResponseEntity<ApiResponse<?>> checkAvailability(
            @RequestBody Map<String, Object> request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
            }

            Date checkIn = Date.valueOf((String) request.get("check_in_date"));
            Date checkOut = Date.valueOf((String) request.get("check_out_date"));
            Integer noOfRooms = (Integer) request.get("required_rooms");
            Integer hotelId = (Integer) request.get("hotel_id");
            Integer roomId = (Integer) request.get("room_id");

            boolean available = hotelBookingService.canBookForCheckDates(
                    noOfRooms, checkIn, checkOut, hotelId, roomId
            );

            if (available) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Rooms available", Map.of("available", true)));
            } else {
                return ResponseEntity.ok(new ApiResponse<>(false, "Not enough rooms available", Map.of("available", false)));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Create hotel booking (PENDING status)
    @PostMapping("/hotels")
    public ResponseEntity<ApiResponse<?>> createHotelBooking(
            @RequestBody HotelBooking hotelBooking,
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
            }

            String token = authHeader.substring(7);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            // the room for which booking is done
            RoomType room = hotelBookingService.hotelDao.findRoomByHotelAndRoomId(hotelBooking.getHotel_id(),hotelBooking.getRoom_id());

            //Check to prevent overcrowding of guest in asked number of rooms
            int guestCount= hotelBooking.getGuest_count();
            int maxCapacityPerRoom = room.getMax_capacity();
            int noOfRoomsRequired= hotelBooking.getNo_of_rooms();

            if(maxCapacityPerRoom*noOfRoomsRequired<guestCount){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false,"Guest count is more than allowed!",null));
            }

            //A backend check for availability of rooms for requested check-in and check-out dates.
            boolean canBook = hotelBookingService.canBookForCheckDates(
                    hotelBooking.getNo_of_rooms(),
                    hotelBooking.getCheck_in_date(),
                    hotelBooking.getCheck_out_date(),
                    hotelBooking.getHotel_id(),
                    hotelBooking.getRoom_id()
            );

            if (!canBook) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, "Not enough rooms to allocate this booking", null));
            }

            //create the booking now as all cases passed.
            int hotelBookingId = hotelBookingService.createHotelBooking(hotelBooking, userId);
            HotelBooking hotelBookingDB = hotelBookingService.getHotelBooking(hotelBookingId);

            return ResponseEntity.ok(new ApiResponse<>(true, "Successfully created booking", hotelBookingDB));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Get one booking by ID
    @GetMapping("/hotels/{bookingId}")
    public ResponseEntity<ApiResponse<?>> getHotelBookingById(
            @PathVariable int bookingId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
            }
            String token = authHeader.substring(7);
            int userId= jwtUtil.getUserIdFromToken(token);

            HotelBooking hotelBookingDB = hotelBookingService.getHotelBooking(bookingId);

            //check to ensure booking belongs to logged-in user.
            if(userId==hotelBookingDB.getCustomer_id()){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false,"This booking doesn't belong to you",null));
            }

            return ResponseEntity.ok(new ApiResponse<>(true, "Successfully fetched booking", hotelBookingDB));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/hotels/{bookingId}/cancel")
    public ResponseEntity<ApiResponse<HotelBooking>> cancelHotelBookingById(
            @PathVariable int bookingId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            // 1. Basic Auth Header Check (moved token parsing to service if preferred, but okay here too)
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
            }
            String token = authHeader.substring(7);
            int userId = jwtUtil.getUserIdFromToken(token); // Ensure this handles potential token errors

            // 2. Delegate all logic to the service
            HotelBooking cancelledBooking = hotelBookingService.cancelHotelBooking(bookingId, userId);

            // 3. Return success response
            return ResponseEntity.ok(new ApiResponse<>(true, "Hotel booking cancelled successfully.", cancelledBooking));

        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid or expired token.", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            // Catch attempts to cancel an already finalized booking
            return ResponseEntity.status(HttpStatus.CONFLICT) // 409 Conflict is appropriate
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Get all bookings for logged-in customer
    @GetMapping("/hotels/my")
    public ResponseEntity<ApiResponse<List<?>>> getAllHotelBookingsOfCustomer(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(name = "status", required = false) String status) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
            }


            System.out.println(status);
            String token = authHeader.substring(7);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            List<HotelBooking> hotelBookingsDB;
            if (status!=null) {
                hotelBookingsDB = hotelBookingService.getHotelBookingsOfCustomerByStatus(userId, status);
            } else {
                hotelBookingsDB = hotelBookingService.getAllHotelBookingsOfCustomer(userId);
            }

            System.out.println("Founded hotel Bookings. Count: "+hotelBookingsDB.size());
            List<HotelBookingDto> hotelBookingDtoList = hotelBookingService.getAllHotelBookingsDtoOfCustomer(hotelBookingsDB);

            return ResponseEntity.ok(new ApiResponse<>(true, "Successfully fetched bookings", hotelBookingDtoList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Add this new endpoint after the existing getAllHotelBookingsOfCustomer method

    @GetMapping("/hotels/{bookingId}/receipt")
    public ResponseEntity<ApiResponse<?>> getBookingReceipt(
            @PathVariable int bookingId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
            }

            String token = authHeader.substring(7);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            Map<String, Object> receipt = hotelBookingService.getBookingReceipt(bookingId, userId);

            return ResponseEntity.ok(new ApiResponse<>(true, "Receipt fetched successfully", receipt));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/hotels/pending")
    public void deletePendingHotelBookings(){
        try{
            hotelBookingService.deletePendingBooking();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    // Stripe checkout
    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> checkoutProducts(@RequestBody ProductRequest productRequest) {
        StripeResponse stripeResponse = stripeService.checkoutProducts(productRequest);
        return ResponseEntity.status(HttpStatus.OK).body(stripeResponse);
    }


}