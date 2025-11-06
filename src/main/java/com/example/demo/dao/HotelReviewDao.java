package com.example.demo.dao;

import com.example.demo.model.Hotel;
import com.example.demo.model.HotelBooking;
import com.example.demo.model.HotelReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class HotelReviewDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Get all reviews of a particular hotel (for finished bookings)
    public List<HotelReview> getReviewsByHotelId(Integer hotelId) {
        String sql = """
            SELECT hr.* 
            FROM HotelReview hr
            JOIN Hotel_Booking hb ON hr.hotel_booking_id = hb.booking_id
            WHERE hb.hotel_id = ? AND hb.status = 'FINISHED'
            """;
        return jdbcTemplate.query(sql, new HotelReviewRowMapper(), hotelId);
    }

    // Get a specific review by hotelId and reviewId
    public HotelReview getReviewByHotelIdAndReviewId(Integer hotelId, Integer reviewId) {
        String sql = """
            SELECT hr.* 
            FROM HotelReview hr
            JOIN Hotel_Booking hb ON hr.hotel_booking_id = hb.booking_id
            WHERE hr.hotel_review_id = ? AND hb.hotel_id = ? and hb.status = 'FINISHED'
            """;
        return jdbcTemplate.queryForObject(sql, new HotelReviewRowMapper(), reviewId, hotelId);
    }

    // Get a review by bookingId (only for finished bookings)
    public HotelReview getReviewByBookingId(Integer bookingId) {
        String sql = """
            SELECT hr.* 
            FROM HotelReview hr
            JOIN Hotel_Booking hb ON hr.hotel_booking_id = hb.booking_id
            WHERE hb.booking_id = ? AND hb.status = 'FINISHED'
            """;
        List<HotelReview> reviews = jdbcTemplate.query(sql, new HotelReviewRowMapper(), bookingId);
        if (reviews.isEmpty()) {
            return null;
        }
        return reviews.get(0);
    }

    // Add a review (use checkout_date from booking for stay_date)
    public Integer createHotelReview(HotelReview review, Integer bookingId) {
        //  Fetch check_out_date from Hotel_Booking
        String getDateSql = "SELECT check_out_date FROM Hotel_Booking WHERE booking_id = ?";
        System.out.println(review);
        Date checkoutDate = jdbcTemplate.queryForObject(getDateSql, Date.class, bookingId);
        System.out.println(checkoutDate);
        System.out.println(bookingId);


        String sql = """
            INSERT INTO HotelReview 
            (overall_rating, cleanliness_rating, review_title, review_body, stay_date, created_at, hotel_booking_id)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        System.out.println("Preparing SQL: " + sql);
        System.out.println("1️⃣ overall_rating = " + review.getOverall_rating());
        System.out.println("2️⃣ cleanliness_rating = " + review.getCleanliness_rating());
        System.out.println("3️⃣ review_title = " + review.getReview_title());
        System.out.println("4️⃣ review_body = " + review.getReview_body());
        System.out.println("5️⃣ stay_date (checkoutDate) = " + checkoutDate);
        System.out.println("6️⃣ created_at = " + Timestamp.valueOf(LocalDateTime.now()));
        System.out.println("7️⃣ booking_id = " + bookingId);


        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setBigDecimal(1, review.getOverall_rating());
            ps.setBigDecimal(2, review.getCleanliness_rating());
            ps.setString(3, review.getReview_title());
            ps.setString(4, review.getReview_body());
            ps.setDate(5, checkoutDate); // ✅ auto-use checkout_date
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(7, bookingId);
            return ps;
        }, keyHolder);

        updateHotelAverageRating(bookingId);

        return keyHolder.getKey().intValue();
    }

    // ✅ 5. Delete a review (by bookingId or reviewId)
    public int deleteHotelReview(Integer bookingId, Integer reviewId) {
        String sql = "DELETE FROM HotelReview WHERE hotel_review_id = ? AND hotel_booking_id = ?";
        return jdbcTemplate.update(sql, reviewId, bookingId);
    }

    //  Update hotel rating (after new review is created)
    public void updateHotelAverageRating(Integer bookingId) {
        String sql = """
            UPDATE Hotel h
            SET h.rating = (
                SELECT ROUND(AVG(hr.overall_rating), 1)
                FROM HotelReview hr
                JOIN Hotel_Booking hb ON hr.hotel_booking_id = hb.booking_id
                WHERE hb.hotel_id = (
                    SELECT  hc.hotel_id
                    FROM Hotel_Booking hc
                    WHERE hc.booking_id=?
                )
            )
            WHERE h.hotel_id = (
                SELECT  hb.hotel_id
                FROM Hotel_Booking hb
                WHERE hb.booking_id=?
            )
            """;
        jdbcTemplate.update(sql, bookingId, bookingId);
    }

    // ✅ RowMapper for HotelReview
    private static class HotelReviewRowMapper implements RowMapper<HotelReview> {
        @Override
        public HotelReview mapRow(ResultSet rs, int rowNum) throws SQLException {
            HotelReview review = new HotelReview();
            review.setHotel_review_id(rs.getInt("hotel_review_id"));
            review.setOverall_rating(rs.getBigDecimal("overall_rating"));
            review.setCleanliness_rating(rs.getBigDecimal("cleanliness_rating"));
            review.setReview_title(rs.getString("review_title"));
            review.setReview_body(rs.getString("review_body"));
            review.setStay_date(rs.getDate("stay_date"));
            review.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
            review.setHotel_booking_id(rs.getInt("hotel_booking_id"));
            return review;
        }
    }
}
