package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.HotelDTO;
import com.example.demo.dto.HotelWithRoom;
import com.example.demo.model.Hotel;
import com.example.demo.model.RoomType;
import com.example.demo.service.AuthService;
import com.example.demo.service.HotelService;
import com.example.demo.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/hotels")
//@CrossOrigin("*")
public class HotelController {

    private HotelService hotelService;
    private JwtUtil jwtUtil;
    private AuthService authService;

    public HotelController(HotelService hotelService, JwtUtil jwtUtil, AuthService authService) {
        this.hotelService = hotelService;
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<Hotel>>> getAllHotels() {
        try {
            // Call the service layer to get all hotels
            List<Hotel> hotels = hotelService.findAllHotels();
            // Build a success response
            ApiResponse<List<Hotel>> response = new ApiResponse<>(
                    true,
                    "Successfully retrieved " + hotels.size() + " hotel(s).",
                    hotels
            );

            return ResponseEntity.ok(response);  // HTTP 200 OK

        } catch (Exception e) {
            // Handle any unexpected exceptions
            ApiResponse<List<Hotel>> errorResponse = new ApiResponse<>(
                    false,
                    "An internal server error occurred.",
                    null
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{hotelId}/")
    public ResponseEntity<?> getHotelWithRooms(@PathVariable Integer hotelId) {
        try {
            Hotel hotel = hotelService.findHotelById(hotelId);
            if (hotel == null) {
                return ResponseEntity.notFound().build();
            }
            List<RoomType> rooms = hotelService.getRoomsByHotelId(hotelId);
            return ResponseEntity.ok(new HotelWithRoom(hotel, rooms));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching hotel: " + e.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createHotel(@RequestBody HotelDTO hotel, @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token found");
        }
        try {
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            Integer userId = jwtUtil.getUserIdFromToken(token);


            // ✅ Only vendors can create hotels
            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to create hotels.");
            }
            hotel.setVendorId(userId);

            if (hotel.getCity() != null) {
                hotel.setCity(hotel.getCity().toLowerCase());
            }
            else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("A city is needed for any hotel");
            }
            Hotel createdHotel = hotelService.createHotel(hotel);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating hotel: " + e.getMessage());
        }
    }

    @PostMapping("/{hotelId}/rooms")
    public ResponseEntity<?> createRooms(@PathVariable Integer hotelId, @RequestBody List<RoomType> rooms, @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token found");
        }
        try {
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);

            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to create rooms.");
            }

            Integer hotelVendorId = hotelService.getVendorIdByHotelId(hotelId);

            // Step 2: Check ownership
            if (!Objects.equals(vendorId, hotelVendorId)) {
                System.out.println(vendorId);
                System.out.println(hotelVendorId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You are not authorized to add rooms to this hotel.");
            }

            // Step 3: Proceed to create room
            for (RoomType room : rooms) {
                room.setHotel_id(hotelId);
                hotelService.createRoom(room); // Use single-room method
            }
            return ResponseEntity.status(201).body("Rooms created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating rooms: " + e.getMessage());
        }
    }

    @PutMapping("/{hotelId}/")
    public ResponseEntity<?> updateHotel(@PathVariable Integer hotelId, @RequestBody Hotel hotel, @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token found");
        }
        try {
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);

            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to update hotels.");
            }
            hotel.setHotel_id(hotelId); // Ensure hotelId is set
            int updatedRows = hotelService.updateHotel(hotel);
            if (updatedRows > 0) {
                return ResponseEntity.ok("Hotel updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hotel not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating hotel: " + e.getMessage());
        }
    }

    @PutMapping("/{hotelId}/room/{roomId}")
    public ResponseEntity<?> updateRoom(
            @PathVariable Integer hotelId,
            @PathVariable Integer roomId,
            @RequestBody RoomType updatedRoom,
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token found");
        }

        try {
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            Integer vendorIdFromToken = jwtUtil.getUserIdFromToken(token);

            if (!"VENDOR".equalsIgnoreCase(userType)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to update rooms.");
            }

            Integer vendorIdOfHotel = hotelService.getVendorIdByHotelId(hotelId);
            if (vendorIdOfHotel == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hotel not found");
            }

            if (!vendorIdOfHotel.equals(vendorIdFromToken)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You are not authorized to update rooms for this hotel");
            }

            boolean roomExists = hotelService.doesRoomBelongToHotel(hotelId, roomId);
            if (!roomExists) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Room does not belong to this hotel");
            }

            updatedRoom.setHotel_id(hotelId);
            updatedRoom.setRoom_id(roomId);

            boolean updated = hotelService.updateRoom(updatedRoom);
            if (!updated) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Failed to update room");
            }

            return ResponseEntity.ok("Room updated successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating room: " + e.getMessage());
        }
    }

    @GetMapping("/{city}")
    public ResponseEntity<?> getHotelsByCity(@PathVariable String city) {
        try {
            List<Hotel> hotels = hotelService.getHotelsByCity(city);
            if (hotels.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hotels found in city: " + city);
            }
            return ResponseEntity.ok(hotels);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching hotels: " + e.getMessage());
        }
    }
}
