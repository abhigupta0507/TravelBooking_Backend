package com.example.demo.service;

import com.example.demo.dao.AuthDao;
import com.example.demo.dao.HotelBookingDao;
import com.example.demo.dao.HotelDAO;
import com.example.demo.model.Hotel;
import com.example.demo.model.HotelBooking;
import com.example.demo.model.RoomType;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class HotelBookingService {
    public HotelBookingDao hotelBookingDao;
    public HotelDAO hotelDao;
    public AuthDao authDao;

    public HotelBookingService(HotelBookingDao hotelBookingDao, HotelDAO hotelDao, AuthDao authDao) {
        this.hotelBookingDao = hotelBookingDao;
        this.hotelDao = hotelDao;
        this.authDao = authDao;
    }

    public int createHotelBooking(HotelBooking hotelBooking, Integer userId) {
        try {
            if (hotelBooking.getCheck_in_date() == null || hotelBooking.getCheck_out_date() == null)
                throw new IllegalArgumentException("checkIn/checkOut required");

            long number_of_nights =   ChronoUnit.DAYS.between(hotelBooking.getCheck_in_date().toLocalDate(), hotelBooking.getCheck_out_date().toLocalDate());
            if(number_of_nights==0){
                throw new IllegalArgumentException("Same checkIn and checkOut dates are not allowed");
            }

            if(number_of_nights<0){
                throw new IllegalArgumentException("Check out must be after check in");
            }

            int number_of_rooms = hotelBooking.getNo_of_rooms();
            RoomType room = hotelDao.findRoomByHotelAndRoomId(hotelBooking.getHotel_id(), hotelBooking.getRoom_id());
            int cost_per_person = room.getCost_per_night();
            int totalCostPerPerson = cost_per_person * (int)number_of_nights;
            int totalCost = totalCostPerPerson * number_of_rooms;

            String roomType= hotelDao.getRoomType(hotelBooking.getHotel_id(),hotelBooking.getRoom_id());
            return hotelBookingDao.createHotelBooking(hotelBooking.getCheck_in_date(), hotelBooking.getCheck_out_date(), hotelBooking.getNo_of_rooms(), roomType, hotelBooking.getGuest_count(), totalCost, (int) number_of_nights, hotelBooking.getHotel_id(), hotelBooking.getRoom_id(), userId);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public HotelBooking getHotelBooking(int id){
        try{
            return hotelBookingDao.getHotelBookingById(id);
        }
        catch (Exception e){
            throw new RuntimeException("Not found");
        }

    }

    public List<HotelBooking> getAllHotelBookingsOfCustomer(Integer userId) {
        try {
            return hotelBookingDao.getAllHotelBookingsOfCustomer(userId);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<HotelBooking> getHotelBookingsOfCustomerByStatus(int userId,String status){
        try{
            return hotelBookingDao.getAllHotelBookingsOfCustomerByStatus(userId,status);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public boolean canBookForCheckDates(Integer noOfRooms, Date checkInDate, Date checkOutDate, Integer hotelId, Integer roomId) {
        try{
            int bookedRoomCount = hotelBookingDao.getCountOfBookedRoomsByCheckDates(checkInDate,checkOutDate,roomId,hotelId);
            int allottedRoomCount = hotelBookingDao.getAllottedRoomCount(hotelId,roomId);
            int availableRoomCount = allottedRoomCount-bookedRoomCount;

            return availableRoomCount >= noOfRooms;
        }
        catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }
}