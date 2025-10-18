package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.HotelDTO;
import com.example.demo.dto.HotelWithRoom;
import com.example.demo.model.Hotel;
import com.example.demo.model.RoomType;
import com.example.demo.service.AuthService;
import com.example.demo.service.HotelService;
import com.example.demo.service.ImageService;
import com.example.demo.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private HotelService hotelService;
    private JwtUtil jwtUtil;
    private AuthService authService;
    private ImageService imageService;

    public HotelController(HotelService hotelService, JwtUtil jwtUtil, AuthService authService, ImageService imageService) {
        this.hotelService = hotelService;
        this.jwtUtil = jwtUtil;
        this.authService = authService;
        this.imageService = imageService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllHotels(@RequestParam(required = false) String city) {
        try {
            if (city != null && !city.isBlank()) {
                List<Hotel> hotels = hotelService.findHotelsByCity(city);
                if (hotels.isEmpty()) {
                    ApiResponse<List<Hotel>> notFound = new ApiResponse<>(
                            false,
                            "No hotels found in city: " + city,
                            null
                    );
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFound);
                }
                ApiResponse<List<Hotel>> okByCity = new ApiResponse<>(
                        true,
                        "Successfully retrieved " + hotels.size() + " hotel(s) in " + city + ".",
                        hotels
                );
                return ResponseEntity.ok(okByCity);
            }

            List<Hotel> hotels = hotelService.findAllHotels();
            ApiResponse<List<Hotel>> response = new ApiResponse<>(
                    true,
                    "Successfully retrieved " + hotels.size() + " hotel(s).",
                    hotels
            );
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ApiResponse<List<Hotel>> errorResponse = new ApiResponse<>(
                    false,
                    "An internal server error occurred.",
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<ApiResponse<List<Hotel>>> getHotelByVendorId(@PathVariable Integer vendorId) {
        try{
            List<Hotel> hotels = hotelService.findHotelsByVendorId(vendorId);
            ApiResponse<List<Hotel>> response = new ApiResponse<>(
                    true,
                    "Successfully retrieved " + hotels.size() + " hotel(s).",
                    hotels
            );

            return ResponseEntity.ok(response);
        }
        catch (Exception e){
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

    // NEW: Create hotel with image upload
    @PostMapping(value = "/", consumes = "multipart/form-data")
    public ResponseEntity<?> createHotel(
            @RequestParam("name") String name,
            @RequestParam("street") String street,
            @RequestParam("city") String city,
            @RequestParam("state") String state,
            @RequestParam("pin") String pin,
            @RequestParam("primary_email") String primaryEmail,
            @RequestParam("primary_phone") String primaryPhone,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token found");
        }

        try {
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to create hotels.");
            }

            String serviceType = authService.getVendorServiceType(userId);
            if(!Objects.equals(serviceType, "Hotel_Provider")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only Hotel_Providers are allowed to create hotels.");
            }

            // Upload image if provided
            String imageUrl = "";
            if (image != null && !image.isEmpty()) {
                imageUrl = imageService.upload(image);
                if (imageUrl.contains("couldn't upload")) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Failed to upload image");
                }
            }

            // Create HotelDTO
            HotelDTO hotelDTO = new HotelDTO();
            hotelDTO.setName(name);
            hotelDTO.setStreet(street);
            hotelDTO.setCity(city);
            hotelDTO.setState(state);
            hotelDTO.setPin(pin);
            hotelDTO.setPrimary_email(primaryEmail);
            hotelDTO.setPrimary_phone(primaryPhone);
            hotelDTO.setImage_url(imageUrl);
            hotelDTO.setVendorId(userId);

            Hotel createdHotel = hotelService.createHotel(hotelDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating hotel: " + e.getMessage());
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

            String serviceType = authService.getVendorServiceType(vendorId);
            if(!Objects.equals(serviceType, "Hotel_Provider")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only Hotel_Providers are allowed to create rooms.");
            }

            Integer hotelVendorId = hotelService.getVendorIdByHotelId(hotelId);

            if (!Objects.equals(vendorId, hotelVendorId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You are not authorized to add rooms to this hotel.");
            }

            for (RoomType room : rooms) {
                room.setHotel_id(hotelId);
                hotelService.createRoom(room);
            }

            return ResponseEntity.status(201).body("Rooms created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating rooms: " + e.getMessage());
        }
    }

    // NEW: Update hotel with optional image upload
    @PutMapping(value = "/{hotelId}/", consumes = "multipart/form-data")
    public ResponseEntity<?> updateHotel(
            @PathVariable Integer hotelId,
            @RequestParam("name") String name,
            @RequestParam("street") String street,
            @RequestParam("city") String city,
            @RequestParam("state") String state,
            @RequestParam("pin") String pin,
            @RequestParam("primary_email") String primaryEmail,
            @RequestParam("primary_phone") String primaryPhone,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "existing_image_url", required = false) String existingImageUrl,
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token found");
        }

        try {
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);

            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to update hotels.");
            }

            String serviceType = authService.getVendorServiceType(vendorId);
            if(!Objects.equals(serviceType, "Hotel_Provider")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only Hotel_Providers are allowed to update hotels.");
            }

            Integer hotelVendorId = hotelService.getVendorIdByHotelId(hotelId);
            if (!Objects.equals(vendorId, hotelVendorId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You are not authorized to update hotels.");
            }

            // Handle image upload
            String imageUrl = existingImageUrl != null ? existingImageUrl : "";
            if (image != null && !image.isEmpty()) {
                imageUrl = imageService.upload(image);
                if (imageUrl.contains("couldn't upload")) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Failed to upload image");
                }
            }

            // Create Hotel object
            Hotel hotel = new Hotel();
            hotel.setHotel_id(hotelId);
            hotel.setName(name);
            hotel.setStreet(street);
            hotel.setCity(city);
            hotel.setState(state);
            hotel.setPin(pin);
            hotel.setPrimary_email(primaryEmail);
            hotel.setPrimary_phone(primaryPhone);
            hotel.setImage_url(imageUrl);
            hotel.setVendor_id(vendorId);

            int updatedRows = hotelService.updateHotel(hotel);
            if (updatedRows > 0) {
                return ResponseEntity.ok("Hotel updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hotel not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating hotel: " + e.getMessage());
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
            String serviceType = authService.getVendorServiceType(vendorIdFromToken);

            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to update rooms.");
            }

            if(!Objects.equals(serviceType, "Hotel_Provider")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only Hotel_Providers are allowed to update rooms.");
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

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<?> deleteHotel(@PathVariable Integer hotelId,
                                         @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token found");
        }

        try {
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            Integer vendorIdFromToken = jwtUtil.getUserIdFromToken(token);
            String serviceType = authService.getVendorServiceType(vendorIdFromToken);

            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to update rooms.");
            }

            if(!Objects.equals(serviceType, "Hotel_Provider")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only Hotel_Providers are allowed to update rooms.");
            }

            Integer vendorIdOfHotel = hotelService.getVendorIdByHotelId(hotelId);
            if (vendorIdOfHotel == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hotel not found");
            }

            if (!vendorIdOfHotel.equals(vendorIdFromToken)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You are not authorized to delete this hotel");
            }
            hotelService.deleteHotelById(hotelId);

            return ResponseEntity.ok("Hotel deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting room: " + e.getMessage());
        }
    }

    @DeleteMapping("/{hotelId}/room/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable Integer hotelId,
                                        @PathVariable Integer roomId,
                                        @RequestHeader ("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No token found");
        }

        try {
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            Integer vendorIdFromToken = jwtUtil.getUserIdFromToken(token);
            String serviceType = authService.getVendorServiceType(vendorIdFromToken);

            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to update rooms.");
            }

            if(!Objects.equals(serviceType, "Hotel_Provider")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only Hotel_Providers are allowed to update rooms.");
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
            hotelService.deleteRoomById(hotelId, roomId);

            return ResponseEntity.ok("Room deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting room: " + e.getMessage());
        }
    }
}