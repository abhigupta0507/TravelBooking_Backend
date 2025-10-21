package com.example.demo.service;

import com.example.demo.dao.HotelReviewDao;
import com.example.demo.dao.HotelBookingDao;
import com.example.demo.model.HotelBooking;
import com.example.demo.model.HotelReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelReviewService {

    @Autowired
    private HotelReviewDao hotelReviewDao;

    @Autowired
    private HotelBookingDao hotelBookingDao;

    // ✅ 1. Get all reviews for a hotel (anyone can call)
    public List<HotelReview> getReviewsByHotelId(Integer hotelId) {
        return hotelReviewDao.getReviewsByHotelId(hotelId);
    }

    // ✅ 2. Get a specific review for a hotel (anyone can call)
    public HotelReview getReviewByHotelIdAndReviewId(Integer hotelId, Integer reviewId) {
        HotelReview review = hotelReviewDao.getReviewByHotelIdAndReviewId(hotelId, reviewId);
        if (review == null) {
            throw new RuntimeException("Review not found for this hotel");
        }
        return review;
    }

    // ✅ 3. Get a review by bookingId (only finished booking, validation done in controller for customer)
    public HotelReview getReviewByBookingId(Integer bookingId) {
        HotelBooking booking = hotelBookingDao.getHotelBookingById(bookingId);
        if (booking == null || !"FINISHED".equalsIgnoreCase(booking.getStatus())) {
            throw new RuntimeException("No review available for this booking");
        }
        return hotelReviewDao.getReviewByBookingId(bookingId);
    }

    // ✅ 4. Create a new review for a booking
    public Integer createHotelReview(HotelReview review, Integer bookingId) {
        HotelBooking booking = hotelBookingDao.getHotelBookingById(bookingId);
        if (booking == null) {
            throw new RuntimeException("Booking not found");
        }
        if (!"FINISHED".equalsIgnoreCase(booking.getStatus())) {
            throw new RuntimeException("Only finished bookings can be reviewed");
        }

        // stay_date will automatically be set to checkout_date in DAO
        return hotelReviewDao.createHotelReview(review, bookingId);
    }

    // ✅ 5. Delete a review (by bookingId and reviewId)
    public int deleteHotelReview(Integer bookingId, Integer reviewId) {
        HotelBooking booking = hotelBookingDao.getHotelBookingById(bookingId);
        if (booking == null) {
            throw new RuntimeException("Booking not found");
        }
        if (!"FINISHED".equalsIgnoreCase(booking.getStatus())) {
            throw new RuntimeException("Only finished bookings can have reviews deleted");
        }
        return hotelReviewDao.deleteHotelReview(bookingId, reviewId);
    }
}
