package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.HotelBooking;
import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import com.example.demo.service.HotelBookingService;
import com.example.demo.util.JwtUtil;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private AuthService authService;
    private JwtUtil jwtUtil;
    private HotelBookingService hotelBookingService;

    public BookingController(AuthService authService, JwtUtil jwtUtil, HotelBookingService hotelBookingService) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.hotelBookingService = hotelBookingService;
    }

    //THIS ONE WILL REGISTER A HOTEL BOOKING WITH STATUS PENDING WAITING FOR PAYMENT. IF PAYMENT DONE FOR THIS BOOKING THEN
    //WE UPDATE ITS PENDING STATUS TO CONFIRMED OTHERWISE BY SOME WAY WE WILL DELETE THIS STALE ENTRY USING BOOKING DATETIME AND STATUS.
    @PostMapping("/hotels")
    public ResponseEntity<ApiResponse<?>> createHotelBooking(@RequestBody HotelBooking hotelBooking, @RequestHeader("Authorization") String authHeader){
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
            }

            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            boolean canBook = hotelBookingService.canBookForCheckDates(hotelBooking.getNo_of_rooms(),hotelBooking.getCheck_in_date(),hotelBooking.getCheck_out_date(),hotelBooking.getHotel_id(),hotelBooking.getRoom_id());
            if(!canBook){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false,"Not enough rooms to allocate this booking",null));
            }
            int hotelBookingId = hotelBookingService.createHotelBooking(hotelBooking,userId);
            HotelBooking hotelBookingDB = hotelBookingService.getHotelBooking(hotelBookingId);

            return ResponseEntity.ok(new ApiResponse<>(true, "Successfully booked Hotel",hotelBookingDB));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }

    }

    //GET ONE BOOKING DETAILS WHOSE ID IS PASSED INTO URL
    @GetMapping("/hotels/{bookingId}")
    public ResponseEntity<? extends ApiResponse<?>> getHotelBookingById(@PathVariable int bookingId, @RequestHeader("Authorization") String authHeader ){
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
            }

            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            HotelBooking hotelBookingDB = hotelBookingService.getHotelBooking(bookingId);

            return ResponseEntity.ok(new ApiResponse<>(true, "Successfully Fetched : Hotel Booking",hotelBookingDB));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // NO UPDATES ARE TO BE ENTERTAINED IN A HOTEL BOOKING DUE TO PAYMENT BEING DONE...


    //GET ALL HOTEL BOOKINGS DONE BY LOGGED IN CUSTOMER (STATUS IS OPTIONAL IF ENTERED MUST BE A PARAM)
    @GetMapping("/hotels/my")
    public ResponseEntity<ApiResponse<List<?>>> getAllHotelBookingsOfCustomer( @RequestHeader("Authorization") String authHeader,@RequestParam(name = "status", required = false) String status){
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
            }

            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            List<HotelBooking> hotelBookingsDB;
            if(status==null) {
                hotelBookingsDB = hotelBookingService.getAllHotelBookingsOfCustomer(userId);
            }
            else{
                hotelBookingsDB= hotelBookingService.getHotelBookingsOfCustomerByStatus(userId,status);
            }
            return ResponseEntity.ok(new ApiResponse<>(true, "Successfully Fetched : Hotel Booking",hotelBookingsDB));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

}
