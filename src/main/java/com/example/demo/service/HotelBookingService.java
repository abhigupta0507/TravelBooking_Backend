package com.example.demo.service;

import com.example.demo.dao.AuthDao;
import com.example.demo.dao.HotelBookingDao;
import com.example.demo.dao.HotelDAO;
import com.example.demo.dto.HotelBookingDto;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.model.*;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * Cancels a hotel booking if the user is authorized and the booking is cancellable.
     * @param bookingId The ID of the booking to cancel.
     * @param userId The ID of the user attempting the cancellation.
     * @return The updated HotelBooking object.
     * @throws com.example.demo.exception.ResourceNotFoundException if the booking is not found.
     * @throws UnauthorizedException if the user is not the owner of the booking.
     * @throws IllegalStateException if the booking is already cancelled or completed.
     */
    public HotelBooking cancelHotelBooking(int bookingId, int userId) {
        // Step 1: Fetch the booking. Handles "not found".
        HotelBooking booking = getHotelBooking(bookingId);

        // Step 2: Verify ownership.
        if (booking.getCustomer_id() != userId) {
            throw new UnauthorizedException("You are not authorized to cancel this booking.");
        }

        // Step 3: Check if the booking is already in a final state.
        String currentStatus = booking.getStatus();
        if ("CANCELLED".equalsIgnoreCase(currentStatus) || "COMPLETED".equalsIgnoreCase(currentStatus)) {
            throw new IllegalStateException("Booking is already " + currentStatus + " and cannot be cancelled.");
        }

        // Step 4: Update the status in the database.
        int rowsAffected = hotelBookingDao.updateHotelBookingStatus("CANCELLED", bookingId);
        if (rowsAffected == 0) {
            // This could happen in a rare race condition or if the DB connection failed mid-way
            throw new RuntimeException("Failed to update booking status in the database.");
        }

        // Step 5: Update the status in our local object and return it.
        booking.setStatus("CANCELLED");
        return booking;
    }

    public List<HotelBooking> getAllHotelBookingsOfCustomer(Integer userId) {
        try {
            return hotelBookingDao.getAllHotelBookingsOfCustomer(userId);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<HotelBookingDto> getAllHotelBookingsDtoOfCustomer(List<HotelBooking> hotelBookingsDB) {
        List<HotelBookingDto> hotelBookingDtoList = new ArrayList<>();

        for(HotelBooking theHotelBooking: hotelBookingsDB){
            Booking theParentBooking = hotelBookingDao.getBookingForHotelBooking(theHotelBooking.getBooking_id());
            hotelBookingDtoList.add(new HotelBookingDto(theHotelBooking,theParentBooking));
        }

        return hotelBookingDtoList;
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

    public void deletePendingBooking() {
        try{
            hotelBookingDao.deletePendingBooking();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    // Add this new method

    public Map<String, Object> getBookingReceipt(int bookingId, Integer userId) {
        try {
            HotelBooking booking = hotelBookingDao.getHotelBookingById(bookingId);

            // Verify booking belongs to user
            if (!booking.getCustomer_id().equals(userId)) {
                throw new RuntimeException("Unauthorized access to booking");
            }

            // Get customer details
            com.example.demo.model.Customer customer = authDao.findCustomerById(userId);

            // Get hotel details
            Hotel hotel = hotelDao.findHotelById(booking.getHotel_id());

            // Get room details
            RoomType room = hotelDao.findRoomByHotelAndRoomId(booking.getHotel_id(), booking.getRoom_id());

            // Get payment details if booking is confirmed
            Payment payment = null;
            if ("CONFIRMED".equals(booking.getStatus()) || "FINISHED".equals(booking.getStatus())) {
                try {
                    payment = hotelBookingDao.getPaymentForBooking(bookingId);
                } catch (Exception e) {
                    // Payment might not exist for some bookings
                }
            }

            Map<String, Object> receipt = new HashMap<>();

            // Booking details
            Map<String, Object> bookingDetails = new HashMap<>();
            bookingDetails.put("booking_id", booking.getBooking_id());
            bookingDetails.put("check_in_date", booking.getCheck_in_date());
            bookingDetails.put("check_out_date", booking.getCheck_out_date());
            bookingDetails.put("booking_date", booking.getBooking_date());
            bookingDetails.put("no_of_rooms", booking.getNo_of_rooms());
            bookingDetails.put("room_type", booking.getRoom_type());
            bookingDetails.put("guest_count", booking.getGuest_count());
            bookingDetails.put("number_of_nights", booking.getNumber_of_nights());
            bookingDetails.put("status", booking.getStatus());
            bookingDetails.put("total_cost", booking.getCost());

            // Customer details
            Map<String, Object> customerDetails = new HashMap<>();
            customerDetails.put("name", customer.getFirst_name() + " " + customer.getLast_name());
            customerDetails.put("email", customer.getEmail());
            customerDetails.put("phone", customer.getPhone());

            // Hotel details
            Map<String, Object> hotelDetails = new HashMap<>();
            hotelDetails.put("name", hotel.getName());
            hotelDetails.put("address", hotel.getStreet() + ", " + hotel.getCity() + ", " + hotel.getState() + " - " + hotel.getPin());
            hotelDetails.put("phone", hotel.getPrimary_phone());
            hotelDetails.put("email", hotel.getPrimary_email());

            // Room details
            Map<String, Object> roomDetails = new HashMap<>();
            roomDetails.put("type", room.getType());
            roomDetails.put("bed_type", room.getBed_type());
            roomDetails.put("max_capacity", room.getMax_capacity());
            roomDetails.put("cost_per_night", room.getCost_per_night());
            roomDetails.put("balcony_available", room.getBalcony_available());

            // Payment details
            Map<String, Object> paymentDetails = new HashMap<>();
            if (payment != null) {
                paymentDetails.put("payment_id", payment.getPayment_id());
                paymentDetails.put("payment_date", payment.getPayment_datetime());
                paymentDetails.put("payment_mode", payment.getPayment_mode());
                paymentDetails.put("transaction_reference", payment.getTransaction_reference());
                paymentDetails.put("amount", payment.getAmount());
                paymentDetails.put("status", payment.getStatus());
            }

            receipt.put("booking", bookingDetails);
            receipt.put("customer", customerDetails);
            receipt.put("hotel", hotelDetails);
            receipt.put("room", roomDetails);
            receipt.put("payment", paymentDetails);

            return receipt;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate receipt: " + e.getMessage());
        }
    }
}