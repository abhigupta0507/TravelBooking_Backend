package com.example.demo.service;

import com.example.demo.dao.HotelDAO;
import com.example.demo.dto.HotelDTO;
import com.example.demo.model.Hotel;
import com.example.demo.model.RoomType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {
    private final HotelDAO hotelDAO;

    // ✅ Constructor injection (same pattern as PackageService)
    public HotelService(HotelDAO hotelDAO) {
        this.hotelDAO = hotelDAO;
    }

    // 🔹 1. Fetch all hotels
    public List<Hotel> findAllHotels() {
        return hotelDAO.findAllHotels();
    }

    public List<Hotel> findHotelsByVendorId(Integer vendorId) {
        return hotelDAO.findHotelByVendorId(vendorId);
    }

    // 🔹 2. Fetch hotel by ID
    public Hotel findHotelById(int hotelId) {
        return hotelDAO.findHotelById(hotelId);
    }

    // 🔹 3. Create a new hotel (using DTO)
    public Hotel createHotel(HotelDTO hotelDTO) {
        // Convert DTO to Model
        Hotel newHotel = new Hotel();
        newHotel.setName(hotelDTO.getName());
        newHotel.setStreet(hotelDTO.getStreet());
        newHotel.setCity(hotelDTO.getCity());
        newHotel.setState(hotelDTO.getState());
        newHotel.setPin(hotelDTO.getPin());
        newHotel.setRating(hotelDTO.getRating());
        newHotel.setTotal_rooms(hotelDTO.getTotalRooms());
        newHotel.setVendor_id(hotelDTO.getVendorId());
        newHotel.setPrimary_email(hotelDTO.getPrimary_email());
        newHotel.setPrimary_phone(hotelDTO.getPrimary_phone());

        // Insert hotel into DB via DAO
        Integer newHotelId = hotelDAO.createHotel(newHotel);

        if (newHotelId == null) {
            throw new RuntimeException("Failed to create the hotel. No ID was returned.");
        }

        // Fetch the newly created hotel to return full data (e.g., DB-generated fields)
        return hotelDAO.findHotelById(newHotelId);
    }

    // 🔹 4. Find all hotels by city
    public List<Hotel> findHotelsByCity(String city) {
        return hotelDAO.findHotelsByCity(city);
    }

    // 🔹 5. Delete hotel by ID
    public boolean deleteHotelById(int hotelId) {
        return hotelDAO.deleteHotelById(hotelId);
    }

    public boolean deleteRoomById(int hotelId, int roomId) {
        return hotelDAO.deleteRoomById(hotelId, roomId);
    }

    public List<RoomType> getRoomsByHotelId(Integer hotelId) {
        return hotelDAO.findRoomsByHotelId(hotelId);
    }

    public void createRoom(RoomType room) {
        hotelDAO.insertRoom(room);
    }

    public int updateHotel(Hotel hotel) {
        return hotelDAO.updateHotelDetails(hotel);
    }

    public List<Hotel> getHotelsByCity(String city) {
        return hotelDAO.findHotelsByCity(city);
    }

    public Integer getVendorIdByHotelId(Integer hotelId){
        return hotelDAO.getVendorIdByHotelId(hotelId);
    }

    public boolean updateRoom(RoomType room){
        return hotelDAO.updateRoom(room);
    }

    public boolean doesRoomBelongToHotel(Integer hotelId, Integer roomId){
        return hotelDAO.doesRoomBelongToHotel(hotelId, roomId);
    }
}
