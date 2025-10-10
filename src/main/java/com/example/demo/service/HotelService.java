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

    public List<Hotel> findHotelsByCity(String city) {
        return hotelDAO.findHotelsByCity(city);
    }

    public int countRoomsOfHotel(int hotelId){
        try{
            return hotelDAO.countTotalRoomsOfHotel(hotelId);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    // 🔹 5. Delete hotel by ID
    public boolean deleteHotelById(int hotelId) {
        return hotelDAO.deleteHotelById(hotelId);
    }

    public boolean deleteRoomById(int hotelId, int roomId) {
        int oldRoomCountInRoom= hotelDAO.findRoomByHotelAndRoomId(hotelId,roomId).getTotal_rooms();
        int oldRoomCountInHotel = hotelDAO.findHotelById(hotelId).getTotal_rooms();
        int newRoomCountForHotel= oldRoomCountInHotel-oldRoomCountInRoom;
        hotelDAO.updateTotalRoomInHotel(hotelId,newRoomCountForHotel);
        return hotelDAO.deleteRoomById(hotelId, roomId);
    }

    public List<RoomType> getRoomsByHotelId(Integer hotelId) {
        return hotelDAO.findRoomsByHotelId(hotelId);
    }

    public void createRoom(RoomType room) {

        hotelDAO.insertRoom(room);
        int hotelId=room.getHotel_id();
        int oldRoomCount = hotelDAO.findHotelById(room.getHotel_id()).getTotal_rooms();
        int roomsInRoom = room.getTotal_rooms();

        int newRoomCount= oldRoomCount+roomsInRoom;
        hotelDAO.updateTotalRoomInHotel(hotelId,newRoomCount);
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


    public RoomType getRoomFromId(int hotelId,int roomId){
        try{
            return hotelDAO.findRoomByHotelAndRoomId(hotelId,roomId);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public boolean updateRoom(RoomType room){
        int oldRoomCountInRoomType = hotelDAO.findRoomByHotelAndRoomId(room.getHotel_id(),room.getRoom_id()).getTotal_rooms();
        boolean flag =  hotelDAO.updateRoom(room);
        if(!flag){
            return false;
        }

        int hotelId=room.getHotel_id();
        int oldRoomCount = hotelDAO.findHotelById(room.getHotel_id()).getTotal_rooms();
        int roomsInRoom = room.getTotal_rooms();

        int newRoomCount= oldRoomCount-oldRoomCountInRoomType+roomsInRoom;
        hotelDAO.updateTotalRoomInHotel(hotelId,newRoomCount);
        return true;
    }

    public boolean doesRoomBelongToHotel(Integer hotelId, Integer roomId){
        return hotelDAO.doesRoomBelongToHotel(hotelId, roomId);
    }
}
